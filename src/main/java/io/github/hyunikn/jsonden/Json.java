package io.github.hyunikn.jsonden;

import io.github.hyunikn.jsonden.parser.antlrgen.JsonLex;
import io.github.hyunikn.jsonden.parser.antlrgen.JsonParse;
import io.github.hyunikn.jsonden.parser.MyParseTreeVisitor;

import io.github.hyunikn.jsonden.exception.ParseError;
import io.github.hyunikn.jsonden.exception.UnreachablePath;

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
      * @throws io.github.hyunikn.jsonden.exception.ParseError when s does not legally represent a Json value.
      */
    public static Json parse(String s) throws ParseError {

        if (s == null) {
            throw new IllegalArgumentException("s must not be null");
        }

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
                } else if (msg.startsWith("a remark line does not start with")) {
                    throw new ParseError(ParseError.CASE_NO_BEGINNING_STAR, msg);
                } else if (msg.startsWith("Json-den does not allow dot")) {
                    throw new ParseError(ParseError.CASE_DOT_IN_MEMBER_KEY, msg);
                }
            }
            throw e;
        }
    }

    /**
      * Produces a string which represents this {@code Json} with the option.
      */
    public String stringify(StrOpt opt) {
        checkStringifyOptions(opt);
        StringBuffer sbuf = new StringBuffer();
        write(sbuf, opt, opt.initialIndentLevel);
        return sbuf.toString();
    }

    /**
      * Produces a string which represents this {@code Json} with {@code indentSize}.
      * @param indentSize size of one level of indentation. It must be between zero and eight inclusive.
      *   Zero indentSize results in a minified JSON text except for remarks and comments, if any.
      */
    public String stringify(int indentSize) {
        StrOpt opt = (indentSize == 4) ? StrOpt.DEFAULT :
                     (indentSize == 0) ? StrOpt.SERIALIZING :
                     new StrOpt().indentSize(indentSize);
        return stringify(opt);
    }

    /**
      * Same as {@code stringify(StrOpt.SERIALIZING)}. That is, produces a minified JSON text
      * except for remarks and comments, if any.
      */
    @Override
    public String toString() { return stringify(StrOpt.SERIALIZING); }

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
    public abstract Json clone();

    // --------------------------------------------------
    // convenience methods

    /**
      * Whether this is one of {@code JsonBool}, {@code JsonNull}, {@code JsonNum} and {@code JsonStr} or not.
      */
    public boolean isLeaf() {
        return false;   // overriden by JsonLeaf
    }

    /**
      * Whether this has no subnodes or not.
      */
    public abstract boolean isTerminal();

    // --------------------------------------------------
    // for six individual JSON types

    /**
      * Returns whether this is a {@link io.github.hyunikn.jsonden.JsonObj JsonObj} or not.
      */
    public boolean isObj()  { return false; }

    /**
      * Returns whether this is a {@link io.github.hyunikn.jsonden.JsonArr JsonArr} or not.
      */
    public boolean isArr()  { return false; }

    /**
      * Returns whether this is a {@link io.github.hyunikn.jsonden.JsonBool JsonBool} or not.
      */
    public boolean isBool() { return false; }

    /**
      * Returns whether this is a {@link io.github.hyunikn.jsonden.JsonNum JsonNum} or not.
      */
    public boolean isNum()  { return false; }

    /**
      * Returns whether this is a {@link io.github.hyunikn.jsonden.JsonNull JsonNull} or not.
      */
    public boolean isNull() { return false; }

    /**
      * Returns whether this is a {@link io.github.hyunikn.jsonden.JsonStr JsonStr} or not.
      */
    public boolean isStr()  { return false; }

    //---------------------------------------------

    /**
      * Returns itself if this is a {@link io.github.hyunikn.jsonden.JsonObj JsonObj}, otherwise {@code null}.
      */
    public JsonObj  asObj()  { return null; }

    /**
      * Returns itself if this is a {@link io.github.hyunikn.jsonden.JsonArr JsonArr}, otherwise {@code null}.
      */
    public JsonArr  asArr()  { return null; }

    /**
      * Returns itself if this is a {@link io.github.hyunikn.jsonden.JsonBool JsonBool}, otherwise {@code null}.
      */
    public JsonBool asBool() { return null; }

    /**
      * Returns itself if this is a {@link io.github.hyunikn.jsonden.JsonNum JsonNum}, otherwise {@code null}.
      */
    public JsonNum  asNum()  { return null; }

    /**
      * Returns itself if this is a {@link io.github.hyunikn.jsonden.JsonNull JsonNull}, otherwise {@code null}.
      */
    public JsonNull asNull() { return null; }

    /**
      * Returns itself if this is a {@link io.github.hyunikn.jsonden.JsonStr JsonStr}, otherwise {@code null}.
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
    protected String[] commentLines;

    protected Json() { }

    // overriden by all
    protected abstract String getTypeName();
    protected abstract void write(StringBuffer sbuf, StrOpt opt, int indentLevel);
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

    protected static void writeRemark(StringBuffer sbuf, String[] lines, int indentSize, int indentLevel) {

        if (lines == null) {
            return;
        }
        writeAnnot(sbuf, lines, indentSize, indentLevel, true);
    }

    protected static void writeComment(StringBuffer sbuf, String[] lines, int indentSize, int indentLevel) {

        if (lines == null) {
            return;
        }
        writeAnnot(sbuf, lines, indentSize, indentLevel, false);
    }

    protected static void writeAnnot(StringBuffer sbuf, String[] lines, int indentSize, int indentLevel,
            boolean isRemark) {

        assert lines.length > 0;

        boolean useIndent = indentSize != 0;

        writeIndent(sbuf, indentSize, indentLevel);
        sbuf.append(isRemark ? "/**" : "/* ");

        if (lines.length == 1) {
            sbuf.append(lines[0] + " */");
        } else {
            boolean first = true;
            for (String s: lines) {

                if (first) {
                    first = false;
                } else {
                    sbuf.append("\n");
                    writeIndent(sbuf, indentSize, indentLevel);
                    if (isRemark) {
                        sbuf.append("  *");
                    }
                }

                sbuf.append(s);
            }

            if (useIndent) {
                sbuf.append("\n");
                writeIndent(sbuf, indentSize, indentLevel);
                sbuf.append(isRemark ? "  */" : " */");
            } else {
                sbuf.append(" */");
            }

        }

        if (useIndent) {
            sbuf.append("\n");
        }

    }

    protected void copyAnnotationsOf(Json that) {

        String[] cl;

        cl = that.remarkLines();
        if (cl != null) {
            setRemarkLines(cl);
        }

        cl = that.commentLines();
        if (cl != null) {
            setCommentLines(cl);
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

    private void checkStringifyOptions(StrOpt opt) {
        int indentSize = opt.indentSize;

        if (indentSize > 8) {
            throw new IllegalArgumentException("indentSize cannot be larger than eight");
        }
        if (indentSize < 0) {
            throw new IllegalArgumentException("indentSize cannot be a negative integer");
        }

        if (opt.initialIndentLevel < 0) {
            throw new IllegalArgumentException("initialIndentLevel cannot be a negative integer");
        }
    }
}
