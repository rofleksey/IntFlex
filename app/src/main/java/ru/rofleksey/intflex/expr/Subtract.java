package ru.rofleksey.intflex.expr;

import java.math.BigDecimal;

public class Subtract extends BinaryOperator {
    public Subtract(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    BigDecimal combine(BigDecimal l, BigDecimal r) {
        return l.subtract(r);
    }
}
