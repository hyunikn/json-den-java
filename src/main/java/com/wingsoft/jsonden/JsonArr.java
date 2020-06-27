package com.wingsoft.jsonden;

import com.wingsoft.jsonden.exception.ParseError;

import java.io.IOException;

import java.util.List;
import java.util.LinkedList;

/**
  * A subclass of {@link com.wingsoft.jsonden.Json Json} which represents JSON arrays.
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
      */
    public JsonArr(List<Json> list) {
        if (list == null) {
            throw new Error("source list cannot be null");
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
        JsonArr that = new JsonArr();
        for (Json elem: list) {
            that.list.add((Json) elem.clone());  // deep copy
        }
        return that;
    }

    /**
      * Parses the string into a {@code JsonArr}.
      * @param s string to parse
      * @return a JsonArr if s legally represent a JSON array.
      * @throws com.wingsoft.jsonden.exception.ParseError when s does not legally represent a Json array.
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
      */
    public Json delete(int index) {
        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            return list.remove(i);
        }
    }

    /**
      * Replaces the element at the index with the {@code Json elem}.
      */
    public Json replace(int index, Json elem) {
        if (elem == null) {
            throw new Error("elem cannot be null");
        }

        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            return list.set(i, elem);
        }
    }

    /**
      * Inserts an element at the index.
      */
    public Json insert(int index, Json elem) {
        if (elem == null) {
            throw new Error("elem cannot be null");
        }

        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            Json old = list.get(i);
            list.add(i, elem);
            return old;
        }
    }

    /**
      * Appends an element at the end.
      */
    public void append(Json elem) {
        if (elem == null) {
            throw new Error("elem cannot be null");
        }

        list.add(elem);
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
      */
    public void clear() {
        list.clear();
    }

    /**
      * Gets the first index of the element
      * @return the index if the element is present in this {@code JsonArr}, otherwise -1.
      */
    public int indexOf(Json elem) {
        if (elem == null) {
            throw new Error("elem cannot be null");
        }

        return list.indexOf(elem);
    }

    /**
      * Gets the last index of the element.
      * @return the index if the element is present in this {@code JsonArr}, otherwise -1.
      */
    public int lastIndexOf(Json elem) {
        if (elem == null) {
            throw new Error("elem cannot be null");
        }

        return list.lastIndexOf(elem);
    }

    /**
      * Returns {@code true}, overriding {@code isArr()} of {@link com.wingsoft.jsonden.Json Json}.
      */
    @Override
    public boolean isArr() { return true; }

    /**
      * Returns {@code this}, overriding {@code asArr()} of {@link com.wingsoft.jsonden.Json Json}.
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

        if (indentSize < 0) {
            // negative indent size indicates that we are right after a key in an object
            indentSize *= -1;
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
            throw new Error(name + " is not an integer and cannot be an index to an array element");
        }
    }
}
