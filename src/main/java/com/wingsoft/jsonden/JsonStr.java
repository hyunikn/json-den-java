package com.wingsoft.jsonden;

public class JsonStr extends JsonSimple {

    // ===================================================
    // Public

    public JsonStr(String str) {
        super(str);
    }

    public static JsonStr parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonStr) {
            return (JsonStr) parsed;
        } else {
            throw new Error("not parsed into a JsonStr but " + parsed.getClass().getSimpleName());
        }
    }

    public String asString() {
        return text;
    }

    // ===================================================
    // Protected

    @Override
    protected String getTypeName() {
        return "string";
    }

    @Override
    protected void write(StringBuffer sbuf, int indentSize, int indentLevel) {

        if (indentSize < 0) {
            // negative indent size indicates that we are right after a key in an object
            indentSize *= -1;
        } else {
            writeIndent(sbuf, indentSize, indentLevel);
        }

        sbuf.append('"');
        sbuf.append(text);
        sbuf.append('"');
    }
}

