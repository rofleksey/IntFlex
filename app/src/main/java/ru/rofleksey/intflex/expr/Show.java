package ru.rofleksey.intflex.expr;

import java.util.HashSet;
import java.util.Set;

import ru.rofleksey.intflex.runtime.IntFlexContext;
import ru.rofleksey.intflex.runtime.error.IntFlexError;

public class Show extends Dependable implements Declaration {
    private String link, name;

    public Show(String s, int showNum) {
        link = s;
        name = "_show" + showNum;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Dependable getDependable() {
        return this;
    }

    @Override
    public void execute(IntFlexContext context) throws IntFlexError {
        context.checkInterrupt();
        context.addToResult(context.get(link));
        context.enterNext();
    }

    @Override
    public Set<String> calcDependencies() {
        HashSet<String> s = new HashSet<>();
        s.add(link);
        return s;
    }

    @Override
    public String toString() {
        return "@" + link;
    }
}
