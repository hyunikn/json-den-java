package com.wingsoft.jsonden;

import com.wingsoft.jsonden.exception.ParseError;

import java.io.IOException;

import java.math.BigInteger;
import java.math.BigDecimal;

/**
  * A subclass of {@link com.wingsoft.jsonden.Json Json} which represents JSON numbers.
  */
public class JsonNum extends JsonSimple {

    // ===================================================
    // Public

    /**
      * Constructs a {@code JsonNum} from a text representing a number.
      */
    public JsonNum(String text) {
        super(text);
    }

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

    /**
      * Checks the value (not reference) equality.
      */
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        JsonNum that = (JsonNum) o;
        return that.getBigDecimal().equals(this.getBigDecimal());
    }

    /**
      * Gets the hash code.
      */
    @Override
    public int hashCode() {
        if (!hashCached) {
            hash = getBigDecimal().hashCode();
            hashCached = true;
        }

        return hash;
    }

    /**
      * Parses the string into a {@code JsonNum}.
      * @param s string to parse
      * @return a JsonNum if s legally represent a JSON number.
      * @throws com.wingsoft.jsonden.exception.ParseError when s does not legally represent a Json number.
      */
    public static JsonNum parse(String s) throws ParseError {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonNum) {
            return (JsonNum) parsed;
        } else {
            throw new ParseError(ParseError.CASE_UNEXPECTED_JSON_TYPE,
                    "not parsed into a JsonNum but " + parsed.getClass().getSimpleName());
        }
    }

    /**
      * Returns {@code true}, overriding {@code isNum()} of {@link com.wingsoft.jsonden.Json Json}.
      */
    @Override
    public boolean isNum() { return true; }

    /**
      * Returns {@code this}, overriding {@code asNum()} of {@link com.wingsoft.jsonden.Json Json}.
      */
    @Override
    public JsonNum asNum() { return this; }

    /**
      * Returns the value as a {@code byte}.
      */
    @Override
    public byte getByte() {
        if (!byteCached) {
            byteVal = Byte.parseByte(text);
            byteCached = true;
        }

        return byteVal;
    }

    /**
      * Returns the value as a {@code short}.
      */
    @Override
    public short getShort() {
        if (!shortCached) {
            shortVal = Short.parseShort(text);
            shortCached = true;
        }

        return shortVal;
    }

    /**
      * Returns the value as an {@code int}.
      */
    @Override
    public int getInt() {
        if (!intCached) {
            intVal = Integer.parseInt(text);
            intCached = true;
        }

        return intVal;
    }

    /**
      * Returns the value as a {@code long}.
      */
    @Override
    public long getLong() {
        if (!longCached) {
            longVal = Long.parseLong(text);
            longCached = true;
        }

        return longVal;
    }

    /**
      * Returns the value as a {@code float}.
      */
    @Override
    public float getFloat() {
        if (!floatCached) {
            floatVal = Float.parseFloat(text);
            floatCached = true;
        }

        return floatVal;
    }

    /**
      * Returns the value as a {@code double}.
      */
    @Override
    public double getDouble() {
        if (!doubleCached) {
            doubleVal = Double.parseDouble(text);
            doubleCached = true;
        }

        return doubleVal;
    }

    /**
      * Returns the value as a {@code BigDecimal}.
      */
    @Override
    public BigDecimal getBigDecimal() {
        if (bigDecimalVal == null) {
            bigDecimalVal = new BigDecimal(text);
        }

        return bigDecimalVal;
    }

   // ===================================================
    // Package

    // invoked internally for a checked text

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

    private int hash;
    private boolean hashCached = false;;

    static String parseNum(String s) {
        try {
            new BigDecimal(s);
        } catch (NumberFormatException e) {
            throw new Error("the argument string '" + s + "' does not represent a valid JSON number");
        }

        return s;
    }
}

