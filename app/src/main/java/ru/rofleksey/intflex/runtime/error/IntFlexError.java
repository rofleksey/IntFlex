package ru.rofleksey.intflex.runtime.error;

public class IntFlexError extends Exception {
    final String message;

    //TODO: save error stacktrace???

    public IntFlexError(String message) {
        super(message);
        this.message = message;
    }
}
