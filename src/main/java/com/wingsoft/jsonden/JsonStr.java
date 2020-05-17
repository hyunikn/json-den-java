package com.wingsoft.jsonden;

public class JsonStr extends JsonPrim {

    // ===================================================
    // Public

    public JsonStr(String str) {
        super(str);
    }

    public String asString() {
        return text;
    }

    // ===================================================
    // Protected

    @Override
    protected void fill(StringBuffer sbuf, int indentSize, int indentLevel) {
        putIndent(sbuf, indentSize, indentLevel);
        sbuf.append('"');
        sbuf.append(text);
        sbuf.append('"');
    }
}

