package ru.rofleksey.intflex.parser;

public enum TokenType {
    LB("("), RB(")"), EOF("EOF"), IDENTIFIER("Var"), NUMBER("Num"), PLUS("+"), MINUS("-"),
    MULT("*"), DIV("/"), MOD("%"), ASSIGN_FLEX(":="), DOTS(".."), FACTORIAL("!"), POW("^"),
    AT("@"), COMMA(","), LINE_BREAK("line"), POTENTIAL("_");
    private final String text;

    TokenType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
