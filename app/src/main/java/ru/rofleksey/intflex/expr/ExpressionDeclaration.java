package ru.rofleksey.intflex.expr;

import ru.rofleksey.intflex.runtime.IntFlexContext;
import ru.rofleksey.intflex.runtime.error.IntFlexError;

public class ExpressionDeclaration implements Declaration {
    public final String name;
    public final Expression expr;

    public ExpressionDeclaration(String name, Expression expr) {
        this.name = name;
        this.expr = expr;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Dependable getDependable() {
        return expr;
    }

    @Override
    public boolean isRange() {
        return false;
    }

    @Override
    public Range getRange() {
        return null;
    }

    @Override
    public void execute(IntFlexContext context) throws IntFlexError {
        context.checkInterrupt();
        context.put(name, expr.calc(context));
        context.enterNext();
    }
}
