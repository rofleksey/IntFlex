package ru.rofleksey.intflex.expr;

import java.math.BigDecimal;

public class Add extends BinaryOperator {
    public Add(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    BigDecimal combine(BigDecimal l, BigDecimal r) {
        return l.add(r);
    }
}
