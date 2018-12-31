package ru.rofleksey.intflex.expr;

import java.math.BigDecimal;

public class Power extends BinaryOperator {
    public Power(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    BigDecimal combine(BigDecimal l, BigDecimal r) {
        return l.pow(r.intValue());
    }
}
