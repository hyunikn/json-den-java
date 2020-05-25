package com.wingsoft.jsonden;

public abstract class JsonSimple extends Json {

    // ===================================================
    // Public

    // ===================================================
    // Protected

    protected final String text;

    protected JsonSimple(String text) {
        if (text == null) {
            throw new Error("text of " + getClass().getSimpleName() + " values cannot be null");
        }
        this.text = text;
    }

    @Override
    public Object clone() {
        return this;    // they are all immutable
    }

    @Override
    protected void write(StringBuffer sbuf, int indentSize, int indentLevel) {

        if (indentSize < 0) {
            // negative indent size indicates that we are right after a key of an object
            indentSize *= -1;
        } else {
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
