import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileFormatter {
	private static Scanner sc;

	public static void main(String[] args) {
		// This will be user input from the GUI
		String newFileName = "output";
		String oldFileName = "input";
		String justified = "left";

		File oldFile = new File(oldFileName);
		File newFile = new File(newFileName);

		// write from file to file
		writeFile(oldFile, newFileName, justified);

		// print analysis report
		analysisReport(oldFile, newFile);

		sc.close();
	}

	// print analysis report
	public static void analysisReport(File oldFile, File newFile) {
		int words = wordCount(newFile);
		int lines = lineCount(newFile);
		int wordsPerLine = 0;
		int avgLineLength = 0;

		if (lines != 0)
		{
			wordsPerLine = words / lines;
			avgLineLength = totalLineLength(newFile) / lines;
		}

		System.out.println("Analysis Report");
		System.out.println("--------------------------");
		System.out.println(words + "\twords processed.");
		System.out.println(lines + "\tlines processed.");
		System.out.println(blankLines(oldFile) + "\tblank lines removed.");
		System.out.println(wordsPerLine + "\taverage words per line.");
		System.out.println(avgLineLength + "\taverage line length.");
	}

	// writes from file to file
	public static void writeFile(File inputFile, String newFileName, String justification) {
		CreateFile newFile = new CreateFile();
		newFile.openFile(newFileName);

		String next, line;
		next = line = "";

		try {
			sc = new Scanner(inputFile);
			while (sc.hasNext()) {
				next = sc.next();
				if (line.length() + next.length() <= 80) // add word
					line += next + " ";
				else { // add line to text
					newFile.writeToFile(line.trim() + "\r", justification);
					line = next + " ";
				}
			}
			newFile.writeToFile(line.trim(), justification);
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
		newFile.closeFile();
	}

	// count the length of each line and divide by the number of lines
	public static int totalLineLength(File file) {
		int totalLineLength = 0;
		try {
			sc = new Scanner(file);

			while (sc.hasNextLine())
				totalLineLength += sc.nextLine().trim().length();
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
		return totalLineLength;
	}

	// count blank lines in a given file
	public static int blankLines(File file) {
		int count = 0;

		try {
			sc = new Scanner(file);
			while (sc.hasNextLine())
				if ("".equals(sc.nextLine()))
					count++;
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
		return count;
	}

	// counts the words in a given file
	public static int wordCount(File file) {
		int count = 0;
		try {
			sc = new Scanner(file);

			while (sc.hasNext()) {
				count++;
				sc.next();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
		return count;
	}

	// counts the lines in a given file
	public static int lineCount(File file) {
		int count = 0;
		try {
			sc = new Scanner(file);

			while (sc.hasNextLine()) {
				count++;
				sc.nextLine();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
		return count;
	}
}