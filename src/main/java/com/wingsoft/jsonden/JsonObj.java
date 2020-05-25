package com.wingsoft.jsonden;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class JsonObj extends Json {

    // ===================================================
    // Public

    public JsonObj() {
        map = new LinkedHashMap<>();
    }

    public JsonObj(LinkedHashMap<String, Json> map) {
        if (map == null) {
            throw new Error("source map cannot be null");
        }
        this.map = new LinkedHashMap<>(map);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        JsonObj that = (JsonObj) o;
        if (!that.map.keySet().equals(this.map.keySet())) {
            return false;
        }

        for (String key: keySet()) {
            Json val0 = that.map.get(key);
            Json val1 = this.map.get(key);
            if (!val0.equals(val1)) {
                return false;
            }
        }

        return true;
    }

    public static JsonObj parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonObj) {
            return (JsonObj) parsed;
        } else {
            throw new Error("not parsed into a JsonObj but " + parsed.getClass().getSimpleName());
        }
    }

    public Json get(String key) {
        if (key == null) {
            throw new Error("key cannot be null");
        } else {
            return map.get(key);
        }
    }

    public Json del(String key) {
        if (key == null) {
            throw new Error("key cannot be null");
        } else {
            return map.remove(key);
        }
    }

    public Json set(String key, Json node) {
        if (key == null) {
            throw new Error("key cannot be null");
        } else if (node == null) {
            throw new Error("node cannot be null");
        } else {
            return map.put(key, node);
        }
    }

    public LinkedHashSet<String> keySet() {
        return new LinkedHashSet<>(map.keySet());
    }

    @Override
    public boolean isObj() { return true; }
    @Override
    public JsonObj asObj() { return this; }
    @Override
    public LinkedHashMap<String, Json> asMap() { return new LinkedHashMap(map); }

    // ---------------------------------------------------

    public void clear() {
        map.clear();
    }

    // ===================================================
    // Protected

    protected final LinkedHashMap<String, Json> map;

    @Override
    protected String getTypeName() {
        return "object";
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

        sbuf.append('{');
        if (useIndents) {
            sbuf.append('\n');
        }

        boolean first = true;
        for (String key: map.keySet()) {
            Json val = map.get(key);
            if (first) {
                first = false;
            } else {
                sbuf.append(",\n");
            }
            writeIndent(sbuf, indentSize, indentLevel + 1);
            sbuf.append('"');
            sbuf.append(key);
            sbuf.append("\":");
            if (useIndents) {
                sbuf.append(' ');
            }
            val.write(sbuf, - indentSize, indentLevel + 1);
        }

        if (useIndents) {
            sbuf.append('\n');
        }
        writeIndent(sbuf, indentSize, indentLevel);
        sbuf.append('}');
    }

    @Override
    protected Json getChild(String key) {
        return map.get(key);
    }

    // ===================================================
    // Private


}

