package com.github.hyunikn.jsonden;

import java.util.LinkedHashMap;

abstract class JsonLeaf extends Json {

    // ===================================================
    // Public

    @Override
    public boolean isLeaf() {
        return true;
    }

    // ===================================================
    // Protected

    protected final String text;


    protected JsonLeaf(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text of " + getClass().getSimpleName() + " values cannot be null");
        }
        this.text = text;
    }

    @Override
    protected LinkedHashMap<String, Json> flattenInner(
            LinkedHashMap<String, Json> accum, String pathToMe, boolean addIntermediateToo) {

        assert (accum == null) == (pathToMe == null);

        if (accum != null) {
            accum.put(pathToMe, this);
        }

        return accum;
    }

    @Override
    protected void write(StringBuffer sbuf, int indentSize, int indentLevel) {

        if (indentLevel < 0) {
            // negative indent size indicates that we are right after a key of an object
            indentLevel *= -1;
        } else {
            writeRemark(sbuf, remarkLines, indentSize, indentLevel);
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
