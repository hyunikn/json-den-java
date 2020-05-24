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

    // for JsonObj, JsonArr
    public Json getx(String path) { return reachAndDo(path, CrudCase.GET, null); }
    public Json removex(String path) { return reachAndDo(path, CrudCase.REMOVE, null); }

    // for JsonObj
    public Json setx(String path, Json json) { return reachAndDo(path, CrudCase.SET, json); }

    // for JsonArr
    public Json updatex(String path, Json json) { return reachAndDo(path, CrudCase.UPDATE, json); }
    public Json insertx(String path, Json json) { return reachAndDo(path, CrudCase.INSERT, json); }
    public void appendx(String path, Json json) { reachAndDo(path, CrudCase.APPEND, json); }

    @Override
    public String toString() { return stringify(-1, 0); } // -1: no indentation and whitespaces (that is, minified)

    // ===================================================
    // Protected

    protected enum CrudCase {
        GET(false),
        REMOVE(false),
        SET(true),
        UPDATE(true),
        INSERT(true),
        APPEND(true),
        BOUND_CRUD_CASE(false);

        CrudCase(boolean needNode) {
            this.needNode = needNode;
        }

        final boolean needNode;
    }

    protected Json() { }

    // overriden by all
    protected abstract String getTypeName();
    protected abstract void write(StringBuffer sbuf, int indentSize, int indentLevel);

    // overriden by JsonObj, JsonArr
    protected Json getChild(String child) { return throwNoChild(child, "get"); }
    protected Json removeChild(String child) { return throwNoChild(child, "remove"); }

    // overriden by JsonObj
    protected Json setChild(String child, Json node) { return throwNoChild(child, "set"); }

    // overriden by JsonArr
    protected Json updateChild(String child, Json node) { return throwNoChild(child, "update"); }
    protected Json insertChild(String child, Json node) { return throwNoChild(child, "insert"); }
    protected void append(Json node) { throwInapplicable("append"); }

    // --------------------------------------------------
    // Utility

    protected static String excAsStr(String msg) { throw new Error(msg); }

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

    private Json throwNoChild(String child, String op) {
        throw new Error("failed to " + op + " a child node " + child + ": " +
                getClass().getSimpleName() + " nodes do not have a child node");
    }

    private Json throwInapplicable(String op) {
        throw new Error("the operation '" + op + "' is not applicable to " +
                getClass().getSimpleName() + " nodes");
    }


    private void checkStringifyOptions(int indentSize, int indentLevel) {
        if (indentSize > 8) {
            throw new Error("indentSize cannot be larger than eight");
        }

        if (indentLevel < 0) {
            throw new Error("indentLevel cannot be a negative integer");
        }
    }

    private Json getNodeAt(String[] path, final int offset) {

        final int pathLen = path.length;

        Json node = this;
        for (int i = 0; i < pathLen - offset; i++) {
            node = node.getChild(path[i]);
            if (node == null) {
                throw new Error("no descendant named '" + path[i] + "'");
            }
        }

        return node;
    }

    private Json getParent(String[] path) {
        return getNodeAt(path, 1);
    }

    private Json getNode(String[] path) {
        return getNodeAt(path, 0);
    }

    private Json reachAndDo(String path, CrudCase case_, Json node) {

        if (path == null) {
            throw new Error("path cannot be null");
        } else {

            if (case_.needNode && node == null) {
                throw new Error("a null node");
            }

            String[] segments = path.split("\\.", -1);  // -1: do not discard trailing empty strings
            switch (case_) {
                case GET:
                    return getParent(segments).getChild(segments[segments.length - 1]);
                case REMOVE:
                    return getParent(segments).removeChild(segments[segments.length - 1]);
                case SET:
                    return getParent(segments).setChild(segments[segments.length - 1], node);
                case UPDATE:
                    return getParent(segments).updateChild(segments[segments.length - 1], node);
                case INSERT:
                    return getParent(segments).insertChild(segments[segments.length - 1], node);
                case APPEND:
                    getNode(segments).append(node);
                    return null;
                default:
                    assert(false);
                    return null;
            }
        }
    }
}
