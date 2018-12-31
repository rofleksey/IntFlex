package ru.rofleksey.intflex.expr;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import ru.rofleksey.intflex.runtime.IntFlexContext;
import ru.rofleksey.intflex.runtime.error.IntFlexError;
import ru.rofleksey.intflex.runtime.type.IntFlexNum;
import ru.rofleksey.intflex.runtime.type.IntFlexObject;

public abstract class BinaryOperator extends Expression {
    private Expression left, right;

    BinaryOperator(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public IntFlexObject calc(IntFlexContext context) throws IntFlexError {
        context.checkInterrupt();
        return new IntFlexNum(combine(left.calc(context).toNum(), right.calc(context).toNum()));
    }

    abstract BigDecimal combine(BigDecimal l, BigDecimal r) throws IntFlexError;

    @Override
    public Set<String> calcDependencies() {
        HashSet<String> set = new HashSet<>();
        set.addAll(left.getDependencies());
        set.addAll(right.getDependencies());
        return set;
    }
}
