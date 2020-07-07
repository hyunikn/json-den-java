
/** modified that in https://github.com/antlr/grammars-v4 */

parser grammar JsonParse;
options { tokenVocab = JsonLex; }

@header {
package com.github.hyunikn.jsonden.parser.antlrgen;
}

@members {
    public String getLiteralName(int idx) {
        return _LITERAL_NAMES.length > idx ? _LITERAL_NAMES[idx] : null;
    }
}

json
    : remarkedValue
    ;

remarkedValue
    : REMARK? value
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
    : LBRACE remarkedPair (COMMA remarkedPair)*? RBRACE
    | LBRACE RBRACE
    ;

remarkedPair
    : REMARK? pair
    ;

pair
    : STRING COLON value
    ;

arr
    : LBRACKET remarkedValue (COMMA remarkedValue)*? RBRACKET
    | LBRACKET RBRACKET
    ;
