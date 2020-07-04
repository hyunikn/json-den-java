package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.parser.antlrgen.JsonLex;
import com.github.hyunikn.jsonden.parser.antlrgen.JsonParse;
import com.github.hyunikn.jsonden.parser.MyParseTreeVisitor;

import com.github.hyunikn.jsonden.exception.ParseError;
import com.github.hyunikn.jsonden.exception.Inapplicable;
import com.github.hyunikn.jsonden.exception.UnreachablePath;

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
      * @throws com.github.hyunikn.jsonden.exception.ParseError when s does not legally represent a Json value.
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
                } else if (msg.startsWith("insufficient leading white spaces of a comment line at")) {
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

    // ---------------------------------------------------------------------
    // series of ...x methods

    /**
      * Gets a {@code Json} located deep in the nested structure of this one.
      * For example, one can use {@code json.getx("a.b.c.d.e")} instead of
      * {@code json.get("a").get("b").get("c").get("d").get("e")} which is common in many
      * JSON libraries.
      * @param path dot delimited segments of a path to a Json.
      *   Each segment represents either a name of a JSON object member or an (integer) index of a JSON array element.
      * @return the Json located at the path if present, otherwise null.
      */
    public Json getx(String path) {

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
      * Clears a {@code JsonObj} or {@code JsonArr} located deep in the nested structure of this one.
      * For example, {@code json.clearx("a.b.c.d.e")} is analogous to
      * {@code ((JsonObj) json.getx("a.b.c.d.e")).clear()} if a JsonObj is at the path, or to
      * {@code ((JsonArr) json.getx("a.b.c.d.e")).clear()} if a JsonArr is at the path.
      * @param path dot delimited segments of a path to a Json.
      *   Each segment represents either a name of a JSON object member or an (integer) index of a JSON array element.
      * @return this Json object for method chaining
      * @throws com.github.hyunikn.jsonden.exception.Inapplicable when neither a JsonObj nor a JsonArr is at the path.
      * @throws com.github.hyunikn.jsonden.exception.UnreachablePath when the path is not reachable.
      */
    public Json clearx(String path) throws Inapplicable, UnreachablePath {

        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }

        Json subnode = getx(path);
        if (subnode == null) {
            throw new UnreachablePath(path + " unreachable");
        } else {
            if (subnode.isObj()) {
                ((JsonObj) subnode).clear();
            } else if (subnode.isArr()) {
                ((JsonArr) subnode).clear();
            } else {
                throw new Inapplicable("clear is not applicable to a " + subnode.getClass().getSimpleName());
            }
        }

        return this;
    }

    /**
      * Deletes a {@code Json} located deep in the nested structure of this one.
      * For example, {@code json.deletex("a.b.c.d.e")} is analogous to
      * {@code ((JsonObj) json.getx("a.b.c.d")).delete("e")} if a JsonObj is at the parent path "a.b.c.d".
      * The behavior is similar when the parent of the target node is a JsonArr.
      * @param path dot delimited segments of a path to a Json.
      *   Each segment represents either a name of a JSON object member or an (integer) index of a JSON array element.
      * @return this Json object for method chaining
      * @throws com.github.hyunikn.jsonden.exception.Inapplicable when the parent is neither a JsonObj nor a JsonArr.
      * @throws com.github.hyunikn.jsonden.exception.UnreachablePath when the parent is not reachable.
      */
    public Json deletex(String path) throws Inapplicable, UnreachablePath {

        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }

        String[] divided = Util.dividePath(path);
        assert divided != null;
        String parentPath = divided[0];

        Json parent;
        if (parentPath == null) {
            parent = this;
        } else {
            parent = getx(parentPath);
            if (parent == null) {
                throw new UnreachablePath(parentPath + " unreachable");
            }
        }

        if (parent.isObj()) {
            ((JsonObj) parent).delete(divided[1]);
        } else if (parent.isArr()) {
            int idx;
            try {
                idx = Integer.parseInt(divided[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("parent of the target node is a JsonArr and " +
                        "the last path segment must be an integer, which is not in " + path);
            }

            ((JsonArr) parent).delete(idx);
        } else {
            throw new Inapplicable("delete is not applicable to a " + parent.getClass().getSimpleName());
        }

        return this;
    }

    /**
      * Removes a {@code Json} located deep in the nested structure of this one.
      * For example, {@code json.deletex("a.b.c.d.e")} is analogous to
      * {@code ((JsonObj) json.getx("a.b.c.d")).remove("e")} if a JsonObj is at the parent path "a.b.c.d".
      * The behavior is similar when the parent of the target node is a JsonArr.
      * @param path dot delimited segments of a path to a Json.
      *   Each segment represents either a name of a JSON object member or an (integer) index of a JSON array element.
      * @return the removed Json, or null if none is removed.
      * @throws com.github.hyunikn.jsonden.exception.Inapplicable when the parent is neither a JsonObj nor a JsonArr.
      * @throws com.github.hyunikn.jsonden.exception.UnreachablePath when the parent is not reachable.
      */
    public Json removex(String path) throws Inapplicable, UnreachablePath {

        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }

        String[] divided = Util.dividePath(path);
        assert divided != null;
        String parentPath = divided[0];

        Json parent;
        if (parentPath == null) {
            parent = this;
        } else {
            parent = getx(parentPath);
            if (parent == null) {
                throw new UnreachablePath(parentPath + " unreachable");
            }
        }

        if (parent.isObj()) {
            return ((JsonObj) parent).remove(divided[1]);
        } else if (parent.isArr()) {
            int idx;
            try {
                idx = Integer.parseInt(divided[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("parent of the target node is a JsonArr and " +
                        "the last path segment must be an integer, which is not in " + path);
            }

            return ((JsonArr) parent).remove(idx);
        } else {
            throw new Inapplicable("remove is not applicable to a " + parent.getClass().getSimpleName());
        }
    }

    /**
      * Sets a {@code Json} into a {@code JsonObj} deep in the nested structure of this one.
      * For example, {@code json.setx("a.b.c.d.e", val)} is analogous to
      * {@code ((JsonObj) json.getx("a.b.c.d")).set("e", val)} if a JsonObj is at the parent path "a.b.c.d".
      * @param path dot delimited segments of a path to a Json.
      *   Each segment represents either a name of a JSON object member or an (integer) index of a JSON array element.
      * @return this Json object for method chaining
      * @throws com.github.hyunikn.jsonden.exception.Inapplicable when the parent is not a JsonObj.
      * @throws com.github.hyunikn.jsonden.exception.UnreachablePath when the parent is not reachable.
      */
    public Json setx(String path, Json val) throws Inapplicable, UnreachablePath {

        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }

        String[] divided = Util.dividePath(path);
        assert divided != null;
        String parentPath = divided[0];

        Json parent;
        if (parentPath == null) {
            parent = this;
        } else {
            parent = getx(parentPath);
            if (parent == null) {
                throw new UnreachablePath(parentPath + " unreachable");
            }
        }

        if (parent.isObj()) {
            ((JsonObj) parent).set(divided[1], val);
        } else {
            throw new Inapplicable("set is not applicable to a " + parent.getClass().getSimpleName());
        }

        return this;
    }

    /**
      * Replaces a {@code Json} into a {@code JsonArr} deep in the nested structure of this one.
      * For example, {@code json.replacex("a.b.c.d.0", val)} is analogous to
      * {@code ((JsonArr) json.getx("a.b.c.d")).replace("0", val)} if a JsonArr is at the parent path "a.b.c.d".
      * @param path dot delimited segments of a path to a Json.
      *   Each segment represents either a name of a JSON object member or an (integer) index of a JSON array element.
      * @return this Json object for method chaining
      * @throws com.github.hyunikn.jsonden.exception.Inapplicable when the parent is not a JsonArr.
      * @throws com.github.hyunikn.jsonden.exception.UnreachablePath when the parent is not reachable.
      */
    public Json replacex(String path, Json val) throws Inapplicable, UnreachablePath {

        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }

        String[] divided = Util.dividePath(path);
        assert divided != null;
        String parentPath = divided[0];

        Json parent;
        if (parentPath == null) {
            parent = this;
        } else {
            parent = getx(parentPath);
            if (parent == null) {
                throw new UnreachablePath(parentPath + " unreachable");
            }
        }

        if (parent.isArr()) {
            int idx;
            try {
                idx = Integer.parseInt(divided[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("parent of the target node is a JsonArr and " +
                        "the last path segment must be an integer, which is not in " + path);
            }

            ((JsonArr) parent).replace(idx, val);
        } else {
            throw new Inapplicable("replace is not applicable to a " + parent.getClass().getSimpleName());
        }

        return this;
    }

    /**
      * Inserts a {@code Json} into a {@code JsonArr} deep in the nested structure of this one.
      * For example, {@code json.insertx("a.b.c.d.0", val)} is analogous to
      * {@code ((JsonArr) json.getx("a.b.c.d")).insert("0", val)} if a JsonArr is at the parent path "a.b.c.d".
      * @param path dot delimited segments of a path to a Json.
      *   Each segment represents either a name of a JSON object member or an (integer) index of a JSON array element.
      * @return this Json object for method chaining
      * @throws com.github.hyunikn.jsonden.exception.Inapplicable when the parent is not a JsonArr.
      * @throws com.github.hyunikn.jsonden.exception.UnreachablePath when the parent is not reachable.
      */
    public Json insertx(String path, Json val) throws Inapplicable, UnreachablePath {

        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }

        String[] divided = Util.dividePath(path);
        assert divided != null;
        String parentPath = divided[0];

        Json parent;
        if (parentPath == null) {
            parent = this;
        } else {
            parent = getx(parentPath);
            if (parent == null) {
                throw new UnreachablePath(parentPath + " unreachable");
            }
        }

        if (parent.isArr()) {
            int idx;
            try {
                idx = Integer.parseInt(divided[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("parent of the target node is a JsonArr and " +
                        "the last path segment must be an integer, which is not in " + path);
            }

            ((JsonArr) parent).insert(idx, val);
        } else {
            throw new Inapplicable("insert is not applicable to a " + parent.getClass().getSimpleName());
        }

        return this;
    }

    /**
      * Appends a {@code Json} into a {@code JsonArr} located deep in the nested structure of this one.
      * For example, {@code json.appendx("a.b.c.d.e", val)} is analogous to
      * {@code ((JsonArr) json.getx("a.b.c.d.e")).append(val)} if a JsonArr is at the path.
      * @param path dot delimited segments of a path to a Json.
      *   Each segment represents either a name of a JSON object member or an (integer) index of a JSON array element.
      * @return this Json object for method chaining
      * @throws com.github.hyunikn.jsonden.exception.Inapplicable when a JsonArr is not at the path.
      * @throws com.github.hyunikn.jsonden.exception.UnreachablePath when the path is not reachable.
      */
    public Json appendx(String path, Json val) throws Inapplicable, UnreachablePath {

        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }

        Json subnode = getx(path);
        if (subnode == null) {
            throw new UnreachablePath(path + " unreachable");
        } else {
            if (subnode.isArr()) {
                ((JsonArr) subnode).append(val);
            } else {
                throw new Inapplicable("append is not applicable to a " + subnode.getClass().getSimpleName());
            }
        }

        return this;
    }

    // series of ...x methods
    // ---------------------------------------------------------------------

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
    public JsonStr  asStr()  { return null; }

    //---------------------------------------------

    /**
      * If this is a {@link com.github.hyunikn.jsonden.JsonObj JsonObj} this method returns
      * the result of calling its {@code getMap()}, otherwise throws
      * {@link com.github.hyunikn.jsonden.exception.Inapplicable}
      */
    public LinkedHashMap<String, Json> getMap() throws Inapplicable {
        return (LinkedHashMap<String, Json>) throwInapplicable("getMap");
    }

    /**
      * If this is a {@link com.github.hyunikn.jsonden.JsonArr JsonArr} this method returns
      * the result of calling its {@code getList()}, otherwise throws
      * {@link com.github.hyunikn.jsonden.exception.Inapplicable}
      */
    public List<Json> getList() throws Inapplicable {
        return (List<Json>) throwInapplicable("getList");
    }

    /**
      * If this is a {@link com.github.hyunikn.jsonden.JsonBool JsonBool} this method returns
      * the result of calling its {@code getBoolean()}, otherwise throws
      * {@link com.github.hyunikn.jsonden.exception.Inapplicable}
      */
    public boolean getBoolean() throws Inapplicable {
        return (boolean) throwInapplicable("getBoolean");
    }

    /**
      * If this is a {@link com.github.hyunikn.jsonden.JsonNum JsonNum} this method returns
      * the result of calling its {@code getByte()}, otherwise throws
      * {@link com.github.hyunikn.jsonden.exception.Inapplicable}
      */
    public byte getByte() throws Inapplicable {
        return (byte) throwInapplicable("getByte");
    }

    /**
      * If this is a {@link com.github.hyunikn.jsonden.JsonNum JsonNum} this method returns
      * the result of calling its {@code getShort()}, otherwise throws
      * {@link com.github.hyunikn.jsonden.exception.Inapplicable}
      */
    public short getShort() throws Inapplicable {
        return (short) throwInapplicable("getShort");
    }

    /**
      * If this is a {@link com.github.hyunikn.jsonden.JsonNum JsonNum} this method returns
      * the result of calling its {@code getInt()}, otherwise throws
      * {@link com.github.hyunikn.jsonden.exception.Inapplicable}
      */
    public int getInt() throws Inapplicable {
        return (int) throwInapplicable("getInt");
    }

    /**
      * If this is a {@link com.github.hyunikn.jsonden.JsonNum JsonNum} this method returns
      * the result of calling its {@code getLong()}, otherwise throws
      * {@link com.github.hyunikn.jsonden.exception.Inapplicable}
      */
    public long getLong() throws Inapplicable {
        return (long) throwInapplicable("getLong");
    }

    /**
      * If this is a {@link com.github.hyunikn.jsonden.JsonNum JsonNum} this method returns
      * the result of calling its {@code getFloat()}, otherwise throws
      * {@link com.github.hyunikn.jsonden.exception.Inapplicable}
      */
    public float getFloat() throws Inapplicable {
        return (float) throwInapplicable("getFloat");
    }

    /**
      * If this is a {@link com.github.hyunikn.jsonden.JsonNum JsonNum} this method returns
      * the result of calling its {@code getDouble()}, otherwise throws
      * {@link com.github.hyunikn.jsonden.exception.Inapplicable}
      */
    public double getDouble() throws Inapplicable {
        return (double) throwInapplicable("getDouble");
    }

    /**
      * If this is a {@link com.github.hyunikn.jsonden.JsonNum JsonNum} this method returns
      * the result of calling its {@code getBigDecimal()}, otherwise throws
      * {@link com.github.hyunikn.jsonden.exception.Inapplicable}
      */
    public BigDecimal getBigDecimal() throws Inapplicable {
        return (BigDecimal) throwInapplicable("getBigDecimal");
    }

    /**
      * If this is a {@link com.github.hyunikn.jsonden.JsonStr JsonStr} this method returns
      * the result of calling its {@code getString()}, otherwise throws
      * {@link com.github.hyunikn.jsonden.exception.Inapplicable}
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

    private void checkStringifyOptions(int indentSize) {
        if (indentSize > 8) {
            throw new IllegalArgumentException("indentSize cannot be larger than eight");
        }
        if (indentSize < 0) {
            throw new IllegalArgumentException("indentSize cannot be a negative integer");
        }
    }
}
