package com.wingsoft.jsonden.exception;

public class ParseError extends Exception {
    public ParseError(String msg) {
        super(msg);
    }

    public ParseError(String msg, Throwable t) {
        super(msg, t);
    }
}
