package ru.rofleksey.intflex.expr;

import ru.rofleksey.intflex.runtime.IntFlexContext;
import ru.rofleksey.intflex.runtime.error.IntFlexError;

public class RangeDeclaration implements Declaration {
    public final String name;
    public final Range range;

    public RangeDeclaration(String name, Range range) {
        this.name = name;
        this.range = range;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Dependable getDependable() {
        return range;
    }

    @Override
    public void execute(IntFlexContext context) throws IntFlexError {
        range.iterate(name, context);
    }

    @Override
    public String toString() {
        return name + " := " + range;
    }
}
