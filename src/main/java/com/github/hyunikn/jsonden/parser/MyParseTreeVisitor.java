package com.github.hyunikn.jsonden.parser;

import com.github.hyunikn.jsonden.*;
import com.github.hyunikn.jsonden.parser.antlrgen.JsonParse;
import com.github.hyunikn.jsonden.parser.antlrgen.JsonParseBaseVisitor;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;

public class MyParseTreeVisitor extends JsonParseBaseVisitor<Json> {

    private static final String[] RTT = new String[0];

    private String stripQuoteMarks(String s) {
        int len = s.length();
        assert len >= 2;
        return s.substring(1, len - 1);
    }

    private String stripCommentMarks(String s) {
        int len = s.length();
        assert len >= 5;

        int i = s.indexOf("/**");
        return s.substring(i + 3, len - 2);
    }

    private String getLocation(TerminalNode tn) {
        Token tk = tn.getSymbol();
        return String.format("(%d,%d)", tk.getLine(), tk.getCharPositionInLine());
    }

    @Override public Json visitJson(JsonParse.JsonContext ctx) {
        return visit(ctx.commentedValue());
    }

    private List<String> getCommentLineList(TerminalNode tn) {
        String text = tn.getText();
        Token tk = tn.getSymbol();
        int row = tk.getLine(); // 1...
        int col = text.substring(1).indexOf("/"); // 0..

        String comment = stripCommentMarks(text);
        String[] lines = comment.split("\n");
        int nLines = lines.length;

        List<String> lineList = new LinkedList<>();

        String firstLine = lines[0].trim();
        if (firstLine.length() > 0) {
            lineList.add(firstLine);
        }

        for (int i = 1; i < nLines; i++) {
            String line = lines[i];

            if (line.length() <= col) {
                if (line.trim().length() == 0) {
                    if (i < nLines - 1) {
                        lineList.add("");
                    }
                } else {
                    throw new Error("insufficient leading white spaces of a comment line at " + (row + i));
                }
            } else {
                if (line.substring(0, col).trim().length() > 0) {
                    throw new Error("insufficient leading white spaces of a comment line at " + (row + i));
                }

                String cutLine = ("." + (line.substring(col))).trim();
                if (cutLine.length() > 1 || i < nLines - 1) {
                    lineList.add(cutLine.substring(1));
                }
            }
        }

        return lineList;
    }

    @Override public Json visitCommentedValue(JsonParse.CommentedValueContext ctx) {
        Json value = visitValue(ctx.value());
        TerminalNode tn = ctx.COMMENT();
        if (tn != null) {
            List<String> lineList = getCommentLineList(tn);
            if (lineList.size() > 0) {
                value.setCommentLines(lineList.toArray(RTT));
            }
        }
        return value;
    }

    @Override public Json visitValue(JsonParse.ValueContext ctx) {

        if (ctx.STRING() != null) {
            return new JsonStr(stripQuoteMarks(ctx.STRING().getText()));
        } else if (ctx.NUMBER() != null) {
            return new JsonNum(ctx.NUMBER().getText());
        } else if (ctx.TRUE() != null) {
            return new JsonBool(true);
        } else if (ctx.FALSE() != null) {
            return new JsonBool(false);
        } else if (ctx.NULL() != null) {
            return new JsonNull();
        } else if (ctx.obj() != null) {
            return visitObj(ctx.obj());
        } else if (ctx.arr() != null) {
            return visitArr(ctx.arr());
        }

        throw new Error("unreachable: value type");
    }

    @Override public Json visitObj(JsonParse.ObjContext ctx) {
        JsonObj jo = new JsonObj();

        for (JsonParse.CommentedPairContext cp: ctx.commentedPair()) {

            JsonParse.PairContext pc = cp.pair();
            String key = stripQuoteMarks(pc.STRING().getText());
            if (key.indexOf('.') >= 0) {
                throw new Error("Json-den does not allow dot(.) characters in JSON object member keys: '" +
                        key + "' at " + getLocation(pc.STRING()));
            }

            Json val = visitValue(pc.value());
            Json old = jo.get(key);
            if (old != null) {
                throw new Error("a duplicate key '" + key + "' at " + getLocation(pc.STRING()));
            }
            jo.set(key, val);

            TerminalNode tn = cp.COMMENT();
            if (tn != null) {
                List<String> lineList = getCommentLineList(tn);
                if (lineList.size() > 0) {
                    val.setCommentLines(lineList.toArray(RTT));
                }
            }
        }

        return jo;
    }

    @Override public Json visitCommentedPair(JsonParse.CommentedPairContext ctx) {
        throw new Error("unreachable: visiting commented pair");
    }

    @Override public Json visitPair(JsonParse.PairContext ctx) {
        throw new Error("unreachable: visiting pair");
    }

    @Override public Json visitArr(JsonParse.ArrContext ctx) {
        JsonArr ja = new JsonArr();

        for (JsonParse.CommentedValueContext cv: ctx.commentedValue()) {
            ja.append(visitCommentedValue(cv));
        }

        return ja;
    }
}
