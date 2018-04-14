import java.util.Formatter;

public class CreateFile {
	private Formatter formatter;
	
	public boolean openFile(String fileName) {
		boolean successful = true;
		try {
			formatter = new Formatter(fileName);
		} catch(Exception e) {
			successful = false;
		} // End of try catch
		return successful;
	} // End of openFile function
	
	// Added a parameter for spacing to determine if it will add one or two %n's at the end of a line
	public void writeToFile(String line, boolean newline, String justified, String spacing, int linelength) {
		if(justified.equals("right")) {
			writeRightJustified(line, newline, spacing, linelength);
		} else if(justified.equals("left")) {
			writeLeftJustified(line, spacing, newline);
		} else {
			// Connor: Calvin, his is where you will likely add your new function call for writeFullJustified
			
		} // End of if else statement
	} // End of writeToFile method
	
	public void writeLeftJustified(String line, String spacing, boolean newline) {
		// If we want to add a new line
		if(newline) {
			// If we are using single space
			if(spacing.equals("single")) {
				formatter.format("%s%n",line);
			// Else we are using double space
			} else {
				formatter.format("%s%n%n",line);
			} // End of inner if else statement
		// Else add the new line to the end of the current line
		} else {
			formatter.format("%s", line);
		} // End of outer if else statement
	} // End of writeLeftJustified
	
	public void writeRightJustified(String line, boolean newline, String spacing, int linelength) {
		// If we want to add a new line
		if(newline) {
			// If we are using single space
			if(spacing.equals("single")) {
				formatter.format("%" + linelength + "s%n",line);
			// Else we are using double space
			} else {
				formatter.format("%" + linelength + "s%n%n",line);
			} // End of inner if else statement
		} else {
			formatter.format("%"+linelength+"s", line);
		} // End of outer if else statement
	} // End of writeRightJustified method
	
	// Connor: Calvin, it's up to you how you want to do this. I suggest following the format of
	// the other two justified methods here
	public void writeFullJustified() {
		
	} // End of writeFullJustified method
	
	// Closes the formatter object
	public void closeFile() {
		formatter.close();
	} // End of closeFile method
	
} // End of CreateFile class
