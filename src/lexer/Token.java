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
	
	public boolean isString() {
		if (this.tag == Tag.TRUE || this.tag == Tag.FALSE
				|| this.tag == Tag.STRING) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isConstant() {
		if (this.tag == Tag.NUM || this.tag == Tag.REAL
				|| isString()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isName() {
		if (this.tag == Tag.ID) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isUnop() {
		if (this.tag == Tag.MINUS || this.tag == Tag.NOT
				|| this.tag == Tag.SIN || this.tag == Tag.COS
				|| this.tag == Tag.TAN) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isBinop() {
		if (this.tag == Tag.PLUS || this.tag == Tag.MINUS
				|| this.tag == Tag.MULT || this.tag == Tag.DIV
				|| this.tag == Tag.MOD || this.tag == Tag.POW
				|| this.tag == Tag.EQ || this.tag == Tag.LT
				|| this.tag == Tag.LE || this.tag == Tag.GT
				|| this.tag == Tag.GE || this.tag == Tag.NE
				|| this.tag == Tag.OR || this.tag == Tag.AND) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isStmt() {
		if (this.tag == Tag.IF || this.tag == Tag.LET
				|| this.tag == Tag.WHILE || this.tag == Tag.STDOUT) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isLSBExpr() {
		if (isStmt() || isUnop() || isBinop()
				|| this.tag == Tag.ASSIGN) {
			return true;
		}
		return false;

	}
}
