// Generated from JsonParse.g4 by ANTLR 4.8

package com.wingsoft.jsonden.parser.antlrgen;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link JsonParse}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface JsonParseVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link JsonParse#json}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson(JsonParse.JsonContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonParse#commentedValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentedValue(JsonParse.CommentedValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonParse#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(JsonParse.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonParse#obj}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObj(JsonParse.ObjContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonParse#commentedPair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentedPair(JsonParse.CommentedPairContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonParse#pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair(JsonParse.PairContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonParse#arr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArr(JsonParse.ArrContext ctx);
}