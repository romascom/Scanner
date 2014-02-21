package parser;

import lexer.*;

public class ParserHelper {

	public static boolean isString(Token lookahead) {
		if (lookahead.tag == Tag.TRUE || lookahead.tag == Tag.FALSE
				|| lookahead.tag == Tag.STRING) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isConstant(Token lookahead) {
		if (lookahead.tag == Tag.NUM || lookahead.tag == Tag.REAL
				|| isString(lookahead)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isName(Token lookahead) {
		if (lookahead.tag == Tag.ID) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isUnop(Token lookahead) {
		if (lookahead.tag == Tag.MINUS || lookahead.tag == Tag.NOT
				|| lookahead.tag == Tag.SIN || lookahead.tag == Tag.COS
				|| lookahead.tag == Tag.TAN) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isBinop(Token lookahead) {
		if (lookahead.tag == Tag.PLUS || lookahead.tag == Tag.MINUS
				|| lookahead.tag == Tag.MULT || lookahead.tag == Tag.DIV
				|| lookahead.tag == Tag.MOD || lookahead.tag == Tag.POW
				|| lookahead.tag == Tag.EQ || lookahead.tag == Tag.LT
				|| lookahead.tag == Tag.LE || lookahead.tag == Tag.GT
				|| lookahead.tag == Tag.GE || lookahead.tag == Tag.NE
				|| lookahead.tag == Tag.OR || lookahead.tag == Tag.AND) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isStmt(Token lookahead) {
		if (lookahead.tag == Tag.IF || lookahead.tag == Tag.LET
				|| lookahead.tag == Tag.WHILE || lookahead.tag == Tag.STDOUT) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isLSBExpr(Token lookahead) {
		if (isStmt(lookahead) || isUnop(lookahead) || isBinop(lookahead)
				|| lookahead.tag == Tag.ASSIGN) {
			return true;
		}
		return false;

	}
}
