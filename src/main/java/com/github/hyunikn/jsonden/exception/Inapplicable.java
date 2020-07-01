package com.github.hyunikn.jsonden.exception;

/**
  * Thrown when value conversion methods are invoked on an object of a wrong run-time type.
  * {@code Json} class has methods to convert an object of each subclass to an object of matching Java native type.
  * Those methods throws this exception by defualt, and each subclass overrides appropriate subset of them.
  */
public class Inapplicable extends Exception {
    public Inapplicable(String msg) {
        super(msg);
    }
}
