package ru.rofleksey.intflex.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final String input;
    private final Scanner scanner;
    private int curPos = -1, curLine = 0, curLinePos = -1;
    private boolean eof = false;


    public Lexer(String input) {
        this.input = input;
        scanner = new Scanner();
        nextChar();
    }

    private void nextChar() {
        if (curPos + 1 < input.length()) {
            curPos++;
            if (input.charAt(curPos) == '\n') {
                curLine++;
                curLinePos = -2;
            }
            curLinePos++;
        } else {
            skip(1);
            eof = true;
        }
    }

    private void skip(int n) {
        curPos += n;
        curLinePos += n;
    }

    private Token genToken(int len, TokenType type) {
        return new Token(type, input.substring(curPos, curPos + len), curLine, curLinePos);
    }

    private boolean isSkipChar() {
        return !eof && Character.isWhitespace(input.charAt(curPos)) && '\n' != input.charAt(curPos);
    }

    private Token nextToken() throws ParseException {
        while (isSkipChar()) {
            nextChar();
        }
        if (eof) {
            return genToken(0, TokenType.EOF);
        }
        return scanner.next();
    }

    public List<Token> getTokens() throws ParseException {
        Token token;
        ArrayList<Token> result = new ArrayList<>();
        while (true) {
            token = nextToken();
            result.add(token);
            if (token.type == TokenType.EOF) {
                return result;
            }
        }
    }

    private class Scanner {
        private final TokenMatcher[] matchers;

        Scanner() {
            matchers = new TokenMatcher[]{
                    new TokenMatcher(TokenType.LB, "(", false),
                    new TokenMatcher(TokenType.RB, ")", false),
                    new TokenMatcher(TokenType.AT, "@", false),
                    new TokenMatcher(TokenType.LINE_BREAK, "\n", false),
                    new TokenMatcher(TokenType.POTENTIAL, "_", false),
                    new TokenMatcher(TokenType.COMMA, ",", false),
                    new TokenMatcher(TokenType.PLUS, "+", false),
                    new TokenMatcher(TokenType.MINUS, "-", false),
                    new TokenMatcher(TokenType.MULT, "*", false),
                    new TokenMatcher(TokenType.DIV, "/", false),
                    new TokenMatcher(TokenType.MOD, "%", false),
                    new TokenMatcher(TokenType.POW, "^", false),
                    new TokenMatcher(TokenType.DOTS, "..", false),
                    new TokenMatcher(TokenType.FACTORIAL, "!", false),
                    new TokenMatcher(TokenType.ASSIGN_FLEX, ":=", false),
                    new TokenMatcher(TokenType.NUMBER, "\\d+(\\.\\d+)?", true),
                    new TokenMatcher(TokenType.IDENTIFIER, "[a-zA-Z][a-zA-Z0-9]*", true),
            };
            for (TokenMatcher m : matchers) {
                m.prepare();
            }
        }

        Token next() throws ParseException {
            for (TokenMatcher m : matchers) {
                m.from(curPos);
            }
            for (TokenMatcher m : matchers) {
                if (m.match()) {
                    Token result = genToken(m.getLen(), m.type);
                    skip(m.getLen() - 1);
                    nextChar();
                    return result;
                }
            }
            throw new InvalidInputException(curLine, curLinePos);
        }

    }

    private class TokenMatcher {
        final TokenType type;
        private final Pattern pattern;
        private Matcher matcher;

        TokenMatcher(TokenType type, String pattern, boolean special) {
            this.type = type;
            this.pattern = Pattern.compile(special ? pattern : Pattern.quote(pattern));
        }

        void prepare() {
            matcher = pattern.matcher(input);
        }

        void from(int ind) {
            matcher.region(ind, input.length());
        }

        boolean match() {
            return matcher.lookingAt();
        }

        int getLen() {
            return matcher.end() - matcher.start();
        }
    }

}
