// Generated from JsonLex.g4 by ANTLR 4.8

package com.wingsoft.jsonden.parser.antlrgen;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JsonLex extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		STRING=1, NUMBER=2, LBRACE=3, RBRACE=4, LBRACKET=5, RBRACKET=6, COMMA=7, 
		COLON=8, TRUE=9, FALSE=10, NULL=11, WS=12, COMMENT=13, DROPPED_BLOCK_COMMENT=14, 
		DROPPED_LINE_COMMENT=15;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"STRING", "ESC", "UNICODE", "HEX", "SAFECODEPOINT", "NUMBER", "INT", 
			"EXP", "LBRACE", "RBRACE", "LBRACKET", "RBRACKET", "COMMA", "COLON", 
			"TRUE", "FALSE", "NULL", "WS", "COMMENT", "DROPPED_BLOCK_COMMENT", "DROPPED_LINE_COMMENT"
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


	public JsonLex(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "JsonLex.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\21\u00ca\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\3\2\7\2\61\n\2\f"+
		"\2\16\2\64\13\2\3\2\3\2\3\3\3\3\3\3\5\3;\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\5\3\5\3\6\3\6\3\7\5\7H\n\7\3\7\3\7\3\7\6\7M\n\7\r\7\16\7N\5\7Q\n\7\3"+
		"\7\5\7T\n\7\3\b\3\b\3\b\7\bY\n\b\f\b\16\b\\\13\b\5\b^\n\b\3\t\3\t\5\t"+
		"b\n\t\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3"+
		"\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3"+
		"\22\3\22\3\23\6\23\u0083\n\23\r\23\16\23\u0084\3\23\3\23\3\24\3\24\3\24"+
		"\3\24\3\24\7\24\u008e\n\24\f\24\16\24\u0091\13\24\3\24\3\24\7\24\u0095"+
		"\n\24\f\24\16\24\u0098\13\24\3\24\7\24\u009b\n\24\f\24\16\24\u009e\13"+
		"\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\7\25\u00a7\n\25\f\25\16\25\u00aa"+
		"\13\25\3\25\3\25\7\25\u00ae\n\25\f\25\16\25\u00b1\13\25\3\25\7\25\u00b4"+
		"\n\25\f\25\16\25\u00b7\13\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3"+
		"\26\7\26\u00c2\n\26\f\26\16\26\u00c5\13\26\3\26\3\26\3\26\3\26\2\2\27"+
		"\3\3\5\2\7\2\t\2\13\2\r\4\17\2\21\2\23\5\25\6\27\7\31\b\33\t\35\n\37\13"+
		"!\f#\r%\16\'\17)\20+\21\3\2\r\n\2$$\61\61^^ddhhppttvv\5\2\62;CHch\5\2"+
		"\2!$$^^\3\2\62;\3\2\63;\4\2GGgg\4\2--//\5\2\13\f\17\17\"\"\3\2,,\3\2\61"+
		"\61\3\2\f\f\2\u00d5\2\3\3\2\2\2\2\r\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2"+
		"\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3"+
		"\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\3-\3\2"+
		"\2\2\5\67\3\2\2\2\7<\3\2\2\2\tB\3\2\2\2\13D\3\2\2\2\rG\3\2\2\2\17]\3\2"+
		"\2\2\21_\3\2\2\2\23e\3\2\2\2\25g\3\2\2\2\27i\3\2\2\2\31k\3\2\2\2\33m\3"+
		"\2\2\2\35o\3\2\2\2\37q\3\2\2\2!v\3\2\2\2#|\3\2\2\2%\u0082\3\2\2\2\'\u0088"+
		"\3\2\2\2)\u00a2\3\2\2\2+\u00bd\3\2\2\2-\62\7$\2\2.\61\5\5\3\2/\61\5\13"+
		"\6\2\60.\3\2\2\2\60/\3\2\2\2\61\64\3\2\2\2\62\60\3\2\2\2\62\63\3\2\2\2"+
		"\63\65\3\2\2\2\64\62\3\2\2\2\65\66\7$\2\2\66\4\3\2\2\2\67:\7^\2\28;\t"+
		"\2\2\29;\5\7\4\2:8\3\2\2\2:9\3\2\2\2;\6\3\2\2\2<=\7w\2\2=>\5\t\5\2>?\5"+
		"\t\5\2?@\5\t\5\2@A\5\t\5\2A\b\3\2\2\2BC\t\3\2\2C\n\3\2\2\2DE\n\4\2\2E"+
		"\f\3\2\2\2FH\7/\2\2GF\3\2\2\2GH\3\2\2\2HI\3\2\2\2IP\5\17\b\2JL\7\60\2"+
		"\2KM\t\5\2\2LK\3\2\2\2MN\3\2\2\2NL\3\2\2\2NO\3\2\2\2OQ\3\2\2\2PJ\3\2\2"+
		"\2PQ\3\2\2\2QS\3\2\2\2RT\5\21\t\2SR\3\2\2\2ST\3\2\2\2T\16\3\2\2\2U^\7"+
		"\62\2\2VZ\t\6\2\2WY\t\5\2\2XW\3\2\2\2Y\\\3\2\2\2ZX\3\2\2\2Z[\3\2\2\2["+
		"^\3\2\2\2\\Z\3\2\2\2]U\3\2\2\2]V\3\2\2\2^\20\3\2\2\2_a\t\7\2\2`b\t\b\2"+
		"\2a`\3\2\2\2ab\3\2\2\2bc\3\2\2\2cd\5\17\b\2d\22\3\2\2\2ef\7}\2\2f\24\3"+
		"\2\2\2gh\7\177\2\2h\26\3\2\2\2ij\7]\2\2j\30\3\2\2\2kl\7_\2\2l\32\3\2\2"+
		"\2mn\7.\2\2n\34\3\2\2\2op\7<\2\2p\36\3\2\2\2qr\7v\2\2rs\7t\2\2st\7w\2"+
		"\2tu\7g\2\2u \3\2\2\2vw\7h\2\2wx\7c\2\2xy\7n\2\2yz\7u\2\2z{\7g\2\2{\""+
		"\3\2\2\2|}\7p\2\2}~\7w\2\2~\177\7n\2\2\177\u0080\7n\2\2\u0080$\3\2\2\2"+
		"\u0081\u0083\t\t\2\2\u0082\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0082"+
		"\3\2\2\2\u0084\u0085\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0087\b\23\2\2"+
		"\u0087&\3\2\2\2\u0088\u0089\7\61\2\2\u0089\u008a\7,\2\2\u008a\u008b\7"+
		",\2\2\u008b\u0096\3\2\2\2\u008c\u008e\n\n\2\2\u008d\u008c\3\2\2\2\u008e"+
		"\u0091\3\2\2\2\u008f\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0092\3\2"+
		"\2\2\u0091\u008f\3\2\2\2\u0092\u0093\7,\2\2\u0093\u0095\n\13\2\2\u0094"+
		"\u008f\3\2\2\2\u0095\u0098\3\2\2\2\u0096\u0094\3\2\2\2\u0096\u0097\3\2"+
		"\2\2\u0097\u009c\3\2\2\2\u0098\u0096\3\2\2\2\u0099\u009b\n\n\2\2\u009a"+
		"\u0099\3\2\2\2\u009b\u009e\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009d\3\2"+
		"\2\2\u009d\u009f\3\2\2\2\u009e\u009c\3\2\2\2\u009f\u00a0\7,\2\2\u00a0"+
		"\u00a1\7\61\2\2\u00a1(\3\2\2\2\u00a2\u00a3\7\61\2\2\u00a3\u00a4\7,\2\2"+
		"\u00a4\u00af\3\2\2\2\u00a5\u00a7\n\n\2\2\u00a6\u00a5\3\2\2\2\u00a7\u00aa"+
		"\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00ab\3\2\2\2\u00aa"+
		"\u00a8\3\2\2\2\u00ab\u00ac\7,\2\2\u00ac\u00ae\n\13\2\2\u00ad\u00a8\3\2"+
		"\2\2\u00ae\u00b1\3\2\2\2\u00af\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0"+
		"\u00b5\3\2\2\2\u00b1\u00af\3\2\2\2\u00b2\u00b4\n\n\2\2\u00b3\u00b2\3\2"+
		"\2\2\u00b4\u00b7\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6"+
		"\u00b8\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b8\u00b9\7,\2\2\u00b9\u00ba\7\61"+
		"\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bc\b\25\2\2\u00bc*\3\2\2\2\u00bd\u00be"+
		"\7\61\2\2\u00be\u00bf\7\61\2\2\u00bf\u00c3\3\2\2\2\u00c0\u00c2\13\2\2"+
		"\2\u00c1\u00c0\3\2\2\2\u00c2\u00c5\3\2\2\2\u00c3\u00c1\3\2\2\2\u00c3\u00c4"+
		"\3\2\2\2\u00c4\u00c6\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c6\u00c7\t\f\2\2\u00c7"+
		"\u00c8\3\2\2\2\u00c8\u00c9\b\26\2\2\u00c9,\3\2\2\2\25\2\60\62:GNPSZ]a"+
		"\u0084\u008f\u0096\u009c\u00a8\u00af\u00b5\u00c3\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}