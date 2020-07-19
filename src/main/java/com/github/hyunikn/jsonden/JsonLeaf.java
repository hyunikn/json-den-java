package com.github.hyunikn.jsonden;

import java.util.LinkedHashMap;

abstract class JsonLeaf extends Json {

    // ===================================================
    // Public

    /**
      * Returns true (this is a leaf node.)
      */
    @Override
    public boolean isLeaf() {
        return true;
    }

    /**
      * Returns true (this is a terminal node.)
      */
    @Override
    public boolean isTerminal() {
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
    protected void write(StringBuffer sbuf, StrOpt opt, int indentLevel) {

        if (indentLevel >= 0) {
            if (!opt.omitComments) {
                writeComment(sbuf, commentLines, opt.indentSize, indentLevel);
            }
            if (!opt.omitRemarks) {
                writeRemark(sbuf, remarkLines, opt.indentSize, indentLevel);
            }
            writeIndent(sbuf, opt.indentSize, indentLevel);
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
