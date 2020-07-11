package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.ParseError;

import java.io.IOException;

/**
  * A subclass of {@link com.github.hyunikn.jsonden.Json Json} which represents JSON booleans.
  */
public class JsonBool extends JsonLeaf {

    // ===================================================
    // Public

    /**
      * Gets an {@code JsonBool}
      */
    public static JsonBool instance(boolean val) {
        return new JsonBool(val);
    }

    /**
      * Checks the value (not reference) equality.
      */
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        JsonBool that = (JsonBool) o;
        return that.val == this.val;
    }

    /**
      * Gets the hash code.
      */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
      * Deep copy
      */
    public Object clone() {
        JsonBool clone = new JsonBool(this.val);

        String[] cl = this.remarkLines();
        if (cl != null) {
            clone.setRemarkLines(cl);
        }

        return clone;
    }

    /**
      * Parses the string into a {@code JsonBool}.
      * @param s string to parse
      * @return a JsonBool if s legally represent a JSON boolean.
      * @throws com.github.hyunikn.jsonden.exception.ParseError when s does not legally represent a Json boolean.
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
      * Returns {@code true}, overriding {@code isBool()} of {@link com.github.hyunikn.jsonden.Json Json}.
      */
    @Override
    public boolean isBool() { return true; }

    /**
      * Returns {@code this}, overriding {@code asBool()} of {@link com.github.hyunikn.jsonden.Json Json}.
      */
    @Override
    public JsonBool asBool() { return this; }

    /**
      * Returns the value as a boolean.
      */
    public boolean getBoolean() {
        return val;
    }

    // ===================================================
    // Protected

    protected final boolean val;

    protected JsonBool(boolean val) {
        super(Boolean.toString(val));
        this.val = val;
        this.hash = val ? 31 : 13;   // Baskin Robbins number and its reverse
    }

    @Override
    protected String getTypeName() {
        return "boolean";
    }

    // ===================================================
    // Private

    private final int hash;
}

