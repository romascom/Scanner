package lexer;

/*
 * For floating point numbers
 */
public class Real extends Token {
	//public final float value;

	public Real(float v) {
		super(Float.toString(v), Tag.REAL);
		//value = v;
	}
	
	public Real(String v) {
		super(v, Tag.REAL);
		//value = v;
	}

	/*public String toString() {
		return "" + value;
	}*/
}
