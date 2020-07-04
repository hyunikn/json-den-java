package com.github.hyunikn.jsonden.exception;

/**
  * Thrown when methods are invoked on an object of a wrong run-time type.
  */
public class Inapplicable extends Exception {
    public Inapplicable(String msg) {
        super(msg);
    }
}
