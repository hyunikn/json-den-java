package com.wingsoft.jsonden;

import com.wingsoft.jsonden.exception.ParseError;

import java.io.IOException;

/**
  * A subclass of {@link com.wingsoft.jsonden.Json Json} which represents the JSON null.
  * There are no constructors of this class. Instead, only a lookup function is provided
  * which returns a ready-made singleton instance of {@code JsonNull}.
  */
public class JsonNull extends JsonSimple {

    // ===================================================
    // Public

    /**
      * Gets the singleton instance of {@code JsonNull}.
      */
    public static JsonNull lookup() {
        return singleton;
    }

    /**
      * Checks the value (and reference) equality.
      */
    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    /**
      * Gets the hash code.
      */
    @Override
    public int hashCode() {
        return 180; // my height in cm
    }

    /**
      * Parses the string into a {@code JsonNull}.
      * @param s string to parse
      * @return a JsonNull if s legally represent a JSON null.
      * @throws com.wingsoft.jsonden.exception.ParseError when s does not legally represent a Json null.
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
      * Returns {@code true}, overriding {@code isNull()} of {@link com.wingsoft.jsonden.Json Json}.
      */
    @Override
    public boolean isNull() { return true; }

    /**
      * Returns {@code this}, overriding {@code asNull()} of {@link com.wingsoft.jsonden.Json Json}.
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

    private static JsonNull singleton = new JsonNull();

    private JsonNull() {
        super("null");
    }
}

