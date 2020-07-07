
/** modified that in https://github.com/antlr/grammars-v4 */

lexer grammar JsonLex;

@header {
package com.github.hyunikn.jsonden.parser.antlrgen;
}

STRING
    : '"' (ESC | SAFECODEPOINT)* '"'
    ;

fragment ESC
    : '\\' (["\\/bfnrt] | UNICODE)
    ;

fragment UNICODE
    : 'u' HEX HEX HEX HEX
    ;

fragment HEX
    : [0-9a-fA-F]
    ;

fragment SAFECODEPOINT
    : ~ ["\\\u0000-\u001F]
    ;

NUMBER
    : '-'? INT ('.' [0-9] +)? EXP?
    ;

fragment INT
    : '0' | [1-9] [0-9]*
    ;

// no leading zeros

fragment EXP
    : [Ee] [+\-]? INT
    ;

// \- since - means "range" inside [...]

LBRACE: '{';
RBRACE: '}';
LBRACKET: '[';
RBRACKET: ']';
COMMA: ',';
COLON: ':';
TRUE: 'true';
FALSE: 'false';
NULL: 'null';

REMARK
    : [\n\r\f] ' '*? '/**' .*? '*/'
    ;

BLOCK_COMMENT
    : '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    : '//' .*? [\n] -> skip
    ;

WS
    : [ \t\n\r] -> skip
    ;
