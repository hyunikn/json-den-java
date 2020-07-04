package com.github.hyunikn.jsonden;

/** Collection of utitlity functions */

public class Util {

    /** Divides the path into parent and child parts.
      * Parent part is null if the path has only one segment.
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

