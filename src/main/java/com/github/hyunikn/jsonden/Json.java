package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.parser.antlrgen.JsonLex;
import com.github.hyunikn.jsonden.parser.antlrgen.JsonParse;
import com.github.hyunikn.jsonden.parser.MyParseTreeVisitor;

import com.github.hyunikn.jsonden.exception.ParseError;
import com.github.hyunikn.jsonden.exception.UnreachablePath;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.IntervalSet;

import java.io.IOException;
import java.io.StringReader;

import java.math.BigInteger;
import java.math.BigDecimal;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
  * Superclass of all JSON value classes.
  * A {@code Json} can keep a string array for its remark lines,
  * which is printed together with its value when stringified.
  */
public abstract class Json {

    // ===================================================
    // Public

    /**
      * Parses the string into a {@code Json}.
      * @param s string to parse
      * @return a Json if s is a legal JSON text
      * @throws com.github.hyunikn.jsonden.exception.ParseError when s does not legally represent a Json value.
      */
    public static Json parse(String s) throws ParseError {
        ANTLRInputStream ais;
        try {
            ais = new ANTLRInputStream(new StringReader('\r' + s)); // \r: detecting remark requires it.
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
                } else if (msg.startsWith("insufficient leading white spaces of a remark line at")) {
                    throw new ParseError(ParseError.CASE_INSUFFICIENT_INDENT, msg);
                } else if (msg.startsWith("Json-den does not allow dot")) {
                    throw new ParseError(ParseError.CASE_DOT_IN_MEMBER_KEY, msg);
                }
            }
            throw e;
        }
    }

    /**
      * Produces a string which represents this {@code Json}.
      * @param indentSize size of one level of indentation. It must be between zero and eight inclusive.
      *   Zero indentSize results in a minified JSON text.
      */
    public String stringify(int indentSize) {

        checkStringifyOptions(indentSize);
        StringBuffer sbuf = new StringBuffer();
        write(sbuf, indentSize, 0);
        return sbuf.toString();
    }

    /**
      * Same as {@code stringify(0, 0)}. That is, stringifies into a minified JSON text.
      */
    @Override
    public String toString() { return stringify(0); }

    /**
      * Gets the remark lines.
      */
    public String[] remarkLines() {
        return remarkLines;
    }

    /**
      * Sets the remark lines.
      */
    public void setRemarkLines(String[] remarkLines) {
        this.remarkLines = remarkLines;
    }

    /**
      * Deep clone.
      */
    @Override
    public abstract Object clone();

    // --------------------------------------------------
    // convenience methods

    /**
      * Whether this is one of {@code JsonBool}, {@code JsonNull}, {@code JsonNum} and {@code JsonStr} or not.
      */
    public boolean isLeaf() {
        return false;   // overriden by JsonLeaf
    }


    // --------------------------------------------------
    // for six individual JSON types

    /**
      * Returns whether this is a {@link com.github.hyunikn.jsonden.JsonObj JsonObj} or not.
      */
    public boolean isObj()  { return false; }

    /**
      * Returns whether this is a {@link com.github.hyunikn.jsonden.JsonArr JsonArr} or not.
      */
    public boolean isArr()  { return false; }

    /**
      * Returns whether this is a {@link com.github.hyunikn.jsonden.JsonBool JsonBool} or not.
      */
    public boolean isBool() { return false; }

    /**
      * Returns whether this is a {@link com.github.hyunikn.jsonden.JsonNum JsonNum} or not.
      */
    public boolean isNum()  { return false; }

    /**
      * Returns whether this is a {@link com.github.hyunikn.jsonden.JsonNull JsonNull} or not.
      */
    public boolean isNull() { return false; }

    /**
      * Returns whether this is a {@link com.github.hyunikn.jsonden.JsonStr JsonStr} or not.
      */
    public boolean isStr()  { return false; }

    //---------------------------------------------

    /**
      * Returns itself if this is a {@link com.github.hyunikn.jsonden.JsonObj JsonObj}, otherwise {@code null}.
      */
    public JsonObj  asObj()  { return null; }

    /**
      * Returns itself if this is a {@link com.github.hyunikn.jsonden.JsonArr JsonArr}, otherwise {@code null}.
      */
    public JsonArr  asArr()  { return null; }

    /**
      * Returns itself if this is a {@link com.github.hyunikn.jsonden.JsonBool JsonBool}, otherwise {@code null}.
      */
    public JsonBool asBool() { return null; }

    /**
      * Returns itself if this is a {@link com.github.hyunikn.jsonden.JsonNum JsonNum}, otherwise {@code null}.
      */
    public JsonNum  asNum()  { return null; }

    /**
      * Returns itself if this is a {@link com.github.hyunikn.jsonden.JsonNull JsonNull}, otherwise {@code null}.
      */
    public JsonNull asNull() { return null; }

    /**
      * Returns itself if this is a {@link com.github.hyunikn.jsonden.JsonStr JsonStr}, otherwise {@code null}.
      */
    public JsonStr asStr()  { return null; }

    // ===================================================
    // Protected

    protected static final int TYPE_OBJECT  = 0;
    protected static final int TYPE_ARRAY   = 1;
    protected static final int TYPE_BOOLEAN = 2;
    protected static final int TYPE_NULL    = 3;
    protected static final int TYPE_NUMBER  = 4;
    protected static final int TYPE_STRING  = 5;

    protected String[] remarkLines;

    protected Json() { }

    // overriden by all
    protected abstract String getTypeName();
    protected abstract void write(StringBuffer sbuf, int indentSize, int indentLevel);
    protected abstract Json getChild(String key);

    // --------------------------------------------------
    // Utility

    protected Json getxp(String path, int typeOfLastNode) throws UnreachablePath {

        assert (path != null);

        String[] segments = path.split("\\.", -1);  // -1: do not discard trailing empty strings
        int len = segments.length;

        Json childNode = null;
        Json parentNode = this;
        int i = 0;
        for (String s: segments) {
            childNode = parentNode.getChild(s);
            if (childNode == null) {
                // the last reachable node can be a simple node
                if (parentNode.isLeaf()) {
                    throw new UnreachablePath("cannot create a node at '" +
                            String.join(".", Arrays.copyOfRange(segments, 0, i + 1)) +
                            "' because its parent is a leaf node whose type is " +
                            parentNode.getClass().getSimpleName());
                }

                try {
                    return createMissingNodes(parentNode, segments, i, typeOfLastNode); // returns the target node.
                } catch (Throwable e) {

                    // revert a possible side effect, that is, remove the possibly added child.
                    if (parentNode.isObj()) {
                        parentNode.asObj().remove(s);
                    } else if (parentNode.isArr()) {
                        try {
                            int idx = Integer.parseInt(s);
                            parentNode.asArr().remove(idx);
                        } catch (NumberFormatException ee) {
                            // do nothing
                        }
                    } else {
                        assert false;
                    }
                    throw e;
                }
            } else {
                i++;
                parentNode = childNode;
            }
        }

        assert childNode != null;
        return childNode;
    }

    protected static void writeIndent(StringBuffer sbuf, int indentSize, int indentLevel) {

        if (indentSize == 0 || indentLevel == 0) {
            return;
        }

        String indent = indents[indentSize];
        for (int i = 0; i < indentLevel; i++) {
            sbuf.append(indent);
        }
    }

    protected static void writeRemark(StringBuffer sbuf, String[] remarkLines, int indentSize, int indentLevel) {

        if (remarkLines == null) {
            return;
        }

        boolean useIndent = indentSize != 0;

        writeIndent(sbuf, indentSize, indentLevel);
        sbuf.append("/**\n");

        for (String s: remarkLines) {
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

    private Json createMissingNodes(Json lastReachable, String[] segments,
            int idxOfFirstMissing, int typeOfLastNode) throws UnreachablePath {

        Json childNode = null;
        int len = segments.length;

        Json parentNode = lastReachable;
        for (int i = idxOfFirstMissing; i < len; i++) {
            String segment = segments[i];

            if (i == len - 1) {
                // the last to create
                switch (typeOfLastNode) {
                    case TYPE_OBJECT:
                        childNode = new JsonObj();
                        break;
                    case TYPE_ARRAY:
                        childNode = new JsonArr();
                        break;
                    default:
                        assert false;   // unreachable
                        childNode = null;   // just to make the compiler happy
                }
            } else {
                String nextSegment = segments[i + 1];
                Integer arrElemIndex = MyParseTreeVisitor.getArrElemIndex(nextSegment);
                if (arrElemIndex == null) {
                    childNode = new JsonObj();
                } else {
                    int idx = arrElemIndex.intValue();
                    if (idx == 0) {
                        childNode = new JsonArr();
                    } else {
                        throw new UnreachablePath("an array node to create at '" +
                                String.join(".", Arrays.copyOfRange(segments, 0, i + 1)) +
                                "' cannot have an element at the non-zero index " + nextSegment);
                    }
                }
            }

            if (parentNode.isObj()) {
                parentNode.asObj().set(segment, childNode);
            } else if (parentNode.isArr()) {
                if (parentNode == lastReachable) {
                    Integer arrElemIndex = MyParseTreeVisitor.getArrElemIndex(segment);
                    if (arrElemIndex == null) {
                        throw new UnreachablePath("cannot create a node at '" +
                                String.join(".", Arrays.copyOfRange(segments, 0, i + 1)) +
                                "' because its parent is an array and the path segment '" +
                                segment + "' is not an integer");
                    }

                    int idx = arrElemIndex.intValue();
                    if (idx != parentNode.asArr().size()) {
                        throw new UnreachablePath("cannot create a node at '" +
                                String.join(".", Arrays.copyOfRange(segments, 0, i + 1)) +
                                "' because its parent array has elements fewer than " + idx);
                    }
                }
                parentNode.asArr().append(childNode);
            } else {
                assert false; // unreachable
            }

            parentNode = childNode;
        }

        assert childNode != null;
        return childNode;
    }

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

    private void checkStringifyOptions(int indentSize) {
        if (indentSize > 8) {
            throw new IllegalArgumentException("indentSize cannot be larger than eight");
        }
        if (indentSize < 0) {
            throw new IllegalArgumentException("indentSize cannot be a negative integer");
        }
    }
}
