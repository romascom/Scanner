package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import lexer.Lexer;
import java.io.File;
//import lexer.Tag;
//import lexer.Token;
//import lexer.Word;
import parser.*;

//import java.io.*;

public class Main {
	public Main() {
	}

	public static void main(String[] args) {
		System.err.println("START the program");// debug
		// String filename = args[0];
		Lexer lex = new Lexer();

		System.out.println("Hello World!");// debug
		// TODO: Argument checking

		System.err.println("args[0] is " + args[0]);// debug

		try {
			if (args[0].equals("-x")) {
				File dir = new File("."); // TODO allow user to specify the
											// directory
				for (File child : dir.listFiles()) {
					if (child.isFile() && child.getPath().endsWith(".txt")) {
						System.err.println("The file being opened is "
								+ child.getPath());// debug
						System.setIn(new FileInputStream(child.getPath()));

						Parser parser = new Parser(lex);
					}
				}
			} else {
				for (String path : args) {
					// Reassign the standard input stream to be a file
					System.err.println("The file being opened is " + path);// debug
					System.setIn(new FileInputStream(path));

					Parser parser = new Parser(lex);
				}
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
