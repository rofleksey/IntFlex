package ru.rofleksey.intflex.expr;

import ru.rofleksey.intflex.runtime.IntFlexContext;
import ru.rofleksey.intflex.runtime.error.IntFlexError;

public interface Declaration {
    String getName();

    Dependable getDependable();

    boolean isRange();

    Range getRange();

    void execute(IntFlexContext context) throws IntFlexError;
}
