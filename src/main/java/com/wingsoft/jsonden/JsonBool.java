package com.wingsoft.jsonden;

public class JsonBool extends JsonSimple {

    // ===================================================
    // Public

    public JsonBool(boolean val) {
        super(Boolean.toString(val));
    }

    public static JsonBool parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonBool) {
            return (JsonBool) parsed;
        } else {
            throw new Error("not parsed into a JsonBool but " + parsed.getClass().getSimpleName());
        }
    }

    @Override
    public boolean asBoolean() {
        if (!cached) {
            val = Boolean.parseBoolean(text);
            cached = true;
        }

        return val;
    }

    // ===================================================
    // Protected

    boolean val;
    boolean cached;

    @Override
    protected String getTypeName() {
        return "boolean";
    }

}

