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
		// System.err.println("depth: " + depth);//debug
		if (this.lexeme != null) { // if node is a terminal
			// System.err.println("We're going to print a terminal");//debug
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
				// System.err.println("Child " + j + " is null");//debug
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

	public void addChild(Token t) { // TODO maybe I need to pass the node in
									// instead of the token
		System.err.println("Child added:");
		System.err.println("	t.tag: " + t.tag);
		System.err.println("	t.lexeme: " + t.lexeme);
		System.err.println("CHILD #" + i);
		Node child = new Node(t);
		child.tag = t.tag; // TODO remove this, eventually
		child.lexeme = t.lexeme;
		// Node child = new Node(t.tag, t.lexeme);
		this.children[i] = child;
		System.err
				.println("children[" + i + "].lexeme = " + children[i].lexeme);// debug
		i++;
	}

	public void addChild(Node child) {
		children[i] = child;
		i++;
	}

	public static Node traverse(Node parent) {
		System.err.println("parent.lexeme: " + parent.lexeme);//debug
		boolean unaryMinus = false;
		for (int i = 0; i < 6; i++) { // Run through all children
			Node oper1;
			Node oper2;
			Node child = parent.children[i];
			
			if (child == null) {
				break;
			}
			System.err.println("child.lexeme: " + child.lexeme);//debug
			if (child.lexeme != null) {
				if (child.getTag() == Tag.MINUS) {
					if (parent.children[i + 2].lexeme == null) { // && lexeme != Tag.RSB
						// is binary
					} else {
						// it’s unary
						unaryMinus = true;
					}
				} if (unaryMinus && child.tok.isUnop()) {
					oper1 = traverse(parent.children[++i]);
					if (oper1.tag == Tag.REAL) {
						System.out.print("f");
					}

					System.out.print(child.lexeme + " ");
				} else if (child.tok.isBinop()) {
					System.out.println("  This child is a binary operator");//debug
					oper1 = traverse(parent.children[++i]);
					oper2 = traverse(parent.children[++i]);

					System.out.print(oper1.lexeme + " ");
					if ((oper1.tag != Tag.REAL) && (oper2.tag == Tag.REAL)) {
						System.out.print("s>f ");
					}

					System.out.print(oper2.lexeme + " ");
					if ((oper1.tag == Tag.REAL) && (oper2.tag != Tag.REAL)) {
						System.out.print("s>f ");
					}

					if ((oper1.tag == Tag.REAL) || (oper2.tag == Tag.REAL)) {
						System.out.print("f");
					}
					System.out.print(child.lexeme + " ");

				} else if (child.tok.isConstant()) {
					/* If we have a Constant pass it up the tree */
					return child;
				}
			} else {
				System.err.println("  This child is a nonterminal");//debug
				return traverse(child);
			}
		}
		return null; // added to stop eclipse from complaining; this line should never be reached
	}

}