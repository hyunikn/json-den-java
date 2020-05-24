package com.wingsoft.jsonden;

import java.util.LinkedList;

public class JsonArr extends Json {

    // ===================================================
    // Public

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
            return arr.get(i);
        }
    }

    public Json remove(int index) {
        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            return arr.remove(i);
        }
    }

    public Json update(int index, Json node) {
        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            return arr.set(i, node);
        }
    }

    public Json insert(int index, Json node) {
        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            Json old = arr.get(i);
            arr.add(i, node);
            return old;
        }
    }

    public void append(Json node) {
        arr.add(node);
    }

    // ===================================================
    // Protected

    @Override
    protected String getTypeName() {
        return "array";
    }

    @Override
    protected void write(StringBuffer sbuf, int indentSize, int indentLevel) {
        // TODO
    }

    @Override
    protected Json getChild(String child) {
        return get(getIndex(child));
    }

    @Override
    protected Json removeChild(String child) {
        return remove(getIndex(child));
    }

    @Override
    protected Json updateChild(String child, Json node) {
        return update(getIndex(child), node);
    }

    @Override
    protected Json insertChild(String child, Json node) {
        return insert(getIndex(child), node);
    }

    // ===================================================
    // Private

    private final LinkedList<Json> arr = new LinkedList<>();

    private int adjustIndex(int index) {

        int size = arr.size();
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
