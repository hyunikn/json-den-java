
/** modified that in https://github.com/antlr/grammars-v4 */

parser grammar JsonParse;
options { tokenVocab = JsonLex; }

@header {
package com.wingsoft.jsonden.parser.antlrgen;
}

json
    : commentedValue
    ;

commentedValue
    : COMMENTS? value
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

obj
    : LBRACE commentedPair (COMMA commentedPair)* RBRACE
    | LBRACE RBRACE
    ;

commentedPair
    : COMMENTS? pair
    ;

pair
    : STRING COLON value
    ;

arr
    : LBRACKET commentedValue (COMMA commentedValue)* RBRACKET
    | LBRACKET RBRACKET
    ;
