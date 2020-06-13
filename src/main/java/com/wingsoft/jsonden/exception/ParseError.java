package com.wingsoft.jsonden.exception;

public class ParseError extends Error {
    public ParseError(String msg) {
        super(msg);
    }

    public ParseError(String msg, Throwable t) {
        super(msg, t);
    }
}
