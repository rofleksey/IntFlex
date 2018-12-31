package ru.rofleksey.intflex.runtime;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.rofleksey.intflex.parser.IntFlexLexer;
import ru.rofleksey.intflex.parser.IntFlexParser;

public class IntFlex {
    private static final ExecutorService service = Executors.newSingleThreadExecutor();

    static void executeSync(String s, IntFlexCallback callback) {
        try {
            IntFlexParser parser = getParser(s);
            parser.main();
            Processor processor = new Processor(parser.ranges, parser.calcs, parser.shows, callback);
            processor.execute();
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    static void execute(String s, IntFlexCallback callback) {
        service.execute(() -> {
            executeSync(s, callback);
        });
    }

    static private IntFlexParser getParser(String statement) throws IOException {
        ANTLRInputStream is = new ANTLRInputStream(new ByteArrayInputStream(statement.getBytes()));
        IntFlexLexer lexer = new IntFlexLexer(is);
        TokenStream ts = new CommonTokenStream(lexer);
        return new IntFlexParser(ts);
    }

    interface IntFlexCallback {
        void onDone(Result result);

        void onPercentage(float percent);

        void onError(Exception e);
    }
}
