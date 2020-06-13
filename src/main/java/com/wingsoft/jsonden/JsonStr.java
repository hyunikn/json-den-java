package com.wingsoft.jsonden;

import java.io.IOException;

public class JsonStr extends JsonSimple {

    // ===================================================
    // Public

    public JsonStr(String str) {
        super(str);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        JsonBool that = (JsonBool) o;
        return that.text.equals(this.text);
    }

    @Override
    public int hashCode() {
        if (!hashCached) {
            hash = text.hashCode();
            hashCached = true;
        }

        return hash;
    }

    public static JsonStr parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonStr) {
            return (JsonStr) parsed;
        } else {
            throw new Error("not parsed into a JsonStr but " + parsed.getClass().getSimpleName());
        }
    }

    @Override
    public boolean isStr() { return true; }
    @Override
    public JsonStr asStr() { return this; }
    @Override
    public String asString() { return text; }

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

    // ===================================================
    // Private

    private int hash;
    private boolean hashCached = false;;

}

