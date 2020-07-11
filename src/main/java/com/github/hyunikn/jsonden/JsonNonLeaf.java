package com.github.hyunikn.jsonden;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedHashMap;

abstract class JsonNonLeaf extends Json {

    /**
      * Converts its hierarchical structure into a single map.
      * The resulting map has paths to all the leaf proper subnodes as its keys.
      */
    public LinkedHashMap<String, Json> flatten() {
        LinkedHashMap<String, Json> accum = new LinkedHashMap<>();
        flattenInner(accum, null, false);
        return accum;
    }

    /**
      * Converts its hierarchical structure into a single map.
      * The resulting map has paths to all the proper subnodes as its keys.
      */
    public LinkedHashMap<String, Json> flatten2() {
        LinkedHashMap<String, Json> accum = new LinkedHashMap<>();
        flattenInner(accum, null, true);
        return accum;
    }

    // ===================================================
    // Protected

    protected LinkedHashMap<String, List<Json>> diffAtCommonPaths(Json that) {
        LinkedHashMap<String, Json> flattened = flatten();

        LinkedHashMap<String, List<Json>> ret = new LinkedHashMap<>();
        for (Map.Entry<String, Json> e: flattened.entrySet()) {
            String key = e.getKey();
            Json thatValue = that.getx(key);
            if (thatValue != null) {
                Json thisValue = e.getValue();
                if (!thisValue.equals(thatValue)) {
                    ret.put(key, Arrays.asList(thisValue, thatValue));
                }
            }
        }

        return ret;
    }

    protected LinkedHashMap<String, Json> intersect(Json that) {

        LinkedHashMap<String, Json> ret;

        // a trivial case
        if (this.equals(that)) {
            ret = new LinkedHashMap<>();
            ret.put(".", this);
            return ret;
        }

        LinkedHashMap<String, Json> flattened = flatten2();

        String prefixToSkip = null;
        ret = new LinkedHashMap<>();
        for (Map.Entry<String, Json> e: flattened.entrySet()) {

            String key = e.getKey();
            if (prefixToSkip != null) {
                if (key.startsWith(prefixToSkip)) {
                    continue;
                } else {
                    prefixToSkip = null;
                }
            }

            Json thatValue = that.getx(key);
            if (thatValue != null) {
                Json thisValue = e.getValue();
                if (thisValue.equals(thatValue)) {
                    ret.put(key, thisValue);
                    if (prefixToSkip == null && !(thisValue instanceof JsonLeaf)) {
                        prefixToSkip = key + ".";
                    }
                }
            }
        }

        return ret;
    }

    protected LinkedHashMap<String, Json> subtract(Json that) {
        LinkedHashMap<String, Json> flattened = flatten2();

        String prefixToSkip = null;
        LinkedHashMap<String, Json> ret = new LinkedHashMap<>();
        for (Map.Entry<String, Json> e: flattened.entrySet()) {

            String key = e.getKey();
            if (prefixToSkip != null) {
                if (key.startsWith(prefixToSkip)) {
                    continue;
                } else {
                    prefixToSkip = null;
                }
            }

            if (!that.has(key)) {
                Json value = e.getValue();
                ret.put(key, value);
                if (prefixToSkip == null && !(value instanceof JsonLeaf)) {
                    prefixToSkip = key + ".";
                }
            }
        }

        return ret;
    }

    protected LinkedHashMap<String, Json> intersectLeaves(Json that) {
        LinkedHashMap<String, Json> flattened = flatten();

        LinkedHashMap<String, Json> ret = new LinkedHashMap<>();
        for (Map.Entry<String, Json> e: flattened.entrySet()) {
            String key = e.getKey();
            Json thatValue = that.getx(key);
            if (thatValue != null) {
                Json thisValue = e.getValue();
                if (thisValue.equals(thatValue)) {
                    ret.put(key, thisValue);
                }
            }
        }

        return ret;
    }

    protected LinkedHashMap<String, Json> subtractLeaves(Json that) {
        LinkedHashMap<String, Json> flattened = flatten();

        LinkedHashMap<String, Json> ret = new LinkedHashMap<>();
        for (Map.Entry<String, Json> e: flattened.entrySet()) {
            String key = e.getKey();
            if (!that.has(key)) {
                ret.put(key, e.getValue());
            }
        }

        return ret;
    }

    protected abstract void flattenInner(
            LinkedHashMap<String, Json> accum, String pathToMe, boolean addNonLeafToo);
}

