package com.wingsoft.jsonden;

abstract class JsonPrim extends Json {

    // ===================================================
    // Public

    @Override
    protected void fill(StringBuffer sbuf, int indentSize, int indentLevel) {
        putIndent(sbuf, indentSize, indentLevel);
        sbuf.append(text);
    }

    @Override
    public Json get(String path) {
        return null;
    }

    // ===================================================
    // Protected

    protected final String text;

    protected JsonPrim(String text) {
        this.text = text;
    }

    // ===================================================
    // Private

}
