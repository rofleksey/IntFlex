package ru.rofleksey.intflex.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import ru.rofleksey.intflex.expr.Declaration;

public class ParserTest {
    public void testSuccess(String input, String... expected) {
        Parser parser = new Parser(input);
        try {
            parser.parse();
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
        ArrayList<Declaration> all = new ArrayList<>();
        all.addAll(parser.exprs);
        all.addAll(parser.ranges);
        all.addAll(parser.shows);
        ArrayList<String> allStrings = new ArrayList<>();
        for (Declaration d : all) {
            allStrings.add(d.toString());
        }
        Assert.assertEquals("Invalid number of declarations found: " + all, expected.length, all.size());
        for (String s : expected) {
            int ii = allStrings.indexOf(s);
            if (ii < 0) {
                Assert.fail("Can't find declaration " + s + " in " + allStrings);
            }
        }
    }


    public void testFail(String input, String expected) {
        Parser parser = new Parser(input);
        try {
            parser.parse();
        } catch (ParseException e) {
            //e.printStackTrace();
            Assert.assertEquals(e.toString(), expected, e.getMessage());
            return;
        }
        Assert.fail("No exception thrown");
    }

    //CORRECT

    @Test
    public void testEmpty() {
        testSuccess("");
    }

    @Test
    public void testConstant() {
        testSuccess("a := 1", "a := 1");
    }

    @Test
    public void testPlus() {
        testSuccess("a := 1 + 1", "a := (1+1)");
    }

    @Test
    public void testPlusMinus() {
        testSuccess("a := 2 -4 +6 -1 -1- 0 +8", "a := ((((((2-4)+6)-1)-1)-0)+8)");
    }

    @Test
    public void testPlusPlus() {
        testSuccess("a := 1 + 1 + 1", "a := ((1+1)+1)");
    }

    @Test
    public void testPlusMult() {
        testSuccess("a := 1 + 2 * 3 + 4", "a := ((1+(2*3))+4)");
    }

    @Test
    public void testPlusDivMult() {
        testSuccess("a := 4*3+ 7+6 / 3", "a := (((4*3)+7)+(6/3))");
    }

    @Test
    public void testPow() {
        testSuccess("a := 2^3^2", "a := (2^(3^2))");
    }

    @Test
    public void testUnary() {
        testSuccess("a := -2!", "a := -((2)!)");
    }

    @Test
    public void testVars() {
        testSuccess("a := b+c", "a := (b+c)");
    }

    @Test
    public void testMethodCall() {
        testSuccess("a := gcd(c+e,d*f/g)", "a := gcd((c+e), ((d*f)/g))");
    }

    @Test
    public void testBrackets() {
        testSuccess("a := (5*7/5) + (23) - 5 * (98-4)/(6*7-42)", "a := ((((5*7)/5)+23)-((5*(98-4))/((6*7)-42)))");
    }

    @Test
    public void testNestedBrackets() {
        testSuccess("a := (( ((2)) + 4))*((5))", "a := ((2+4)*5)");
    }

    @Test
    public void testRange() {
        testSuccess("a := 0..2", "a := 0..2");
    }

    @Test
    public void testRangeStep() {
        testSuccess("a := 0..2,3", "a := 0..2,3");
    }

    @Test
    public void testAtCorrect() {
        testSuccess("@a", "@a");
    }

    @Test
    public void testMultiple() {
        testSuccess("n := 100 i := 0..n @i", "n := 100", "i := 0..n", "@i");
    }

    //ERRORS

    @Test
    public void testInvalidInput() {
        testFail("kek % lol $ orel", "Invalid input at line = 1 at pos = 11");
    }

    @Test
    public void testVarOnly() {
        testFail("a", "Mismatched token EOF. Token types [ASSIGNFLEX] expected at line = 1 at pos = 2");
    }

    @Test
    public void testAssignFlex() {
        testFail("a:=", "Mismatched token EOF. Token types [IDENTIFIER, NUMBER, LB, MINUS] expected at line = 1 at pos = 4");
    }

    @Test
    public void testAssignOp() {
        testFail("a := 1*", "Mismatched token EOF. Token types [IDENTIFIER, NUMBER, LB, MINUS] expected at line = 1 at pos = 8");
    }

    @Test
    public void testMissingComma() {
        testFail("a := a(b", "Mismatched token EOF. Token types [COMMA, RB] expected at line = 1 at pos = 9");
    }

    @Test
    public void testMissingArg() {
        testFail("a := c(1,", "Mismatched token EOF. Token types [IDENTIFIER, NUMBER, LB, MINUS] expected at line = 1 at pos = 10");
    }

    @Test
    public void testRangePartial() {
        testFail("a := 0..", "Mismatched token EOF. Token types [IDENTIFIER, NUMBER, LB, MINUS] expected at line = 1 at pos = 9");
    }

    @Test
    public void testRangeOver() {
        testFail("a := 0..1..2", "Mismatched token DOTS. Token types [EOF, AT, IDENTIFIER, MINUS, PLUS, MULT, DIV, MOD, FACTORIAL, LB, COMMA] expected at line = 1 at pos = 10");
    }

    @Test
    public void testAt() {
        testFail("@", "Mismatched token EOF. Token types [IDENTIFIER] expected at line = 1 at pos = 2");
    }

    @Test
    public void testAtInvalid() {
        testFail("@1", "Mismatched token NUMBER. Token types [IDENTIFIER] expected at line = 1 at pos = 2");
    }

    //PARTIAL

    @Test
    public void testPostEmpty() {
        testFail("~", "Mismatched token POTENTIAL. Token types [AT, IDENTIFIER] expected at line = 1 at pos = 1");
    }

    @Test
    public void testPostAll() {
        testFail("a := f(1)~", "Mismatched token POTENTIAL. Token types [EOF, AT, IDENTIFIER, MINUS, PLUS, MULT, DIV, MOD, FACTORIAL, LB, DOTS] expected at line = 1 at pos = 10");
    }

    @Test
    public void testPostShow() {
        testFail("@a~", "Mismatched token POTENTIAL. Token types [EOF, AT, IDENTIFIER, MINUS, PLUS, MULT, DIV, MOD, FACTORIAL, LB] expected at line = 1 at pos = 3");
    }

    @Test
    public void testPostRange() {
        testFail("a := 1..2~", "Mismatched token POTENTIAL. Token types [EOF, AT, IDENTIFIER, MINUS, PLUS, MULT, DIV, MOD, FACTORIAL, LB, COMMA] expected at line = 1 at pos = 10");
    }

    @Test
    public void testPostRangeWithStep() {
        testFail("a := 1..2,3~", "Mismatched token POTENTIAL. Token types [EOF, AT, IDENTIFIER, MINUS, PLUS, MULT, DIV, MOD, FACTORIAL, LB] expected at line = 1 at pos = 12");
    }
}