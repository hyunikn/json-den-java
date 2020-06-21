package com.wingsoft.jsonden.exception;

/**
  * Exception thrown when the parse() method of the {@link com.wingsoft.jsonden.Json Json}
  * class or one of its subclasses fails.
  * Currently, there are two cases of this kind of failure:
  * (1) wrong JSON syntax (2) unexpected JSON type (3) a duplicate key in a JSON object.
  * A public final field {@code errCase} indicates one of them.
  * Method {@code getMessage()} will return more detailed description of the error.
  */
public class ParseError extends Exception {

    public static final int CASE_WRONG_JSON_SYNTAX      = 0;
    public static final int CASE_UNEXPECTED_JSON_TYPE   = 1;
    public static final int CASE_DUPLICATE_KEY          = 2;

    final public int errCase;

    public ParseError(int errCase, String msg) {
        super(msg);
        this.errCase = errCase;
    }
}
