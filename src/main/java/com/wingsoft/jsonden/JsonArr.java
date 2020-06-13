package com.wingsoft.jsonden;

import java.io.IOException;

import java.util.List;
import java.util.LinkedList;

public class JsonArr extends Json {

    // ===================================================
    // Public

    public JsonArr() {
        list = new LinkedList<>();
    }

    public JsonArr(Json[] array) {
        if (array == null) {
            throw new Error("source array cannot be null");
        }

        list = new LinkedList<>();
        for (Json j: array) {
            list.add(j);
        }
    }

    public JsonArr(List<Json> list) {
        if (list == null) {
            throw new Error("source list cannot be null");
        }
        this.list = new LinkedList<>(list);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        JsonArr that = (JsonArr) o;
        return that.list.equals(this.list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public Object clone() {
        JsonArr that = new JsonArr();
        for (Json elem: list) {
            that.list.add((Json) elem.clone());  // deep copy
        }
        return that;
    }

    public static JsonArr parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonArr) {
            return (JsonArr) parsed;
        } else {
            throw new Error("not parsed into a JsonArr but " + parsed.getClass().getSimpleName());
        }
    }

    public Json get(int index) {
        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            return list.get(i);
        }
    }

    public Json del(int index) {
        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            return list.remove(i);
        }
    }

    public Json update(int index, Json node) {
        if (node == null) {
            throw new Error("node cannot be null");
        }

        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            return list.set(i, node);
        }
    }

    public Json insert(int index, Json node) {
        if (node == null) {
            throw new Error("node cannot be null");
        }

        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            Json old = list.get(i);
            list.add(i, node);
            return old;
        }
    }

    public void append(Json node) {
        if (node == null) {
            throw new Error("node cannot be null");
        }

        list.add(node);
    }

    public int size() {
        return list.size();
    }

    // ---------------------------------------------------

    public void clear() {
        list.clear();
    }

    public int indexOf(Json node) {
        if (node == null) {
            throw new Error("node cannot be null");
        }

        return list.indexOf(node);
    }

    public int lastIndexOf(Json node) {
        if (node == null) {
            throw new Error("node cannot be null");
        }

        return list.lastIndexOf(node);
    }

    @Override
    public boolean isArr() { return true; }
    @Override
    public JsonArr asArr() { return this; }
    @Override
    public Json[] asArray() { return list.toArray(runtimeTyper); }
    @Override
    public List<Json> asList() { return new LinkedList(list); }

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
