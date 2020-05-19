package com.wingsoft.jsonden;

public class JsonObj extends Json {

    // ===================================================
    // Public

    public static JsonObj parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonObj) {
            return (JsonObj) parsed;
        } else {
            throw new Error("not parsed into a JsonObj but " + parsed.getClass().getSimpleName());
        }
    }

    @Override
    public Json getChild(String name) {
        // TODO
        return null;
    }

    @Override
    public Json putChild(String name, Json child) {
        // TODO
        return null;
    }

    // ===================================================
    // Protected

    @Override
    protected void _stringify(StringBuffer sbuf, int indentSize, int indentLevel) {
        // TODO
    }

}

