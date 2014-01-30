package lexer;

/*
 * Manages lexemes for reserved words, identifiers, and composite tokens (e.g. &&).
 * Manages the written form operators in the intermediate code (e.g. the source text -2 has the intermediate form minus 2).
 */
public class Word extends Token {
	public String lexeme = "";

	public Word(String s, int tag) {
		super(s, tag);
		lexeme = s;
	}

	public String toString() {
		return lexeme;
	}

	public static final Word lsb = new Word("[", Tag.LSB), rsb = new Word("]",
			Tag.RSB), assign = new Word(":=", Tag.ASSIGN), plus = new Word("+",
			Tag.PLUS), minus = new Word("-", Tag.MINUS), mult = new Word("*",
			Tag.MULT), div = new Word("/", Tag.DIV), mod = new Word("%",
			Tag.MOD), pow = new Word("^", Tag.POW), and = new Word("and",
			Tag.AND), or = new Word("or", Tag.OR), not = new Word("not",
			Tag.NOT), sin = new Word("sin", Tag.SIN), cos = new Word("cos",
			Tag.COS), tan = new Word("tan", Tag.TAN), True = new Word("true",
			Tag.TRUE), False = new Word("false", Tag.FALSE), stdout = new Word(
			"stdout", Tag.STDOUT), If = new Word("if", Tag.IF),
			While = new Word("while", Tag.WHILE),
			let = new Word("let", Tag.LET), bool = new Word("bool", Tag.BASIC),
			Int = new Word("int", Tag.BASIC), Float = new Word("float",
					Tag.BASIC), string = new Word("string", Tag.BASIC),
			lt = new Word("<", Tag.LT), gt = new Word(">", Tag.GT),
			eq = new Word("==", Tag.EQ), ne = new Word("!=", Tag.NE),
			le = new Word("<=", Tag.LE), ge = new Word(">=", Tag.GE),
			eof = new Word("-1", Tag.EOF);/*
										 * , minus = new Word("-", Tag .MINUS)
										 */// TODO:
											// Differentiate
											// between
											// unary
											// '-'
											// and
											// binary
											// '-'
	// = new Word("", Tag.),
}
