
/** modified that in https://github.com/antlr/grammars-v4 */

parser grammar JsonParse;
options { tokenVocab = JsonLex; }

@header {
package com.wingsoft.jsonden.parser.antlrgen;
}

json
    : valueWithComment
    ;

obj
    : LBRACE pairWithComment (COMMA pairWithComment)* RBRACE
    | LBRACE RBRACE
    ;

pairWithComment
    : COMMENT_LINES? pair
    ;

pair
    : STRING COLON value
    ;

arr
    : LBRACKET valueWithComment (COMMA valueWithComment)* RBRACKET
    | LBRACKET RBRACKET
    ;

valueWithComment
    : COMMENT_LINES? value
    ;

value
    : STRING
    | NUMBER
    | TRUE
    | FALSE
    | NULL
    | obj
    | arr
    ;
