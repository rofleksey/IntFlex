package ru.rofleksey.intflex.expr;

import java.math.BigDecimal;
import java.util.Set;

import ru.rofleksey.intflex.runtime.IntFlexContext;
import ru.rofleksey.intflex.runtime.error.IntFlexError;
import ru.rofleksey.intflex.runtime.type.IntFlexNum;
import ru.rofleksey.intflex.runtime.type.IntFlexObject;

public abstract class UnaryOperator extends Expression {
    private Expression child;

    UnaryOperator(Expression child) {
        this.child = child;
    }

    @Override
    public IntFlexObject calc(IntFlexContext context) throws IntFlexError {
        context.checkInterrupt();
        return new IntFlexNum(combine(child.calc(context).toNum()));
    }

    abstract BigDecimal combine(BigDecimal c) throws IntFlexError;

    @Override
    public Set<String> calcDependencies() {
        return child.getDependencies();
    }
}
