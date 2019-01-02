package ru.rofleksey.intflex.runtime;

import java.util.ArrayList;
import java.util.List;

import ru.rofleksey.intflex.runtime.type.IntFlexObject;

public class Result {
    ArrayList<ResultPair> result = new ArrayList<>();

    void add(IntFlexContext context, IntFlexObject o) {
        result.add(new ResultPair(context.varsString(), o.toString()));
    }

    public List<String> getResults() {
        ArrayList<String> res = new ArrayList<>(result.size());
        for (ResultPair p : result) {
            res.add(p.b);
        }
        return res;
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
