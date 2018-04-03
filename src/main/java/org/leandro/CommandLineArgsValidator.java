package org.leandro;

import java.io.File;

public class CommandLineArgsValidator {

	private String[] args;
	
	private String inputDir;
	
	private String outputFormat;
	
	public void setArguments(String[] args) {
		this.args = args;
	}
	
	public void execute() {
		if (args != null && args.length > 0) {
			inputDir = args[0];
			
			File dir = new File(inputDir);
			
			if (!dir.isDirectory()) {
				System.out.println("Invalid directory : "+inputDir);
				System.out.println("Aborting...");
				System.exit(-1);
			}
			
			if (args.length > 1) {
				outputFormat = args[1];
				if (!outputFormat.equalsIgnoreCase(GpsImageUtility.OUTPUT_FORMAT_CSV) && 
					!outputFormat.equalsIgnoreCase(GpsImageUtility.OUTPUT_FORMAT_HTML)) {
					System.out.println("Warning: Unknown output format. Will use csv");
					outputFormat = GpsImageUtility.OUTPUT_FORMAT_CSV;
				}
			} else {
				System.out.println("Warning: No output format specified. Will use csv");
				outputFormat = GpsImageUtility.OUTPUT_FORMAT_CSV;
			}
		} else {
			System.out.println("Warning: No directory specified. Will use current directory");
			
			inputDir = System.getProperty("user.dir");
			outputFormat = GpsImageUtility.OUTPUT_FORMAT_CSV;
		}
	}
	
	public String getInputDir() {
		return inputDir;
	}
	
	public String getOutputFormat() {
		return outputFormat;
	}
}
