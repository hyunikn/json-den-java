package com.wingsoft.jsonden;

public class JsonNull extends JsonSimple {

    // ===================================================
    // Public

    public static JsonNull getJsonNull() {
        if (singleton == null) {
            singleton = motherNull.createJsonNull();
        }
        return singleton;
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
    public boolean isNull() { return true; }
    @Override
    public JsonNull asNull() { return this; }

    // ===================================================
    // Protected

    @Override
    protected String getTypeName() {
        return "null";
    }

    // overridable factory method
    protected JsonNull createJsonNull() {
        return new JsonNull();
    }

    // ===================================================
    // Private

    private static final JsonNull motherNull = new JsonNull();
    private static JsonNull singleton;

    private JsonNull() {
        super("null");
    }
}

