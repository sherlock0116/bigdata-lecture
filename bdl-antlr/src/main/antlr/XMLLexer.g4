lexer grammar XMLLexer;

@header{
package cgs.bigdata.antlr.xml;
}

// 默认"模式": 所有在标签之外的东西
OPEN            : '<'                   -> pushMode(INSIDE);
COMMENT   : '<!--' .*? '-->'  -> skip;
EntiryRef     : '&' [a-z]+ ';';

// 匹配除了<和&之外的 16 位字符
TEXT           : ~('<'|'&')+;

//--------------------- 所有标签之内的东西 ---------------------
mode INSIDE;
CLOSE               : '>'       -> popMode;     // 回退到默认模式
SLASH_MODE : '/>'      -> popMode;
EQUALS           : '=';
STRINGS         : '"' .*? '"';
SlashName        : '/' Name;
Name                 : ALPHA (ALPHA|DIGIT)*;
S                        : [ \t\r\n]+      -> skip;

fragment
ALPHA   : [a-zA-Z];

fragment
DIGIT   : [0-9];