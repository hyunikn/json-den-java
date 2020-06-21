package com.wingsoft.jsonden;

import com.wingsoft.jsonden.exception.ParseError;

import java.io.IOException;

/**
  * A subclass of {@link com.wingsoft.jsonden.Json Json} which represents JSON boolean.
  * There are no constructors of this class, instead, only a lookup function {@code getJsonBool()}
  * which returns one of ready-made doubleton instances of {@code JsonBool}.
  */
public class JsonBool extends JsonSimple {

    // ===================================================
    // Public

    /**
      * Gets one of two instances of {@code JsonBool} matching the boolean {@code val}.
      */
    public static JsonBool getJsonBool(boolean val) {
        return val ? trueVal : falseVal;
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
        return hash;
    }

    /**
      * Parses the string into a {@code JsonBool}.
      * @param s string to parse
      * @return a JsonBool if s legally represent a JSON boolean.
      * @throws com.wingsoft.jsonden.exception.ParseError when s does not legally represent a Json boolean.
      */
    public static JsonBool parse(String s) throws ParseError {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonBool) {
            return (JsonBool) parsed;
        } else {
            throw new ParseError(ParseError.CASE_UNEXPECTED_JSON_TYPE,
				    "not parsed into a JsonBool but " + parsed.getClass().getSimpleName());
        }
    }

    /**
      * Returns {@code true}, overriding {@code isBool} of {@link com.wingsoft.jsonden.Json Json}.
      */
    @Override
    public boolean isBool() { return true; }

    /**
      * Returns {@code this}, overriding {@code asBool} of {@link com.wingsoft.jsonden.Json Json}.
      */
    @Override
    public JsonBool asBool() { return this; }

    /**
      * Returns the value as a boolean.
      */
    @Override
    public boolean getBoolean() {
        return val;
    }

    // ===================================================
    // Protected

    protected final boolean val;

    @Override
    protected String getTypeName() {
        return "boolean";
    }

    // ===================================================
    // Private

    private static final JsonBool trueVal = new JsonBool(true);
    private static final JsonBool falseVal = new JsonBool(false);

    private final int hash;

    private JsonBool(boolean val) {
        super(Boolean.toString(val));
        this.val = val;
        this.hash = val ? 31 : 13;   // Baskin Robbins number and its reverse
    }
}

