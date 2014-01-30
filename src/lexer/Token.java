package lexer;

public class Token {
	public final int tag;
	public final String lexeme;

	public Token(String s, int t) {
		lexeme = s;
		tag = t;
	}

	public String toString() {
		return "" + (char) tag;
	}
}
