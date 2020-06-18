package com.wingsoft.jsonden;

import com.wingsoft.jsonden.parser.antlrgen.JsonLex;
import com.wingsoft.jsonden.parser.antlrgen.JsonParse;
import com.wingsoft.jsonden.parser.MyParseTreeVisitor;

import com.wingsoft.jsonden.exception.ParseError;

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
  * Superclass of all classes representing JSON values.
  * A Json can keep a string array as its comment lines, which is printed together with its value when stringified.
  */
public abstract class Json {

    // ===================================================
    // Public

    /**
      * Parses given string into a Json.
      * @param s string to parse
      * @return a Json if s is a legal JSON text
      * @throws com.wingsoft.jsonden.exception.ParseError when s does not legally represent a Json value.
      *   Invoking getMessage() of the thrown exception will yield a description of the problem.
      */
    public static Json parse(String s) throws ParseError {
        ANTLRInputStream ais;
        try {
            ais = new ANTLRInputStream(new StringReader(s));
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
            throw new ParseError(getDesc(e, parser));
        }

        MyParseTreeVisitor visitor = new MyParseTreeVisitor();
        return visitor.visit(tree);
    }

    /**
      * Produces a string which represents this Json with given indentation specification.
      * @param indentSize size of one level of the indent. It must be between zero and eight inclusive.
      *   Zero indentSize results in a minified JSON text.
      * @param indentLevel starting level of the indent. It must be larger than or equal to zero.
      */
    public String stringify(int indentSize, int indentLevel) {

        checkStringifyOptions(indentSize, indentLevel);
        StringBuffer sbuf = new StringBuffer();
        write(sbuf, indentSize, indentLevel);
        return sbuf.toString();
    }

    /**
      * Same as stringify(indentSize, 0)
      */
    public String stringify(int indentSize) {
        return stringify(indentSize, 0);
    }

    /**
      * Same as stringify(0, 0)
      */
    @Override
    public String toString() { return stringify(0, 0); }

    /**
      * Conveniently gets a Json located deep in the nested hierarchy of a Json structure.
      * For example, one can use json.getx("how.deep.is.your.love") instead of
      * json.get("how").get("deep").get("is").get("your").get("love") which is common in many
      * JSON handling libraries (and the latter form is also possible in this library).
      * @param path dot delimited segments of a path to a Json.
      *   A segment is either a name of a JSON object member or an (integer) index of a JSON array element.
      * @return the Json located at the path if present, otherwise null.
      */
    public Json getx(String path) {

        if (path == null) {
            throw new Error("path cannot be null");
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
      * Gets comment lins.
      */
    public String[] getCommentLines() {
        return commentLines;
    }

    /**
      * Sets comment lines.
      */
    public void setCommentLines(String[] commentLines) {
        this.commentLines = commentLines;
    }

    @Override
    public abstract Object clone();

    // --------------------------------------------------
    // convenience methods

    public boolean has(String path) {
        return getx(path) != null;
    }

    public String getLongestReachablePrefix(String path) {

        if (path == null) {
            throw new Error("path cannot be null");
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

    public boolean isObj()  { return false; }
    public boolean isArr()  { return false; }
    public boolean isBool() { return false; }
    public boolean isNum()  { return false; }
    public boolean isNull() { return false; }
    public boolean isStr()  { return false; }

    public JsonObj  asObj()  { return null; }
    public JsonArr  asArr()  { return null; }
    public JsonBool asBool() { return null; }
    public JsonNum  asNum()  { return null; }
    public JsonNull asNull() { return null; }
    public JsonStr  asStr()  { return null; }

    public LinkedHashMap<String, Json> asMap() { return (LinkedHashMap<String, Json>) throwInapplicable("asMap"); }
    public Json[]       asArray()       { return (Json[]) throwInapplicable("asArray"); }
    public List<Json>   asList()        { return (List<Json>) throwInapplicable("asList"); }
    public boolean      asBoolean()     { return (boolean) throwInapplicable("asBoolean"); }
    public byte         asByte()        { return (byte) throwInapplicable("asByte"); }
    public short        asShort()       { return (short) throwInapplicable("asShort"); }
    public int          asInt()         { return (int) throwInapplicable("asInt"); }
    public long         asLong()        { return (long) throwInapplicable("asLong"); }
    public float        asFloat()       { return (float) throwInapplicable("asFloat"); }
    public double       asDouble()      { return (double) throwInapplicable("asDouble"); }
    public BigDecimal   asBigDecimal()  { return (BigDecimal) throwInapplicable("asBigDecimal"); }
    public String       asString()      { return (String) throwInapplicable("asString"); }


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

    private Object throwInapplicable(String op) {
        throw new Error(op + " is not applicable to " + getClass().getSimpleName() + " nodes");
    }

    private void checkStringifyOptions(int indentSize, int indentLevel) {
        if (indentSize > 8) {
            throw new Error("indentSize cannot be larger than eight");
        }
        if (indentSize < 0) {
            throw new Error("indentSize cannot be a negative integer");
        }

        if (indentLevel < 0) {
            throw new Error("indentLevel cannot be a negative integer");
        }
    }
}
