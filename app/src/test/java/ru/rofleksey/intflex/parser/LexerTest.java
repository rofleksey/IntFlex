package ru.rofleksey.intflex.parser;


import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class LexerTest {

    public void testFail(String input, String message) {
        Lexer lex = new Lexer(input);
        List<Token> list;
        try {
            list = lex.getTokens();
        } catch (ParseException e) {
            Assert.assertEquals(message, e.getMessage());
            return;
        }
        Assert.fail("No exception thrown: " + list);
    }

    public void testSuccess(String input, Token... tokens) {
        Lexer lex = new Lexer(input);
        try {
            Assert.assertEquals(Arrays.asList(tokens), lex.getTokens());
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testBasic() {
        testSuccess("a := 1",
                new Token(TokenType.IDENTIFIER, "a", 0, 0),
                new Token(TokenType.ASSIGNFLEX, ":=", 0, 2),
                new Token(TokenType.NUMBER, "1", 0, 5),
                new Token(TokenType.EOF, "", 0, 6));
    }

    @Test
    public void testEverything() {
        testSuccess("ab*2 !+3^4-5/(\n65%4)..7.5",
                new Token(TokenType.IDENTIFIER, "ab", 0, 0),
                new Token(TokenType.MULT, "*", 0, 2),
                new Token(TokenType.NUMBER, "2", 0, 3),
                new Token(TokenType.FACTORIAL, "!", 0, 5),
                new Token(TokenType.PLUS, "+", 0, 6),
                new Token(TokenType.NUMBER, "3", 0, 7),
                new Token(TokenType.POW, "^", 0, 8),
                new Token(TokenType.NUMBER, "4", 0, 9),
                new Token(TokenType.MINUS, "-", 0, 10),
                new Token(TokenType.NUMBER, "5", 0, 11),
                new Token(TokenType.DIV, "/", 0, 12),
                new Token(TokenType.LB, "(", 0, 13),
                new Token(TokenType.NUMBER, "65", 1, 0),
                new Token(TokenType.MOD, "%", 1, 2),
                new Token(TokenType.NUMBER, "4", 1, 3),
                new Token(TokenType.RB, ")", 1, 4),
                new Token(TokenType.DOTS, "..", 1, 5),
                new Token(TokenType.NUMBER, "7.5", 1, 7),
                new Token(TokenType.EOF, "", 1, 10));
    }

    @Test
    public void testMisc() {
        testSuccess("a,@b",
                new Token(TokenType.IDENTIFIER, "a", 0, 0),
                new Token(TokenType.COMMA, ",", 0, 1),
                new Token(TokenType.AT, "@", 0, 2),
                new Token(TokenType.IDENTIFIER, "b", 0, 3),
                new Token(TokenType.EOF, "", 0, 4));
    }

    @Test
    public void testLines() {
        testSuccess("a\n:=\n1",
                new Token(TokenType.IDENTIFIER, "a", 0, 0),
                new Token(TokenType.ASSIGNFLEX, ":=", 1, 0),
                new Token(TokenType.NUMBER, "1", 2, 0),
                new Token(TokenType.EOF, "", 2, 1));
    }

    @Test
    public void testEmpty() {
        testSuccess("",
                new Token(TokenType.EOF, "", 0, 0));
    }

    @Test
    public void testContainsEmptyLines() {
        testSuccess("\n\n    a    \n\n",
                new Token(TokenType.IDENTIFIER, "a", 2, 4),
                new Token(TokenType.EOF, "", 4, 0));
    }

    @Test
    public void testError1() {
        testFail("&", "Invalid input at line = 1 at pos = 1");
    }

    @Test
    public void testError2() {
        testFail("5*$", "Invalid input at line = 1 at pos = 3");
    }
}