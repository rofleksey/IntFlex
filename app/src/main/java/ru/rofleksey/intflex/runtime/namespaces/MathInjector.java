package ru.rofleksey.intflex.runtime.namespaces;

import java.math.BigDecimal;
import java.util.ArrayList;

import ru.rofleksey.intflex.expr.Factorial;
import ru.rofleksey.intflex.runtime.IntFlexContext;
import ru.rofleksey.intflex.runtime.error.IntFlexError;
import ru.rofleksey.intflex.runtime.type.IntFlexFunction;
import ru.rofleksey.intflex.runtime.type.IntFlexNum;
import ru.rofleksey.intflex.runtime.type.IntFlexObject;

public class MathInjector implements NamespaceInjector {
    @Override
    public void inject(IntFlexContext context) {
        IntFlexFunction[] funcs = new IntFlexFunction[]{
                new Max(), new Min(), new CNK()
        };
        for (IntFlexFunction f : funcs) {
            context.putSys(f.name, f);
        }
    }

    class Max extends IntFlexFunction {
        Max() {
            super("max");
        }

        public IntFlexObject execute(IntFlexContext context, ArrayList<IntFlexObject> args) throws IntFlexError {
            ArrayList<BigDecimal> decimals = IntFlexFunction.assertNumbers(args, true);
            BigDecimal max = decimals.get(0);
            for (int i = 1; i < decimals.size(); i++) {
                if (max.compareTo(decimals.get(i)) < 0) {
                    max = decimals.get(i);
                }
            }
            return new IntFlexNum(max);
        }
    }


    class Min extends IntFlexFunction {
        Min() {
            super("min");
        }

        public IntFlexObject execute(IntFlexContext context, ArrayList<IntFlexObject> args) throws IntFlexError {
            ArrayList<BigDecimal> decimals = IntFlexFunction.assertNumbers(args, true);
            BigDecimal min = decimals.get(0);
            for (int i = 1; i < decimals.size(); i++) {
                if (min.compareTo(decimals.get(i)) > 0) {
                    min = decimals.get(i);
                }
            }
            return new IntFlexNum(min);
        }
    }

    class CNK extends IntFlexFunction {
        CNK() {
            super("c");
        }

        public IntFlexObject execute(IntFlexContext context, ArrayList<IntFlexObject> args) throws IntFlexError {
            requireExactArguments(args, 2);
            ArrayList<BigDecimal> decimals = IntFlexFunction.assertNumbers(args, true);
            BigDecimal n = decimals.get(0);
            BigDecimal k = decimals.get(1);
            BigDecimal result = Factorial.factorial(n, context).divide(Factorial.factorial(k, context).multiply(Factorial.factorial(n.subtract(k), context)), BigDecimal.ROUND_HALF_UP);
            return new IntFlexNum(result);
        }
    }

}
