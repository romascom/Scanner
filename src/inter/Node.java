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
	
	public void addChild(Token t) {
		//if(tag == 0) return;
		
		children[i] = new Node(t.tag, t.lexeme);
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
