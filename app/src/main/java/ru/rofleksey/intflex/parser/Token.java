package ru.rofleksey.intflex.parser;

public class Token {
    public final String text;
    public final int line, pos;
    public final TokenType type;

    public Token(TokenType type, String text, int line, int pos) {
        this.type = type;
        this.text = text;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public boolean equals(Object o) { // for tests
        if (o == this) {
            return true;
        }
        if (!o.getClass().equals(getClass())) {
            return false;
        }
        Token other = (Token) o;
        return text.equals(other.text) && type.equals(other.type) && line == other.line && pos == other.pos;
    }

    @Override
    public String toString() {
        return "('" + text + "', " + type + ", line=" + line + ", pos=" + pos + ")";
    }
}
