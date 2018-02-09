import java.util.Formatter;

public class CreateFile {
	private Formatter formatter;
	
	public void openFile(String fileName) {
		try {
			formatter = new Formatter(fileName);
		}catch(Exception e) {
			System.out.println("Could not open file.");
		}
	}
	
	public void writeToFile(String line, String justified) {
		if(justified.equals("right"))
			writeRightJustified(line);
		else
			writeLeftJustified(line);
	}
	
	public void writeLeftJustified(String line) {
		formatter.format("%s",line);
	}
	
	public void writeRightJustified(String line) {
		formatter.format("%81s", line);
	}
	
	public void closeFile() {
		formatter.close();
	}
}
