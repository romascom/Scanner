package inter;

public class Node {
	Node[] children = new Node[5];
	int tag;
	int i = 0;
	
	public Node() {
	}
	
	public Node(int tag) {
		this.tag = tag;
	}
	
	public void addChild(int tag) {
		//if(tag == 0) return;
		
		children[i] = new Node(tag);
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
