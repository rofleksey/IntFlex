package ru.rofleksey.intflex.runtime;

import java.util.ArrayList;

import ru.rofleksey.intflex.runtime.type.IntFlexObject;

public class Result {
    ArrayList<ResultPair> result = new ArrayList<>();

    void add(IntFlexContext context, IntFlexObject o) {
        result.add(new ResultPair(context.varsString(), o.toString()));
    }

    @Override
    public String toString() {
        return result.toString();
    }

    static class ResultPair {
        final String a;
        final String b;

        ResultPair(String a, String b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString() {
            return "(" + a + ", " + b + ")";
        }
    }
}
