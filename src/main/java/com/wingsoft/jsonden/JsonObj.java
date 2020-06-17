package com.wingsoft.jsonden;

import com.wingsoft.jsonden.exception.ParseError;

import java.io.IOException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class JsonObj extends Json {

    // ===================================================
    // Public

    public JsonObj() {
        map = new LinkedHashMap<>();
        commentMap = new HashMap<>();
    }

    public JsonObj(LinkedHashMap<String, Json> map) {
        if (map == null) {
            throw new Error("source map cannot be null");
        }
        this.map = new LinkedHashMap<>(map);
        this.commentMap = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        JsonObj that = (JsonObj) o;
        return that.map.equals(this.map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public Object clone() {
        JsonObj that = new JsonObj();
        for (String key: map.keySet()) {
            that.map.put(key, (Json) map.get(key).clone());    // deep copy
        }
        return that;
    }

    public static JsonObj parse(String s) throws ParseError {
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

    public String[] getCommentLines(String key) {
        return commentMap.get(key);
    }

    public void setCommentLines(String key, String[] commentLines) {
        commentMap.put(key, commentLines);
    }

    // ---------------------------------------------------

    public void clear() {
        map.clear();
    }

    // ===================================================
    // Protected

    protected final LinkedHashMap<String, Json> map;
    protected final HashMap<String, String[]> commentMap;

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
            writeComment(sbuf, commentLines, indentSize, indentLevel);
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
                if (useIndents) {
                    sbuf.append(",\n");
                } else {
                    sbuf.append(",");
                }
            }

            writeComment(sbuf, commentMap.get(key), indentSize, indentLevel + 1);

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

