package ru.rofleksey.intflex.expr;

import ru.rofleksey.intflex.runtime.IntFlexContext;
import ru.rofleksey.intflex.runtime.error.IntFlexError;
import ru.rofleksey.intflex.runtime.type.IntFlexObject;

public abstract class Expression extends Dependable {
    abstract IntFlexObject calc(IntFlexContext context) throws IntFlexError;
}
