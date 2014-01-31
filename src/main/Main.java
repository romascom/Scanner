package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import lexer.Lexer;
//import lexer.Tag;
import lexer.Token;
import lexer.Word;

public class Main {
	public Main() {
	}

	public static void main(String[] args) {
		String filename = args[0];
		// String outputFilename = "stutest.out";
		Lexer lex = new Lexer();
		//Token eof = new Word("-1", Tag.EOF);
		//Token eof = new Token("-1", Tag.EOF);

		System.out.println("Hello World!");
		// TODO: Argument checking

		try {
			// Reassign the standard input stream to be a file
			System.setIn(new FileInputStream(filename));

			Token tok;
			/*do {
				tok = lex.scan();
				System.out
						.println("Tag: " + tok.tag + " Lexeme: " + tok.lexeme); // TODO:
																				// Print
																				// out
																				// tokens
				// TODO: Store tokens in data structure
			} while (!tok.equals(eof));*/

			while (true) {
				tok = lex.scan();
				if (tok == null) {
					System.err.println("Invalid lexeme");
					break;
				}
				if (tok.equals(Word.eof)) {
					System.err.println("Reached EOF");
					break;
				}
				System.out.println("<" + tok.tag + ", " + tok.lexeme + ">"); // TODO:
																				// Print
																				// out
																				// tokens
				// TODO: Store tokens in data structure
			}

			/*
			 * while (!lex.scan().equals(eof)) { System.out.println(); // TODO:
			 * Print out tokens // TODO: Store tokens in data structure }
			 */

		} catch (FileNotFoundException e) {
			// TODO
			System.err.println("Caught FileNotFoundException: "
					+ e.getMessage());
		} catch (IOException e) {
			// TODO
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}
}
