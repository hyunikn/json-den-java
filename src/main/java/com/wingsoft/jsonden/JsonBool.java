package com.wingsoft.jsonden;

public class JsonBool extends JsonPrim {

    // ===================================================
    // Public

    public static Json parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonBool) {
            return (JsonBool) parsed;
        } else {
            throw new Error("not parsed into a JsonBool but " + parsed.getClass().getSimpleName());
        }
    }

    public JsonBool(boolean b) {
        super(Boolean.toString(b));
    }
}

