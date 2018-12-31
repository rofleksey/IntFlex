package ru.rofleksey.intflex.runtime.error;

public class IntFlexError extends Exception {
    final String message;
    final int line, pos;

    public IntFlexError(String message) {
        super(message);
        this.message = message;
        //TODO: implement
        this.line = 0;
        this.pos = 0;
    }
}
