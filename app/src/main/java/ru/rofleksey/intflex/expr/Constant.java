package ru.rofleksey.intflex.expr;

import java.util.HashSet;
import java.util.Set;

import ru.rofleksey.intflex.runtime.IntFlexContext;
import ru.rofleksey.intflex.runtime.type.IntFlexNum;
import ru.rofleksey.intflex.runtime.type.IntFlexObject;

public class Constant extends Expression {
    private final IntFlexNum computed;

    public Constant(String str) {
        computed = new IntFlexNum(str);
    }

    @Override
    public IntFlexObject calc(IntFlexContext context) {
        return computed;
    }

    @Override
    public Set<String> calcDependencies() {
        return new HashSet<>();
    }

    @Override
    public String toString() {
        return computed.toString();
    }
}
