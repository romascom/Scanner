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
		printSymbolTree(root, 0);
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
	 * Print a symbol tree, given the tree's root node, indenting according to
	 * depth.
	 * 
	 * @param node
	 *            The symbol tree's root node
	 * @param depth
	 *            The node's depth (root should be set to 0)
	 */
	private void printSymbolTree(Node node, int depth) {
		for (int i = 0; i < 5; i++) { // for each of node's children
			if (node.getLexeme() != null) { // if node is a terminal
				int d = depth;
				while (d != 0) { // indent according to appropriate depth
					System.out.println("  ");
					d--;
				}
				System.out.println(node.getLexeme());

			}
			printSymbolTree(node.getChild(i), depth++);
		}
	}

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
		if (lookahead.tag == t) {
			node.addChild(lookahead);
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
		match(Tag.LSB, root);
		root.addChild(S());
		match(Tag.RSB, root);
		return root;
	}

	private Node S() {
		Node node = new Node();
		if (lookahead.tag == Tag.LSB) {
			match(Tag.LSB, node);
			if (lookahead.tag != Tag.RSB) {
				node.addChild(S());
			}
			match(Tag.RSB, node);
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
			match(Tag.TRUE, node);
		case Tag.FALSE:
			match(Tag.FALSE, node);
		case Tag.STRING:
			match(Tag.STRING, node);
		default:
			error("syntax error");
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
