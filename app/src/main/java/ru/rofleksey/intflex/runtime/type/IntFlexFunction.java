package ru.rofleksey.intflex.runtime.type;

import java.math.BigDecimal;
import java.util.ArrayList;

import ru.rofleksey.intflex.runtime.error.IntFlexError;

public class IntFlexFunction extends IntFlexObject {
    public final String name;

    public IntFlexFunction(String name) {
        this.name = name;
    }

    public static ArrayList<BigDecimal> assertNumbers(ArrayList<IntFlexObject> arr, boolean nonZero) throws IntFlexError {
        if (arr.isEmpty()) {
            throw new IntFlexError("Non zero number of arguments required");
        }
        ArrayList<BigDecimal> output = new ArrayList<>();
        for (IntFlexObject o : arr) {
            output.add(o.toNum());
        }
        return output;
    }

    public static void requireExactArguments(ArrayList<IntFlexObject> arr, int num) throws IntFlexError {
        if (arr.size() != num) {
            throw new IntFlexError("Invalid number of arguments specified: " + num + " expected, but " + arr.size() + " found");
        }
    }

    @Override
    public String getTypeName() {
        return "Function '" + name + "'";
    }
}
