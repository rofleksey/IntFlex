package ru.rofleksey.intflex.expr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ru.rofleksey.intflex.runtime.IntFlexContext;
import ru.rofleksey.intflex.runtime.error.IntFlexError;
import ru.rofleksey.intflex.runtime.type.IntFlexObject;

public class FunctionCall extends Expression {
    final Expression what;
    final ArrayList<Expression> args;

    public FunctionCall(Expression what) {
        this.what = what;
        this.args = new ArrayList<>();
    }

    public FunctionCall(Expression what, ArrayList<Expression> args) {
        this.what = what;
        this.args = args;
    }

    @Override
    public IntFlexObject calc(IntFlexContext context) throws IntFlexError {
        context.checkInterrupt();
        ArrayList<IntFlexObject> arr = new ArrayList<>(args.size());
        for (Expression e : args) {
            arr.add(e.calc(context));
        }
        return what.calc(context).execute(context, arr);
    }

    @Override
    public Set<String> calcDependencies() {
        HashSet<String> set = new HashSet<>();
        for (Expression e : args) {
            set.addAll(e.getDependencies());
        }
        return set;
    }
}
