package com.wingsoft.jsonden;

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
    protected String getTypeName() {
        return "array";
    }

}
