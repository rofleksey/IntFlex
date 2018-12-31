package ru.rofleksey.intflex.expr;

import java.math.BigDecimal;

public class Divide extends BinaryOperator {
    public Divide(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    BigDecimal combine(BigDecimal l, BigDecimal r) {
        return l.divide(r, BigDecimal.ROUND_HALF_UP);
    }
}
