package ru.rofleksey.intflex.expr;

import java.math.BigDecimal;

public class Multiply extends BinaryOperator {
    public Multiply(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    BigDecimal combine(BigDecimal l, BigDecimal r) {
        return l.multiply(r);
    }
}
