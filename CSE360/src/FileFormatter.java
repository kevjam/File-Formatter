import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileFormatter {
	private static Scanner sc;

	public static void main(String[] args) {
		// This will be user input from the GUI
		String oldFileName = "input";
		String newFileName = "output";
		String justified = "right";

		// read file to string
		String fileText = readFile(oldFileName);
		
		// get blank lines in the file
		int blankLines = blankLines(fileText);

		// merge lines and reduce them to <= 80 char
		fileText = reduceLineLength(fileText);
		
		// write reduced text to file
		writeFile(fileText, newFileName, justified);

		// everything below will be part of the analysis report
		int words = wordCount(fileText);
		int lines = lineCount(fileText);
		double avgLineLength = avgLineLength(fileText, lines);
		System.out.println("Analysis Report");
		System.out.println("Word count: " + words);
		System.out.println("Line count: " + lines);
		System.out.println("Blank lines removed: " + blankLines);
		System.out.println("Average words per line: " + words / lines);
		System.out.println("Average line length: " + avgLineLength);
		sc.close();
	}

	public static void writeFile(String text, String newFileName, String justified) {
		CreateFile newFile = new CreateFile();
		newFile.openFile(newFileName);

		sc = new Scanner(text);
		while (sc.hasNextLine())
			newFile.writeToFile(sc.nextLine() + "\r", justified);

		newFile.closeFile();
	}

	public static String readFile(String fileName) {
		String text = "";

		try {
			File file = new File(fileName);
			sc = new Scanner(file);
			while (sc.hasNextLine())
				text += sc.nextLine() + "\r";
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
		return text;
	}

	public static String reduceLineLength(String text) {
		String next, line, newText;
		line = next = newText = "";
		sc = new Scanner(text);

		while (sc.hasNext()) {
			next = sc.next();
			if (line.length() + next.length() <= 80) // add word
				line += next + " ";
			else { // add line to text
				newText += line.trim() + "\r";
				line = next + " ";
			}
		}
		return newText += line;
	}
	
	public static double avgLineLength(String text, int lines) {
		double avgLineLength = 0;
		if (lines == 0)
			avgLineLength = 0;
		else {
			sc = new Scanner(text);

			while (sc.hasNextLine())
				avgLineLength += sc.nextLine().length();
			
			avgLineLength /= lines;
		}
		return avgLineLength;
	}

	public static int blankLines(String text) {
		int count = 0;
		sc = new Scanner(text);

		while (sc.hasNextLine())
			if ("".equals(sc.nextLine()))
				count++;

		return count;
	}

	public static int wordCount(String text) {
		int count = 0;
		sc = new Scanner(text);

		while (sc.hasNext()) {
			count++;
			sc.next();
		}

		return count;
	}

	public static int lineCount(String text) {
		int count = 0;
		sc = new Scanner(text);

		while (sc.hasNextLine()) {
			count++;
			sc.nextLine();
		}

		return count;
	}
}