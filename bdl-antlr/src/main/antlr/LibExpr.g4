grammar LibExpr;

import CommonLexerRules;

@header{
package cgs.bigdata.antlr.libexpr;
}

prog: stat+ ;
stat: expr NEWLINE
       | ID '=' expr
       | NEWLINE
       ;

expr: expr ('+'|'-') expr
       | expr ('*'|'/') expr
       | INT
       | ID
       | '(' expr ')'
       ;

