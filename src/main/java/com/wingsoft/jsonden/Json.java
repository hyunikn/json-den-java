package com.wingsoft.jsonden;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

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
    public Json getX(String path) {

        if (path == null) {
            throw new Error("path cannot be null");
        }

        String[] segments = path.split("\\.", -1);  // -1: do not discard trailing empty strings

        Json node = this;
        for (String s: segments) {
            node = node.getChild(s);
            if (node == null) {
                return null;
            }
        }

        return node;
    }

    @Override
    public String toString() { return stringify(0, 0); }

    // --------------------------------------------------
    // for six individual JSON types

    public boolean isObj()  { return false; }
    public boolean isArr()  { return false; }
    public boolean isBool() { return false; }
    public boolean isNum()  { return false; }
    public boolean isNull() { return false; }
    public boolean isStr()  { return false; }

    public JsonObj  asObj()  { return null; }
    public JsonArr  asArr()  { return null; }
    public JsonBool asBool() { return null; }
    public JsonNum  asNum()  { return null; }
    public JsonNull asNull() { return null; }
    public JsonStr  asStr()  { return null; }

    public LinkedHashMap<String, Json> asMap() { return (LinkedHashMap<String, Json>) throwInapplicable("asMap"); }
    public Json[]       asArray()       { return (Json[]) throwInapplicable("asArray"); }
    public boolean      asBoolean()     { return (boolean) throwInapplicable("asBoolean"); }
    public byte         asByte()        { return (byte) throwInapplicable("asByte"); }
    public short        asShort()       { return (short) throwInapplicable("asShort"); }
    public int          asInt()         { return (int) throwInapplicable("asInt"); }
    public long         asLong()        { return (long) throwInapplicable("asLong"); }
    public float        asFloat()       { return (float) throwInapplicable("asFloat"); }
    public double       asDouble()      { return (double) throwInapplicable("asDouble"); }
    public BigDecimal   asBigDecimal()  { return (BigDecimal) throwInapplicable("asBigDecimal"); }
    public String       asString()      { return (String) throwInapplicable("asString"); }

    // ===================================================
    // Protected

    protected Json() { }

    // overriden by all
    protected abstract String getTypeName();
    protected abstract void write(StringBuffer sbuf, int indentSize, int indentLevel);
    protected abstract Json getChild(String key);

    // --------------------------------------------------
    // Utility

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

    private Object throwInapplicable(String op) {
        throw new Error(op + " is not applicable to " + getClass().getSimpleName() + " nodes");
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
}
