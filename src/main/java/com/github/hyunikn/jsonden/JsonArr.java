package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.ParseError;
import com.github.hyunikn.jsonden.exception.UnreachablePath;

import com.github.hyunikn.jsonden.parser.MyParseTreeVisitor;

import java.io.IOException;

import java.math.BigInteger;
import java.math.BigDecimal;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.LinkedHashMap;

/**
  * A subclass of {@link com.github.hyunikn.jsonden.Json Json} which represents JSON arrays.
  */
public class JsonArr extends JsonNonLeaf {

    // ===================================================
    // Public

    /**
      * Gets an empty {@code JsonArr}.
      */
    public static JsonArr instance() {
        return new JsonArr();
    }

    /**
      * Gets a {@code JsonArr} from a list.
      * @param list must not be null.
      */
    public static JsonArr instance(List<Json> list) {
        return new JsonArr(list);
    }

    /**
      * Checks the value (not reference) equality
      */
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        JsonArr that = (JsonArr) o;
        return that.myList.equals(this.myList);
    }

    /**
      * Gets the hash code.
      */
    @Override
    public int hashCode() {
        return myList.hashCode();
    }

    /**
      * Deep copy
      */
    @Override
    public Object clone() {
        JsonArr clone = new JsonArr();
        for (Json elem: myList) {
            clone.myList.add((Json) elem.clone());  // deep copy
        }

        String[] cl = this.remarkLines();
        if (cl != null) {
            clone.setRemarkLines(cl);
        }

        return clone;
    }

    /**
      * Parses the string into a {@code JsonArr}.
      * @param s string to parse
      * @return a JsonArr if s legally represent a JSON array.
      * @throws com.github.hyunikn.jsonden.exception.ParseError when s does not legally represent a Json array.
      */
    public static JsonArr parse(String s) throws ParseError {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonArr) {
            return (JsonArr) parsed;
        } else {
            throw new ParseError(ParseError.CASE_UNEXPECTED_JSON_TYPE,
                    "not parsed into a JsonArr but " + parsed.getClass().getSimpleName());
        }
    }

    /**
      * Gets the element at the index.
      * @param index This method returns a non-null value if the index is one of -n, -n+1, ..., 0, 1, ..., n-1
      * where n is the size of this JsonArr. -1 indicates the last element, -2 the second last, and so on like
      * in Python list.
      */
    public Json get(int index) {
        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            return myList.get(i);
        }
    }

    /**
      * Deletes the element at the index.
      * @param index This method deletes nothing if the index is not one of -n, -n+1, ..., 0, 1, ..., n-1
      * where n is the size of this JsonArr. -1 indicates the last element, -2 the second last, and so on like
      * in Python list.
      * @return this {@code JsonArr} for method chaining
      */
    public JsonArr delete(int index) {
        int i = adjustIndex(index);
        if (i >= 0) {
            myList.remove(i);
        }

        return this;
    }

    /**
      * Removes the element at the index and returns it.
      * @param index This method removes nothing and returns null if
      * the index is not one of -n, -n+1, ..., 0, 1, ..., n-1 where n is the size of this JsonArr.
      * -1 indicates the last element, -2 the second last, and so on like in Python list.
      * @return the removed value of the index, or null if no such value.
      */
    public Json remove(int index) {
        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            return myList.remove(i);
        }
    }

    /**
      * Replaces an existing element at the index with {@code elem}.
      * @param index must be one of -n, -n+1, ..., 0, 1, ..., n-1 where n is the size of this JsonArr.
      * -1 indicates the last element, -2 the second last, and so on like in Python list.
      * @param elem if {@code elem} is null then it is understood as a JsonNull.
      */
    public JsonArr replace(int index, Json elem) {

        int i = adjustIndex(index);
        if (i < 0) {
            throw new IllegalArgumentException("failed to replace: no element at the index " + index);
        } else {

            if (elem == null) {
                elem = new JsonNull();
            }

            myList.set(i, elem);
            return this;
        }
    }
    /** short for {@code replace(index, new JsonBool(b))} */
    public JsonArr replace(int index, boolean b) {
        return replace(index, new JsonBool(b));
    }
    /** short for {@code replace(index, new JsonNum(n))} */
    public JsonArr replace(int index, byte n) {
        return replace(index, new JsonNum(n));
    }
    /** short for {@code replace(index, new JsonNum(n))} */
    public JsonArr replace(int index, short n) {
        return replace(index, new JsonNum(n));
    }
    /** short for {@code replace(index, new JsonNum(n))} */
    public JsonArr replace(int index, int n) {
        return replace(index, new JsonNum(n));
    }
    /** short for {@code replace(index, new JsonNum(n))} */
    public JsonArr replace(int index, long n) {
        return replace(index, new JsonNum(n));
    }
    /** short for {@code replace(index, new JsonNum(n))} */
    public JsonArr replace(int index, float n) {
        return replace(index, new JsonNum(n));
    }
    /** short for {@code replace(index, new JsonNum(n))} */
    public JsonArr replace(int index, double n) {
        return replace(index, new JsonNum(n));
    }
    /** short for {@code replace(index, new JsonNum(n))} */
    public JsonArr replace(int index, BigInteger n) {
        return replace(index, new JsonNum(n));
    }
    /** short for {@code replace(index, new JsonNum(n))} */
    public JsonArr replace(int index, BigDecimal n) {
        return replace(index, new JsonNum(n));
    }
    /** short for {@code replace(index, new JsonStr(s))} */
    public JsonArr replace(int index, String s) {
        return replace(index, new JsonStr(s));
    }

    /**
      * Inserts {@code elem} before an existing element at the index.
      * @param index must be one of -n, -n+1, ..., 0, 1, ..., n-1 where n is the size of this JsonArr.
      * -1 indicates the last element, -2 the second last, and so on like in Python list.
      * @param elem if {@code elem} is null then it is understood as a JsonNull.
      * @return this {@code JsonArr} for method chaining
      */
    public JsonArr insert(int index, Json elem) {

        int i = adjustIndex(index);
        if (i < 0) {
            throw new IllegalArgumentException("failed to insert: no element at the index " + index);
        } else {

            if (elem == null) {
                elem = new JsonNull();
            }

            myList.add(i, elem);
            return this;
        }
    }
    /** short for {@code insert(index, new JsonBool(b))} */
    public JsonArr insert(int index, boolean b) {
        return insert(index, new JsonBool(b));
    }
    /** short for {@code insert(index, new JsonNum(n))} */
    public JsonArr insert(int index, byte n) {
        return insert(index, new JsonNum(n));
    }
    /** short for {@code insert(index, new JsonNum(n))} */
    public JsonArr insert(int index, short n) {
        return insert(index, new JsonNum(n));
    }
    /** short for {@code insert(index, new JsonNum(n))} */
    public JsonArr insert(int index, int n) {
        return insert(index, new JsonNum(n));
    }
    /** short for {@code insert(index, new JsonNum(n))} */
    public JsonArr insert(int index, long n) {
        return insert(index, new JsonNum(n));
    }
    /** short for {@code insert(index, new JsonNum(n))} */
    public JsonArr insert(int index, float n) {
        return insert(index, new JsonNum(n));
    }
    /** short for {@code insert(index, new JsonNum(n))} */
    public JsonArr insert(int index, double n) {
        return insert(index, new JsonNum(n));
    }
    /** short for {@code insert(index, new JsonNum(n))} */
    public JsonArr insert(int index, BigInteger n) {
        return insert(index, new JsonNum(n));
    }
    /** short for {@code insert(index, new JsonNum(n))} */
    public JsonArr insert(int index, BigDecimal n) {
        return insert(index, new JsonNum(n));
    }
    /** short for {@code insert(index, new JsonStr(s))} */
    public JsonArr insert(int index, String s) {
        return insert(index, new JsonStr(s));
    }

    /**
      * Appends {@code elem} at the end.
      * @param elem if {@code elem} is null then it is understood as a JsonNull.
      * @return this {@code JsonArr} for method chaining
      */
    public JsonArr append(Json elem) {

        if (elem == null) {
            elem = new JsonNull();
        }

        myList.add(elem);
        return this;
    }
    /** short for {@code append(new JsonBool(b))} */
    public JsonArr append(boolean b) {
        return append(new JsonBool(b));
    }
    /** short for {@code append(new JsonNum(n))} */
    public JsonArr append(byte n) {
        return append(new JsonNum(n));
    }
    /** short for {@code append(new JsonNum(n))} */
    public JsonArr append(short n) {
        return append(new JsonNum(n));
    }
    /** short for {@code append(new JsonNum(n))} */
    public JsonArr append(int n) {
        return append(new JsonNum(n));
    }
    /** short for {@code append(new JsonNum(n))} */
    public JsonArr append(long n) {
        return append(new JsonNum(n));
    }
    /** short for {@code append(new JsonNum(n))} */
    public JsonArr append(float n) {
        return append(new JsonNum(n));
    }
    /** short for {@code append(new JsonNum(n))} */
    public JsonArr append(double n) {
        return append(new JsonNum(n));
    }
    /** short for {@code append(new JsonNum(n))} */
    public JsonArr append(BigInteger n) {
        return append(new JsonNum(n));
    }
    /** short for {@code append(new JsonNum(n))} */
    public JsonArr append(BigDecimal n) {
        return append(new JsonNum(n));
    }
    /** short for {@code append(new JsonStr(s))} */
    public JsonArr append(String s) {
        return append(new JsonStr(s));
    }

    /**
      * Gets the size of this {@code JsonArr}.
      */
    public int size() {
        return myList.size();
    }

    // ---------------------------------------------------

    /**
      * Erases all the elements.
      * @return this {@code JsonArr} for method chaining
      */
    public JsonArr clear() {
        myList.clear();
        return this;
    }

    /**
      * Gets the first index of the element
      * @param elem must not be null.
      * @return the index if the element is present in this {@code JsonArr}, otherwise -1.
      */
    public int indexOf(Json elem) {
        if (elem == null) {
            throw new IllegalArgumentException("elem cannot be null");
        }

        return myList.indexOf(elem);
    }

    /**
      * Gets the last index of the element.
      * @param elem must not be null.
      * @return the index if the element is present in this {@code JsonArr}, otherwise -1.
      */
    public int lastIndexOf(Json elem) {
        if (elem == null) {
            throw new IllegalArgumentException("elem cannot be null");
        }

        return myList.lastIndexOf(elem);
    }

    /**
      * Returns {@code true}, overriding {@code isArr()} of {@link com.github.hyunikn.jsonden.Json Json}.
      */
    @Override
    public boolean isArr() { return true; }

    /**
      * Returns {@code this}, overriding {@code asArr()} of {@link com.github.hyunikn.jsonden.Json Json}.
      */
    @Override
    public JsonArr asArr() { return this; }

    /**
      * Returns the elements as a list.
      */
    public List<Json> getList() { return new LinkedList(myList); }

    /**
      * Gets the leaf nodes whose paths are common of this and that {@code Json}s and whose values are different.
      * @return map of paths to the different values.
      */
    public Map<String, List<Json>> diffAtCommonPaths(JsonArr that) {
        return super.diffAtCommonPaths(that);
    }

    /**
      * Gets the nodes whose paths are common of this and that {@code Json}s
      * and whose values are equal.
      * If a non-leaf ({@code JsonObj} or {@code JsonArr}) is contained in the result then
      * its subnodes are not included in the result.
      * In particular, if this and that {@code Json} are equal then the result is a map whose key is
      * a spacial path ".", which represent the path to itself, and whose value is this {@code Json}.
      */
    public LinkedHashMap<String, Json> intersect(JsonArr that) {
        return super.intersect(that);
    }

    /**
      * Gets the nodes whose paths are in this {@code Json} but not in that {@code Json}.
      * If a non-leaf ({@code JsonObj} or {@code JsonArr}) is contained in the result then
      * its subnodes are not included in the result.
      */
    public LinkedHashMap<String, Json> subtract(JsonArr that) {
        return super.subtract(that);
    }

    /**
      * Gets the leaf nodes whose paths are common of this and that {@code Json}s and whose values are equal.
      */
    public LinkedHashMap<String, Json> intersectLeaves(JsonArr that) {
        return super.intersectLeaves(that);
    }

    /**
      * Gets the leaf nodes whose paths are in this {@code Json} but not in that {@code Json}.
      */
    public LinkedHashMap<String, Json> subtractLeaves(JsonArr that) {
        return super.subtractLeaves(that);
    }

    /**
      * Sets {@code val} at the path, possibly deep in a nested structure, creating parent nodes as needed.
      * This behavior is reminiscent of the UNIX shell command {@code mkdir} with {@code -p} option.
      * Each of the created parent nodes is either a {@code JsonArr} or a {@code JsonObj} depending on whether
      * the next path segment is a hash(#) followed by an integer (array element index) or not.
      * For example, {@code eo.setx("a.b.#0.c.d", val)} for an empty {@code JsonObj} {@code eo}
      * results in {@code {"a":{"b": [ {"c":"{"d": val}} ]}}} with newly created four parent nodes
      * corresponding to a, b, #0 and c, respectively.
      * @param path dot delimited segments of a path to a subnode.
      *  Each segment represents either an object member key or an array element index (hash followed by an integer).
      * @return this {@code Json} for method chaining
      * @throws com.github.hyunikn.jsonden.exception.UnreachablePath when the missing parent nodes cannot be created.
      */
    public JsonArr setx(String path, Json val) throws UnreachablePath {
        return (JsonArr) super.setx(path, val);
    }
    /** short for {@code setx(path, new JsonBool(b))} */
    public JsonArr setx(String path, boolean b) throws UnreachablePath {
        return setx(path, new JsonBool(b));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonArr setx(String path, byte n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonArr setx(String path, short n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonArr setx(String path, int n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonArr setx(String path, long n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonArr setx(String path, float n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonArr setx(String path, double n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonArr setx(String path, BigInteger n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonArr setx(String path, BigDecimal n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonStr(s))} */
    public JsonArr setx(String path, String s) throws UnreachablePath {
        return setx(path, new JsonStr(s));
    }

    /**
      * Same as {@code loadFlattened(flattened, true)}
      */
    public JsonArr loadFlattened(LinkedHashMap<String, Json> flattened) throws UnreachablePath {
        return (JsonArr) super.loadFlattened(flattened, true);
    }

    /**
      * Loads a flattened {@code Json} value into this array.
      * This method causes in-place modification of the current array.
      * If the current array already has a value at a path in {@code flattened} then the value is overwritten
      * by the one in {@code flattened}.
      * @param flattened a flattened {@code Json} value obtained by
      *   {@link com.github.hyunikn.jsonden.JsonNonLeaf#flatten flatten},
      *   {@link com.github.hyunikn.jsonden.JsonNonLeaf#flatten2 flatten2},
      *   {@link com.github.hyunikn.jsonden.JsonNonLeaf#intersect intersect},
      *   {@link com.github.hyunikn.jsonden.JsonNonLeaf#subtract subtract}, etc.
      * @param clone whether to clone the {@code Json} values while loading or not.
      * @return the current array for method chaining.
      */
    public JsonArr loadFlattened(LinkedHashMap<String, Json> flattened, boolean clone) throws UnreachablePath {
        return (JsonArr) super.loadFlattened(flattened, clone);
    }

    // ===================================================
    // Protected

    protected final LinkedList<Json> myList;

    protected JsonArr() {
        myList = new LinkedList<>();
    }

    protected JsonArr(List<Json> list) {
        if (list == null) {
            throw new IllegalArgumentException("source list cannot be null");
        }
        this.myList = new LinkedList<>(list);
    }

    @Override
    protected void flattenInner(LinkedHashMap<String, Json> accum, String pathToMe, boolean addNonLeafToo) {

        String prefix;
        if (pathToMe == null) {
            prefix = "#";
        } else {
            if (addNonLeafToo) {
                accum.put(pathToMe, this);
            }
            prefix = pathToMe + ".#";
        }

        int i = 0;
        for (Json val: myList) {
            if (val instanceof JsonNonLeaf) {
                ((JsonNonLeaf) val).flattenInner(accum, prefix + i, addNonLeafToo);
            } else {
                accum.put(prefix + i, val);
            }
            i++;
        }
    }

    @Override
    protected String getTypeName() {
        return "array";
    }

    @Override
    protected void write(StringBuffer sbuf, int indentSize, int indentLevel) {

        boolean useIndents = indentSize != 0;

        if (indentLevel < 0) {
            // negative indent size indicates that we are right after a key in an object
            indentLevel *= -1;
        } else {
            writeRemark(sbuf, remarkLines, indentSize, indentLevel);
            writeIndent(sbuf, indentSize, indentLevel);
        }

        if (myList.isEmpty()) {
            sbuf.append("[]");
            return;
        }

        sbuf.append('[');
        if (useIndents) {
            sbuf.append('\n');
        }

        boolean first = true;
        for (Json val: myList) {
            if (first) {
                first = false;
            } else {
                if (useIndents) {
                    sbuf.append(",\n");
                } else {
                    sbuf.append(",");
                }
            }
            val.write(sbuf, indentSize, indentLevel + 1);
        }

        if (useIndents) {
            sbuf.append('\n');
        }
        writeIndent(sbuf, indentSize, indentLevel);
        sbuf.append(']');
    }

    @Override
    protected Json getChild(String key) {
        Integer intKey = MyParseTreeVisitor.getArrElemIndex(key);
        if (intKey == null) {
            return null;
        } else {
            return get(intKey.intValue());
        }
    }

    // ===================================================
    // Private

    private final Json[] runtimeTyper = new Json[0];

    private int adjustIndex(int index) {

        int size = myList.size();
        if (index < -size || index >= size) {
            return -1;
        }

        if (index < 0) {
            return index + size;
        } else {
            return index;
        }
    }
}
