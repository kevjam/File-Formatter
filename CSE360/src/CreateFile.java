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
			writeFullJustified(line, newline, spacing, linelength);
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
	
	public void writeFullJustified(String line, boolean newline, String spacing, int linelength) {
		//First we need to compare line's length to the linelength variable
		int difference = linelength - line.length();
		//Only modify line if difference is greater than 0. A negative value indicates that it's just one
		//Big word, and a zero value indicates that we're fine
		if (difference > 0) {
			String[] splitLine = line.split(" ");
			int spacesToAdd = difference;
			while (spacesToAdd > 0)
            {
                for (int i = 0; i < splitLine.length - 1 && spacesToAdd > 0; i++)
                {
                    splitLine[i] = splitLine[i] + " ";
                    spacesToAdd--;
                }
            }
			//Now we recombine the line
			String newLine = "";
			for (int i = 0; i < splitLine.length; i++)
			{
				//First we have a case for the last item in the array
				if (i == splitLine.length - 1)
					newLine = newLine + splitLine[i];
				else
					newLine = newLine + splitLine[i] + " ";
			}
			line = newLine;
		}
		//If we want to add a new line
		if(newline) {
			//If we are using single space
			if(spacing.equals("single")) {
				formatter.format("%s%n", line);
			//Else we're using double space
			} else {
				formatter.format("%s%n%n", line);
			}//End of inner if else statement
			//Else add the new line to the end of the current line
		} else {
			formatter.format("%s", line);
		}	
	} // End of writeFullJustified method
	
	// Closes the formatter object
	public void closeFile() {
		formatter.close();
	} // End of closeFile method
	
} // End of CreateFile class

