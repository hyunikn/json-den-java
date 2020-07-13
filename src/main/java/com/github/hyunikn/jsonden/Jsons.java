package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.UnreachablePath;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedHashMap;

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

    /** Gets a string which represents a flattened {@code Json} value.
      * This is mainly for debugging.
      */
    public static String prettyPrintFlattened(LinkedHashMap<String, Json> flattened) {

        StringBuffer sbuf = new StringBuffer();
        for (Map.Entry<String, Json> e: flattened.entrySet()) {
            sbuf.append(String.format("\n%s: %s", e.getKey(), e.getValue().stringify(4)));
        }
        return sbuf.toString();
    }

    /** Gets a string which represents a result of diffing two {@code Json} values.
      * This is mainly for debugging.
      */
    public static String prettyPrintDiff(Map<String, List<Json>> diff) {

        StringBuffer sbuf = new StringBuffer();
        for (Map.Entry<String, List<Json>> e: diff.entrySet()) {
            List<Json> list = e.getValue();
            if (list.size() != 2) {
                throw new IllegalArgumentException("every entry of diff must have two Jsons as its value," +
                       " whi is not for " + list);
            }
            Json left = list.get(0);
            Json right = list.get(1);
            sbuf.append(String.format("\n\n%s:\nL: %s\nR: %s", e.getKey(),
                        left == null ? "<none>" : left.stringify(4),
                        right == null ? "<none>" : right.stringify(4)));
        }
        return sbuf.toString();
    }

    // ============================================================
    // Private

    private Jsons() { }
}

