// Generated from LabeledExpr.g4 by ANTLR 4.5.3

package cgs.bigdata.antlr.labeledexpr;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LabeledExprLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, CLEAR=4, MUL=5, DIV=6, ADD=7, SUB=8, NEWLINE=9, 
		ID=10, INT=11, WS=12;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "CLEAR", "MUL", "DIV", "ADD", "SUB", "NEWLINE", 
		"ID", "INT", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'='", "'('", "')'", null, "'*'", "'/'", "'+'", "'-'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, "CLEAR", "MUL", "DIV", "ADD", "SUB", "NEWLINE", 
		"ID", "INT", "WS"
	};
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


	public LabeledExprLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "LabeledExpr.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\16M\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\5\5,\n\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\5\n\67\n\n"+
		"\3\n\3\n\3\13\6\13<\n\13\r\13\16\13=\3\f\3\f\7\fB\n\f\f\f\16\fE\13\f\3"+
		"\r\6\rH\n\r\r\r\16\rI\3\r\3\r\2\2\16\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\f\27\r\31\16\3\2\6\4\2C\\c|\3\2\63;\3\2\62;\4\2\13\13\"\""+
		"Q\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\3\33\3\2\2\2\5\35\3\2\2\2\7\37\3\2\2\2\t+\3\2\2\2\13-\3"+
		"\2\2\2\r/\3\2\2\2\17\61\3\2\2\2\21\63\3\2\2\2\23\66\3\2\2\2\25;\3\2\2"+
		"\2\27?\3\2\2\2\31G\3\2\2\2\33\34\7?\2\2\34\4\3\2\2\2\35\36\7*\2\2\36\6"+
		"\3\2\2\2\37 \7+\2\2 \b\3\2\2\2!\"\7E\2\2\"#\7N\2\2#$\7G\2\2$%\7C\2\2%"+
		",\7T\2\2&\'\7e\2\2\'(\7n\2\2()\7g\2\2)*\7c\2\2*,\7t\2\2+!\3\2\2\2+&\3"+
		"\2\2\2,\n\3\2\2\2-.\7,\2\2.\f\3\2\2\2/\60\7\61\2\2\60\16\3\2\2\2\61\62"+
		"\7-\2\2\62\20\3\2\2\2\63\64\7/\2\2\64\22\3\2\2\2\65\67\7\17\2\2\66\65"+
		"\3\2\2\2\66\67\3\2\2\2\678\3\2\2\289\7\f\2\29\24\3\2\2\2:<\t\2\2\2;:\3"+
		"\2\2\2<=\3\2\2\2=;\3\2\2\2=>\3\2\2\2>\26\3\2\2\2?C\t\3\2\2@B\t\4\2\2A"+
		"@\3\2\2\2BE\3\2\2\2CA\3\2\2\2CD\3\2\2\2D\30\3\2\2\2EC\3\2\2\2FH\t\5\2"+
		"\2GF\3\2\2\2HI\3\2\2\2IG\3\2\2\2IJ\3\2\2\2JK\3\2\2\2KL\b\r\2\2L\32\3\2"+
		"\2\2\b\2+\66=CI\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}