package com.wingsoft.jsonden;

import java.math.BigInteger;
import java.math.BigDecimal;

public class JsonNum extends JsonSimple {

    // ===================================================
    // Public

    public JsonNum(byte val) {
        super(Byte.toString(val));
    }

    public JsonNum(short val) {
        super(Short.toString(val));
    }

    public JsonNum(int val) {
        super(Integer.toString(val));
    }

    public JsonNum(long val) {
        super(Long.toString(val));
    }

    public JsonNum(float val) {
        super(Float.toString(val));
    }

    public JsonNum(double val) {
        super(Double.toString(val));
    }

    public JsonNum(BigDecimal val) {
        super(val.toPlainString());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        JsonNum that = (JsonNum) o;
        return that.asBigDecimal().equals(this.asBigDecimal());
    }

    public static JsonNum parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonNum) {
            return (JsonNum) parsed;
        } else {
            throw new Error("not parsed into a JsonNum but " + parsed.getClass().getSimpleName());
        }
    }

    @Override
    public boolean isNum() { return true; }
    @Override
    public JsonNum asNum() { return this; }

    @Override
    public byte asByte() {
        if (!byteCached) {
            byteVal = Byte.parseByte(text);
            byteCached = true;
        }

        return byteVal;
    }

    @Override
    public short asShort() {
        if (!shortCached) {
            shortVal = Short.parseShort(text);
            shortCached = true;
        }

        return shortVal;
    }

    @Override
    public int asInt() {
        if (!intCached) {
            intVal = Integer.parseInt(text);
            intCached = true;
        }

        return intVal;
    }

    @Override
    public long asLong() {
        if (!longCached) {
            longVal = Long.parseLong(text);
            longCached = true;
        }

        return longVal;
    }

    @Override
    public float asFloat() {
        if (!floatCached) {
            floatVal = Float.parseFloat(text);
            floatCached = true;
        }

        return floatVal;
    }

    @Override
    public double asDouble() {
        if (!doubleCached) {
            doubleVal = Double.parseDouble(text);
            doubleCached = true;
        }

        return doubleVal;
    }

    @Override
    public BigDecimal asBigDecimal() {
        if (bigDecimalVal == null) {
            bigDecimalVal = new BigDecimal(text);
        }

        return bigDecimalVal;
    }

   // ===================================================
    // Package

    // invoked internally for a checked text
    JsonNum(String text) {
        super(text);
    }

    // ===================================================
    // Protected

    protected byte byteVal;
    protected boolean byteCached;

    protected short shortVal;
    protected boolean shortCached;

    protected int intVal;
    protected boolean intCached;

    protected long longVal;
    protected boolean longCached;

    protected float floatVal;
    protected boolean floatCached;

    protected double doubleVal;
    protected boolean doubleCached;

    protected BigDecimal bigDecimalVal;

    @Override
    protected String getTypeName() {
        return "number";
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

