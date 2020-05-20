package com.wingsoft.jsonden;

public abstract class Json {

    // ===================================================
    // Public

    public static Json parse(String s) {
        // TODO:
        return null;
    }

    public String stringify(int indentSize, int indentLevel) {

        checkStringifyOptions(indentSize, indentLevel);
        StringBuffer sbuf = new StringBuffer();
        write(sbuf, indentSize, indentLevel);
        return sbuf.toString();
    }

    public Json get(String path) {

        if (path == null) {
            throw new Error("path cannot be null");
        } else {
            String[] segments = path.split("\\.", -1);  // -1: do not discard trailing empty strings

            Json ret = this;
            for (String s: segments) {
                ret = ret.getChild(s);
                if (ret == null) {
                    return null;
                }
            }

            return ret;
        }
    }

    public Json put(String path, Json json) {

        if (path == null) {
            throw new Error("path cannot be null");
        } else {
            String[] segments = path.split("\\.", -1);  // -1: do not discard trailing empty strings
            int pathLen = segments.length;

            Json parent = this;
            for (int i = 0; i < pathLen - 1; i++) {
                parent = parent.getChild(segments[i]);
                if (parent == null) {
                    throw new Error("no descendant named '" + segments[i] + "'");
                }
            }

            return parent.putChild(segments[pathLen - 1], json);
        }
    }

    @Override
    public String toString() {
        return stringify(-1, 0); // -1: without indentation and delimiting whitespaces (that is, minified)
    }

    // ===================================================
    // Protected

    protected Json() { }
    protected abstract void write(StringBuffer sbuf, int indentSize, int indentLevel);
    protected abstract Json getChild(String name);
    protected abstract Json putChild(String name, Json child);
    protected abstract String getTypeName();

    // --------------------------------------------------
    // Utility

    protected static String excAsStr(String msg) {
        throw new Error(msg);
    }

    protected static void writeIndent(StringBuffer sbuf, int indentSize, int indentLevel) {

        if (indentSize <= 0) {
            return;
        }
        if (indentLevel == 0) {
            return;
        }

        String indent = indents[indentSize];
        for (int i = 0; i < indentLevel; i++) {
            sbuf.append(indent);
        }
    }

    // ===================================================
    // Private

    private static final String[] indents = new String[] {
        null,
        " ",
        "  ",
        "   ",
        "    ",
        "     ",
        "      ",
        "       ",
        "        "
    };

    private void checkStringifyOptions(int indentSize, int indentLevel) {
        if (indentSize > 8) {
            throw new Error("indentSize cannot be larger than eight");
        }

        if (indentLevel < 0) {
            throw new Error("indentLevel cannot be a negative integer");
        }
    }
}
