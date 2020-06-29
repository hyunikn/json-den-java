package com.wingsoft.jsonden;

abstract class JsonSimple extends Json {

    // ===================================================
    // Public

    // ===================================================
    // Protected

    protected final String text;

    protected JsonSimple(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text of " + getClass().getSimpleName() + " values cannot be null");
        }
        this.text = text;
    }

    @Override
    protected void write(StringBuffer sbuf, int indentSize, int indentLevel) {

        if (indentLevel < 0) {
            // negative indent size indicates that we are right after a key of an object
            indentLevel *= -1;
        } else {
            writeComment(sbuf, commentLines, indentSize, indentLevel);
            writeIndent(sbuf, indentSize, indentLevel);
        }

        sbuf.append(text);
    }

    @Override
    protected Json getChild(String key) {
        return null;
    }

    // ===================================================
    // Private

}
