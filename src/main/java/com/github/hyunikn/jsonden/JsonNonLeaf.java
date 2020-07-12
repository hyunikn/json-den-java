package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.UnreachablePath;
import com.github.hyunikn.jsonden.parser.MyParseTreeVisitor;

import java.math.BigInteger;
import java.math.BigDecimal;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedHashMap;

abstract class JsonNonLeaf extends Json {

    /**
      * Gets a proper subnode located at the path in the structure of this {@code Json}.
      * For example, one can use {@code json.getx("a.b.#0.c.d")} instead of
      * {@code json.get("a").get("b").get(0).get("c").get("d")}.
      * @param path dot delimited segments of a path to a subnode.
      *  Each segment represents either an object member key or an array element index (hash followed by an integer).
      * @return the Json located at the path if present, otherwise null.
      */
    public Json getx(String path) {

        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }

        String[] segments = path.split("\\.", -1);  // -1: do not discard trailing empty strings

        Json node = this;
        for (String s: segments) {
            node = node.getChild(s);
            if (node == null) {
                return null;
            }
        }

        return node;
    }

    /**
      * Returns whether a descendant JSON node exists at the path or not.
      * @param path same as in getx()
      * @throws java.lang.IllegalArgumentException when path is null
      */
    public boolean has(String path) throws IllegalArgumentException {
        return getx(path) != null;
    }

    /**
      * Returns the longest reachable prefix of the path.
      * @param path same as in getx()
      * @throws java.lang.IllegalArgumentException when path is null
      */
    public String longestReachablePrefix(String path) throws IllegalArgumentException {

        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }

        String[] segments = path.split("\\.", -1);  // -1: do not discard trailing empty strings

        int len = 0;
        Json node = this;
        for (String s: segments) {
            node = node.getChild(s);
            if (node == null) {
                break;
            } else {
                len++;
            }
        }

        if (len == 0) {
            return null;
        } else {
            return String.join(".", Arrays.copyOf(segments, len));
        }
    }

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

    protected JsonNonLeaf setx(String path, Json val) throws UnreachablePath {

        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }

        String[] divided = Jsons.dividePath(path);
        assert divided != null;
        String parentPath = divided[0];
        Integer arrElemIndex = MyParseTreeVisitor.getArrElemIndex(divided[1]);

        Json parent;
        if (parentPath == null) {
            parent = this;
        } else {
            parent = getxp(parentPath, arrElemIndex == null ? TYPE_OBJECT : TYPE_ARRAY);
        }

        if (parent.isObj()) {
            parent.asObj().set(divided[1], val);
        } else if (parent.isArr()) {
            if (arrElemIndex == null) {
                throw new UnreachablePath(
                        "the parent of the target node is an array but the last segment is not an integer");
            } else {
                JsonArr parentArr = parent.asArr();
                int idx = arrElemIndex.intValue();
                if (idx == parentArr.size()) {
                    parentArr.append(val);
                } else if (idx < parentArr.size()) {
                    parentArr.replace(idx, val);
                } else {
                    throw new UnreachablePath(
                            "the parent of the target node is an array but the last segment is " +
                            divided[1] + " which is larger than the size of the array");
                }
            }
        } else {
            throw new UnreachablePath("node at '" + parentPath + "' cannot have a subnode because it is a " +
                    parent.getClass().getSimpleName());
        }

        return this;
    }

    protected LinkedHashMap<String, List<Json>> diffAtCommonPaths(JsonNonLeaf that) {
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

    protected JsonNonLeaf loadFlattened(LinkedHashMap<String, Json> flattened, boolean clone) throws UnreachablePath {

        String prefixToSkip = null;
        for (Map.Entry<String, Json> e: flattened.entrySet()) {

            String key = e.getKey();
            if (".".equals(key)) {
                throw new IllegalArgumentException("flattened cannot contain a key \".\": " +
                        "this method sets only proper subnodes");
            }
            if (prefixToSkip != null) {
                if (key.startsWith(prefixToSkip)) {
                    continue;
                } else {
                    prefixToSkip = null;
                }
            }

            Json val = clone ? (Json) e.getValue().clone() : e.getValue();
            setx(key, val);
            if (prefixToSkip == null && val instanceof JsonNonLeaf) {
                prefixToSkip = key + ".";
            }
        }
        return this;
    }

    protected LinkedHashMap<String, Json> intersect(JsonNonLeaf that) {

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
                    if (prefixToSkip == null && thisValue instanceof JsonNonLeaf) {
                        prefixToSkip = key + ".";
                    }
                }
            }
        }

        return ret;
    }

    protected LinkedHashMap<String, Json> subtract(JsonNonLeaf that) {
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
                if (prefixToSkip == null && value instanceof JsonNonLeaf) {
                    prefixToSkip = key + ".";
                }
            }
        }

        return ret;
    }

    protected LinkedHashMap<String, Json> intersectLeaves(JsonNonLeaf that) {
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

    protected LinkedHashMap<String, Json> subtractLeaves(JsonNonLeaf that) {
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

