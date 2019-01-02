package ru.rofleksey.intflex.expr;

import java.math.BigDecimal;
import java.util.Set;

import ru.rofleksey.intflex.runtime.IntFlexContext;
import ru.rofleksey.intflex.runtime.error.IntFlexError;
import ru.rofleksey.intflex.runtime.type.IntFlexNum;
import ru.rofleksey.intflex.runtime.type.IntFlexObject;

public class Factorial extends Expression {
    private Expression child;

    public Factorial(Expression child) {
        this.child = child;
    }

    public static BigDecimal factorial(BigDecimal n, IntFlexContext context) throws IntFlexError {
        context.checkInterrupt();
        return n.compareTo(BigDecimal.ZERO) <= 0 ? BigDecimal.ONE : n.multiply(factorial(n.subtract(BigDecimal.ONE), context));
    }

    @Override
    public IntFlexObject calc(IntFlexContext context) throws IntFlexError {
        context.checkInterrupt();
        BigDecimal dec = child.calc(context).toNum();
        return new IntFlexNum(factorial(dec, context));
    }

    @Override
    public Set<String> calcDependencies() {
        return child.getDependencies();
    }

    @Override
    public String toString() {
        return "(" + child + ")!";
    }
}
