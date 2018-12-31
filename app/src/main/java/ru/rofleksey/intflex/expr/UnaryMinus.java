package ru.rofleksey.intflex.expr;

import java.math.BigDecimal;

import ru.rofleksey.intflex.runtime.error.IntFlexError;

public class UnaryMinus extends UnaryOperator {
    public UnaryMinus(Expression child) {
        super(child);
    }

    @Override
    BigDecimal combine(BigDecimal c) throws IntFlexError {
        return c.negate();
    }
}
