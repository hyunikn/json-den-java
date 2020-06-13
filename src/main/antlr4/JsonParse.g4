
/** modified that in https://github.com/antlr/grammars-v4 */

parser grammar JsonParse;
options { tokenVocab = JsonLex; }

@header {
package com.wingsoft.jsonden.parser.antlrgen;
}

@members {
    public String getLiteralName(int idx) {
        return _LITERAL_NAMES.length > idx ? _LITERAL_NAMES[idx] : null;
    }
}

json
    : commentedValue
    ;

commentedValue
    : COMMENT? value
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
    : COMMENT? pair
    ;

pair
    : STRING COLON value
    ;

arr
    : LBRACKET commentedValue (COMMA commentedValue)* RBRACKET
    | LBRACKET RBRACKET
    ;
