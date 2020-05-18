package com.wingsoft.jsonden;

public class JsonStr extends JsonPrim {

    // ===================================================
    // Public

    public static Json parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonStr) {
            return (JsonStr) parsed;
        } else {
            throw new Error("not parsed into a JsonStr but " + parsed.getClass().getSimpleName());
        }
    }

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

