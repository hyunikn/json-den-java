package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.ParseError;

import java.io.IOException;

/**
  * A subclass of {@link com.github.hyunikn.jsonden.Json Json} which represents the JSON null.
  */
public class JsonNull extends JsonSimple {

    // ===================================================
    // Public

    public JsonNull() {
        super("null");
    }

    /**
      * Checks the value (not reference) equality.
      */
    @Override
    public boolean equals(Object o) {
        return (o != null && o.getClass() == this.getClass());
    }

    /**
      * Gets the hash code.
      */
    @Override
    public int hashCode() {
        return 180; // my height in cm
    }

    /**
      * Deep copy
      */
    public Object clone() {
        JsonNull clone = new JsonNull();

        String[] cl = this.remarkLines();
        if (cl != null) {
            clone.setRemarkLines(cl);
        }

        return clone;
    }

    /**
      * Parses the string into a {@code JsonNull}.
      * @param s string to parse
      * @return a JsonNull if s legally represent a JSON null.
      * @throws com.github.hyunikn.jsonden.exception.ParseError when s does not legally represent a Json null.
      */
    public static JsonNull parse(String s) throws ParseError {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonNull) {
            return (JsonNull) parsed;
        } else {
            throw new ParseError(ParseError.CASE_UNEXPECTED_JSON_TYPE,
                    "not parsed into a JsonNull but " + parsed.getClass().getSimpleName());
        }
    }

    /**
      * Returns {@code true}, overriding {@code isNull()} of {@link com.github.hyunikn.jsonden.Json Json}.
      */
    @Override
    public boolean isNull() { return true; }

    /**
      * Returns {@code this}, overriding {@code asNull()} of {@link com.github.hyunikn.jsonden.Json Json}.
      */
    @Override
    public JsonNull asNull() { return this; }

    // ===================================================
    // Protected

    @Override
    protected String getTypeName() {
        return "null";
    }

    // ===================================================
    // Private

}

