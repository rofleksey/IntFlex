package ru.rofleksey.intflex.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import ru.rofleksey.intflex.expr.Declaration;
import ru.rofleksey.intflex.expr.ExpressionDeclaration;
import ru.rofleksey.intflex.expr.RangeDeclaration;
import ru.rofleksey.intflex.expr.Show;
import ru.rofleksey.intflex.runtime.error.IntFlexError;
import ru.rofleksey.intflex.runtime.namespaces.Injectors;

public class Processor {
    final ArrayList<ExpressionDeclaration> exprs;
    final ArrayList<RangeDeclaration> ranges;
    final ArrayList<Show> shows;
    final ArrayList<Declaration> all;
    final HashMap<String, Integer> dfsMap;
    final int[] dfsColor, dfsPrev;
    final IntFlex.IntFlexCallback callback;

    Processor(ArrayList<RangeDeclaration> ranges, ArrayList<ExpressionDeclaration> exprs, ArrayList<Show> shows, IntFlex.IntFlexCallback callback) {
        this.ranges = ranges;
        this.exprs = exprs;
        this.shows = shows;
        this.callback = callback;
        all = new ArrayList<>(ranges.size() + exprs.size() + shows.size());
        all.addAll(ranges);
        all.addAll(exprs);
        all.addAll(shows);
        dfsMap = new HashMap<>(all.size(), 1);
        dfsColor = new int[all.size()];
        dfsPrev = new int[all.size()];
        for (int i = 0; i < all.size(); i++) {
            dfsMap.put(all.get(i).getName(), i);
        }
    }

    private void cyclesCheckDFS(int v) throws ProcessError {
        if (dfsColor[v] == 2) {
            return;
        }
        Declaration decl = all.get(v);
        dfsColor[v] = 1;
        for (String next : decl.getDependable().getDependencies()) {
            int ii = dfsMap.get(next);
            if (dfsColor[ii] == 1) {
                StringBuilder builder = new StringBuilder("Cyclic dependencies detected: ");
                builder.append(all.get(v).getName());
                if (v != ii) {
                    for (int i = dfsPrev[v]; ; i = dfsPrev[i]) {
                        builder.append("->").append(all.get(i).getName());
                        if (i == ii) {
                            break;
                        }
                    }
                }
                builder.append("->").append(all.get(v).getName());
                throw new ProcessError(builder.toString());
            } else if (dfsColor[ii] == 0) {
                dfsPrev[ii] = v;
                cyclesCheckDFS(ii);
            }
        }
        dfsColor[v] = 2;
    }

    private void validate() throws ProcessError {
        HashSet<String> set = new HashSet<>();
        for (Declaration e : all) {
            if (set.contains(e.getName())) {
                throw new ProcessError("Duplicate declaration '" + e.getName() + "'");
            }
            set.add(e.getName());
        }
        for (Declaration d : all) {
            for (String s : d.getDependable().getDependencies()) {
                if (!set.contains(s)) {
                    throw new ProcessError("Declaration '" + s + "' not found");
                }
            }
        }
        Arrays.fill(dfsPrev, -1);
        for (int i = 0; i < all.size(); i++) {
            cyclesCheckDFS(i);
        }
        /*for(Declaration d : all) {
            System.out.println(d.getName()+" "+d.getDependable().toString());
        }*/
    }

    void sortDfs(int i, ArrayList<Declaration> temp) {
        dfsColor[i] = 1;
        for (String s : all.get(i).getDependable().getDependencies()) {
            int ii = dfsMap.get(s);
            if (dfsColor[ii] == 0) {
                sortDfs(ii, temp);
            }
        }
        temp.add(all.get(i));
    }

    ArrayList<Declaration> sort() {
        Arrays.fill(dfsColor, 0);
        ArrayList<Declaration> temp = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (dfsColor[i] == 0) {
                sortDfs(i, temp);
            }
        }
        return temp;
    }

    void execute() throws ProcessError, IntFlexError {
        validate();
        IntFlexContext context = new IntFlexContext(sort());
        new Injectors().inject(context);
        context.run();
        callback.onDone(context.getResult());
    }

    static class ProcessError extends Exception {
        ProcessError(String s) {
            super(s);
        }
    }
}
