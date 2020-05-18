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

    @Override
    public String toString() {
        return stringify(-1, 0); // -1: without indentation and delimiting whitespaces (minified)
    }

    public abstract Json getChild(String name);
    public abstract Json putChild(String name, Json child);

    public Json get(String path) {

        String[] segments = checkAndSplitPath(path);

        Json ret = this;
        for (String s: segments) {
            ret = ret.getChild(s);
            if (ret == null) {
                return null;
            }
        }

        return ret;
    }

    // ===================================================
    // Protected

    protected Json() { }

    protected abstract void write(StringBuffer sbuf, int indentSize, int indentLevel);






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

    private String[] checkAndSplitPath(String path) {

        if (path == null || path.length() == 0) {
            throw new Error("path cannot be a null or empty string");
        }

        String[] segments = path.split("\\.", -1);  // -1: do not discard trailing empty strings
        for (String s: segments) {
            if (s.length() == 0) {
                throw new Error("path cannot contain an empty segment");
            }
        }

        return segments
    }
}
