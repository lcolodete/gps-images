package org.leandro;

import java.io.PrintWriter;
import java.util.List;

public class HtmlWriter extends OutputWriter {

	@Override
	public void printHeader(PrintWriter pw) {
		pw.println("<!doctype html>");
		pw.println("<html lang=\"en\">");
		pw.println("<head>");
		pw.println("<meta charset=\"iso-8859-1\">");
		pw.println("<title>Images and Coordinates</title>");
		pw.println("</head>");
		
	}

	@Override
	public void printFooter(PrintWriter pw) {
		pw.println("</html>");
	}

	@Override
	public void printLines(PrintWriter pw, List<String> outputLines) {
		pw.println("<body>");
		for (String line : outputLines) {
			
			String[] parts = line.split(",");
			String imageFile = parts[0];
			String latitude = parts[1];
			String longitude = parts[2];
			
			pw.println("<div>");
			pw.println("<img src=\""+imageFile+"\" width=\"30%\" height=\"30%\">");
			pw.println("<span>");
			pw.println(latitude);
			pw.println("</span>");
			pw.println("<span>");
			pw.println(longitude);
			pw.println("</span>");
			pw.println("</div>");
			
		}
		pw.println("</body>");
	}



}
