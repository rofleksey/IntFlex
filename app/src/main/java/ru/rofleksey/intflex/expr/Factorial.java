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

    private BigDecimal factorial(BigDecimal n) {
        return n.compareTo(BigDecimal.ZERO) <= 0 ? BigDecimal.ONE : n.multiply(factorial(n.subtract(BigDecimal.ONE)));
    }

    @Override
    public IntFlexObject calc(IntFlexContext context) throws IntFlexError {
        context.checkInterrupt();
        BigDecimal dec = child.calc(context).toNum();
        return new IntFlexNum(factorial(dec));
    }

    @Override
    public Set<String> calcDependencies() {
        return child.getDependencies();
    }
}
