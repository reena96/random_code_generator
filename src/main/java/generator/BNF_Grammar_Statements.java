package generator;

public class BNF_Grammar_Statements {
    String[] classbnf = {

            "<statements> ::= <statement> <statements>?",

            "<statement> ::= <assignment>; | <unaryExpression>; | <binaryAssignmentExpression>; | <ifStatement> | <whileStatement>",

            "<assignment> ::= <intExpressionLH> = <intExpression> | <stringExpressionLH> = <stringExpression> | <boolExpressionLH> = <boolExpression> ",
            "<expression> ::= <identifier> | <value>",
            "<expressionLH> ::= <identifier>",
            "<binaryExpression> ::= <identifier> <binaryOperator> <expression>",
            "<unaryExpression> ::= <unaryOperator> <intExpressionLH> | <intExpressionLH> <unaryOperator>",
            "<binaryAssignmentExpression> ::= <intExpressionLH> <binaryAssignmentOperator> <intExpression>",

            "<binaryOperator> ::= - | + | / | * | % ",
            "<binaryAssignmentOperator> ::= -= | += | /= | *= | %= ",
            "<unaryOperator> ::= ++ | -- ",
            "<ifStatement> ::= if( <booleanExpression> ){ <statements> }",
            "<whileStatement> ::= while( <booleanExpression> ){ <statements> }",

            "<booleanExpressions> ::= (<booleanExpression>) | (<booleanExpression>) <booleanBinaryOperator> (<booleanExpressions>)",
            "<booleanExpression> ::= <intExpression> <intComparison> <intExpression> | <stringExpression> <stringComparison> <stringExpression> | <boolExpression> <boolComparison> <boolExpression> ",

            "<intComparison> ::= < | > | <= | >= | != ",
            "<stringComparison> ::= == | != ",
            "<boolComparison> ::= == | != ",
            "<intExpression> ::= <expression int>",
            "<intExpressionLH> ::= <expressionLH int>",
            "<stringExpression> ::= <expression String>",
            "<stringExpressionLH> ::= <expressionLH String>",
            "<boolExpression> ::= <expression boolean>",
            "<boolExpressionLH> ::= <expressionLH boolean>"
    };

}