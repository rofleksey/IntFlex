package ru.rofleksey.intflex.runtime;

import org.junit.Assert;
import org.junit.Test;

public class IntFlexTest {

    @Test
    public void testEmpty() {
        IntFlex.executeSync("",
                new SuccessCallback("[]"));
    }

    @Test
    public void testLinear() {
        IntFlex.executeSync("i := 0..10\n @i",
                new SuccessCallback("[({i=0}, 0), ({i=1}, 1), ({i=2}, 2), ({i=3}, 3), ({i=4}, 4), ({i=5}, 5), ({i=6}, 6), ({i=7}, 7), ({i=8}, 8), ({i=9}, 9), ({i=10}, 10)]"));
    }

    @Test
    public void testQuadro() {
        IntFlex.executeSync("i := 0..3 \nj := 0..3  \nresult := i * j + j\n  @result",
                new SuccessCallback("[({result=0, i=0, j=0}, 0), ({result=1, i=0, j=1}, 1), ({result=2, i=0, j=2}, 2), ({result=3, i=0, j=3}, 3), ({result=0, i=1, j=0}, 0), ({result=2, i=1, j=1}, 2), ({result=4, i=1, j=2}, 4), ({result=6, i=1, j=3}, 6), ({result=0, i=2, j=0}, 0), ({result=3, i=2, j=1}, 3), ({result=6, i=2, j=2}, 6), ({result=9, i=2, j=3}, 9), ({result=0, i=3, j=0}, 0), ({result=4, i=3, j=1}, 4), ({result=8, i=3, j=2}, 8), ({result=12, i=3, j=3}, 12)]"));
    }

    @Test
    public void testPairs() {
        IntFlex.executeSync("i := 0..3 \nj := i+1..3  \nresult := i * j + j \n @result",
                new SuccessCallback("[({result=1, i=0, j=1}, 1), ({result=2, i=0, j=2}, 2), ({result=3, i=0, j=3}, 3), ({result=4, i=1, j=2}, 4), ({result=6, i=1, j=3}, 6), ({result=9, i=2, j=3}, 9)]"));
    }

    @Test
    public void testConstant() {
        IntFlex.executeSync("i := -5 + 10 * 5^2 + 16 * 4! - (50-10*4)! \n  @i",
                new SuccessCallback("[({i=-3628171}, -3628171)]"));
    }

    @Test
    public void testPower() {
        IntFlex.executeSync("i := 0.1^100\n  @i",
                new SuccessCallback("[({i=0.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001}, 0.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001)]"));
    }

    @Test
    public void testRangeStepPositive() {
        IntFlex.executeSync("i := 1..10,4\n @i",
                new SuccessCallback("[({i=1}, 1), ({i=5}, 5), ({i=9}, 9)]"));
    }

    @Test
    public void testRangeStepFractional() {
        IntFlex.executeSync("i := 0.25..0.75,0.25\n @i",
                new SuccessCallback("[({i=0.25}, 0.25), ({i=0.5}, 0.5), ({i=0.75}, 0.75)]"));
    }

    @Test
    public void testRangeNegative() {
        IntFlex.executeSync("i := 5..0\n @i",
                new SuccessCallback("[]"));
    }

    @Test
    public void testRangeStepNegative() {
        IntFlex.executeSync("i := 9..1,-4 \n@i",
                new SuccessCallback("[({i=9}, 9), ({i=5}, 5), ({i=1}, 1)]"));
    }

    @Test
    public void testNoShows() {
        IntFlex.executeSync("i := 100",
                new SuccessCallback("[]"));
    }

    @Test
    public void testMultipleShows() {
        IntFlex.executeSync("i := 0..4\n j := i^2 \n@i\n @j",
                new SuccessCallback("[({i=0, j=0}, 0), ({i=0, j=0}, 0), ({i=1, j=1}, 1), ({i=1, j=1}, 1), ({i=2, j=4}, 2), ({i=2, j=4}, 4), ({i=3, j=9}, 3), ({i=3, j=9}, 9), ({i=4, j=16}, 4), ({i=4, j=16}, 16)]"));
    }

    @Test
    public void testMin() {
        IntFlex.executeSync("i := min(2,1)\n@i",
                new SuccessCallback("[({i=1}, 1)]"));
    }

    @Test
    public void testMax() {
        IntFlex.executeSync("i := max(2,1)\n@i",
                new SuccessCallback("[({i=2}, 2)]"));
    }

    @Test
    public void testCnk() {
        IntFlex.executeSync("i := c(10,4)\n@i",
                new SuccessCallback("[({i=210}, 210)]"));
    }

    @Test
    public void dublicateDeclaration() {
        ErrorCallback callback;
        IntFlex.executeSync("i := 0..10 \n i := 0..10 \n @i", callback = new ErrorCallback("Duplicate declaration 'i'"));
        callback.assertError();
    }

    @Test
    public void undefinedVar() {
        ErrorCallback callback;
        IntFlex.executeSync("i := 0..n \n @i", callback = new ErrorCallback("Declaration 'n' not found"));
        callback.assertError();
    }


    @Test
    public void cycleSelf() {
        ErrorCallback callback;
        IntFlex.executeSync("i := 0..i \n @i", callback = new ErrorCallback("Cyclic dependencies detected: i->i"));
        callback.assertError();
    }

    @Test
    public void cycleComplex() {
        ErrorCallback callback;
        IntFlex.executeSync("i := 0..j \n j := 0..i \n @i", callback = new ErrorCallback("Cyclic dependencies detected: j->i->j"));
        callback.assertError();
    }

    @Test
    public void testRangeStepZero() {
        ErrorCallback callback;
        IntFlex.executeSync("i := 1..10,0 \n@i", callback = new ErrorCallback("Step of range equals to zero"));
        callback.assertError();
    }

    @Test
    public void testMinZeroArgs() {
        ErrorCallback callback;
        IntFlex.executeSync("i := min()\n@i", callback = new ErrorCallback("Non zero number of arguments required"));
        callback.assertError();
    }

    @Test
    public void testMaxZeroArgs() {
        ErrorCallback callback;
        IntFlex.executeSync("i := max()\n@i", callback = new ErrorCallback("Non zero number of arguments required"));
        callback.assertError();
    }

    @Test
    public void testCnkInvalidArgs() {
        ErrorCallback callback;
        IntFlex.executeSync("i := c(10,2,1)\n@i", callback = new ErrorCallback("Invalid number of arguments specified: 2 expected, but 3 found"));
        callback.assertError();
    }

    class ErrorCallback implements IntFlex.IntFlexCallback {
        final String message;
        boolean error;

        ErrorCallback(String message) {
            this.message = message;
        }

        @Override
        public void onDone(Result result) {

        }

        @Override
        public void onPercentage(float percent) {

        }

        @Override
        public void onError(Exception e) {
            error = true;
            if (!message.equals(e.getMessage())) {
                e.printStackTrace();
            }
            Assert.assertEquals(e.toString(), message, e.getMessage());
        }

        void assertError() {
            if (!error) {
                Assert.fail("No error found");
            }
        }
    }

    class SuccessCallback implements IntFlex.IntFlexCallback {

        final String expected;

        SuccessCallback(String expected) {
            this.expected = expected;
        }

        @Override
        public void onDone(Result result) {
            Assert.assertEquals(expected, result.toString());
        }

        @Override
        public void onPercentage(float percent) {

        }

        @Override
        public void onError(Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
}