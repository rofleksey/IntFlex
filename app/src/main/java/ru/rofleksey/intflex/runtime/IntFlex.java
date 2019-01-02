package ru.rofleksey.intflex.runtime;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.rofleksey.intflex.parser.InvalidInputException;
import ru.rofleksey.intflex.parser.MismatchedTokenException;
import ru.rofleksey.intflex.parser.Parser;
import ru.rofleksey.intflex.parser.TokenType;

public class IntFlex {
    private static final ExecutorService service = Executors.newSingleThreadExecutor();

    static void executeSync(String s, IntFlexCallback callback) {
        try {
            Parser parser = new Parser(s);
            parser.parse();
            Processor processor = new Processor(parser.ranges, parser.exprs, parser.shows, callback);
            processor.execute();
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    public static void execute(String s, IntFlexCallback callback) {
        service.execute(() -> {
            executeSync(s, callback);
        });
    }

    public static TokenType[] getNext(String s) {
        Parser parser = Parser.getNext(s);
        try {
            parser.parse();
        } catch (InvalidInputException e) {
            return null;
        } catch (MismatchedTokenException e) {
            return e.getExpected();
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public interface IntFlexCallback {
        void onDone(Result result);

        void onPercentage(float percent);

        void onError(Exception e);
    }
}
