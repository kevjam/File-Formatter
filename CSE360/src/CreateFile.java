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
	
	public void writeToFile(String line, String justified, int linelength) {
		if(justified.equals("right"))
			writeRightJustified(line, linelength);
		else
			writeLeftJustified(line);
	}
	
	public void writeLeftJustified(String line) {
		formatter.format("%s",line);
	}
	
	public void writeRightJustified(String line, int linelength) {
		formatter.format("%"+(linelength+1)+"s", line);
	}
	
	public void closeFile() {
		formatter.close();
	}
}
