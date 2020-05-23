package com.wingsoft.jsonden;

abstract class JsonSimple extends Json {

    // ===================================================
    // Public

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

    // ===================================================
    // Private

}
