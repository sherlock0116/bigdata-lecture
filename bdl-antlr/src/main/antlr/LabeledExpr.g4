grammar LabeledExpr;

import CommonLexerRules;

@header{
package cgs.bigdata.antlr.labeledexpr;
}

prog: stat+;

stat : expr NEWLINE                 # printExpr
		| ID '=' expr NEWLINE      # assign
		| NEWLINE                         # blank
		| CLEAR                                # clear
		;

expr : expr op=('+'|'-') expr         # AddSub
		| expr op=('*'|'/') expr         # MulDiv
		| INT                                    # int
		| ID                                       # id
		| '(' expr ')'                          # parents
		;

CLEAR: ('CLEAR'|'clear');
MUL: '*';
DIV: '/';
ADD: '+';
SUB: '-';
