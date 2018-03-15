import java.util.Formatter;

public class CreateFile {
	private Formatter formatter;
	
	public boolean openFile(String fileName) {
		boolean successful = true;
		try {
			formatter = new Formatter(fileName);
		}catch(Exception e) {
			successful = false;
		}
		return successful;
	}
	
	public void writeToFile(String line, boolean newline, String justified, int linelength) {
		if(justified.equals("right")) writeRightJustified(line, newline, linelength);
		else writeLeftJustified(line, newline);
	}
	
	public void writeLeftJustified(String line, boolean newline) {
		if(newline) formatter.format("%s%n",line);
		else formatter.format("%s", line);
	}
	
	public void writeRightJustified(String line, boolean newline, int linelength) {
		if(newline) formatter.format("%" + linelength + "s%n",line);
		else formatter.format("%"+linelength+"s", line);
	}
	
	public void closeFile() {
		formatter.close();
	}
}
