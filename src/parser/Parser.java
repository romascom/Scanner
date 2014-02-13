package parser;

import java.io.IOException;

import lexer.*;
import inter.*;

public class Parser {		// see pg. 982 of the text
	private Lexer lex;		// this parser's lexical analyzer
	private Token lookahead;		// lookahead token
	//Env top = null;			// current or top symbol table
	//int used = 0;			// storage used for declarations
	
	public Parser(Lexer l) throws IOException { lex = l; nextToken(); }
	
	
	/**
	 * Sets the lookahead token to the next token scanned by the lexer.
	 * Consumes a token.
	 * @throws IOException
	 */
	void nextToken() throws IOException { lookahead = lex.scan(); }
	
	void error(String s) { throw new Error("near line " + lex.line + ": " + s); } // TODO: Define lex.line ... or not
	
	/**
	 * Ensures that t matches the lookahead token before consuming another token.
	 * @param t
	 * @throws IOException
	 */
	void match(int t) throws IOException {
		if( lookahead.tag == t ) {
			nextToken();
		} else {
			error("syntax error");
		}
	}
	
	private Node T() {
		Node root = new Node();
		match('['); root.addChild(Tag.LSB);
		root.addChild(S());
		match(']'); root.addChild(Tag.RSB);
		return root;
	}
	
	private Node S() {
		Node node = new Node();
		if ( lookahead.tag == Tag.LSB ) {
			match('['); node.addChild(Tag.LSB);
			if ( lookahead.tag != Tag.RSB ) {
				node.addChild(S());
			}
			match(']'); node.addChild(Tag.RSB);
		} else {
			node.addChild(expr()); 
		}
		node.addChild(SPrime());
		return node;
	}
	
	private Node SPrime() {
		Node node = new Node();
		
		if ( lookahead.tag == Tag.RSB ) { return null; }
		node.addChild(S()); 
		return node;
	}
}
