package com.wingsoft.jsonden;

abstract class JsonPrim extends Json {

    // ===================================================
    // Public

    public static JsonPrim parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonPrim) {
            return (JsonPrim) parsed;
        } else {
            throw new Error("not parsed into a JsonPrim but " + parsed.getClass().getSimpleName());
        }
    }

    @Override
    public Json getChild(String name) {
        return null;
    }

    @Override
    public Json putChild(String name, Json child) {
        // TODO
        return null;
    }

    // ===================================================
    // Protected

    protected final String text;

    protected JsonPrim(String text) {
        this.text = text;
    }

    @Override
    protected void _stringify(StringBuffer sbuf, int indentSize, int indentLevel) {
        putIndent(sbuf, indentSize, indentLevel);
        sbuf.append(text);
    }

    // ===================================================
    // Private

}
