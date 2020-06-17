package com.wingsoft.jsonden;

import com.wingsoft.jsonden.exception.ParseError;

import java.io.IOException;

public class JsonNull extends JsonSimple {

    // ===================================================
    // Public

    public static JsonNull getJsonNull() {
        return singleton;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return 180; // my height in cm
    }

    public static JsonNull parse(String s) throws ParseError {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonNull) {
            return (JsonNull) parsed;
        } else {
            throw new Error("not parsed into a JsonNull but " + parsed.getClass().getSimpleName());
        }
    }

    @Override
    public boolean isNull() { return true; }
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

