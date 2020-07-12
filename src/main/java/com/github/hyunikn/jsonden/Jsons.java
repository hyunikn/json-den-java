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
                        left == null ? "---" : left.stringify(4),
                        right == null ? "---" : right.stringify(4)));
        }
        return sbuf.toString();
    }

    /** Diffs two {@code JsonObj}s.
      * Unlike {@code diffLeaves}, the result can contain an empty object or an empty array at some paths.
      * @return map from paths to pairs of {@code Json} values. In each pair, the first element comes from {@code n1}
      * and the second one comes from {@code n2}. These two {@code Json} values in each pair are always different
      * in the result map.
      */
    public static Map<String, List<Json>> diff(JsonObj n1, JsonObj n2) {
        return diff((JsonNonLeaf) n1, (JsonNonLeaf) n2, false);
    }

    /** Diffs two {@code JsonArr}s.
      * Unlike {@code diffLeaves}, the result can contain an empty object or an empty array at some paths.
      * @return map from paths to pairs of {@code Json} values. In each pair, the first element comes from {@code n1}
      * and the second one comes from {@code n2}. These two {@code Json} values in each pair are always different
      * in the result map.
      */
    public static Map<String, List<Json>> diff(JsonArr n1, JsonArr n2) {
        return diff((JsonNonLeaf) n1, (JsonNonLeaf) n2, false);
    }

    /** Diffs leaf nodes of two {@code JsonObj}s.
      * Unlike {@code diff}, {@code diffLeaves} compares only values at leaf positions.
      * @return map from paths to pairs of {@code Json} values. In each pair, the first element comes from {@code n1}
      * and the second one comes from {@code n2}. These two {@code Json} values in each pair are always different
      * in the result map.
      */
    public static Map<String, List<Json>> diffLeaves(JsonObj n1, JsonObj n2) {
        return diff((JsonNonLeaf) n1, (JsonNonLeaf) n2, true);
    }

    /** Diffs leaf nodes of two {@code JsonArr}s.
      * Unlike {@code diff}, {@code diffLeaves} compares only values at leaf positions.
      * @return map from paths to pairs of {@code Json} values. In each pair, the first element comes from {@code n1}
      * and the second one comes from {@code n2}. These two {@code Json} values in each pair are always different
      * in the result map.
      */
    public static Map<String, List<Json>> diffLeaves(JsonArr n1, JsonArr n2) {
        return diff((JsonNonLeaf) n1, (JsonNonLeaf) n2, true);
    }

    /** Intersects two {@code JsonObj}s.
      * Gets the common part, common paths and the identical values at each of the common paths,
      * of {@code n1} and {@code n2}.
      * @return {@code JsonObj} that represent the result. All the subnodes in the result are clones of their sources.
      */
    public static JsonObj intersect(JsonObj n1, JsonObj n2) throws UnreachablePath {
        JsonObj accum = JsonObj.instance();
        return (JsonObj) intersect(accum, (JsonNonLeaf) n1, (JsonNonLeaf) n2, false);
    }

    /** Intersects two {@code JsonArr}s.
      * Gets the common part, common paths and the identical values at each of the common paths,
      * of {@code n1} and {@code n2}.
      * @return {@code JsonArr} that represent the result. All the subnodes in the result are clones of their sources.
      */
    public static JsonArr intersect(JsonArr n1, JsonArr n2) throws UnreachablePath {
        JsonArr accum = JsonArr.instance();
        return (JsonArr) intersect(accum, (JsonNonLeaf) n1, (JsonNonLeaf) n2, false);
    }

    /** Intersects leaf nodes of two {@code JsonObj}s.
      * Gets the common leaves, common paths and the identical values at each of the common paths,
      * of {@code n1} and {@code n2}.
      * @return {@code JsonObj} that represent the result. All the subnodes in the result are clones of their sources.
      */
    public static JsonObj intersectLeaves(JsonObj n1, JsonObj n2) throws UnreachablePath {
        JsonObj accum = JsonObj.instance();
        return (JsonObj) intersect(accum, (JsonNonLeaf) n1, (JsonNonLeaf) n2, true);
    }

    /** Intersects leaf nodes of two {@code JsonArr}s.
      * Gets the common leaves, common paths and the identical values at each of the common paths,
      * of {@code n1} and {@code n2}.
      * @return {@code JsonArr} that represent the result. All the subnodes in the result are clones of their sources.
      */
    public static JsonArr intersectLeaves(JsonArr n1, JsonArr n2) throws UnreachablePath {
        JsonArr accum = JsonArr.instance();
        return (JsonArr) intersect(accum, (JsonNonLeaf) n1, (JsonNonLeaf) n2, true);
    }

    /** Subtracts two {@code JsonObj}s.
      * Gets paths which are in {@code n1} but not in {@code n2} and {@code Json} values at those paths.
      * @return {@code JsonObj} that represent the result. All the subnodes in the result are clones of their sources.
      */
    public static JsonObj subtract(JsonObj n1, JsonObj n2) throws UnreachablePath {
        JsonObj accum = JsonObj.instance();
        subtract(accum, (JsonNonLeaf) n1, (JsonNonLeaf) n2, false);
        return accum;
    }

    /** Subtracts two {@code JsonArr}s.
      * Gets paths which are in {@code n1} but not in {@code n2} and {@code Json} values at those paths.
      * @return {@code JsonArr} that represent the result. All the subnodes in the result are clones of their sources.
      */
    public static JsonArr subtract(JsonArr n1, JsonArr n2) throws UnreachablePath {
        JsonArr accum = JsonArr.instance();
        subtract(accum, (JsonNonLeaf) n1, (JsonNonLeaf) n2, false);
        return accum;
    }

    /** Subtracts leaf nodes of two {@code JsonObj}s.
      * Gets leaf paths which are in {@code n1} but not in {@code n2} and {@code Json} values at those paths.
      * @return {@code JsonObj} that represent the result. All the subnodes in the result are clones of their sources.
      */
    public static JsonObj subtractLeaves(JsonObj n1, JsonObj n2) throws UnreachablePath {
        JsonObj accum = JsonObj.instance();
        subtract(accum, (JsonNonLeaf) n1, (JsonNonLeaf) n2, true);
        return accum;
    }

    /** Subtracts leaf nodes of two {@code JsonArr}s.
      * Gets leaf paths which are in {@code n1} but not in {@code n2} and {@code Json} values at those paths.
      * @return {@code JsonArr} that represent the result. All the subnodes in the result are clones of their sources.
      */
    public static JsonArr subtractLeaves(JsonArr n1, JsonArr n2) throws UnreachablePath {
        JsonArr accum = JsonArr.instance();
        subtract(accum, (JsonNonLeaf) n1, (JsonNonLeaf) n2, true);
        return accum;
    }

    /** Overlaps two {@code JsonObj}s.
      * Overwrites nodes of {@code n2} onto nodes of {@code n1}.
      * @return {@code JsonObj} that represent the result. All the subnodes in the result are clones of their sources.
      */
    public static JsonObj overlap(JsonObj n1, JsonObj n2) throws UnreachablePath {
        return (JsonObj) overlap((JsonNonLeaf) n1, (JsonNonLeaf) n2, false);
    }

    /** Overlaps two {@code JsonArr}s.
      * Overwrites nodes of {@code n2} onto nodes of {@code n1}.
      * @return {@code JsonArr} that represent the result. All the subnodes in the result are clones of their sources.
      */
    public static JsonArr overlap(JsonArr n1, JsonArr n2) throws UnreachablePath {
        return (JsonArr) overlap((JsonNonLeaf) n1, (JsonNonLeaf) n2, false);
    }

    /** Overlaps leaf nodes of two {@code JsonObj}s.
      * Overwrites leaf nodes of {@code n2} onto leaf nodes of {@code n1}.
      * @return {@code JsonObj} that represent the result. All the subnodes in the result are clones of their sources.
      */
    public static JsonObj overlapLeaves(JsonObj n1, JsonObj n2) throws UnreachablePath {
        return (JsonObj) overlap((JsonNonLeaf) n1, (JsonNonLeaf) n2, true);
    }

    /** Overlaps leaf nodes of two {@code JsonArr}s.
      * Overwrites leaf nodes of {@code n2} onto leaf nodes of {@code n1}.
      * @return {@code JsonArr} that represent the result. All the subnodes in the result are clones of their sources.
      */
    public static JsonArr overlapLeaves(JsonArr n1, JsonArr n2) throws UnreachablePath {
        return (JsonArr) overlap((JsonNonLeaf) n1, (JsonNonLeaf) n2, true);
    }

    // ============================================================
    // Private

    private Jsons() { }

    private static Map<String, List<Json>> diff(JsonNonLeaf n1, JsonNonLeaf n2, boolean forLeaves) {

        Map<String, List<Json>> ret = n1.diffAtCommonPaths(n2);

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

        JsonNonLeaf ret;
        if (forLeaves) {
            ret = n1.isObj() ? JsonObj.instance() : JsonArr.instance();
            ret.loadFlattened(n1.flatten(), true);
        } else {
            ret = (JsonNonLeaf) n1.clone();
        }

        LinkedHashMap<String, Json> flattened = forLeaves ? n2.flatten() : n2.flatten2();
        return ret.loadFlattened(flattened, true);
    }
}

