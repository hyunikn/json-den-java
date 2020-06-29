package com.wingsoft.jsonden;

import com.wingsoft.jsonden.parser.antlrgen.JsonLex;
import com.wingsoft.jsonden.parser.antlrgen.JsonParse;
import com.wingsoft.jsonden.parser.MyParseTreeVisitor;

import com.wingsoft.jsonden.exception.ParseError;
import com.wingsoft.jsonden.exception.Inapplicable;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.IntervalSet;

import java.io.IOException;
import java.io.StringReader;

import java.math.BigDecimal;

import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
  * Superclass of all JSON value classes.
  * A {@code Json} can keep a string array for its comment lines, which is printed together with its value when stringified.
  */
public abstract class Json {

    // ===================================================
    // Public

    /**
      * Parses the string into a {@code Json}.
      * @param s string to parse
      * @return a Json if s is a legal JSON text
      * @throws com.wingsoft.jsonden.exception.ParseError when s does not legally represent a Json value.
      */
    public static Json parse(String s) throws ParseError {
        ANTLRInputStream ais;
        try {
            ais = new ANTLRInputStream(new StringReader('\r' + s)); // \r: detecting preserved comment requires it.
        } catch (IOException e ) {
            throw new Error(e);
        }
        JsonLex lexer = new JsonLex(ais);
        JsonParse parser = new JsonParse(new CommonTokenStream(lexer));
        parser.setErrorHandler(new DefaultErrorStrategy() {
                    public void reportError(Parser p, RecognitionException e) { } // suppress console output
                    public void recover(Parser p, RecognitionException e) { throw e; } // do not recover
                });

        ParseTree tree;
        try {
            tree = parser.json();
        } catch (RecognitionException e) {
            throw new ParseError(ParseError.CASE_WRONG_JSON_SYNTAX, getDesc(e, parser));
        }

        MyParseTreeVisitor visitor = new MyParseTreeVisitor();
        try {
            return visitor.visit(tree);
        } catch (Error e) {
            String msg = e.getMessage();
            if (msg != null) {
                if (msg.startsWith("a duplicate key")) {
                    throw new ParseError(ParseError.CASE_DUPLICATE_KEY, msg);
                } else if (msg.startsWith("insufficient leading white spaces in a comment line at")) {
                    throw new ParseError(ParseError.CASE_INSUFFICIENT_INDENT, msg);
                }
            }
            throw e;
        }
    }

    /**
      * Produces a string which represents this {@code Json} which conforms to the indentation specification.
      * @param indentSize size of one level of indentation. It must be between zero and eight inclusive.
      *   Zero indentSize results in a minified JSON text.
      * @param indentLevel starting level of indentation. It must be larger than or equal to zero.
      */
    public String stringify(int indentSize, int indentLevel) {

        checkStringifyOptions(indentSize, indentLevel);
        StringBuffer sbuf = new StringBuffer();
        write(sbuf, indentSize, indentLevel);
        return sbuf.toString();
    }

    /**
      * Same as {@code stringify(indentSize, 0)}. That is, stringifies from indentation level zero.
      */
    public String stringify(int indentSize) {
        return stringify(indentSize, 0);
    }

    /**
      * Same as {@code stringify(0, 0)}. That is, stringifies into a minified JSON text.
      */
    @Override
    public String toString() { return stringify(0, 0); }

    /**
      * Conveniently gets a {@code Json} located deep in the nested structure of this one.
      * For example, one can use {@code json.getx("how.deep.is.your.love")} instead of
      * {@code json.get("how").get("deep").get("is").get("your").get("love")} which is common in many
      * JSON handling libraries.
      * @param path dot delimited segments of a path to a Json.
      *   A segment represents either a name of a JSON object member or an (integer) index of a JSON array element.
      * @return the Json located at the path if present, otherwise null.
      * @throws java.lang.IllegalArgumentException when path is null
      */
    public Json getx(String path) throws IllegalArgumentException {

        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }

        String[] segments = path.split("\\.", -1);  // -1: do not discard trailing empty strings

        Json node = this;
        for (String s: segments) {
            node = node.getChild(s);
            if (node == null) {
                return null;
            }
        }

        return node;
    }

    /**
      * Gets the comment lines.
      */
    public String[] commentLines() {
        return commentLines;
    }

    /**
      * Sets the comment lines.
      */
    public void setCommentLines(String[] commentLines) {
        this.commentLines = commentLines;
    }

    /**
      * Deep clone.
      */
    @Override
    public abstract Object clone();

    // --------------------------------------------------
    // convenience methods

    /**
      * Returns whether a descendant JSON node exists at the path or not.
      * @param path same as in getx()
      * @throws java.lang.IllegalArgumentException when path is null
      */
    public boolean has(String path) throws IllegalArgumentException {
        return getx(path) != null;
    }

    /**
      * Returns the longest reachable prefix of the path.
      * @param path same as in getx()
      * @throws java.lang.IllegalArgumentException when path is null
      */
    public String longestReachablePrefix(String path) throws IllegalArgumentException {

        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }

        String[] segments = path.split("\\.", -1);  // -1: do not discard trailing empty strings

        int len = 0;
        Json node = this;
        for (String s: segments) {
            node = node.getChild(s);
            if (node == null) {
                break;
            } else {
                len++;
            }
        }

        if (len == 0) {
            return null;
        } else {
            return String.join(".", Arrays.copyOf(segments, len));
        }
    }


    // --------------------------------------------------
    // for six individual JSON types

    /**
      * Returns whether this is a {@link com.wingsoft.jsonden.JsonObj JsonObj} or not.
      */
    public boolean isObj()  { return false; }

    /**
      * Returns whether this is a {@link com.wingsoft.jsonden.JsonArr JsonArr} or not.
      */
    public boolean isArr()  { return false; }

    /**
      * Returns whether this is a {@link com.wingsoft.jsonden.JsonBool JsonBool} or not.
      */
    public boolean isBool() { return false; }

    /**
      * Returns whether this is a {@link com.wingsoft.jsonden.JsonNum JsonNum} or not.
      */
    public boolean isNum()  { return false; }

    /**
      * Returns whether this is a {@link com.wingsoft.jsonden.JsonNull JsonNull} or not.
      */
    public boolean isNull() { return false; }

    /**
      * Returns whether this is a {@link com.wingsoft.jsonden.JsonStr JsonStr} or not.
      */
    public boolean isStr()  { return false; }

    //---------------------------------------------

    /**
      * Returns itself if this is a {@link com.wingsoft.jsonden.JsonObj JsonObj}, otherwise {@code null}.
      */
    public JsonObj  asObj()  { return null; }

    /**
      * Returns itself if this is a {@link com.wingsoft.jsonden.JsonArr JsonArr}, otherwise {@code null}.
      */
    public JsonArr  asArr()  { return null; }

    /**
      * Returns itself if this is a {@link com.wingsoft.jsonden.JsonBool JsonBool}, otherwise {@code null}.
      */
    public JsonBool asBool() { return null; }

    /**
      * Returns itself if this is a {@link com.wingsoft.jsonden.JsonNum JsonNum}, otherwise {@code null}.
      */
    public JsonNum  asNum()  { return null; }

    /**
      * Returns itself if this is a {@link com.wingsoft.jsonden.JsonNull JsonNull}, otherwise {@code null}.
      */
    public JsonNull asNull() { return null; }

    /**
      * Returns itself if this is a {@link com.wingsoft.jsonden.JsonStr JsonStr}, otherwise {@code null}.
      */
    public JsonStr  asStr()  { return null; }

    //---------------------------------------------

    /**
      * If this is a {@link com.wingsoft.jsonden.JsonObj JsonObj} this method returns
      * the result of calling its {@code getMap()}, otherwise throws
      * {@link com.wingsoft.jsonden.exception.Inapplicable}
      */
    public LinkedHashMap<String, Json> getMap() throws Inapplicable {
        return (LinkedHashMap<String, Json>) throwInapplicable("getMap");
    }

    /**
      * If this is a {@link com.wingsoft.jsonden.JsonArr JsonArr} this method returns
      * the result of calling its {@code getList()}, otherwise throws
      * {@link com.wingsoft.jsonden.exception.Inapplicable}
      */
    public List<Json> getList() throws Inapplicable {
        return (List<Json>) throwInapplicable("getList");
    }

    /**
      * If this is a {@link com.wingsoft.jsonden.JsonBool JsonBool} this method returns
      * the result of calling its {@code getBoolean()}, otherwise throws
      * {@link com.wingsoft.jsonden.exception.Inapplicable}
      */
    public boolean getBoolean() throws Inapplicable {
        return (boolean) throwInapplicable("getBoolean");
    }

    /**
      * If this is a {@link com.wingsoft.jsonden.JsonNum JsonNum} this method returns
      * the result of calling its {@code getByte()}, otherwise throws
      * {@link com.wingsoft.jsonden.exception.Inapplicable}
      */
    public byte getByte() throws Inapplicable {
        return (byte) throwInapplicable("getByte");
    }

    /**
      * If this is a {@link com.wingsoft.jsonden.JsonNum JsonNum} this method returns
      * the result of calling its {@code getShort()}, otherwise throws
      * {@link com.wingsoft.jsonden.exception.Inapplicable}
      */
    public short getShort() throws Inapplicable {
        return (short) throwInapplicable("getShort");
    }

    /**
      * If this is a {@link com.wingsoft.jsonden.JsonNum JsonNum} this method returns
      * the result of calling its {@code getInt()}, otherwise throws
      * {@link com.wingsoft.jsonden.exception.Inapplicable}
      */
    public int getInt() throws Inapplicable {
        return (int) throwInapplicable("getInt");
    }

    /**
      * If this is a {@link com.wingsoft.jsonden.JsonNum JsonNum} this method returns
      * the result of calling its {@code getLong()}, otherwise throws
      * {@link com.wingsoft.jsonden.exception.Inapplicable}
      */
    public long getLong() throws Inapplicable {
        return (long) throwInapplicable("getLong");
    }

    /**
      * If this is a {@link com.wingsoft.jsonden.JsonNum JsonNum} this method returns
      * the result of calling its {@code getFloat()}, otherwise throws
      * {@link com.wingsoft.jsonden.exception.Inapplicable}
      */
    public float getFloat() throws Inapplicable {
        return (float) throwInapplicable("getFloat");
    }

    /**
      * If this is a {@link com.wingsoft.jsonden.JsonNum JsonNum} this method returns
      * the result of calling its {@code getDouble()}, otherwise throws
      * {@link com.wingsoft.jsonden.exception.Inapplicable}
      */
    public double getDouble() throws Inapplicable {
        return (double) throwInapplicable("getDouble");
    }

    /**
      * If this is a {@link com.wingsoft.jsonden.JsonNum JsonNum} this method returns
      * the result of calling its {@code getBigDecimal()}, otherwise throws
      * {@link com.wingsoft.jsonden.exception.Inapplicable}
      */
    public BigDecimal getBigDecimal() throws Inapplicable {
        return (BigDecimal) throwInapplicable("getBigDecimal");
    }

    /**
      * If this is a {@link com.wingsoft.jsonden.JsonStr JsonStr} this method returns
      * the result of calling its {@code getString()}, otherwise throws
      * {@link com.wingsoft.jsonden.exception.Inapplicable}
      */
    public String getString() throws Inapplicable {
        return (String) throwInapplicable("getString");
    }

    // ===================================================
    // Protected

    protected String[] commentLines;

    protected Json() { }

    // overriden by all
    protected abstract String getTypeName();
    protected abstract void write(StringBuffer sbuf, int indentSize, int indentLevel);
    protected abstract Json getChild(String key);

    // --------------------------------------------------
    // Utility

    protected static void writeIndent(StringBuffer sbuf, int indentSize, int indentLevel) {

        if (indentSize == 0 || indentLevel == 0) {
            return;
        }

        String indent = indents[indentSize];
        for (int i = 0; i < indentLevel; i++) {
            sbuf.append(indent);
        }
    }

    protected static void writeComment(StringBuffer sbuf, String[] commentLines, int indentSize, int indentLevel) {

        if (commentLines == null) {
            return;
        }

        boolean useIndent = indentSize != 0;

        writeIndent(sbuf, indentSize, indentLevel);
        sbuf.append("/**\n");

        for (String s: commentLines) {
            writeIndent(sbuf, indentSize, indentLevel);
            sbuf.append(s);
            sbuf.append("\n");
        }
        writeIndent(sbuf, indentSize, indentLevel);
        sbuf.append(" */");

        if (useIndent) {
            sbuf.append("\n");
        }

    }

    // ===================================================
    // Private

    private static String getDesc(RecognitionException cause, JsonParse parser) {

        String caseStr;
        if (cause instanceof FailedPredicateException) {
            caseStr = "failed predicate";
        } else if (cause instanceof InputMismatchException) {
            caseStr = "input mismatch";
        } else if (cause instanceof LexerNoViableAltException) {
            caseStr = "ambiguous while parsing";
        } else if (cause instanceof NoViableAltException) {
            caseStr = "ambiguous while tokenizing";
        } else {
            caseStr = "failed recognition";
        }

        String ret;

        Token token = cause.getOffendingToken();
        if (token == null) {
            ret = caseStr;
        } else {
            ret = String.format("%s at (%d,%d) \"%s\"",
                    caseStr, token.getLine(), token.getCharPositionInLine(), token.getText());
        }

        IntervalSet expected = cause.getExpectedTokens();
        if (expected != null) {

            Set<Integer> literalIndexes = expected.toSet();

            Set<String> set = new HashSet<>();
            for (int i: literalIndexes){
                String literal = parser.getLiteralName(i);
                if (literal == null) {
                    System.err.println("unlikely: token index out of bounds: " + expected.get(i));
                } else {
                    set.add(literal);
                }
            }

            ret = ret + ", expected " + set;
        }

        return ret;
    }

    private static final String[] indents = new String[] {
        null,
        " ",
        "  ",
        "   ",
        "    ",
        "     ",
        "      ",
        "       ",
        "        "
    };

    private Object throwInapplicable(String op) throws Inapplicable {
        throw new Inapplicable(op + " is not applicable to " + getClass().getSimpleName() + " nodes");
    }

    private void checkStringifyOptions(int indentSize, int indentLevel) {
        if (indentSize > 8) {
            throw new IllegalArgumentException("indentSize cannot be larger than eight");
        }
        if (indentSize < 0) {
            throw new IllegalArgumentException("indentSize cannot be a negative integer");
        }

        if (indentLevel < 0) {
            throw new IllegalArgumentException("indentLevel cannot be a negative integer");
        }
    }
}
