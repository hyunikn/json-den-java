package com.github.hyunikn.jsonden.exception;

/**
  * Thrown when a {@code Json} does not or cannot have a subnode at a required path.
  */
public class UnreachablePath extends Exception {
    public UnreachablePath(String msg) {
        super(msg);
    }
}
