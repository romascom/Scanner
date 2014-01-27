package lexer;

import java.io.*;
import java.util.*;
import symbols.*;

/**
 * @author Robert Partch and Matthew Romasco
 * 
 */
public class Lexer {
	public static int line = 1;
	char peek = ' ';

	// Create a string table to map lexemes to tokens
	Hashtable words = new Hashtable();

	void reserve(Word w) {
		words.put(w.lexeme, w);
	}

	/*
	 * Reserve selected keywords
	 */
	public Lexer() {
		reserve(new Word("if", Tag.IF));
		reserve(new Word("else", Tag.ELSE));
		reserve(new Word("while", Tag.WHILE));
		reserve(new Word("do", Tag.DO));
		reserve(new Word("break", Tag.BREAK));
		reserve(Word.True);
		reserve(Word.False);
		reserve(Type.Int);
		reserve(Type.Char);
		reserve(Type.Bool);
		reserve(Type.Float);
	}

	/**
	 * Sets peek to the next byte of data from the input stream.
	 * 
	 * @throws IOException
	 */
	void readch() throws IOException {
		peek = (char) System.in.read();
	}

	/**
	 * Checks whether the given character equals the next byte of data from the
	 * input stream.
	 * 
	 * @param c
	 *            The given character
	 * @return True if the given character equals the next byte of data from the
	 *         input stream; false,
	 * @throws IOException
	 */
	boolean readch(char c) throws IOException {
		readch();
		if (peek != c) {
			return false;
		} else {
			peek = ' ';
			return true;
		}
	}

	/**
	 * Recognizes numbers, identifiers, and reserved words.
	 * 
	 * @return Next token
	 * @throws IOException
	 */
	public Token scan() throws IOException {
		// Skip white space
		for (;; readch()) {
			if (peek == ' ' || peek == '\t')
				continue;
			else if (peek == '\n')
				line = line + 1;
			else
				break;
		}

		// Recognize composite tokens (e.g. <=)
		switch (peek) {
		case '&':
			if (readch('&'))
				return Word.and;
			else
				return new Token('&');
		case '|':
			if (readch('|'))
				return Word.or;
			else
				return new Token('|');
		case '=':
			if (readch('='))
				return Word.eq;
			else
				return new Token('=');
		case '!':
			if (readch('='))
				return Word.ne;
			else
				return new Token('!');
		case '<':
			if (readch('='))
				return Word.le;
			else
				return new Token('<');
		case '>':
			if (readch('='))
				return Word.ge;
			else
				return new Token('>');
		}

		// Recognize numbers (e.g. 365 and 3.14)
		if (Character.isDigit(peek)) {
			// Get integer part
			int v = 0;
			do {
				v = 10 * v + Character.digit(peek, 10);
				readch();
			} while (Character.isDigit(peek));
			if (peek != '.') { // If an integer
				return new Num(v);
			} else { // Get fraction part
				float x = v;
				float d = 10;
				for (;;) {
					readch();
					if (!Character.isDigit(peek))
						break;
					x = x + Character.digit(peek, 10) / d;
					d = d * 10;
				}
				return new Real(x);
			}
		}

		// Collect word
		if (Character.isLetter(peek)) {
			// Get string representation of next lexeme
			StringBuffer b = new StringBuffer();
			do {
				b.append(peek);
				readch();
			} while (Character.isLetterOrDigit(peek));
			String s = b.toString();

			Word w = (Word) words.get(s);
			if (w != null) { // If lexeme is in symbol table
				return w;
			} else { // Lexeme must be an identifier and should be added to the
						// symbol table
				w = new Word(s, Tag.ID);
				words.put(s, w);
				return w;
			}
		}

		// Return any remaining characters as tokens
		Token tok = new Token(peek);
		peek = ' ';
		return tok;
	}
}
