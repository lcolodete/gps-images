package org.leandro;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public abstract class OutputWriter {

	public void writeOutputFile(String fileName, List<String> outputLines) {
		
		try (PrintWriter pw = new PrintWriter(fileName)) {
			
			printHeader(pw);

			printLines(pw, outputLines);
			
			printFooter(pw);
			
		} catch (FileNotFoundException e) {
			System.out.println("Error creating output file");
			e.printStackTrace();
			System.out.println("Aborting...");
			System.exit(-3);
		}



		
	}

	public abstract void printHeader(PrintWriter pw);
	
	public abstract void printFooter(PrintWriter pw);
	
	public abstract void printLines(PrintWriter pw, List<String> outputLines);
}
