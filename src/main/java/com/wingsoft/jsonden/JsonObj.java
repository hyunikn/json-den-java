package com.wingsoft.jsonden;

public class JsonObj extends Json {

    // ===================================================
    // Public

    public static JsonObj parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonObj) {
            return (JsonObj) parsed;
        } else {
            throw new Error("not parsed into a JsonObj but " + parsed.getTypeName());
        }
    }

    // ===================================================
    // Protected

    @Override
    protected void write(StringBuffer sbuf, int indentSize, int indentLevel) {
        // TODO
    }

    @Override
    protected Json getChild(String name) {
        // TODO
        return null;
    }

    @Override
    protected Json putChild(String name, Json child) {
        // TODO
        return null;
    }

    @Override
    protected Json removeChild(String name) {
        // TODO
        return null;
    }

    @Override
    protected String getTypeName() {
        return "object";
    }

    // ===================================================
    // Private

}

