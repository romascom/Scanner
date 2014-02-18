package parser;

import java.io.IOException;

import lexer.*;
import inter.*;

public class Parser { // see pg. 982 of the text
	private Lexer lex; // this parser's lexical analyzer
	private Token lookahead = null; // lookahead token
	private Token lookaheadTwo = null; // second lookahead

	// Env top = null; // current or top symbol table
	// int used = 0; // storage used for declarations

	public Parser(Lexer l) throws IOException {
		lex = l;
		nextToken();
		Node root = T(); // start building tree
	}

	/**
	 * Sets the lookahead tokens to the next two tokens scanned by the lexer.
	 * Consumes two tokens.
	 * 
	 * @throws IOException
	 */
	void nextToken() throws IOException {
		if (lookahead == null) {
			lookahead = lex.scan();
		} else {
			lookahead = lookaheadTwo;
		}

		lookaheadTwo = lex.scan();
	}

	void error(String s) {
		throw new Error("near line " + lex.line + ": " + s);
	} // TODO: Define lex.line ... or not

	/**
	 * Ensures that t matches the lookahead token before consuming another
	 * token.
	 * 
	 * @param t
	 * @throws IOException
	 */
	void match(int t) {
		if (lookahead.tag == t) {
			try {
				nextToken();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				System.exit(0);
			}
		} else {
			error("syntax error");
		}
	}

	private Node T() {
		Node root = new Node();
		match(Tag.LSB);
		root.addChild(Tag.LSB);
		root.addChild(S());
		match(Tag.RSB);
		root.addChild(Tag.RSB);
		return root;
	}

	private Node S() {
		Node node = new Node();
		if (lookahead.tag == Tag.LSB) {
			match(Tag.LSB);
			node.addChild(Tag.LSB);
			if (lookahead.tag != Tag.RSB) {
				node.addChild(S());
			}
			match(Tag.RSB);
			node.addChild(Tag.RSB);
		} else {
			node.addChild(expr());
		}
		node.addChild(SPrime());
		return node;
	}

	private Node SPrime() {
		Node node = new Node();

		if (lookahead.tag == Tag.RSB) {
			return null;
		}
		node.addChild(S());
		return node;
	}

	private Node expr() {
		Node node = new Node();

		if (ParserHelper.isStmt(lookaheadTwo)) {
			node.addChild(stmts());
		} else {
			node.addChild(oper());
		}
		return node;
	}

	private Node oper() {
		Node node = new Node();
		if (ParserHelper.isConstant(lookahead)) {
			node.addChild(constants());
		} else if (ParserHelper.isName(lookahead)) {
			node.addChild(name());
		} else {
			match(Tag.LSB);
			node.addChild(Tag.LSB);
			if (lookahead.tag == Tag.ASSIGN) {
				match(Tag.ASSIGN);
				node.addChild(Tag.ASSIGN);
				node.addChild(oper());
				node.addChild(oper());
			} else if (lookahead.tag == Tag.MINUS) {
				match(Tag.MINUS); node.addChild(Tag.MINUS);
				if (lookaheadTwo.tag != Tag.RSB){
					node.addChild(oper());
				}
				node.addChild(oper());
			} else if (ParserHelper.isBinop(lookahead)) {
				node.addChild(lookahead.tag);
				match(lookahead.tag);
				node.addChild(oper());
				node.addChild(oper());
			} else if (ParserHelper.isUnop(lookahead)) {
				node.addChild(lookahead.tag);
				match(lookahead.tag);
				node.addChild(oper());
			}
			match(Tag.RSB);
			node.addChild(Tag.RSB);
		}

		return node;
	}

	private Node constants() {
		Node node = new Node();
		switch (lookahead.tag) {
		case Tag.NUM:
			node.addChild(ints());
		case Tag.REAL:
			node.addChild(floats());
		default:
			node.addChild(strings());
		}
		return node;
	}

	private Node strings() {
		Node node = new Node();
		switch (lookahead.tag) {
		case Tag.TRUE:
			match(Tag.TRUE);
			node.addChild(Tag.TRUE);
		case Tag.FALSE:
			match(Tag.FALSE);
			node.addChild(Tag.FALSE);
		case Tag.STRING:
			node.addChild(lookahead.lexeme);
			match(Tag.STRING);
		default:
			error("syntax error");
		}
		return node;
	}

	private Node name() {
		Node node = new Node();
		node.addChild(lookahead.lexeme);
		match(Tag.ID);
		return node;
	}

	private Node ints() {
		Node node = new Node();
		node.addChild(lookahead.lexeme);
		match(Tag.NUM);
		return node;
	}

	private Node floats() {
		Node node = new Node();
		node.addChild(lookahead.lexeme);
		match(Tag.REAL);
		return node;
	}

	private Node stmts() {
		Node node = new Node();
		switch (lookahead.tag) {
		case Tag.STDOUT:
			node.addChild(printstmts());
			break;
		case Tag.IF:
			node.addChild(ifstmts());
			break;
		case Tag.WHILE:
			node.addChild(whilestmts());
			break;
		case Tag.LET:
			node.addChild(letstmts());
			break;
		default:
			error("syntax error");
			break;
		}
		return node;
	}

	private Node printstmts() {
		Node node = new Node();
		match(Tag.LSB);
		node.addChild(Tag.LSB);
		match(Tag.STDOUT);
		node.addChild(Tag.STDOUT);
		node.addChild(oper());
		match(Tag.RSB);
		node.addChild(Tag.RSB);
		return node;
	}

	private Node ifstmts() {
		Node node = new Node();
		match(Tag.LSB);
		node.addChild(Tag.LSB);
		match(Tag.IF);
		node.addChild(Tag.IF);
		node.addChild(expr());
		node.addChild(expr());
		if (lookahead.tag != Tag.RSB) {
			node.addChild(expr());
		}
		match(Tag.RSB);
		node.addChild(Tag.RSB);
		return node;
	}

	private Node whilestmts() {
		Node node = new Node();

		match(Tag.LSB);
		node.addChild(Tag.LSB);
		match(Tag.WHILE);
		node.addChild(Tag.WHILE);
		node.addChild(expr());
		node.addChild(exprlist());
		match(Tag.RSB);
		node.addChild(Tag.RSB);
		return node;
	}

	private Node exprlist() { // TODO
		Node node = new Node();
		node.addChild(expr());
		if (lookahead.tag != Tag.RSB) {
			node.addChild(exprlist());
		}
		return node;
	}

	private Node letstmts() {
		Node node = new Node();
		match(Tag.LSB);
		node.addChild(Tag.LSB);
		match(Tag.LET);
		node.addChild(Tag.LET);
		match(Tag.LSB);
		node.addChild(Tag.LSB);
		node.addChild(varlist());
		match(Tag.RSB);
		node.addChild(Tag.RSB);
		match(Tag.RSB);
		node.addChild(Tag.RSB);
		return node;
	}

	private Node varlist() {
		Node node = new Node();
		match(Tag.LSB);
		node.addChild(Tag.LSB);
		node.addChild(name());
		node.addChild(type());
		match(Tag.RSB);
		node.addChild(Tag.RSB);
		if (lookahead.tag == Tag.LSB) {
			node.addChild(varlist());
		}
	}

	private Node type() {
		Node node = new Node();
		switch (lookahead.lexeme) {
		case "int":
			match(Tag.BASIC);
			node.addChild(Tag.TYPE_INT);
			break;
		case "string":
			match(Tag.BASIC);
			node.addChild(Tag.TYPE_STRING);
			break;
		case "float":
			match(Tag.BASIC);
			node.addChild(Tag.TYPE_FLOAT);
			break;
		case "bool":
			match(Tag.BASIC);
			node.addChild(Tag.TYPE_BOOL);
			break;
		default:
			error("syntax error");
			break;
		}

		return node;
	}
}
