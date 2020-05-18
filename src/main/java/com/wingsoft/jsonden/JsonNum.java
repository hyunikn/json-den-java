package com.wingsoft.jsonden;

import java.math.BigInteger;
import java.math.BigDecimal;

public class JsonNum extends JsonPrim {

    // ===================================================
    // Public

    public static Json parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonNum) {
            return (JsonNum) parsed;
        } else {
            throw new Error("not parsed into a JsonNum but " + parsed.getClass().getSimpleName());
        }
    }

    public JsonNum(byte b) {
        super(Byte.toString(b));
    }

    public JsonNum(short s) {
        super(Short.toString(s));
    }

    public JsonNum(int i) {
        super(Integer.toString(i));
    }

    public JsonNum(long l) {
        super(Long.toString(l));
    }

    public JsonNum(float f) {
        super(Float.toString(f));
    }

    public JsonNum(double d) {
        super(Double.toString(d));
    }

    public JsonNum(BigInteger bi) {
        super(bi == null ? excAsStr("argument cannot be null") : bi.toString());
    }

    public JsonNum(BigDecimal bd) {
        super(bd == null ? excAsStr("argument cannot be null") : bd.toString());
    }

    // ===================================================
    // Package

    // invoked internally for a checked text (for example, by the above parse() or the lexer)
    JsonNum(String text) {
        super(text);
    }

    // ===================================================
    // Private

    static String parseNum(String s) {
        try {
            new BigDecimal(s);
        } catch (NumberFormatException e) {
            throw new Error("the argument string '" + s + "' does not represent a valid JSON number");
        }

        return s;
    }

}

