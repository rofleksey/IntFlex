package ru.rofleksey.intflex.expr;

import java.util.HashSet;
import java.util.Set;

import ru.rofleksey.intflex.runtime.IntFlexContext;
import ru.rofleksey.intflex.runtime.error.IntFlexError;
import ru.rofleksey.intflex.runtime.type.IntFlexObject;

public class Var extends Expression {
    final String name;

    public Var(String name) {
        this.name = name;
    }

    @Override
    public IntFlexObject calc(IntFlexContext context) throws IntFlexError {
        return context.get(name);
    }

    @Override
    public Set<String> calcDependencies() {
        HashSet<String> set = new HashSet<>();
        set.add(name);
        return set;
    }
}
