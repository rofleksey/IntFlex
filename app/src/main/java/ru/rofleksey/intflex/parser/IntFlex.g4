grammar IntFlex;

main
    :   statementList? EOF
    ;

statementList
    :   statement
    |   statementList statement
    ;

statement
    :   assignmentExpression
    ;

primaryExpression
    :   e1=Identifier
    |   e1=Constant
    |   e1=StringLiteral+
    |   '(' assignmentExpression ')'
    ;

postfixExpression
    :   primaryExpression
    |   postfixExpression '(' argumentExpressionList? ')'
    |   postfixExpression '.' Identifier
    ;

argumentExpressionList
    :   assignmentExpression #assListNextLevel
    |   e1=argumentExpressionList t1=Comma e2=assignmentExpression #assList1
    ;

unaryExpression
    :   postfixExpression
    |   unaryOperator unaryExpression
    ;

unaryOperator
    :   e=Plus | e=Minus | e=Not
    ;

multiplicativeExpression
    :   unaryExpression #multNext
    |   e1=multiplicativeExpression op=Star e2=unaryExpression #multOp
    |   e1=multiplicativeExpression op=Div e2=unaryExpression #multOp
    |   e1=multiplicativeExpression op=Mod e2=unaryExpression #multOp
    ;

additiveExpression
    :   multiplicativeExpression #addNext
    |   e1=additiveExpression op=Plus e2=multiplicativeExpression #addOp
    |   e1=additiveExpression op=Minus e2=multiplicativeExpression #addOp
    ;

relationalExpression
    :   additiveExpression #relNext
    |   e1=relationalExpression op=Less e2=additiveExpression #relOp
    |   e1=relationalExpression op=Greater e2=additiveExpression #relOp
    |   e1=relationalExpression op=LessEqual e2=additiveExpression #relOp
    |   e1=relationalExpression op=GreaterEqual e2=additiveExpression #relOp
    ;

equalityExpression
    :   relationalExpression #eqNext
    |   e1=equalityExpression op=Equal e2=relationalExpression #eqOp
    |   e1=equalityExpression op=NotEqual e2=relationalExpression #eqOp
    ;

logicalAndExpression
    :   equalityExpression #andNext
    |   e1=logicalAndExpression op=AndAnd e2=equalityExpression #andOp
    ;

logicalOrExpression
    :   logicalAndExpression #orNext
    |   e1=logicalOrExpression op=OrOr e2=logicalAndExpression #orOp
    ;

assignmentExpression
    :   logicalOrExpression #assExprNextLevel
    |   e1=unaryExpression op=assignmentOperator e2=assignmentExpression #assExpr1
    //|   DigitSequence
    ;

assignmentOperator
    :   Assign | StarAssign | DivAssign | ModAssign | PlusAssign | MinusAssign
    ;


LeftParen : '(';
RightParen : ')';
LeftBracket : '[';
RightBracket : ']';
LeftBrace : '{';
RightBrace : '}';

Less : '<';
LessEqual : '<=';
Greater : '>';
GreaterEqual : '>=';


Plus : '+';
Minus : '-';
Star : '*';
Div : '/';
Mod : '%';

AndAnd : '&&';
OrOr : '||';
Not : '!';

Colon : ':';
Semi : ';';
Comma : ',';

Assign : '=';
FlexAssign: ':=';
// '*=' | '/=' | '%=' | '+=' | '-=' | '<<=' | '>>=' | '&=' | '^=' | '|='
StarAssign : '*=';
DivAssign : '/=';
ModAssign : '%=';
PlusAssign : '+=';
MinusAssign : '-=';

Equal : '==';
NotEqual : '!=';

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
    :   [a-zA-Z_]
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
HexadecimalPrefix
    :   '0' [xX]
    ;

fragment
NonzeroDigit
    :   [1-9]
    ;


fragment
FractionalConstant
    :   DigitSequence? '.' DigitSequence
    |   DigitSequence '.'
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