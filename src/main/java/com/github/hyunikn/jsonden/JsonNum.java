package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.ParseError;

import java.io.IOException;

import java.math.BigInteger;
import java.math.BigDecimal;

/**
  * A subclass of {@link com.github.hyunikn.jsonden.Json Json} which represents JSON numbers.
  */
public class JsonNum extends JsonLeaf {

    // ===================================================
    // Public

    /**
      * Gets a {@code JsonNum} from a text which represents a number.
      */
    public static JsonNum instance(String text) {
        return new JsonNum(text);
    }
    /**
      * Gets a {@code JsonNum} from a byte.
      */
    public static JsonNum instance(byte val) {
        return new JsonNum(val);
    }
    /**
      * Gets a {@code JsonNum} from a short.
      */
    public static JsonNum instance(short val) {
        return new JsonNum(val);
    }
    /**
      * Gets a {@code JsonNum} from a int.
      */
    public static JsonNum instance(int val) {
        return new JsonNum(val);
    }
    /**
      * Gets a {@code JsonNum} from a long.
      */
    public static JsonNum instance(long val) {
        return new JsonNum(val);
    }
    /**
      * Gets a {@code JsonNum} from a float.
      */
    public static JsonNum instance(float val) {
        return new JsonNum(val);
    }
    /**
      * Gets a {@code JsonNum} from a double.
      */
    public static JsonNum instance(double val) {
        return new JsonNum(val);
    }
    /**
      * Gets a {@code JsonNum} from a BigInteger.
      */
    public static JsonNum instance(BigInteger val) {
        return new JsonNum(val);
    }
    /**
      * Gets a {@code JsonNum} from a BigDecimal.
      */
    public static JsonNum instance(BigDecimal val) {
        return new JsonNum(val);
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
      * Deep copy
      */
    @Override
    public JsonNum clone() {
        JsonNum clone = new JsonNum(this.text);

        String[] cl = this.remarkLines();
        if (cl != null) {
            clone.setRemarkLines(cl);
        }

        return clone;
    }

    /**
      * Parses the string into a {@code JsonNum}.
      * @param s string to parse
      * @return a JsonNum if s legally represent a JSON number.
      * @throws com.github.hyunikn.jsonden.exception.ParseError when s does not legally represent a Json number.
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
      * Returns {@code true}, overriding {@code isNum()} of {@link com.github.hyunikn.jsonden.Json Json}.
      */
    @Override
    public boolean isNum() { return true; }

    /**
      * Returns {@code this}, overriding {@code asNum()} of {@link com.github.hyunikn.jsonden.Json Json}.
      */
    @Override
    public JsonNum asNum() { return this; }

    /**
      * Returns the value as a {@code byte}.
      */
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
    public double getDouble() {
        if (!doubleCached) {
            doubleVal = Double.parseDouble(text);
            doubleCached = true;
        }

        return doubleVal;
    }

    /**
      * Returns the value as a {@code BigInteger}.
      */
    public BigInteger getBigInteger() {
        if (bigIntegerVal == null) {
            bigIntegerVal = new BigInteger(text);
        }

        return bigIntegerVal;
    }

    /**
      * Returns the value as a {@code BigDecimal}.
      */
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

    protected BigInteger bigIntegerVal;

    protected BigDecimal bigDecimalVal;

    // constructors
    protected JsonNum(String text) {
        super(text);
    }
    protected JsonNum(byte val) {
        super(Byte.toString(val));
    }
    protected JsonNum(short val) {
        super(Short.toString(val));
    }
    protected JsonNum(int val) {
        super(Integer.toString(val));
    }
    protected JsonNum(long val) {
        super(Long.toString(val));
    }
    protected JsonNum(float val) {
        super(Float.toString(val));
    }
    protected JsonNum(double val) {
        super(Double.toString(val));
    }
    protected JsonNum(BigInteger val) {
        super(val.toString());
    }
    protected JsonNum(BigDecimal val) {
        super(val.toPlainString());
    }

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
            throw new IllegalArgumentException("the argument string '" + s +
                    "' does not represent a valid JSON number");
        }

        return s;
    }
}

