
/** Taken from https://github.com/antlr/grammars-v4 */

/** Taken from "The Definitive ANTLR 4 Reference" by Terence Parr */

// Derived from http://json.org
grammar JsonParse;
options { tokenVocab = JsonLex; }

@header {
package com.wingsoft.jsonden.parser.antlrgen;
}

json
    : valueWithComment
    ;

obj
    : '{' pairWithComment (',' pairWithComment)* '}'
    | '{' '}'
    ;

pairWithComment
    : COMMENT_LINES? pair
    ;

pair
    : STRING ':' value
    ;

arr
    : '[' valueWithComment (',' valueWithComment)* ']'
    | '[' ']'
    ;

valueWithComment
    : COMMENT_LINES? value
    ;

value
    : STRING
    | NUMBER
    | obj
    | arr
    | 'true'
    | 'false'
    | 'null'
    ;
