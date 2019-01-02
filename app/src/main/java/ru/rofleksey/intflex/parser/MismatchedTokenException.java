package ru.rofleksey.intflex.parser;

import java.util.Arrays;

public class MismatchedTokenException extends ParseException {
    private final TokenType[] expected;
    private final TokenType invalid;

    public MismatchedTokenException(TokenType type, int line, int linePos, TokenType... expected) {
        super("Mismatched token " + type + ". Token types " + Arrays.asList(expected) + " expected", line, linePos);
        invalid = type;
        this.expected = expected;
    }

    public TokenType[] getExpected() {
        return expected;
    }
}
