package com.wingsoft.jsonden;

public class JsonNull extends JsonSimple {

    // ===================================================
    // Public

    public static JsonNull parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonNull) {
            return (JsonNull) parsed;
        } else {
            throw new Error("not parsed into a JsonNull but " + parsed.getTypeName());
        }
    }

    public JsonNull() {
        super("null");
    }

    // ===================================================
    // Protected

    @Override
    protected String getTypeName() {
        return "null";
    }
}

