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
        _stringify(sbuf, indentSize, indentLevel);
        return sbuf.toString();
    }

    public Json getChild(String name) {
        if (name == null || name.length() == 0) {
            throw new Error("child name cannot be null or empty");
        }

        return _getChild(name);
    }

    public Json get(String path) {

        String[] segments = checkAndSplitPath(path);
        assert segments.length > 0;

        Json ret = this;
        for (String s: segments) {
            ret = ret._getChild(s);
            if (ret == null) {
                return null;
            }
        }

        return ret;
    }

    public Json putChild(String name, Json child) {
        if (name == null || name.length() == 0) {
            throw new Error("child name cannot be null or empty");
        }
        if (child == null) {
            throw new Error("child cannot be null");
        }

        return _putChild(name, child);
    }

    public Json put(String path, Json child) {

        String[] segments = checkAndSplitPath(path);
        int pathLen = segments.length;
        assert pathLen > 0;

        Json parent = this;
        for (int i = 0; i < pathLen - 1; i++) {
            parent = parent._getChild(segments[i]);
            if (parent == null) {
                throw new Error("no dependent with name " + segments[i]);
            }
        }

        return parent._putChild(segments[pathLen - 1], child);
    }

    @Override
    public String toString() {
        return stringify(-1, 0); // -1: without indentation and delimiting whitespaces (minified)
    }

    // ===================================================
    // Protected

    protected Json() { }
    protected abstract void _stringify(StringBuffer sbuf, int indentSize, int indentLevel);
    protected abstract Json _getChild(String name);
    protected abstract Json _putChild(String name, Json child);

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
            throw new Error("path cannot be null or empty");
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
