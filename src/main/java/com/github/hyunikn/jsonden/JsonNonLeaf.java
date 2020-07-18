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

    /** Converts its hierarchical structure into a single map.
      * The keys of result map are paths to all the terminal proper subnodes.
      */
    public LinkedHashMap<String, Json> flatten() {
        LinkedHashMap<String, Json> accum = new LinkedHashMap<>();
        flattenInner(accum, null);
        return accum;
    }

    public abstract JsonNonLeaf clear();

    // ===================================================
    // Protected

    protected abstract void flattenInner(LinkedHashMap<String, Json> accum, String pathToMe);

    protected JsonNonLeaf delx(String path) {

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
            parent = getx(parentPath);
        }

        if (parent != null) {
            if (parent.isObj()) {
                parent.asObj().delete(divided[1]);
            } else if (parent.isArr()) {
                if (arrElemIndex != null) {
                    parent.asArr().delete(arrElemIndex.intValue());
                }
            }
        }

        return this;
    }

    protected JsonNonLeaf loadFlattened(LinkedHashMap<String, Json> flattened, boolean clone)
            throws UnreachablePath {

        for (Map.Entry<String, Json> e: flattened.entrySet()) {

            String key = e.getKey();
            Json val = e.getValue();

            if (!val.isTerminal()) {
                throw new IllegalArgumentException("flattened cannot contain a non-terminal value: " +
                        val.stringify(4));
            }

            if (clone) {
                val = (Json) val.clone();
            }

            setx(key, val);
        }

        return this;
    }

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
                int idx = arrElemIndex.intValue();

                JsonArr parentArr = parent.asArr();
                int currSize = parentArr.size();

                if (idx < currSize) {
                    parentArr.replace(idx, val);
                } else {
                    for (int i = currSize; i < idx; i++) {
                        parentArr.append(JsonNull.instance());  // fill the gap with nulls.
                    }
                    parentArr.append(val);
                }
            }
        } else {
            throw new UnreachablePath("node at '" + parentPath + "' cannot have a subnode because it is a " +
                    parent.getClass().getSimpleName());
        }

        return this;
    }

    protected Map<String, List<Json>> diffAtCommonPaths(JsonNonLeaf that) {

        LinkedHashMap<String, List<Json>> ret = new LinkedHashMap<>();

        LinkedHashMap<String, Json> flattened = flatten();
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

    protected Map<String, List<Json>> diff(JsonNonLeaf that) {

        LinkedHashMap<String, List<Json>> ret = new LinkedHashMap<>();

        LinkedHashMap<String, Json> flattened = flatten();
        for (Map.Entry<String, Json> e: flattened.entrySet()) {
            String key = e.getKey();
            Json thisValue = e.getValue();
            Json thatValue = that.getx(key);
            if (thatValue == null) {
                ret.put(key, Arrays.asList(thisValue, null));
            } else {
                if (!thisValue.equals(thatValue)) {
                    ret.put(key, Arrays.asList(thisValue, thatValue));
                }
            }
        }

        flattened = that.flatten();
        for (Map.Entry<String, Json> e: flattened.entrySet()) {
            String key = e.getKey();
            Json thisValue = this.getx(key);
            if (thisValue == null) {
                ret.put(key, Arrays.asList(null, e.getValue()));
            }
        }

        return ret;
    }

    protected LinkedHashMap<String, Json> intersectFlattened(JsonNonLeaf that) throws UnreachablePath {

        LinkedHashMap<String, Json> accum = new LinkedHashMap<>();

        LinkedHashMap<String, Json> flattened = flatten();
        for (Map.Entry<String, Json> e: flattened.entrySet()) {

            String key = e.getKey();
            Json thisValue = e.getValue();
            if (thisValue.equals(that.getx(key))) {
                accum.put(key, thisValue);
            }
        }

        return accum;
    }
    protected JsonNonLeaf intersect(JsonNonLeaf that) throws UnreachablePath {
        LinkedHashMap<String, Json> intersect = intersectFlattened(that);
        return this.clear().loadFlattened(intersect, false);
    }

    protected LinkedHashMap<String, Json> subtractFlattened(JsonNonLeaf that) throws UnreachablePath {

        LinkedHashMap<String, Json> accum = new LinkedHashMap<>();

        LinkedHashMap<String, Json> flattened = flatten();
        for (Map.Entry<String, Json> e: flattened.entrySet()) {

            String key = e.getKey();
            Json thisValue = e.getValue();
            if (!thisValue.equals(that.getx(key))) {
                accum.put(key, thisValue);
            }
        }

        return accum;
    }
    protected JsonNonLeaf subtract(JsonNonLeaf that) throws UnreachablePath {
        LinkedHashMap<String, Json> subtract = subtractFlattened(that);
        return this.clear().loadFlattened(subtract, false);
    }

    protected JsonNonLeaf merge(JsonNonLeaf that) throws UnreachablePath {

        LinkedHashMap<String, Json> flattened = that.flatten();
        for (Map.Entry<String, Json> e: flattened.entrySet()) {
            setx(e.getKey(), (Json) e.getValue().clone());
        }

        return this;
    }

    // ===================================================
    // Private

    private Json createMissingNodes(Json lastReachable, String[] segments,
            int idxOfFirstMissing, int typeOfLastNode) throws UnreachablePath {

        Json childNode = null;
        int len = segments.length;

        Json parentNode = lastReachable;
        for (int i = idxOfFirstMissing; i < len; i++) {
            String segment = segments[i];

            if (i == len - 1) {
                // the last to create
                switch (typeOfLastNode) {
                    case TYPE_OBJECT:
                        childNode = new JsonObj();
                        break;
                    case TYPE_ARRAY:
                        childNode = new JsonArr();
                        break;
                    default:
                        assert false;   // unreachable
                        childNode = null;   // just to make the compiler happy
                }
            } else {
                String nextSegment = segments[i + 1];
                Integer arrElemIndex = MyParseTreeVisitor.getArrElemIndex(nextSegment);
                if (arrElemIndex == null) {
                    childNode = new JsonObj();
                } else {
                    childNode = new JsonArr();
                }
            }

            if (parentNode.isObj()) {
                parentNode.asObj().set(segment, childNode);
            } else if (parentNode.isArr()) {

                JsonArr parentArr = parentNode.asArr();

                Integer arrElemIndex = MyParseTreeVisitor.getArrElemIndex(segment);
                if (arrElemIndex == null) {
                    throw new UnreachablePath("cannot create a node at '" +
                            String.join(".", Arrays.copyOfRange(segments, 0, i + 1)) +
                            "' because its parent is an array and the path segment '" +
                            segment + "' is not an array element index");
                }
                int idx = arrElemIndex.intValue();
                assert idx >= parentArr.size();

                for (int j = parentArr.size(); j < idx; j++) {
                    parentArr.append(JsonNull.instance());  // fill the gap with nulls.
                }
                parentArr.append(childNode);
            } else {
                assert false; // unreachable
            }

            parentNode = childNode;
        }

        assert childNode != null;
        return childNode;
    }

    private Json getxp(String path, int typeOfLastNode) throws UnreachablePath {

        assert (path != null);

        String[] segments = path.split("\\.", -1);  // -1: do not discard trailing empty strings
        int len = segments.length;

        Json childNode = null;
        Json parentNode = this;
        int i = 0;
        for (String s: segments) {
            childNode = parentNode.getChild(s);
            if (childNode == null) {
                // the last reachable node can be a simple node
                if (parentNode.isLeaf()) {
                    throw new UnreachablePath("cannot create a node at '" +
                            String.join(".", Arrays.copyOfRange(segments, 0, i + 1)) +
                            "' because its parent is a leaf node whose type is " +
                            parentNode.getClass().getSimpleName());
                }

                JsonArr parentArr = null;
                int origArrSize = -1;
                if (parentNode.isArr()) {
                    parentArr = parentNode.asArr();
                    origArrSize = parentArr.size();
                }

                try {
                    return createMissingNodes(parentNode, segments, i, typeOfLastNode); // returns the target node.
                } catch (Throwable e) {

                    // revert a possible side effect, that is, remove the possibly added child.
                    if (parentNode.isObj()) {
                        parentNode.asObj().remove(s);
                    } else if (parentNode.isArr()) {
                        while (parentArr.size() > origArrSize) {
                            parentArr.remove(-1);   // remove elem at the end
                        }
                        assert parentArr.size() == origArrSize;
                    } else {
                        assert false;
                    }
                    throw e;
                }
            } else {
                i++;
                parentNode = childNode;
            }
        }

        assert childNode != null;
        return childNode;
    }
}

