package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import lexer.Lexer;
import lexer.Token;

public class Main {
	public Main() {
	}

	public static void main(String[] args) {
		String filename = args[0];
		//String outputFilename = "stutest.out";
		Lexer lex = new Lexer();
		Token eof = new Token(-1);

		// TODO: Argument checking

		try {
			// Reassign the standard input stream to be a file
			System.setIn(new FileInputStream(filename));

			while (!lex.scan().equals(eof)) {
				// TODO: Print out tokens
				// TODO: Store tokens in data structure
			}

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
