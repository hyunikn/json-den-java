package com.wingsoft.jsonden;

import java.util.LinkedList;

public class JsonArr extends Json {

    // ===================================================
    // Public

    public JsonArr() {}

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

    public Json[] toArray() {
        return list.toArray(runtimeTyper);
    }

    // ===================================================
    // Protected

    protected final LinkedList<Json> list = new LinkedList<>();

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
                sbuf.append(",\n");
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

    @Override
    protected Json delChild(String key) {
        return del(getIndex(key));
    }

    @Override
    protected Json updateChild(String key, Json node) {
        return update(getIndex(key), node);
    }

    @Override
    protected Json insertChild(String key, Json node) {
        return insert(getIndex(key), node);
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
