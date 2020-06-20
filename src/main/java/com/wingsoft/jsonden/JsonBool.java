package com.wingsoft.jsonden;

import com.wingsoft.jsonden.exception.ParseError;

import java.io.IOException;

public class JsonBool extends JsonSimple {

    // ===================================================
    // Public

    public static JsonBool getJsonBool(boolean val) {
        return val ? trueVal : falseVal;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    public static JsonBool parse(String s) throws ParseError {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonBool) {
            return (JsonBool) parsed;
        } else {
            throw new Error("not parsed into a JsonBool but " + parsed.getClass().getSimpleName());
        }
    }

    @Override
    public boolean isBool() { return true; }
    @Override
    public JsonBool asBool() { return this; }
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

