package org.leandro;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;

/**
 * Utility class that reads all the images in a given directory, <br/>
 * extracts their EXIF GPS data (longitude and latitude), and then writes <br/>
 * the name of the image and the coordinates for each image to a CSV file.
 * 
 * It takes a directory as a parameter, and defaults to the current directory if none is specified.
 * <br/>
 * <br/>
 * Usage:<br/>
 * - first argument : full directory path<br/>
 * - second argument (optional) : output format. It accepts CSV (default) or HTML.
 * 
 * @author leandro
 *
 */
public class GpsImageUtility {

	public static final String OUTPUT_FORMAT_CSV = "csv";
	public static final String OUTPUT_FORMAT_HTML = "html";
	
	private String inputDir;
	private String outputFormat;
	private List<String> outputLines = new ArrayList<>();
	
	public static void main(String[] args) {
		CommandLineArgsValidator argsValidator = new CommandLineArgsValidator();
		argsValidator.setArguments(args);
		argsValidator.execute();
		
		String inputDir = argsValidator.getInputDir();
		String outputFormat = argsValidator.getOutputFormat();
		
		GpsImageUtility gpsImageUtility = new GpsImageUtility(inputDir, outputFormat);
		gpsImageUtility.execute();
	}

	public GpsImageUtility(String inputDir, String outputFormat) {
		this.inputDir = inputDir;
		this.outputFormat = outputFormat;
	}
	
	private void visitFiles() {
		System.out.println("Reading files in the following directory : "+inputDir);
		
		Path inputPath = Paths.get(inputDir);
		
		try {
			Files.walkFileTree(inputPath, new GpsImageFileVisitor(outputLines, inputPath));
		} catch (IOException e) {
			System.out.println("Error while reading files");
			e.printStackTrace();
			System.out.println("Aborting...");
			System.exit(-2);
		}
		
	}
	
	private void writeOutputFile() {
		String fileName = "images_and_coordinates."+outputFormat;

		OutputWriter writer = null;
		if (OUTPUT_FORMAT_CSV.equalsIgnoreCase(outputFormat)) {
			writer = new CsvWriter();
		} else if (OUTPUT_FORMAT_HTML.equalsIgnoreCase(outputFormat)) {
			writer = new HtmlWriter();
		}
		writer.writeOutputFile(fileName, outputLines);
	}
	
	/**
	 * Basic algorithm:<br/>
	 * 1) visit all files in all subdirectories<br/>
	 * 2) for each image file, read its GPS attributes<br/>
	 * 3) write to output file  
	 */
	private void execute() {
		visitFiles();
		writeOutputFile();
	}
}

class GpsImageFileVisitor implements FileVisitor<Path> {

	private List<String> outputLines;
	private Path inputPath;
	
	public GpsImageFileVisitor(List<String> outputLines, Path inputPath) {
		this.outputLines = outputLines;
		this.inputPath = inputPath;
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		String fileName = inputPath.relativize(file).toString();
						
		if (!fileName.contains(".DS_Store")) {

			try {
				Metadata metadata = ImageMetadataReader.readMetadata(file.toFile());
				GpsDirectory gpsDir = metadata.getFirstDirectoryOfType(GpsDirectory.class);
				if (gpsDir != null) {
					
					StringBuilder sb = new StringBuilder();
					sb.append(fileName)
						.append(",")
						.append(gpsDir.getDescription(GpsDirectory.TAG_LATITUDE).replaceAll(",", "."))
						.append(",")
						.append(gpsDir.getDescription(GpsDirectory.TAG_LONGITUDE).replaceAll(",", "."));
					outputLines.add(sb.toString());
				}
				
			} catch (ImageProcessingException e) {
				System.out.println("Exception visiting file : "+file.getFileName());
				e.printStackTrace();
			}
			
		}
		
		
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		System.out.println("Failure visiting file : "+file.getFileName());
		exc.printStackTrace();
		return FileVisitResult.TERMINATE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}
	
}