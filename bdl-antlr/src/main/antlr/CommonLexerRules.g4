lexer grammar CommonLexerRules;

NEWLINE: '\r'? '\n';
ID: [a-zA-Z]+;
INT: [1-9][0-9]*;
WS: [ \t]+ -> skip;