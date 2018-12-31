package ru.rofleksey.intflex.runtime.type;

import java.math.BigDecimal;

import ru.rofleksey.intflex.runtime.error.IntFlexError;

public class IntFlexNum extends IntFlexObject {
    final BigDecimal bi;

    public IntFlexNum(String constant) {
        bi = new BigDecimal(constant);
    }

    public IntFlexNum(BigDecimal bi) {
        this.bi = bi;
    }

    @Override
    public BigDecimal toNum() throws IntFlexError {
        return bi;
    }

    @Override
    public String getTypeName() {
        return "Num";
    }

    @Override
    public String toString() {
        return bi.stripTrailingZeros().toPlainString();
    }
}
