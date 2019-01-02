grammar old;

@header {
import ru.rofleksey.intflex.expr.*;
import java.util.ArrayList;
}

@parser::members {
        public ArrayList<RangeDeclaration> ranges = new ArrayList<>();
        public ArrayList<ExpressionDeclaration> calcs = new ArrayList<>();
        public ArrayList<Show> shows = new ArrayList<>();
        int showNum = 0;
}

main
    :   statementList EOF
    ;

statementList
    :   declaration
    |   statementList declaration
    ;

declaration
    : name=Identifier Assign r=range {ranges.add(new RangeDeclaration($name.text, $r.r));}
    | name=Identifier FlexAssign e2=additiveExpression {calcs.add(new ExpressionDeclaration($name.text, $e2.expr));}
    |  At name=Identifier {shows.add(new Show($name.text, showNum++));}
    ;

range returns [Range r]
    : from=additiveExpression Dots to=additiveExpression Comma step=additiveExpression {$r = new Range($from.expr, $to.expr, $step.expr);}
    | from=additiveExpression Dots to=additiveExpression {$r = new Range($from.expr, $to.expr);}
    ;

primaryExpression returns [Expression expr]
    :   e1=Identifier {$expr = new Var($e1.text);}
    |   e1=Constant {$expr = new Constant($e1.text);}
    |   '(' a=additiveExpression ')' {$expr = $a.expr;}
    ;

postfixExpression returns [Expression expr]
    :   p=primaryExpression {$expr = $p.expr;}
    |   what=postfixExpression '(' args=argumentExpressionList ')' {$expr = new FunctionCall($what.expr, $args.args);}
    |   what=postfixExpression '(' ')' {$expr = new FunctionCall($what.expr);}
    //|   what=postfixExpression '.' Identifier {$expr = new MemberGet($what.expr);}
    |   what=postfixExpression '!' {$expr = new Factorial($what.expr);}
    ;

argumentExpressionList returns [ArrayList<Expression> args]
    :   a=additiveExpression {$args = new ArrayList<>(); $args.add($a.expr);}
    |   e1=argumentExpressionList t1=Comma e2=additiveExpression {$e1.args.add($e2.expr);}
    ;

unaryExpression returns [Expression expr]
    :   p=postfixExpression {$expr = $p.expr;}
    |   Minus u=unaryExpression {$expr = new UnaryMinus($u.expr);}
    ;

powerExpression returns [Expression expr]
    :   u=unaryExpression {$expr = $u.expr;}
    |   e1=unaryExpression op=Pow e2=powerExpression {$expr = new Power($e1.expr, $e2.expr);}
    ;

multiplicativeExpression returns [Expression expr]
    :   p=powerExpression {$expr = $p.expr;}
    |   e1=multiplicativeExpression op=Star e2=powerExpression {$expr = new Multiply($e1.expr, $e2.expr);}
    |   e1=multiplicativeExpression op=Div e2=powerExpression {$expr = new Divide($e1.expr, $e2.expr);}
    |   e1=multiplicativeExpression op=Mod e2=powerExpression {$expr = new Remainder($e1.expr, $e2.expr);}
    ;

additiveExpression returns [Expression expr]
    :   m=multiplicativeExpression {$expr = $m.expr;}
    |   e1=additiveExpression op=Plus e2=multiplicativeExpression {$expr = new Add($e1.expr, $e2.expr);}
    |   e1=additiveExpression op=Minus e2=multiplicativeExpression {$expr = new Subtract($e1.expr, $e2.expr);}
    ;


At : '@';
Dots: '..';
Factorial: '!';
//Dot: '.';

LeftParen : '(';
RightParen : ')';
LeftBracket : '[';
RightBracket : ']';


Plus : '+';
Minus : '-';
Star : '*';
Pow : '^';
Div : '/';
Mod : '%';
Comma : ',';

Assign: '=';
FlexAssign: ':=';

SpecialSymb: '$';

Identifier
    :   IdentifierNondigit
        (   IdentifierNondigit
        |   Digit
        )*
    ;

fragment
IdentifierNondigit
    :   Nondigit
    ;

fragment
Nondigit
    :   [a-zA-Z]
    ;

fragment
Digit
    :   [0-9]
    ;

Constant
    :   DecimalConstant
    |   FractionalConstant
    ;

fragment
DecimalConstant
    :   Digit+
    ;

fragment
NonzeroDigit
    :   [1-9]
    ;


fragment
FractionalConstant
    :   DigitSequence '.' DigitSequence
    ;

fragment
Sign
    :   '+' | '-'
    ;

DigitSequence
    :   Digit+
    ;

fragment
CCharSequence
    :   CChar+
    ;

fragment
CChar
    :   ~['\\\r\n]
    |   SimpleEscapeSequence
    ;
fragment
SimpleEscapeSequence
    :   '\\' ['"?abfnrtv\\]
    ;

StringLiteral
    :   '"' SCharSequence? '"'
    ;
fragment
SCharSequence
    :   SChar+
    ;
fragment
SChar
    :   ~["\\\r\n]
    |   SimpleEscapeSequence
    |   '\\\n'   // Added line
    |   '\\\r\n' // Added line
    ;

Whitespace
    :   [ \t]+
        -> skip
    ;

Newline
    :   (   '\r' '\n'?
        |   '\n'
        )
        -> skip
    ;

BlockComment
    :   '/*' .*? '*/'
        -> skip
    ;

LineComment
    :   '//' ~[\r\n]*
        -> skip
    ;