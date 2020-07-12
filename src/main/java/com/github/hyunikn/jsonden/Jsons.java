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

    /**
      * TODO
      */
    public static String prettyPrintFlattened(LinkedHashMap<String, Json> flattened) {

        StringBuffer sbuf = new StringBuffer();
        for (Map.Entry<String, Json> e: flattened.entrySet()) {
            sbuf.append(String.format("\n%s: %s", e.getKey(), e.getValue().stringify(4)));
        }
        return sbuf.toString();
    }

    /**
      * TODO
      */
    public static String prettyPrintDiff(LinkedHashMap<String, List<Json>> diff) {

        StringBuffer sbuf = new StringBuffer();
        for (Map.Entry<String, List<Json>> e: diff.entrySet()) {
            List<Json> list = e.getValue();
            if (list.size() != 2) {
                throw new IllegalArgumentException("every entry of diff must have two Jsons as its value," +
                       " whi is not for " + list);
            }
            sbuf.append(String.format("\n. %s:\nL: %s\nR: %s", e.getKey(),
                        list.get(0).stringify(4), list.get(1).stringify(4)));
        }
        return sbuf.toString();
    }

    /**
      * TODO
      */
    public static LinkedHashMap<String, List<Json>> diff(JsonObj o1, JsonObj o2) {
        return diff((JsonNonLeaf) o1, (JsonNonLeaf) o2, false);
    }

    /**
      * TODO
      */
    public static LinkedHashMap<String, List<Json>> diff(JsonArr a1, JsonArr a2) {
        return diff((JsonNonLeaf) a1, (JsonNonLeaf) a2, false);
    }

    /**
      * TODO
      */
    public static LinkedHashMap<String, List<Json>> diffLeaves(JsonObj o1, JsonObj o2) {
        return diff((JsonNonLeaf) o1, (JsonNonLeaf) o2, true);
    }

    /**
      * TODO
      */
    public static LinkedHashMap<String, List<Json>> diffLeaves(JsonArr a1, JsonArr a2) {
        return diff((JsonNonLeaf) a1, (JsonNonLeaf) a2, true);
    }

    /**
      * TODO
      */
    public static JsonObj intersect(JsonObj o1, JsonObj o2) throws UnreachablePath {
        JsonObj accum = JsonObj.instance();
        return (JsonObj) intersect(accum, (JsonNonLeaf) o1, (JsonNonLeaf) o2, false);
    }

    /**
      * TODO
      */
    public static JsonArr intersect(JsonArr a1, JsonArr a2) throws UnreachablePath {
        JsonArr accum = JsonArr.instance();
        return (JsonArr) intersect(accum, (JsonNonLeaf) a1, (JsonNonLeaf) a2, false);
    }

    /**
      * TODO
      */
    public static JsonObj intersectLeaves(JsonObj o1, JsonObj o2) throws UnreachablePath {
        JsonObj accum = JsonObj.instance();
        return (JsonObj) intersect(accum, (JsonNonLeaf) o1, (JsonNonLeaf) o2, true);
    }

    /**
      * TODO
      */
    public static JsonArr intersectLeaves(JsonArr a1, JsonArr a2) throws UnreachablePath {
        JsonArr accum = JsonArr.instance();
        return (JsonArr) intersect(accum, (JsonNonLeaf) a1, (JsonNonLeaf) a2, true);
    }

    /**
      * TODO
      */
    public static JsonObj subtract(JsonObj o1, JsonObj o2) throws UnreachablePath {
        JsonObj accum = JsonObj.instance();
        subtract(accum, (JsonNonLeaf) o1, (JsonNonLeaf) o2, false);
        return accum;
    }

    /**
      * TODO
      */
    public static JsonArr subtract(JsonArr a1, JsonArr a2) throws UnreachablePath {
        JsonArr accum = JsonArr.instance();
        subtract(accum, (JsonNonLeaf) a1, (JsonNonLeaf) a2, false);
        return accum;
    }

    /**
      * TODO
      */
    public static JsonObj subtractLeaves(JsonObj o1, JsonObj o2) throws UnreachablePath {
        JsonObj accum = JsonObj.instance();
        subtract(accum, (JsonNonLeaf) o1, (JsonNonLeaf) o2, true);
        return accum;
    }

    /**
      * TODO
      */
    public static JsonArr subtractLeaves(JsonArr a1, JsonArr a2) throws UnreachablePath {
        JsonArr accum = JsonArr.instance();
        subtract(accum, (JsonNonLeaf) a1, (JsonNonLeaf) a2, true);
        return accum;
    }

    /**
      * TODO
      */
    public static JsonObj overlap(JsonObj o1, JsonObj o2) throws UnreachablePath {
        return (JsonObj) overlap((JsonNonLeaf) o1, (JsonNonLeaf) o2, false);
    }

    /**
      * TODO
      */
    public static JsonArr overlap(JsonArr a1, JsonArr a2) throws UnreachablePath {
        return (JsonArr) overlap((JsonNonLeaf) a1, (JsonNonLeaf) a2, false);
    }

    /**
      * TODO
      */
    public static JsonObj overlapLeaves(JsonObj o1, JsonObj o2) throws UnreachablePath {
        return (JsonObj) overlap((JsonNonLeaf) o1, (JsonNonLeaf) o2, true);
    }

    /**
      * TODO
      */
    public static JsonArr overlapLeaves(JsonArr a1, JsonArr a2) throws UnreachablePath {
        return (JsonArr) overlap((JsonNonLeaf) a1, (JsonNonLeaf) a2, true);
    }

    // ============================================================
    // Private

    private static LinkedHashMap<String, List<Json>> diff(JsonNonLeaf n1, JsonNonLeaf n2, boolean forLeaves) {

        LinkedHashMap<String, List<Json>> ret = n1.diffAtCommonPaths(n2);

        LinkedHashMap<String, Json> left = forLeaves ? n1.subtractLeaves(n2) : n1.subtract(n2);
        for (Map.Entry<String, Json> e: left.entrySet()) {
            ret.put(e.getKey(), Arrays.asList(e.getValue(), null));
        }

        LinkedHashMap<String, Json> right = forLeaves ? n2.subtractLeaves(n1) : n2.subtract(n1);
        for (Map.Entry<String, Json> e: right.entrySet()) {
            ret.put(e.getKey(), Arrays.asList(null, e.getValue()));
        }

        return ret;
    }

    private static JsonNonLeaf intersect(JsonNonLeaf accum, JsonNonLeaf n1, JsonNonLeaf n2, boolean forLeaves)
            throws UnreachablePath {

        // trivial case
        if (!forLeaves && n1.equals(n2)) {
            return (JsonNonLeaf) n1.clone();
        }

        LinkedHashMap<String, Json> flattened = forLeaves ? n1.intersectLeaves(n2) : n1.intersect(n2);
        return accum.loadFlattened(flattened, true);
    }

    private static void subtract(JsonNonLeaf accum, JsonNonLeaf n1, JsonNonLeaf n2, boolean forLeaves)
            throws UnreachablePath {

        LinkedHashMap<String, Json> flattened = forLeaves ? n1.subtractLeaves(n2) : n1.subtract(n2);
        accum.loadFlattened(flattened, true);
    }

    private static JsonNonLeaf overlap(JsonNonLeaf n1, JsonNonLeaf n2, boolean forLeaves) throws UnreachablePath {

        LinkedHashMap<String, Json> flattened = forLeaves ? n2.flatten() : n2.flatten2();
        return ((JsonNonLeaf) n1.clone()).loadFlattened(flattened, true);
    }
}

