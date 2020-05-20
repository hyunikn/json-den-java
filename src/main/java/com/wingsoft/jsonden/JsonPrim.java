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

    // ===================================================
    // Protected

    protected final String text;

    protected JsonPrim(String text) {
        this.text = text;
    }

    @Override
    protected void write(StringBuffer sbuf, int indentSize, int indentLevel) {
        writeIndent(sbuf, indentSize, indentLevel);
        sbuf.append(text);
    }

    @Override
    protected Json getChild(String name) {
        return null;
    }

    @Override
    protected Json putChild(String name, Json child) {
        throw new Error("a JSON node of a primitive type (in this case, " + this.getTypeName() +
                ") cannot have a child node");
    }

    // ===================================================
    // Private

}
