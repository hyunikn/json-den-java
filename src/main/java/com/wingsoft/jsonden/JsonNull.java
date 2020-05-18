package com.wingsoft.jsonden;

public class JsonNull extends JsonPrim {

    // ===================================================
    // Public

    public static Json parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonNull) {
            return (JsonNull) parsed;
        } else {
            throw new Error("not parsed into a JsonNull but " + parsed.getClass().getSimpleName());
        }
    }

    public JsonNull() {
        super("null");
    }
}

