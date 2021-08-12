grammar ArrayInit;

@header{
package cgs.bigdata.antlr.arrayinit;
}

init : '{' value (',' value)* '}';
value : init
         | INT
         ;

INT : [1-9][0-9]*;
WS  : [ \t\r\n]+ -> skip;