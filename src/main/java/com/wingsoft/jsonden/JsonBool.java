package com.wingsoft.jsonden;

public class JsonBool extends JsonSimple {

    // ===================================================
    // Public

    public static JsonBool parse(String s) {
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

    // ===================================================
    // Protected

    @Override
    protected String getTypeName() {
        return "boolean";
    }

}

