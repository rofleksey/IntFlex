package ru.rofleksey.intflex.expr;

import java.math.BigDecimal;

public class Remainder extends BinaryOperator {
    public Remainder(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    BigDecimal combine(BigDecimal l, BigDecimal r) {
        return l.remainder(r);
    }
}
