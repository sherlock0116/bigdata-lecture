grammar Expr;

@header{
package cgs.bigdata.antlr.expr;
}

prog: stat+;

stat: expr NEWLINE
       | ID '=' expr NEWLINE
       | NEWLINE
       ;

expr: expr ('*'|'/') expr
       | expr ('+'|'-') expr
       | INT
       | ID
       | '(' expr ')'
       ;

INT: [1-9][0-9]*;
ID: [a-zA-Z]+;
NEWLINE: '\r'? '\n';
WS: [ \t]+ -> skip;