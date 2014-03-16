package inter;

import java.util.*;
import lexer.*;

public class Node {
	private static HashMap<String, String> variables = new HashMap<String, String>();

	private Node[] children = new Node[6];
	private int tag = 0;
	private String lexeme = null;
	private Token tok;
	private int i = 0;
	private String type; // the variable type associated with this node
	// private boolean isNestedComp = false; // True if node is nested within a
	// node that has Gforth compilation semantics
	private boolean stringConcat = false;
	private boolean floatConversion = false; // flag for determining whether a
												// given number must be
												// converted to a float

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
		/*
		 * System.err.println("Child added:"); System.err.println("	t.tag: " +
		 * t.tag); System.err.println("	t.lexeme: " + t.lexeme);
		 * System.err.println("CHILD #" + i);
		 */
		Node child = new Node(t);
		child.tag = t.tag; // TODO remove this, eventually
		child.lexeme = t.lexeme;
		// Node child = new Node(t.tag, t.lexeme);
		this.children[i] = child;
		// System.err.println("children[" + i + "].lexeme = " +
		// children[i].lexeme);// debug
		i++;
	}

	public void addChild(Node child) {
		children[i] = child;
		i++;
	}

	public String getType() {
		return type;
	}

	public void setType(String t) {
		this.type = t; // should be “float” “int” “string” or “bool”
	}
	
	public static void traverseVarlist(Node parent) {
		String name = parent.children[1].children[0].lexeme;
		String type = parent.children[2].children[0].lexeme;
		System.out.print("create " + name + " ");
		variables.put(name, type);
		if (parent.children[4] != null) {
		traverseVarlist(parent.children[4]);
		}
	}

	/**
	 * Recursively traverses the supplied tree. Outputs gforth code.
	 * 
	 * @param parent
	 *            The node being traversed
	 * @param print
	 *            True if we want to print all of the tree's lexemes (excluding
	 *            square brackets); otherwise False
	 * @param inDef
	 *            True if the node being traversed is in a subtree, where the
	 *            root corresponds to a Gforth compile-only construct; otherwise
	 *            False
	 * @return
	 */
	public static Node traverse(Node parent, boolean print, boolean inDef) {
		boolean def = inDef;
		boolean willPrint = print;
		// System.err.println("parent.lexeme: " + parent.lexeme);// debug
		boolean unaryMinus = false;
		boolean floatFlag = false;
		for (int i = 0; i < 6; i++) { // Run through all children
			Node oper1;
			Node oper2;
			Node child = parent.children[i];

			if (child == null) {
				break;
			}
			// System.err.println("child.lexeme: " + child.lexeme);// debug
			// System.err.println("  child.tag: " + child.tag);// debug
			if (child.lexeme != null) {
				if (child.getTag() == Tag.MINUS) {
					if (parent.children[i + 2].lexeme == null) { // && lexeme !=
																	// Tag.RSB
						// is binary
					} else {
						// it’s unary
						// System.err.println("unaryMinus has been set to true");//
						// debug
						unaryMinus = true;
					}
				}
				if (unaryMinus && child.tok.isUnop()) { // Why do we need
														// isUnop() here?
					oper1 = traverse(parent.children[++i], true, def);
					if (oper1.tag == Tag.REAL) {
						floatFlag = true;
						System.out.print("f");
					}
					System.out.print("negate ");
					// System.out.print(child.lexeme + " ");
				} else if (child.tok.isBinop()) {
					// System.out.println("  This child is a binary operator");//
					// debug
					if (parent.children[i].stringConcat == true) { // concatenation
						oper1 = traverse(parent.children[++i], false, def);
						oper2 = traverse(parent.children[++i], false, def);
						if (willPrint == true) {
							System.out.print("s\" " + oper1.tok.lexeme
									+ "\" s\" " + oper2.tok.lexeme + "\" s+ ");
						}
						return new Node(Tag.STRING, null);
					}

					oper1 = traverse(parent.children[++i], willPrint, def);

					/*
					 * if (oper1.lexeme != null && willPrint == true) {
					 * System.out.print(oper1.lexeme + " "); }
					 */
					if (parent.children[i].floatConversion && willPrint == true) {
						System.out.print("s>f ");
					}

					oper2 = traverse(parent.children[++i], willPrint, def);
					/*
					 * if (oper2.lexeme != null && willPrint == true) {
					 * System.out.print(oper2.lexeme + " "); }
					 */
					if ((oper1.tag == Tag.REAL) && (oper2.tag != Tag.REAL)
							&& willPrint == true) {
						System.out.print("s>f ");
					}

					if ((oper1.tag == Tag.REAL) || (oper2.tag == Tag.REAL)) {
						floatFlag = true;
						if (willPrint == true) {
							System.out.print("f");
						}
					}

					if (willPrint == true) {
						if (child.tag == Tag.POW) {
							System.out.print("** ");
						} else if (child.tag == Tag.MOD) {
							System.out.print("mod ");
						} else {
							System.out.print(child.lexeme + " ");
						}
					}

				} else if (child.tok.isUnop()) {
					oper1 = traverse(parent.children[++i], true, def);
					if (oper1.tag == Tag.REAL) {
						floatFlag = true;
						System.out.print("f");
					}
					if (child.lexeme.equals("not")) {
						System.out.print("negate ");
					} else { // it's either sin, cos, or tan
						System.out.print(child.lexeme + " ");
					}

				} else if (child.tok.tag == Tag.IF) {
					// TODO: May need to ensure that definitions are unique
					if (def == false) {
						System.out.print(": def "); // define new word
						def = true;
					}
					traverse(parent.children[++i], true, def);
					System.out.print("if ");
					traverse(parent.children[++i], true, def);
					if (parent.children[++i].tag != Tag.RSB) { // if else stmt
																// exists
						System.out.print("else ");
						traverse(parent.children[i], true, def);
					}
					System.out.print("endif ");
					if (inDef == false) {
						System.out.print("; def "); // call new word
					}
				} else if (child.tag == Tag.LET) {
					i = i + 2;
					traverseVarlist(parent.children[i]);
				} else if (child.tok.tag == Tag.ID) {
					System.out.print("create " + child.tok.lexeme + " ");
					variables.put(child.tok.lexeme, "");
					System.err.println(i);
					//System.err.println(parent.children[2].lexeme);
					traverse(parent.children[++i], true, def);
					i++; // skip RSB
					if (parent.children[++i] != null) {
						traverse(parent.children[i], false, def);
					}
					//return child;
				} else if (child.tok.tag == Tag.BASIC) {
					if (child.lexeme.equals("int")
							|| child.lexeme.equals("float")
							|| child.lexeme.equals("bool")
							|| child.lexeme.equals("string")) {
						variables.put(parent.children[i - 1].tok.lexeme,
								child.lexeme);
					}
					//return child;
				} else if (child.tok.tag == Tag.STDOUT) {
					oper1 = traverse(parent.children[++i], false, def);
					switch (oper1.tag) {
					case Tag.STRING:
						// System.out.print(".\" " + oper1.lexeme + "\" ");
						// System.out.print("s\" " + oper1.lexeme + "\" type ");
						if (oper1.lexeme == null) {
							traverse(parent.children[i], true, def);
						} else {
							System.out.print("s\" " + oper1.lexeme + "\" ");
						}
						System.out.print("type ");
						break;
					case Tag.REAL:
						traverse(parent.children[i], true, def);
						System.out.print("f. ");
						break;
					case Tag.NUM:
						traverse(parent.children[i], true, def);
						System.out.print(". ");
						break;
					default:
						traverse(parent.children[i], true, def);
						/*
						 * if (oper1.lexeme != null) {
						 * System.out.print(oper1.lexeme + " "); }
						 */
						// System.out.print(". ");
						System.out.print(". ");
						break;
					}
				} else if (child.tok.isConstant()) {
					/* If we have a Constant pass it up the tree */
					// System.err.println("Child is constant");// debug
					if (willPrint == true) {
						System.out.print(child.lexeme);
					}
					if (child.tok.tag == Tag.REAL
							&& !child.tok.lexeme.contains("e")) {
						System.out.print("e");
					}

					System.out.print(" ");
					return child;
				}
			} /*else if (child.children[0].tok.tag != 0) {
				if (child.children[0].tok.tag == Tag.ID || child.children[0].tok.tag == Tag.BASIC) {
					
				} else {
					return traverse(child, willPrint, def);
				}
			}*/ else {
				// System.err.println("  This child is a nonterminal");// debug
				return traverse(child, willPrint, def);
			}
		}

		if (floatFlag) {
			// System.err.println("This is a float");//debug
			Node node = new Node(Tag.REAL, null);
			return node;
		} else {
			Node node = new Node(0, null);
			return node;
		}
		// return null; // added to stop eclipse from complaining; this line
		// should
		// never be reached
	}

	public void setFloatConversion() {
		this.floatConversion = true;
	}

	public void setStringConcat() {
		this.stringConcat = true;
	}

	/**
	 * Recursively marks the nodes of the supplied tree if either their lexemes
	 * need to be converted to floating point values or the two operands are
	 * strings that need to be concatenated.
	 * 
	 * @param parent
	 *            The supplied tree's root node
	 * @return
	 */
	public static Node floatConverter(Node parent) {
		boolean unaryMinus = false;
		boolean floatFlag = false;
		for (int i = 0; i < 6; i++) { // Run through all children
			Node oper1;
			Node oper2;
			Node child = parent.children[i];
			if (child == null) {
				break;
			}
			if (child.lexeme != null) {
				if (child.getTag() == Tag.MINUS) {
					if (parent.children[i + 2].tag == 0) {
						// is binary
					} else {
						// it’s unary
						unaryMinus = true;
					}
				}
				if (unaryMinus && child.tok.isUnop()) {
					oper1 = floatConverter(parent.children[++i]);
					if (oper1.tag == Tag.REAL) {
						floatFlag = true;
					}
				} else if (child.tok.isBinop()) {
					oper1 = floatConverter(parent.children[++i]);
					oper2 = floatConverter(parent.children[++i]);

					if ((oper1.tag != Tag.REAL) && (oper2.tag == Tag.REAL)) {
						parent.children[i - 1].setFloatConversion();
					}
					if ((oper1.tag == Tag.REAL) || (oper2.tag == Tag.REAL)) {
						floatFlag = true;
					}
					// type check
					if ((oper1.tag == Tag.REAL) && (oper2.tag == Tag.STRING)) {
						System.err
								.println("A binary operation cannot be performed on a float and a string");
						System.exit(1);
					}
					if (oper1.tok != null && oper2.tok != null) {
						if (child.tok.tag == Tag.PLUS
								&& oper1.tok.tag == Tag.STRING
								&& oper2.tok.tag == Tag.STRING) { // concatenation
							parent.children[i - 2].setStringConcat();
						}
					}

				} else if (child.tok.isUnop()) {
					oper1 = Node.floatConverter(parent.children[++i]);
					if (oper1.tag == Tag.REAL) {
						floatFlag = true;
					}

				} else if (child.tok.isConstant()) {
					/* If we have a Constant pass it up the tree */
					return child;

				}
			} else {
				return floatConverter(child);
			}
		}

		if (floatFlag) {
			Node node = new Node(Tag.REAL, null);
			return node;
		} else {
			Node node = new Node(0, null);
			return node;
		}
		// return node;
	}
}