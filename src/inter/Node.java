package inter;

public class Node {
	Node[] children = new Node[5];
	int tag;
	String lexeme;
	int i = 0;
	
	public Node() {
	}
	
	public Node(int tag) {
		this.tag = tag;
	}
	
	public Node(String lexeme) {
		this.lexeme = lexeme;
	}
	
	public void addChild(int tag) {
		//if(tag == 0) return;
		
		children[i] = new Node(tag);
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
