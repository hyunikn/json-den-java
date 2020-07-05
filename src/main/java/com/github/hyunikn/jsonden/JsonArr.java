package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.ParseError;

import java.io.IOException;

import java.math.BigInteger;
import java.math.BigDecimal;

import java.util.List;
import java.util.LinkedList;

/**
  * A subclass of {@link com.github.hyunikn.jsonden.Json Json} which represents JSON arrays.
  */
public class JsonArr extends Json {

    // ===================================================
    // Public

    /**
      * Constructs an empty {@code JsonArr}.
      */
    public JsonArr() {
        list = new LinkedList<>();
    }

    /**
      * Constructs a {@code JsonArr} from a list.
      * @param list must not be null.
      */
    public JsonArr(List<Json> list) {
        if (list == null) {
            throw new IllegalArgumentException("source list cannot be null");
        }
        this.list = new LinkedList<>(list);
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
        return that.list.equals(this.list);
    }

    /**
      * Gets the hash code.
      */
    @Override
    public int hashCode() {
        return list.hashCode();
    }

    /**
      * Deep copy
      */
    @Override
    public Object clone() {
        JsonArr clone = new JsonArr();
        for (Json elem: list) {
            clone.list.add((Json) elem.clone());  // deep copy
        }

        String[] cl = this.commentLines();
        if (cl != null) {
            clone.setCommentLines(cl);
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
            return list.get(i);
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
            list.remove(i);
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
            return list.remove(i);
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

            list.set(i, elem);
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

            list.add(i, elem);
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

        list.add(elem);
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
        return list.size();
    }

    // ---------------------------------------------------

    /**
      * Erases all the elements.
      * @return this {@code JsonArr} for method chaining
      */
    public JsonArr clear() {
        list.clear();
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

        return list.indexOf(elem);
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

        return list.lastIndexOf(elem);
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
    @Override
    public List<Json> getList() { return new LinkedList(list); }

    // ===================================================
    // Protected

    protected final LinkedList<Json> list;

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
            writeComment(sbuf, commentLines, indentSize, indentLevel);
            writeIndent(sbuf, indentSize, indentLevel);
        }

        sbuf.append('[');
        if (useIndents) {
            sbuf.append('\n');
        }

        boolean first = true;
        for (Json val: list) {
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
        return get(getIndex(key));
    }

    // ===================================================
    // Private

    private final Json[] runtimeTyper = new Json[0];

    private int adjustIndex(int index) {

        int size = list.size();
        if (index < -size || index >= size) {
            return -1;
        }

        if (index < 0) {
            return index + size;
        } else {
            return index;
        }
    }

    private int getIndex(String name) {

		assert name != null;

        try {
            return Integer.parseInt(name);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(name + " is not an integer and cannot be an index to an array element");
        }
    }
}
