package com.wingsoft.jsonden;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class JsonObj extends Json {

    // ===================================================
    // Public

    public JsonObj() {}

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
            throw new Error("key name cannot be null");
        } else {
            return getChild(key);
        }
    }

    public Json del(String key) {
        if (key == null) {
            throw new Error("key name cannot be null");
        } else {
            return delChild(key);
        }
    }

    public Json set(String key, Json node) {
        if (key == null) {
            throw new Error("key name cannot be null");
        } else {
            return setChild(key, node);
        }
    }

    public LinkedHashSet<String> keySet() {
        return new LinkedHashSet<>(map.keySet());
    }

    // ---------------------------------------------------

    public void clear() {
        map.clear();
    }

    // ===================================================
    // Protected

    protected final LinkedHashMap<String, Json> map = new LinkedHashMap<>();

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

    @Override
    protected Json delChild(String key) {
        return map.remove(key);
    }

    @Override
    protected Json setChild(String key, Json node) {
        return map.put(key, node);
    }

    // ===================================================
    // Private


}

