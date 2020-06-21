package com.wingsoft.jsonden;

import com.wingsoft.jsonden.exception.ParseError;

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

    /**
      * Parses the string into a {@code JsonStr}.
      * @param s string to parse
      * @return a JsonStr if s legally represent a JSON string.
      * @throws com.wingsoft.jsonden.exception.ParseError when s does not legally represent a Json string.
      */
    public static JsonStr parse(String s) throws ParseError {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonStr) {
            return (JsonStr) parsed;
        } else {
            throw new ParseError(ParseError.CASE_UNEXPECTED_JSON_TYPE,
                    "not parsed into a JsonStr but " + parsed.getClass().getSimpleName());
        }
    }

    @Override
    public boolean isStr() { return true; }
    @Override
    public JsonStr asStr() { return this; }
    @Override
    public String getString() { return text; }

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

