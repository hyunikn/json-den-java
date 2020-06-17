// Generated from JsonParse.g4 by ANTLR 4.8

package com.wingsoft.jsonden.parser.antlrgen;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JsonParse extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		STRING=1, NUMBER=2, LBRACE=3, RBRACE=4, LBRACKET=5, RBRACKET=6, COMMA=7, 
		COLON=8, TRUE=9, FALSE=10, NULL=11, WS=12, COMMENT=13, DROPPED_BLOCK_COMMENT=14, 
		DROPPED_LINE_COMMENT=15;
	public static final int
		RULE_json = 0, RULE_commentedValue = 1, RULE_value = 2, RULE_obj = 3, 
		RULE_commentedPair = 4, RULE_pair = 5, RULE_arr = 6;
	private static String[] makeRuleNames() {
		return new String[] {
			"json", "commentedValue", "value", "obj", "commentedPair", "pair", "arr"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'{'", "'}'", "'['", "']'", "','", "':'", "'true'", 
			"'false'", "'null'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "STRING", "NUMBER", "LBRACE", "RBRACE", "LBRACKET", "RBRACKET", 
			"COMMA", "COLON", "TRUE", "FALSE", "NULL", "WS", "COMMENT", "DROPPED_BLOCK_COMMENT", 
			"DROPPED_LINE_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "JsonParse.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


	    public String getLiteralName(int idx) {
	        return _LITERAL_NAMES.length > idx ? _LITERAL_NAMES[idx] : null;
	    }

	public JsonParse(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class JsonContext extends ParserRuleContext {
		public CommentedValueContext commentedValue() {
			return getRuleContext(CommentedValueContext.class,0);
		}
		public JsonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_json; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParseVisitor ) return ((JsonParseVisitor<? extends T>)visitor).visitJson(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonContext json() throws RecognitionException {
		JsonContext _localctx = new JsonContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_json);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			commentedValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommentedValueContext extends ParserRuleContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode COMMENT() { return getToken(JsonParse.COMMENT, 0); }
		public CommentedValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commentedValue; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParseVisitor ) return ((JsonParseVisitor<? extends T>)visitor).visitCommentedValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentedValueContext commentedValue() throws RecognitionException {
		CommentedValueContext _localctx = new CommentedValueContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_commentedValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(17);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMENT) {
				{
				setState(16);
				match(COMMENT);
				}
			}

			setState(19);
			value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(JsonParse.STRING, 0); }
		public TerminalNode NUMBER() { return getToken(JsonParse.NUMBER, 0); }
		public TerminalNode TRUE() { return getToken(JsonParse.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(JsonParse.FALSE, 0); }
		public TerminalNode NULL() { return getToken(JsonParse.NULL, 0); }
		public ObjContext obj() {
			return getRuleContext(ObjContext.class,0);
		}
		public ArrContext arr() {
			return getRuleContext(ArrContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParseVisitor ) return ((JsonParseVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_value);
		try {
			setState(28);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(21);
				match(STRING);
				}
				break;
			case NUMBER:
				enterOuterAlt(_localctx, 2);
				{
				setState(22);
				match(NUMBER);
				}
				break;
			case TRUE:
				enterOuterAlt(_localctx, 3);
				{
				setState(23);
				match(TRUE);
				}
				break;
			case FALSE:
				enterOuterAlt(_localctx, 4);
				{
				setState(24);
				match(FALSE);
				}
				break;
			case NULL:
				enterOuterAlt(_localctx, 5);
				{
				setState(25);
				match(NULL);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 6);
				{
				setState(26);
				obj();
				}
				break;
			case LBRACKET:
				enterOuterAlt(_localctx, 7);
				{
				setState(27);
				arr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(JsonParse.LBRACE, 0); }
		public List<CommentedPairContext> commentedPair() {
			return getRuleContexts(CommentedPairContext.class);
		}
		public CommentedPairContext commentedPair(int i) {
			return getRuleContext(CommentedPairContext.class,i);
		}
		public TerminalNode RBRACE() { return getToken(JsonParse.RBRACE, 0); }
		public List<TerminalNode> COMMA() { return getTokens(JsonParse.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JsonParse.COMMA, i);
		}
		public ObjContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_obj; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParseVisitor ) return ((JsonParseVisitor<? extends T>)visitor).visitObj(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjContext obj() throws RecognitionException {
		ObjContext _localctx = new ObjContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_obj);
		int _la;
		try {
			setState(43);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(30);
				match(LBRACE);
				setState(31);
				commentedPair();
				setState(36);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(32);
					match(COMMA);
					setState(33);
					commentedPair();
					}
					}
					setState(38);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(39);
				match(RBRACE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(41);
				match(LBRACE);
				setState(42);
				match(RBRACE);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommentedPairContext extends ParserRuleContext {
		public PairContext pair() {
			return getRuleContext(PairContext.class,0);
		}
		public TerminalNode COMMENT() { return getToken(JsonParse.COMMENT, 0); }
		public CommentedPairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commentedPair; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParseVisitor ) return ((JsonParseVisitor<? extends T>)visitor).visitCommentedPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentedPairContext commentedPair() throws RecognitionException {
		CommentedPairContext _localctx = new CommentedPairContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_commentedPair);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMENT) {
				{
				setState(45);
				match(COMMENT);
				}
			}

			setState(48);
			pair();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PairContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(JsonParse.STRING, 0); }
		public TerminalNode COLON() { return getToken(JsonParse.COLON, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public PairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParseVisitor ) return ((JsonParseVisitor<? extends T>)visitor).visitPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairContext pair() throws RecognitionException {
		PairContext _localctx = new PairContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_pair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			match(STRING);
			setState(51);
			match(COLON);
			setState(52);
			value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(JsonParse.LBRACKET, 0); }
		public List<CommentedValueContext> commentedValue() {
			return getRuleContexts(CommentedValueContext.class);
		}
		public CommentedValueContext commentedValue(int i) {
			return getRuleContext(CommentedValueContext.class,i);
		}
		public TerminalNode RBRACKET() { return getToken(JsonParse.RBRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(JsonParse.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JsonParse.COMMA, i);
		}
		public ArrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParseVisitor ) return ((JsonParseVisitor<? extends T>)visitor).visitArr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrContext arr() throws RecognitionException {
		ArrContext _localctx = new ArrContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_arr);
		int _la;
		try {
			setState(67);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(54);
				match(LBRACKET);
				setState(55);
				commentedValue();
				setState(60);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(56);
					match(COMMA);
					setState(57);
					commentedValue();
					}
					}
					setState(62);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(63);
				match(RBRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(65);
				match(LBRACKET);
				setState(66);
				match(RBRACKET);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\21H\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\3\2\3\3\5\3\24\n\3\3\3"+
		"\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\37\n\4\3\5\3\5\3\5\3\5\7\5%\n\5\f"+
		"\5\16\5(\13\5\3\5\3\5\3\5\3\5\5\5.\n\5\3\6\5\6\61\n\6\3\6\3\6\3\7\3\7"+
		"\3\7\3\7\3\b\3\b\3\b\3\b\7\b=\n\b\f\b\16\b@\13\b\3\b\3\b\3\b\3\b\5\bF"+
		"\n\b\3\b\2\2\t\2\4\6\b\n\f\16\2\2\2L\2\20\3\2\2\2\4\23\3\2\2\2\6\36\3"+
		"\2\2\2\b-\3\2\2\2\n\60\3\2\2\2\f\64\3\2\2\2\16E\3\2\2\2\20\21\5\4\3\2"+
		"\21\3\3\2\2\2\22\24\7\17\2\2\23\22\3\2\2\2\23\24\3\2\2\2\24\25\3\2\2\2"+
		"\25\26\5\6\4\2\26\5\3\2\2\2\27\37\7\3\2\2\30\37\7\4\2\2\31\37\7\13\2\2"+
		"\32\37\7\f\2\2\33\37\7\r\2\2\34\37\5\b\5\2\35\37\5\16\b\2\36\27\3\2\2"+
		"\2\36\30\3\2\2\2\36\31\3\2\2\2\36\32\3\2\2\2\36\33\3\2\2\2\36\34\3\2\2"+
		"\2\36\35\3\2\2\2\37\7\3\2\2\2 !\7\5\2\2!&\5\n\6\2\"#\7\t\2\2#%\5\n\6\2"+
		"$\"\3\2\2\2%(\3\2\2\2&$\3\2\2\2&\'\3\2\2\2\')\3\2\2\2(&\3\2\2\2)*\7\6"+
		"\2\2*.\3\2\2\2+,\7\5\2\2,.\7\6\2\2- \3\2\2\2-+\3\2\2\2.\t\3\2\2\2/\61"+
		"\7\17\2\2\60/\3\2\2\2\60\61\3\2\2\2\61\62\3\2\2\2\62\63\5\f\7\2\63\13"+
		"\3\2\2\2\64\65\7\3\2\2\65\66\7\n\2\2\66\67\5\6\4\2\67\r\3\2\2\289\7\7"+
		"\2\29>\5\4\3\2:;\7\t\2\2;=\5\4\3\2<:\3\2\2\2=@\3\2\2\2><\3\2\2\2>?\3\2"+
		"\2\2?A\3\2\2\2@>\3\2\2\2AB\7\b\2\2BF\3\2\2\2CD\7\7\2\2DF\7\b\2\2E8\3\2"+
		"\2\2EC\3\2\2\2F\17\3\2\2\2\t\23\36&-\60>E";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}