package com.wingsoft.jsonden;

public abstract class Json {

    // ===================================================
    // Public

    public static Json parse(String s) {
        // TODO:
        return null;
    }

    public String stringify(int indentSize, int indentLevel) {

        if (indentSize <= 0) {
            throw new Error("indentSize cannot be a negative integer");
        }

        StringBuffer sbuf = new StringBuffer();
        write(sbuf, indentSize, indentLevel);
        return sbuf.toString();
    }

    public void prettyPrint(int indentSize, int indentLevel) {
        if (indentSize <= 0) {
            throw new Error("indentSize cannot be a negative integer");
        }

        StringBuffer sbuf = new StringBuffer();
        write(sbuf, indentSize, indentLevel);
        System.out.print(sbuf.toString());
    }

    public void write(StringBuffer sbuf, int indentSize, int indentLevel) {
        if (indentSize <= 0) {
            throw new Error("indentSize cannot be a negative integer");
        }

        write(sbuf, indentSize, indentLevel);
    }

    @Override
    public String toString() {
        return stringify(0, 0);
    }

    public abstract Json get(String path);

    // ===================================================
    // Protected

    protected Json() { }

    protected abstract void fill(StringBuffer sbuf, int indentSize, int indentLevel);

    // --------------------------------------------------
    // Utility

    protected static String excAsStr(String msg) {
        throw new Error(msg);
    }

    protected static void putIndent(StringBuffer sbuf, int indentSize, int indentLevel) {

        if (indentSize == 0) {
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

    private void checkWriteOptions(int indentSize, int indentLevel) {
        if (indentSize < 0) {
            throw new Error("indentSize cannot be a negative integer");
        }
        if (indentSize > 8) {
            throw new Error("indentSize cannot be larger than eight");
        }

        if (indentLevel < 0) {
            throw new Error("indentLevel cannot be a negative integer");
        }
    }
}
