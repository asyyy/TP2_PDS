lexer grammar VSLLexer;

options {
  language = Java;
}

@header {
  package TP2;
}

WS : (' '|'\n'|'\t') -> skip
   ;

COMMENT : '//' (~'\n')* -> skip
        ;

fragment LETTER : 'a'..'z' ;
fragment DIGIT  : '0'..'9' ;
fragment ASCII  : ~('\n'|'"');

// keywords
LP    : '(' ;
RP    : ')' ;
LC : '[';
RC : ']';
PLUS : '+';
MOINS : '-' ;
DIV   : '/' ;
MUL : '*' ;
AFFECT: ':=';
ACCOL : '{';
ACCOR : '}' ;
VIRG : ',';
FUNC : 'FUNC';
INT : 'INT';
VOID :'VOID';
IF : 'IF';
FI : 'FI';
THEN : 'THEN';
ELSE : 'ELSE';
WHILE : 'WHILE';
DO : 'DO';
DONE : 'DONE';
PROTO : 'PROTO';
RETURN : 'RETURN';
PRINT : 'PRINT';
READ : 'READ';


// TODO : other keywords

// other tokens (no conflict with keywords in VSL)
IDENT   : LETTER (LETTER|DIGIT)*;
TEXT    : '"' (ASCII)* '"' { setText(getText().substring(1, getText().length() - 1)); };
INTEGER : (DIGIT)+ ;
