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
    public Json getX(String path) { return (Json) reachAndDo(path, CrudCase.GET, null); }
    public Json delX(String path) { return (Json) reachAndDo(path, CrudCase.DEL, null); }

    // for JsonObj
    public Json setX(String path, Json json) { return (Json) reachAndDo(path, CrudCase.SET, json); }

    // for JsonArr
    public Json updateX(String path, Json json) { return (Json) reachAndDo(path, CrudCase.UPDATE, json); }
    public Json insertX(String path, Json json) { return (Json) reachAndDo(path, CrudCase.INSERT, json); }
    public void appendX(String path, Json json) { reachAndDo(path, CrudCase.APPEND, json); }

    // for JsonBool
    public boolean asBooleanX(String path) { return (Boolean) reachAndDo(path, CrudCase.AS_BOOLEAN, null); }

    // for JsonNull
    public boolean isNullX(String path) { return (Boolean) reachAndDo(path, CrudCase.IS_NULL, null); }

    // for JsonNum
    public byte asByteX(String path) { return (Byte) reachAndDo(path, CrudCase.AS_BYTE, null); }
    public short asShortX(String path) { return (Short) reachAndDo(path, CrudCase.AS_SHORT, null); }
    public int asIntX(String path) { return (Integer) reachAndDo(path, CrudCase.AS_INT, null); }
    public long asLongX(String path) { return (Long) reachAndDo(path, CrudCase.AS_LONG, null); }
    public float asFloatX(String path) { return (Float) reachAndDo(path, CrudCase.AS_FLOAT, null); }
    public double asDoubleX(String path) { return (Double) reachAndDo(path, CrudCase.AS_DOUBLE, null); }

    // for JsonStr
    public String asStringX(String path) { return (String) reachAndDo(path, CrudCase.AS_STRING, null); }

    @Override
    public String toString() { return stringify(-1, 0); } // -1: no indentation and whitespaces (that is, minified)

    // ===================================================
    // Protected

    protected enum CrudCase {
        GET(false),
        DEL(false),
        SET(true),
        UPDATE(true),
        INSERT(true),
        APPEND(true),
        AS_BOOLEAN(false),
        IS_NULL(false),
        AS_BYTE(false),
        AS_SHORT(false),
        AS_INT(false),
        AS_LONG(false),
        AS_FLOAT(false),
        AS_DOUBLE(false),
        AS_STRING(false),
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
    protected Json getChild(String key) { return throwNoChild(key, "get"); }
    protected Json delChild(String key) { return throwNoChild(key, "del"); }

    // overriden by JsonObj
    protected Json setChild(String key, Json node) { return throwNoChild(key, "set"); }

    // overriden by JsonArr
    protected Json updateChild(String key, Json node) { return throwNoChild(key, "update"); }
    protected Json insertChild(String key, Json node) { return throwNoChild(key, "insert"); }
    protected void append(Json node) { throwInapplicable("append"); }

    // overriden by JsonBool
    public boolean asBoolean() { return (Boolean) throwInapplicable("asBoolean"); }

    // overriden by JsonNull
    public boolean isNull() { return (Boolean) throwInapplicable("isNull"); }

    // overriden by JsonNum
    public byte asByte() { return (Byte) throwInapplicable("asByte"); }
    public short asShort() { return (Short) throwInapplicable("asShort"); }
    public int asInt() { return (Integer) throwInapplicable("asInt"); }
    public long asLong() { return (Long) throwInapplicable("asLong"); }
    public float asFloat() { return (Float) throwInapplicable("asFloat"); }
    public double asDouble() { return (Double) throwInapplicable("asDouble"); }

    // overriden by JsonStr
    public String asString() { return (String) throwInapplicable("asString"); }

    // --------------------------------------------------
    // Utility

    protected static String excAsStr(String msg) { throw new Error(msg); }

    protected static void writeIndent(StringBuffer sbuf, int indentSize, int indentLevel) {

        if (indentSize == 0 || indentLevel == 0) {
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

    private Json throwNoChild(String key, String op) {
        throw new Error("failed to " + op + " a child node " + key + ": " +
                getClass().getSimpleName() + " nodes do not have a child node");
    }

    private Object throwInapplicable(String op) {
        throw new Error("the operation '" + op + "' is not applicable to " +
                getClass().getSimpleName() + " nodes");
    }


    private void checkStringifyOptions(int indentSize, int indentLevel) {
        if (indentSize > 8) {
            throw new Error("indentSize cannot be larger than eight");
        }
        if (indentSize < 0) {
            throw new Error("indentSize cannot be a negative integer");
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
        if (path == null) {
            throw new Error("path cannot be null");
        } else {
            return getNodeAt(path, 1);
        }
    }

    private Json getNode(String[] path) {
        if (path == null) {
            return this;
        } else {
            return getNodeAt(path, 0);
        }
    }

    private Object reachAndDo(String path, CrudCase case_, Json node) {

        if (case_.needNode && node == null) {
            throw new Error("node cannot be null");
        }

        String[] segments = (path == null) ? null : path.split("\\.", -1);  // -1: do not discard trailing empty strings
        switch (case_) {
            case GET:
                return getParent(segments).getChild(segments[segments.length - 1]);
            case DEL:
                return getParent(segments).delChild(segments[segments.length - 1]);
            case SET:
                return getParent(segments).setChild(segments[segments.length - 1], node);
            case UPDATE:
                return getParent(segments).updateChild(segments[segments.length - 1], node);
            case INSERT:
                return getParent(segments).insertChild(segments[segments.length - 1], node);
            case APPEND:
                getNode(segments).append(node);
                return null;
            case AS_BOOLEAN:
                return getNode(segments).asBoolean();
            case IS_NULL:
                return getNode(segments).isNull();
            case AS_BYTE:
                return getNode(segments).asByte();
            case AS_SHORT:
                return getNode(segments).asShort();
            case AS_INT:
                return getNode(segments).asInt();
            case AS_LONG:
                return getNode(segments).asLong();
            case AS_FLOAT:
                return getNode(segments).asFloat();
            case AS_DOUBLE:
                return getNode(segments).asDouble();
            case AS_STRING:
                return getNode(segments).asString();
            default:
                assert(false);
                return null;
        }
    }
}
