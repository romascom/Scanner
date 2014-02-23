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
		System.err.println("Tree has been created");
		root.printSymbolTree(0);
	}

	/**
	 * Sets the lookahead tokens to the next two tokens scanned by the lexer.
	 * Consumes two tokens.
	 * 
	 * @throws IOException
	 */
	void nextToken() throws IOException {
		System.err.println("nextToken() was called");// debug
		if (lookahead == null) {
			System.err.println("lookahead is null");// debug
			lookahead = lex.scan();
			System.err.println("lookahead.lexeme is " + lookahead.lexeme);//debug
		} else {
			lookahead = lookaheadTwo;
		}

		lookaheadTwo = lex.scan();

		System.err.println("lookahead.lexeme is " + lookahead.lexeme);//debug
		System.err.println("lookahead.tag is " + lookahead.tag);//debug
		System.err.println("\nlookaheadTwo.lexeme is " +
		lookaheadTwo.lexeme);//debug
		System.err.println("\nlookaheadTwo.tag is " +
		lookaheadTwo.tag);//debug
	}

	void error(String s) {
		System.err.println(s);
		//new Exception().printStackTrace();// debug
		System.exit(1);
		// throw new Error("near line " + lex.line + ": " + s);
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
				System.err.println(e.getMessage());
				System.exit(1);
			}
		} else {
			error("syntax error");
		}
	}

	/**
	 * If the supplied tag matches the first lookahead, consume the first
	 * lookahead and add it to supplied node's list of children.
	 * 
	 * @param t
	 *            The supplied tag.
	 * @param node
	 * @throws IOException
	 */
	void match(int t, Node node) {
		System.err.println("Tag to match: " + t);// debug
		if (lookahead.tag == Tag.EOF) {
			return;
		}
		if (lookahead.tag == t) {
			node.addChild(lookahead);
			try {
				nextToken();
			} catch (IOException e) {
				System.err.println(e.getMessage());
				System.exit(1);
			}
		} else {
			error("syntax error: " + lookahead.tag + " != " + t);
		}
	}

	private Node T() {
		Node root = new Node();
		match(Tag.LSB, root);
		root.addChild(S());
		match(Tag.RSB, root);
		// System.err.println(root.getChild(0).getLexeme());//debug
		return root;
	}

	private Node S() {
		Node node = new Node();
		if (lookahead.tag == Tag.LSB) {
			if (ParserHelper.isLSBExpr(lookaheadTwo)) {
				node.addChild(expr());
			} else {
				match(Tag.LSB, node);
				if (lookahead.tag != Tag.RSB) {
					node.addChild(S());
				}
				match(Tag.RSB, node);
			}
		} else {
			node.addChild(expr());
		}
		node.addChild(SPrime());
		return node;
	}

	private Node SPrime() { // S' -> ] S
		Node node = new Node();
		if (lookahead.tag != Tag.RSB && lookahead.tag != Tag.EOF) {
		node.addChild(S()); node.addChild(SPrime());
		}
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
			match(Tag.LSB, node);
			if (lookahead.tag == Tag.ASSIGN) {
				match(Tag.ASSIGN, node);
				node.addChild(oper());
				node.addChild(oper());
			} else if (lookahead.tag == Tag.MINUS) {
				match(Tag.MINUS, node);
				if (lookaheadTwo.tag != Tag.RSB) {
					node.addChild(oper());
				}
				node.addChild(oper());
			} else if (ParserHelper.isBinop(lookahead)) {
				match(lookahead.tag, node);
				node.addChild(oper());
				node.addChild(oper());
			} else if (ParserHelper.isUnop(lookahead)) {
				match(lookahead.tag, node);
				node.addChild(oper());
			}
			match(Tag.RSB, node);
		}

		return node;
	}

	private Node constants() { // constants -> ints | floats | strings
		Node node = new Node();
		switch (lookahead.tag) {
		case Tag.NUM:
			node.addChild(ints());
			break;
		case Tag.REAL:
			node.addChild(floats());
			break;
		default:
			node.addChild(strings());
			break;
		}
		return node;
	}

	private Node strings() {
		Node node = new Node();
		switch (lookahead.tag) {
		case Tag.TRUE:
			match(Tag.TRUE, node);
			break;
		case Tag.FALSE:
			match(Tag.FALSE, node);
			break;
		case Tag.STRING:
			match(Tag.STRING, node);
			break;
		default:
			error("syntax error");
			break;
		}
		return node;
	}

	private Node name() {
		Node node = new Node();
		match(Tag.ID, node);
		return node;
	}

	private Node ints() {
		Node node = new Node();
		match(Tag.NUM, node);
		return node;
	}

	private Node floats() {
		Node node = new Node();
		match(Tag.REAL, node);
		return node;
	}

	private Node stmts() {
		Node node = new Node();
		switch (lookaheadTwo.tag) {
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
		match(Tag.LSB, node);
		match(Tag.STDOUT, node);
		node.addChild(oper());
		match(Tag.RSB, node);
		return node;
	}

	private Node ifstmts() {
		Node node = new Node();
		match(Tag.LSB, node);
		match(Tag.IF, node);
		node.addChild(expr());
		node.addChild(expr());
		if (lookahead.tag != Tag.RSB) {
			node.addChild(expr());
		}
		match(Tag.RSB, node);
		return node;
	}

	private Node whilestmts() {
		Node node = new Node();

		match(Tag.LSB, node);
		match(Tag.WHILE, node);
		node.addChild(expr());
		node.addChild(exprlist());
		match(Tag.RSB, node);
		
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
		match(Tag.LSB, node);
		match(Tag.LET, node);
		match(Tag.LSB, node);
		node.addChild(varlist());
		match(Tag.RSB, node);
		match(Tag.RSB, node);
		return node;
	}

	private Node varlist() {
		Node node = new Node();
		match(Tag.LSB, node);
		node.addChild(name());
		node.addChild(type());
		match(Tag.RSB, node);
		if (lookahead.tag == Tag.LSB) {
			node.addChild(varlist());
		}
		return node;
	}

	private Node type() {
		Node node = new Node();
		switch (lookahead.lexeme) {
		case "int":
			match(Tag.BASIC, node);
			break;
		case "string":
			match(Tag.BASIC, node);
			break;
		case "float":
			match(Tag.BASIC, node);
			break;
		case "bool":
			match(Tag.BASIC, node);
			break;
		default:
			error("syntax error");
			break;
		}

		return node;
	}
}
