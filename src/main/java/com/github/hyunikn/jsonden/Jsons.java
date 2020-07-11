package com.github.hyunikn.jsonden;

/**
  * Utility functions for {@code Json}s
  */
public class Jsons {

    /** Divides the path into parent and child parts.
      * Parent part is null if the path has only one segment.
      * @return two element String array whose first element is the parent part, and the second is the child part.
      */
    public static String[] dividePath(String path) {
        if (path == null) {
            return null;
        }

        int idx = path.lastIndexOf('.');
        if (idx < 0) {
            return new String[]{ null, path };    // no parent part.
        } else {
            return new String[]{ path.substring(0, idx), path.substring(idx + 1) };
        }
    }
}

