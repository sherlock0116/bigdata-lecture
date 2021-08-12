grammar Hello;

// package
@header {
package cgs.bigdata.antlr.hello;
}

r : 'hello' ID;
ID : [a-z]+;
WS : [\t\r\n]+ -> skip;