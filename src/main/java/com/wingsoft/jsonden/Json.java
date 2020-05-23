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
    public Json getx(String path) {
        return reachAndDo(CrudCase.GET, path, null);
    }
    public Json removex(String path) {
        return reachAndDo(CrudCase.REMOVE, path, null);
    }

    // for JsonObj
    public Json setx(String path, Json json) {
        return reachAndDo(CrudCase.SET, path, json);
    }

    // for JsonArr
    public Json updatex(String path, Json json) {
        return reachAndDo(CrudCase.UPDATE, path, json);
    }
    public Json insertx(String path, Json json) {
        return reachAndDo(CrudCase.INSERT, path, json);
    }
    public Json appendx(String path, Json json) {
        return reachAndDo(CrudCase.APPEND, path, json);
    }

    @Override
    public String toString() {
        return stringify(-1, 0); // -1: without indentation and delimiting whitespaces (that is, minified)
    }

    // ===================================================
    // Protected

    protected enum CrudCase {
        GET,
        REMOVE,
        SET,
        UPDATE,
        INSERT,
        APPEND,
        BOUND_CRUD_CASE;
    }

    protected Json() { }
    protected abstract void write(StringBuffer sbuf, int indentSize, int indentLevel);
    protected abstract String getTypeName();

    protected Json getChild(String child) {
        return throwNoChild(child, "get");
    }
    protected abstract Json removeChild(String child) {
        return throwNoChild(child, "remove");
    }
    protected abstract Json setChild(String child, Json child) {
        return throwNoChild(child, "set");
    }
    protected abstract Json updateChild(String child, Json child) {
        return throwNoChild(child, "update");
    }
    protected abstract Json insertChild(String child, Json child) {
        return throwNoChild(child, "insert");
    }
    protected abstract Json appendChild(Json child) {
        return throwInapplicable("append");
    }

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

    private Json throwNoChild(String child, String op) {
        throw new Error("failed to " + op + " a child node " + name + ": " +
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

    private Json reachAndDo(CrudCase case_, String path, Json node) {

        if (path == null) {
            throw new Error("path cannot be null");
        } else {

            if (case_ == CrudCase.SET || case_ == CrudCase.UPDATE ||
                case_ == CrudCase.INSERT || case_ == CurdCase.APPEND) {

                if (node == null) {
                    throw new Error("a null node");
                }
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
                    return getNode(segments).appendChild(node);
                default:
                    assert(false);
                    return null;
            }
        }
    }
}
