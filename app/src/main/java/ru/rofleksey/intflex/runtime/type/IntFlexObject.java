package ru.rofleksey.intflex.runtime.type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import ru.rofleksey.intflex.runtime.IntFlexContext;
import ru.rofleksey.intflex.runtime.error.IntFlexError;

public class IntFlexObject {
    HashMap<String, IntFlexObject> fields = new HashMap<>();

    public IntFlexObject execute(IntFlexContext context, ArrayList<IntFlexObject> args) throws IntFlexError {
        throw new IntFlexError("Type " + getTypeName() + " is not executable");
    }

    public BigDecimal toNum() throws IntFlexError {
        throw new IntFlexError("Object " + this + " is not a number");
    }

    public String getTypeName() {
        return "@";
    }

    public String toString() {
        return fields.toString();
    }
}
