package parser;

import java.io.IOException;

import lexer.*;
import inter.*;

public class Parser { // see pg. 982 of the text
	private Lexer lex; // this parser's lexical analyzer
	private Token lookahead; // lookahead token

	// Env top = null; // current or top symbol table
	// int used = 0; // storage used for declarations

	public Parser(Lexer l) throws IOException {
		lex = l;
		nextToken();
	}

	/**
	 * Sets the lookahead token to the next token scanned by the lexer. Consumes
	 * a token.
	 * 
	 * @throws IOException
	 */
	void nextToken() throws IOException {
		lookahead = lex.scan();
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

		if (ParserHelper.isStmt(lookahead)) {
			node.addChild(stmts());
		} else {
			node.addChild(oper());
		}
		return node;
	}

	private Node stmts() {
		Node node = new Node();
		switch ( lookahead.tag ) {
		case Tag.:
			printstmts();
			break;
		case ifstmts:
			ifstmts();
			break;
		case whilestmts:
			whilestmts();
			break;
		case letstmts:
			letstmts();
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
