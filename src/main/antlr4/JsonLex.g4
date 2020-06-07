
/** modified that in https://github.com/antlr/grammars-v4 */

lexer grammar JsonLex;

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

WS
    : [ \t\n\r] + -> skip
    ;

PRESERVED_COMMENT_START
    : '/**' -> pushMode(PRESERVED)
    ;

DROPPED_BLOCK_COMMENT
    : '/*' .*? '*/' -> skip
    ;

DROPPED_LINE_COMMENT
    : '//' .*? [\n] -> skip
    ;

// ------------------------------------------------------

mode PRESERVED;

// TODO: count depth
PRESERVED_COMMENT_END
    : '*/' -> popMode
    ;

COMMENT_LINES
    : .*?
    ;
