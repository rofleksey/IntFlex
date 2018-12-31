package ru.rofleksey.intflex.expr;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import ru.rofleksey.intflex.runtime.IntFlexContext;
import ru.rofleksey.intflex.runtime.error.IntFlexError;
import ru.rofleksey.intflex.runtime.type.IntFlexNum;

public class Range extends Dependable {
    final Expression from, to, step;

    public Range(Expression from, Expression to) {
        this.from = from;
        this.to = to;
        step = null;
    }

    public Range(Expression from, Expression to, Expression step) {
        this.from = from;
        this.to = to;
        this.step = step;
    }

    @Override
    public Set<String> calcDependencies() {
        HashSet<String> set = new HashSet<>(from.getDependencies());
        if (to != null) {
            set.addAll(to.getDependencies());
        }
        if (step != null) {
            set.addAll(step.getDependencies());
        }
        return set;
    }

    void iterate(String name, IntFlexContext context) throws IntFlexError {
        context.checkInterrupt();
        BigDecimal f = from.calc(context).toNum();
        BigDecimal t = to.calc(context).toNum();
        BigDecimal s = step == null ? BigDecimal.ONE : step.calc(context).toNum();
        BigDecimal cur = f;
        if (s.signum() == 0) {
            throw new IntFlexError("Step of range equals to zero");
        }
        while (cur.compareTo(t) * s.signum() <= 0) {
            context.checkInterrupt();
            context.put(name, new IntFlexNum(cur));
            context.enterNext();
            cur = cur.add(s);
        }
    }
}
