package io.github.hyunikn.jsonden;

import io.github.hyunikn.jsonden.exception.ParseError;

import java.io.IOException;

/**
  * A subclass of {@link io.github.hyunikn.jsonden.Json Json} which represents JSON strings.
  */
public class JsonStr extends JsonLeaf {

    // ===================================================
    // Public

    /**
      * Gets a {@code JsonStr}.
      */
    public static JsonStr instance(String str) {
        return new JsonStr(str);
    }

    /**
      * Checks the value (not reference) equality.
      */
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        JsonStr that = (JsonStr) o;
        return that.text.equals(this.text);
    }

    /**
      * Gets the hash code.
      */
    @Override
    public int hashCode() {
        if (!hashCached) {
            hash = text.hashCode();
            hashCached = true;
        }

        return hash;
    }

    /**
      * Deep copy.
      */
    @Override
    public JsonStr clone() {
        JsonStr clone = new JsonStr(this.text);
        clone.copyAnnotationsOf(this);
        return clone;
    }

    /**
      * Parses the string into a {@code JsonStr}.
      * @param s string to parse
      * @return a JsonStr if s legally represent a JSON string.
      * @throws io.github.hyunikn.jsonden.exception.ParseError when s does not legally represent a Json string.
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

    /**
      * Returns {@code true}, overriding {@code isStr()} of {@link io.github.hyunikn.jsonden.Json Json}.
      */
    @Override
    public boolean isStr() { return true; }

    /**
      * Returns {@code this}, overriding {@code asStr()} of {@link io.github.hyunikn.jsonden.Json Json}.
      */
    @Override
    public JsonStr asStr() { return this; }

    /**
      * Returns the value as a {@code String}.
      */
    public String getString() { return text; }

    // ===================================================
    // Protected

    protected JsonStr(String str) {
        super(str);
    }

    @Override
    protected String getTypeName() {
        return "string";
    }

    @Override
    protected void write(StringBuffer sbuf, StrOpt opt, int indentLevel) {

        if (indentLevel >= 0) {
            if (!opt.omitComments) {
                writeComment(sbuf, commentLines, opt.indentSize, indentLevel);
            }
            if (!opt.omitRemarks) {
                writeRemark(sbuf, remarkLines, opt.indentSize, indentLevel);
            }
            writeIndent(sbuf, opt.indentSize, indentLevel);
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

