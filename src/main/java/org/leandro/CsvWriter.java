package org.leandro;

import java.io.PrintWriter;
import java.util.List;

public class CsvWriter extends OutputWriter {

	@Override
	public void printLines(PrintWriter pw, List<String> outputLines) {
		for (String line : outputLines) 
			pw.println(line);
		
	}

	@Override
	public void printHeader(PrintWriter pw) {
	}

	@Override
	public void printFooter(PrintWriter pw) {
	}

}
