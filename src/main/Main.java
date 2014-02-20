package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import lexer.Lexer;
//import lexer.Tag;
//import lexer.Token;
//import lexer.Word;
import parser.*;
//import java.io.*;

public class Main {
	public Main() {
	}

	public static void main(String[] args) {
		System.err.println("START the program");//debug
		//String filename = args[0];
		Lexer lex = new Lexer();

		System.out.println("Hello World!");
		// TODO: Argument checking
		for (String path: args) {
			try {
				// Reassign the standard input stream to be a file
				System.err.println("The file being opened is " + path);//debug
				System.setIn(new FileInputStream(path));
				
				Parser parser = new Parser(lex);
				
				/*
				// Print lexer output
				Token tok;
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
					System.out.println("<" + tok.tag + ", " + tok.lexeme + ">");
				}*/

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
}
