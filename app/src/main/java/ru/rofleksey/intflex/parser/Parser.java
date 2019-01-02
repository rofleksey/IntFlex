package ru.rofleksey.intflex.parser;

import java.util.ArrayList;
import java.util.List;

import ru.rofleksey.intflex.expr.Add;
import ru.rofleksey.intflex.expr.Constant;
import ru.rofleksey.intflex.expr.Divide;
import ru.rofleksey.intflex.expr.Expression;
import ru.rofleksey.intflex.expr.ExpressionDeclaration;
import ru.rofleksey.intflex.expr.Factorial;
import ru.rofleksey.intflex.expr.FunctionCall;
import ru.rofleksey.intflex.expr.Multiply;
import ru.rofleksey.intflex.expr.Power;
import ru.rofleksey.intflex.expr.Range;
import ru.rofleksey.intflex.expr.RangeDeclaration;
import ru.rofleksey.intflex.expr.Remainder;
import ru.rofleksey.intflex.expr.Show;
import ru.rofleksey.intflex.expr.Subtract;
import ru.rofleksey.intflex.expr.UnaryMinus;
import ru.rofleksey.intflex.expr.Var;

public class Parser {
    public final ArrayList<ExpressionDeclaration> exprs;
    public final ArrayList<Show> shows;
    public final ArrayList<RangeDeclaration> ranges;
    private final String input;
    private List<Token> tokens;
    private int curToken = 0;
    private int showNum = 0;
    private boolean lastWasRangeWithoutStep = false, lastWasSimple = false;

    public Parser(String input) {
        this.input = input;
        exprs = new ArrayList<>();
        shows = new ArrayList<>();
        ranges = new ArrayList<>();
    }

    public static Parser getNext(String input) {
        return new Parser(input + "~");
    }

    public void parse() throws ParseException {
        Lexer lex = new Lexer(input);
        tokens = lex.getTokens();
        while (!curIs(TokenType.EOF)) {
            decl();
            if (lastWasRangeWithoutStep) {
                expected(TokenType.EOF, TokenType.AT, TokenType.IDENTIFIER,
                        TokenType.MINUS, TokenType.PLUS, TokenType.MULT,
                        TokenType.DIV, TokenType.MOD, TokenType.FACTORIAL, TokenType.LB, TokenType.COMMA);
            } else if (lastWasSimple) {
                expected(TokenType.EOF, TokenType.AT, TokenType.IDENTIFIER,
                        TokenType.MINUS, TokenType.PLUS, TokenType.MULT,
                        TokenType.DIV, TokenType.MOD, TokenType.FACTORIAL, TokenType.LB, TokenType.DOTS);
            } else {
                expected(TokenType.EOF, TokenType.AT, TokenType.IDENTIFIER,
                        TokenType.MINUS, TokenType.PLUS, TokenType.MULT,
                        TokenType.DIV, TokenType.MOD, TokenType.FACTORIAL, TokenType.LB);
            }
        }
    }

    private boolean curIs(TokenType type) {
        return tokens.get(curToken).type == type;
    }

    private String getLastText() {
        return tokens.get(curToken - 1).text;
    }

    private boolean skip(TokenType type) {
        if (curIs(type)) {
            curToken++;
            return true;
        }
        return false;
    }

    private void skipForce(TokenType type) throws ParseException {
        expected(type);
        skip(type);
    }

    private void expected(TokenType... types) throws ParseException {
        Token cur = tokens.get(curToken);
        for (TokenType t : types) {
            if (cur.type == t) {
                return;
            }
        }
        throw new MismatchedTokenException(cur.type, cur.line, cur.pos, types);
    }

    private void decl() throws ParseException {
        lastWasRangeWithoutStep = false;
        lastWasSimple = false;
        expected(TokenType.AT, TokenType.IDENTIFIER);
        if (skip(TokenType.AT)) {
            shows.add(show());
            return;
        }
        skipForce(TokenType.IDENTIFIER);
        String name = getLastText();
        skipForce(TokenType.ASSIGNFLEX);
        Expression e1 = add();
        if (skip(TokenType.DOTS)) {
            Expression e2 = add();
            if (skip(TokenType.COMMA)) {
                Expression e3 = add();
                ranges.add(new RangeDeclaration(name, new Range(e1, e2, e3)));
            } else {
                lastWasRangeWithoutStep = true;
                ranges.add(new RangeDeclaration(name, new Range(e1, e2)));
            }
        } else {
            lastWasSimple = true;
            exprs.add(new ExpressionDeclaration(name, e1));
        }
    }

    private Show show() throws ParseException {
        skipForce(TokenType.IDENTIFIER);
        return new Show(getLastText(), showNum++);
    }


    private Expression add() throws ParseException {
        Expression next = mult();
        while (true) {
            if (skip(TokenType.PLUS)) {
                next = new Add(next, mult());
            } else if (skip(TokenType.MINUS)) {
                next = new Subtract(next, mult());
            } else return next;
        }
    }

    private Expression mult() throws ParseException {
        Expression next = power();
        while (true) {
            if (skip(TokenType.MULT)) {
                next = new Multiply(next, power());
            } else if (skip(TokenType.DIV)) {
                next = new Divide(next, power());
            } else if (skip(TokenType.MOD)) {
                next = new Remainder(next, power());
            } else return next;
        }
    }

    private Expression power() throws ParseException {
        Expression next = unary();
        if (skip(TokenType.POW)) {
            return new Power(next, power());
        }
        return next;
    }

    private Expression unary() throws ParseException {
        if (skip(TokenType.MINUS)) {
            return new UnaryMinus(unary());
        }
        return postfix();
    }

    private Expression postfix() throws ParseException {
        Expression p = primary();
        if (skip(TokenType.LB)) {
            if (skip(TokenType.RB)) {
                return new FunctionCall(p);
            } else {
                return new FunctionCall(p, args());
            }
        }
        if (skip(TokenType.FACTORIAL)) {
            return new Factorial(p);
        }
        return p;
    }

    private ArrayList<Expression> args() throws ParseException {
        ArrayList<Expression> result = new ArrayList<>();
        result.add(add());
        ;
        while (!skip(TokenType.RB)) {
            expected(TokenType.COMMA, TokenType.RB);
            skipForce(TokenType.COMMA);
            result.add(add());
        }
        return result;
    }

    private Expression primary() throws ParseException {
        expected(TokenType.IDENTIFIER, TokenType.NUMBER, TokenType.LB, TokenType.MINUS);
        if (skip(TokenType.IDENTIFIER)) {
            return new Var(getLastText());
        }
        if (skip(TokenType.NUMBER)) {
            return new Constant(getLastText());
        }
        skipForce(TokenType.LB);
        Expression inner = add();
        skipForce(TokenType.RB);
        return inner;
    }


}
