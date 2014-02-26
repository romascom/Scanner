package lexer;

public class Num extends Token {
	//public final int value;
	public Num(int v) { super(Integer.toString(v), Tag.NUM); }
	public Num(String v) { super(v, Tag.NUM); }
	//public String toString() { return  "" + value; }
}