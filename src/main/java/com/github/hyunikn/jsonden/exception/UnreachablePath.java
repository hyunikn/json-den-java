package com.github.hyunikn.jsonden.exception;

/**
  * Thrown when a Json does not have a subnode at the specified path.
  */
public class UnreachablePath extends Exception {
    public UnreachablePath(String msg) {
        super(msg);
    }
}
