package inter;

import lexer.*;

public class Node {
	private Node[] children = new Node[5];
	private int tag = 0;
	private String lexeme = null;
	private int i = 0;
	
	public Node() {
	}
	
	public Node(int tag) {
		this.tag = tag;
	}
	
	public Node(String lexeme) {
		this.lexeme = lexeme;
	}
	
	public Node(int tag, String lexeme) {
		this.tag = tag;
		this.lexeme = lexeme;
	}
	
	/**
	 * Print a symbol tree, given the tree's root node, indenting according to
	 * depth.
	 * 
	 * @param node
	 *            The symbol tree's root node
	 * @param depth
	 *            The node's depth (root should be set to 0)
	 */
	public void printSymbolTree(int depth) {
		System.err.println("depth: " + depth);//debug
		if (this.lexeme != null) { // if node is a terminal
			System.err.println("We're going to print a terminal");//debug
			int d = depth;
			while (d != 0) { // indent according to appropriate depth
				System.out.print("  ");
				d--;
			}
			System.out.println(this.lexeme);
		}
		for (int j = 0; j < this.i; j++) { // for each of node's children
			if (this.children[j] == null) {
				System.err.println("Child " + j + " is null");//debug
			} else {
				this.children[j].printSymbolTree(++depth);
			}
		}
	}
	
	public String getLexeme() {
		return lexeme;
	}
	
	public Node getChild(int index) {
		return children[i];
	}
	
	public void addChild(int tag) {
		//if(tag == 0) return;
		
		children[i] = new Node(tag);
		i++;
		// TODO handle out of bounds exception
	}
	
	public void setTag(int tag) {
		this.tag = tag;
	}
	
	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}
	
	public void setChild(Node child, int i) {
		this.children[i] = child;
	}
	
	public void addChild(Token t) { //TODO maybe I need to pass the node in instead of the token
		//if(tag == 0) return;
		System.err.println("t.tag: " + t.tag);
		System.err.println("t.lexeme: " + t.lexeme);
		System.err.println("CHILD #" + i);
		Node child = new Node(t.tag, t.lexeme);
		//this.setChild(child, i);
		this.children[i] = child;
		System.err.println("children[" + i + "].lexeme = " + children[i].lexeme);//debug
		//children[i] = new Node(t.tag, t.lexeme);
		
		//children[i].setTag(t.tag);
		//children[i].setLexeme(t.lexeme);
		i++;
		// TODO handle out of bounds exception
	}
	
	public void addChild(String lexeme) {
		//if(tag == 0) return;
		
		children[i] = new Node(lexeme);
		i++;
		// TODO handle out of bounds exception
	}
	
	public void addChild(Node child) {
		//if(tag == 0) return;
		
		children[i] = child;
		i++;
		// TODO handle out of bounds exception
	}
}
