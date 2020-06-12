package com.wingsoft.jsonden.parser;

import com.wingsoft.jsonden.*;
import com.wingsoft.jsonden.parser.antlrgen.JsonParse;
import com.wingsoft.jsonden.parser.antlrgen.JsonParseBaseVisitor;

import org.antlr.v4.runtime.tree.TerminalNode;

public class ParseTreeVisitor extends JsonParseBaseVisitor<Json> {

    private String stripQuoteMarks(String s) {
        int len = s.length();
        assert len >= 2;
        return s.substring(1, len - 1);
    }

    private String stripCommentMarks(String s) {
        int len = s.length();
        assert len >= 5;
        return s.substring(3, len - 2);
    }

    @Override public Json visitJson(JsonParse.JsonContext ctx) {
        return visit(ctx.commentedValue());
    }

    @Override public Json visitCommentedValue(JsonParse.CommentedValueContext ctx) {
        Json value = visitValue(ctx.value());
        TerminalNode tn = ctx.COMMENT();
        if (tn != null) {
            String comment = stripCommentMarks(tn.getText());
            value.setCommentLines(comment.split("\n"));
        }
        return value;
    }

    @Override public Json visitValue(JsonParse.ValueContext ctx) {

        if (ctx.STRING() != null) {
            return new JsonStr(stripQuoteMarks(ctx.STRING().getText()));
        } else if (ctx.NUMBER() != null) {
            return new JsonNum(ctx.NUMBER().getText());
        } else if (ctx.TRUE() != null) {
            return JsonBool.getJsonBool(true);
        } else if (ctx.FALSE() != null) {
            return JsonBool.getJsonBool(false);
        } else if (ctx.NULL() != null) {
            return JsonNull.getJsonNull();
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
            Json val = visitValue(pc.value());
            jo.set(key, val);

            TerminalNode tn = cp.COMMENT();
            if (tn != null) {
                String comment = stripCommentMarks(tn.getText());
                jo.setCommentLines(key, comment.split("\n"));
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
