package inter;

import lexer.*;

public class Node {
	private Node[] children = new Node[6];
	private int tag = 0;
	private String lexeme = null;
	private Token tok;
	private int i = 0;
	
	public Node() {
	}
	
	public Node(int tag, String lexeme) {
		this.tag = tag;
		this.lexeme = lexeme;
	}
	
	public Node(Token tok) {
		this.tok = tok;
	}
	
	/**
	 * Print a symbol tree, given the tree's root node, indenting according to
	 * depth.
	 * 
	 * @param node
	 *            The symbol tree's root node
	 * @param depth
	 *            The node's (root should be set to 0)
	 */
	public void printSymbolTree(int depth) {
		//System.err.println("depth: " + depth);//debug
		if (this.lexeme != null) { // if node is a terminal
			//System.err.println("We're going to print a terminal");//debug
			int d = depth;
			while (d != 0) { // indent according to appropriate depth
				System.out.print("  ");
				d--;
			}
			System.out.println(this.lexeme);
		}
		for (int j = 0; j < this.i; j++) { // for each of node's children
			int temp = depth;
			if (this.children[j] == null) {
				//System.err.println("Child " + j + " is null");//debug
			} else {
				this.children[j].printSymbolTree(++temp);
			}
		}
	}
	
	public String getLexeme() {
		return lexeme;
	}
	
	public int getTag() {
		return this.tag;
	}
	
	public void addChild(Token t) { //TODO maybe I need to pass the node in instead of the token
		System.err.println("Child added:");
		System.err.println("	t.tag: " + t.tag);
		System.err.println("	t.lexeme: " + t.lexeme);
		System.err.println("CHILD #" + i);
		Node child = new Node(t);
		child.tag = t.tag; // TODO remove this, eventually
		child.lexeme = t.lexeme;
		//Node child = new Node(t.tag, t.lexeme);
		this.children[i] = child;
		System.err.println("children[" + i + "].lexeme = " + children[i].lexeme);//debug
		i++;
	}
	
	public void addChild(Node child) {
		children[i] = child;
		i++;
	}
}
