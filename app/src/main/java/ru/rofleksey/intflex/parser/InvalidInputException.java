package ru.rofleksey.intflex.parser;

public class InvalidInputException extends ParseException {
    public InvalidInputException(int line, int linePos) {
        super("Invalid input", line, linePos);
    }
}
