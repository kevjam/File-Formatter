import java.util.Formatter;

public class CreateFile {
	private Formatter formatter;

	public boolean openFile(String fileName) {
		boolean successful = true;
		try {
			formatter = new Formatter(fileName);
		} catch (Exception e) {
			successful = false;
		}
		return successful;
	} // End of openFile function

	public void writeToFile(String line, boolean newline, String justified, String spacing, int linelength) {
		if (justified.equals("right"))
			writeRightJustified(line, newline, spacing, linelength);
		else if (justified.equals("left"))
			writeLeftJustified(line, spacing, newline);
		else
			writeFullJustified(line, newline, spacing, linelength);
	} // End of writeToFile method

	public void writeLeftJustified(String line, String spacing, boolean newline) {

		if (newline) {

			if (spacing.equals("single")) {
				formatter.format("%s%n", line);

			} else {
				formatter.format("%s%n%n", line);
			}
			// Else add the new line to the end of the current line
		} else {
			formatter.format("%s", line);
		} // End of outer if else statement
	} // End of writeLeftJustified

	public void writeRightJustified(String line, boolean newline, String spacing, int linelength) {

		if (newline) {

			if (spacing.equals("single")) {
				formatter.format("%" + linelength + "s%n", line);

			} else {
				formatter.format("%" + linelength + "s%n%n", line);
			}
		} else {
			formatter.format("%" + linelength + "s", line);
		}
	} // End of writeRightJustified method

	public void writeFullJustified(String line, boolean newline, String spacing, int linelength) {
		// Inserts spaces between words until the line length is reached
		if (newline && line.contains(" "))
			while (line.length() < linelength) {
				for (int i = 0; i < line.length() && line.length() < linelength; i++)
					if (line.charAt(i) == ' ') {
						i++;
						line = line.substring(0, i) + " " + line.substring(i, line.length());
					}
			}

		if (newline) {
			if (spacing.equals("single"))
				formatter.format("%s%n", line);
			else
				formatter.format("%s%n%n", line);
		} else
			formatter.format("%s", line);
	} // End of writeFullJustified method

	// Closes the formatter object
	public void closeFile() {
		formatter.close();
	}
} // End of CreateFile class