package com.wingsoft.jsonden;

public abstract class JsonSimple extends Json {

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

        if (indentSize < 0) {
            // negative indent size indicates that we are right after a key in an object
            indentSize *= -1;
        } else {
            writeIndent(sbuf, indentSize, indentLevel);
        }

        sbuf.append(text);
    }

    @Override
    protected Json getChild(String key) {
        throw new Error("failed to get a child node " + key + ": " +
                getClass().getSimpleName() + " nodes do not have children nodes");
    }

    // ===================================================
    // Private

}
