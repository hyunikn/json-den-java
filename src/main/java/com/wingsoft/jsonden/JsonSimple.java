package com.wingsoft.jsonden;

abstract class JsonSimple extends Json {

    // ===================================================
    // Public

    public static JsonSimple parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonSimple) {
            return (JsonSimple) parsed;
        } else {
            throw new Error("not parsed into a JsonSimple but " + parsed.getTypeName());
        }
    }

    // ===================================================
    // Protected

    protected final String text;

    protected JsonSimple(String text) {
        this.text = text;
    }

    @Override
    protected void write(StringBuffer sbuf, int indentSize, int indentLevel) {
        writeIndent(sbuf, indentSize, indentLevel);
        sbuf.append(text);
    }

    @Override
    protected Json getChild(String name) {
        return throwNoChild();
    }

    @Override
    protected Json putChild(String name, Json child) {
        return throwNoChild();
    }

    @Override
    protected Json removeChild(String name) {
        return throwNoChild();
    }

    // ===================================================
    // Private

    private Json throwNoChild() {
        throw new Error("a JSON node of a simple type (in this case, " + this.getTypeName() +
                ") do not have a child node");
    }
}
