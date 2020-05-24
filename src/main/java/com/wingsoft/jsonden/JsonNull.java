package com.wingsoft.jsonden;

public class JsonNull extends JsonSimple {

    // ===================================================
    // Public

    public JsonNull() {
        super("null");
    }

    public static JsonNull parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonNull) {
            return (JsonNull) parsed;
        } else {
            throw new Error("not parsed into a JsonNull but " + parsed.getClass().getSimpleName());
        }
    }

    @Override
    public boolean isNull() {
        return true;
    }

    // ===================================================
    // Protected

    @Override
    protected String getTypeName() {
        return "null";
    }
}

