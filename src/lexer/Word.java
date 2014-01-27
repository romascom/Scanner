package lexer;

/*
 * Manages lexemes for reserved words, identifiers, and composite tokens (e.g. &&).
 * Manages the written form operators in the intermediate code (e.g. the source text -2 has the intermediate form minus 2).
 */
public class Word extends Token {
	public String lexeme = "";

	public Word(String s, int tag) {
		super(tag);
		lexeme = s;
	}

	public String toString() {
		return lexeme;
	}

	public static final Word lsb = new Word("[", Tag.LSB), RSB = new Word("]", Tag.RSB), = new Word("", Tag.), = new Word("", Tag.),  and = new Word("and", Tag.AND), or = new Word(
			"or", Tag.OR), not = new Word("not", Tag.NOT), sin = new Word(
			"sin", Tag.SIN), cos = new Word("cos", Tag.COS), tan = new Word(
			"tan", Tag.TAN), stdout = new Word("stdout", Tag.STDOUT),
			eq = new Word("==", Tag.EQ), ne = new Word("!=", Tag.NE),
			le = new Word("<=", Tag.LE), ge = new Word(">=", Tag.GE),
			minus = new Word("-", Tag.MINUS), True = new Word("true",
					Tag.TRUE), False = new Word("false", Tag.FALSE);

}
