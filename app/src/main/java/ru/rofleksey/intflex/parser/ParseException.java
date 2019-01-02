package ru.rofleksey.intflex.parser;

public class ParseException extends Exception {
    public ParseException(String msg, int line, int linePos) {
        super(msg + " at line = " + (line + 1) + " at pos = " + (linePos + 1));
    }
}
