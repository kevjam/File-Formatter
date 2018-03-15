import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileFormatter extends Application {
	private static Scanner sc;
	public static final int linelength = 80;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// Input File Field
		TextField inputField = new TextField();
		Button inputBrowse = new Button("Browse");
		HBox inputBox = textBox("Input File: \t", inputField, inputBrowse);

		// Ouput File Field
		TextField outputField = new TextField();
		Button outputBrowse = new Button("Browse");
		HBox outputBox = textBox("Output File: \t", outputField, outputBrowse);

		// Justification Selection
		RadioButton left = new RadioButton("Left (default)");
		RadioButton right = new RadioButton("Right");
		HBox justification = radioButtonBox("Justification: \t", left, right);

		// Run program / analysis
		HBox operations = new HBox();
		Button runButton = new Button("Run Formatter");
		CheckBox analysisCheckBox = new CheckBox("Show Analysis");
		operations.setSpacing(20);
		operations.getChildren().addAll(runButton, analysisCheckBox);

		// Root pane
		VBox root = new VBox();
		VBox txtFields = new VBox();
		txtFields.getChildren().addAll(inputBox, outputBox);
		root.setSpacing(5);
		root.setPadding(new Insets(20));
		root.getChildren().addAll(txtFields, justification, operations);

		// Primary Stage
		primaryStage.setTitle("File Formatter");
		primaryStage.setScene(new Scene(root, 350, 150));
		primaryStage.setResizable(false);
		primaryStage.show();

		// Input File Browser
		inputBrowse.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Input File");
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
				File inputFile = fileChooser.showOpenDialog(primaryStage);
				if (inputFile != null) {
					inputField.setText(inputFile.getAbsolutePath());
				}
			}
		});

		// Output File Browser
		outputBrowse.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Output File");
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
				File outputFile = fileChooser.showOpenDialog(primaryStage);
				if (outputFile != null) {
					outputField.setText(outputFile.getAbsolutePath());
				}
			}
		});

		// Run Formatter button event
		runButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String justified;
				if (left.isSelected()) justified = "left";
				else justified = "right";

				String oldFileName = inputField.getText();
				String newFileName = outputField.getText();
				File oldFile = new File(oldFileName);
				File newFile = new File(newFileName);

				// Write to file if not in the same path
				if(oldFileName.equals("")) errorWindow("No input file selected.");
				else if(newFileName.equals("")) errorWindow("No output file selected.");
				else if (samePath(oldFile, newFile)) errorWindow("Same input and output path.");
				else {
					writeFile(oldFile, newFileName, justified);
					if (analysisCheckBox.isSelected()) 
						analysisWindow(oldFile, newFile);
				}
				if (sc != null)
					sc.close();
			}
		});
	}
	
	// Sets up an HBox for a file field and browse button
	public static HBox textBox(String prompt, TextField field, Button button) {
		HBox textBox = new HBox();
		Text txt = new Text(prompt);
		field.setDisable(true);
		textBox.getChildren().addAll(txt, field, button);
		return textBox;
	 }
	
	// Sets up an HBox for a grouped radiobutton selection
	public static HBox radioButtonBox(String prompt, RadioButton... buttons) {
		HBox radioButtonBox = new HBox();
		ToggleGroup group = new ToggleGroup();
		Text txt = new Text(prompt);
		
		radioButtonBox.getChildren().add(txt);
		for(int i = 0; i < buttons.length; i++) {
			buttons[i].setToggleGroup(group);
			radioButtonBox.getChildren().add(buttons[i]);
		}
		buttons[0].setSelected(true);
		radioButtonBox.setSpacing(20);
		return radioButtonBox;
	}

	// Takes a string as a message and creates an error popup
	public static void errorWindow(String message) {
		Stage errorPopup = new Stage();
		errorPopup.setTitle("Error!");
		VBox errorBox = new VBox();
		errorBox.setPadding(new Insets(20));
		Text error = new Text(message);
		errorBox.getChildren().add(error);
		errorPopup.setScene(new Scene(errorBox, 250, 100));
		errorPopup.setResizable(false);
		errorPopup.show();
	}
	
	// Method to create an analysisWindow popup containing calculations
	// performed on two files
	public static void analysisWindow(File oldFile, File newFile) {
		Stage analysisPopup = new Stage();
		analysisPopup.setTitle("Analysis Report");
		VBox analysisReport = new VBox();
		analysisReport.setPadding(new Insets(20));
		Text analysis = new Text(analysisReport(oldFile, newFile));
		analysisReport.getChildren().add(analysis);
		analysisPopup.setScene(new Scene(analysisReport, 250, 150));
		analysisPopup.setResizable(false);
		analysisPopup.show();
	}

	// Returns true if two files share the same path
	private static boolean samePath(File file1, File file2) {
		return file1.getAbsolutePath().equals(file2.getAbsolutePath());
	}

	// Returns a string containing the analysis report
	public static String analysisReport(File oldFile, File newFile) {
		String report = "";
		int words = wordCount(newFile);
		int lines = lineCount(newFile);
		int wordsPerLine = 0;
		int avgLineLength = 0;

		if (lines != 0) {
			wordsPerLine = words / lines;
			avgLineLength = totalLineLength(newFile) / lines;
		}
		report += words + "\twords processed.\n";
		report += lines + "\tlines processed.\n";
		report += blankLines(oldFile) + "\tblank lines removed.\n";
		report += wordsPerLine + "\taverage words per line.\n";
		report += avgLineLength + "\taverage line length.\n";
		return report;
	}

	// Writes from file to file
	public static void writeFile(File inputFile, String newFileName, String justification) {
		CreateFile newFile = new CreateFile();
		if (!newFile.openFile(newFileName)) {
			errorWindow("Could not open file.");
		} else {
			String next, line;
			next = line = "";

			try {
				sc = new Scanner(inputFile);
				while (sc.hasNext()) {
					next = sc.next();
					if (line.length() + next.length() <= linelength) // add word
						line += next + " ";
					else { // add line to text
						newFile.writeToFile(line.trim() + "\r", justification, linelength);
						line = next + " ";
					}
				}
				newFile.writeToFile(line.trim(), justification, linelength);
			} catch (FileNotFoundException e) {
				errorWindow("Input file not found.");
			}
			newFile.closeFile();
		}
	}

	// Count the length of all lines
	public static int totalLineLength(File file) {
		int totalLineLength = 0;
		try {
			sc = new Scanner(file);

			while (sc.hasNextLine())
				totalLineLength += sc.nextLine().trim().length();
		} catch (FileNotFoundException e) {
			errorWindow("Output file not found.");
		}
		return totalLineLength;
	}

	// Count blank lines in a given file
	public static int blankLines(File file) {
		int count = 0;

		try {
			sc = new Scanner(file);
			while (sc.hasNextLine())
				if ("".equals(sc.nextLine()))
					count++;
		} catch (FileNotFoundException e) {
			errorWindow("Input file not found.");
		}
		return count;
	}

	// Counts the words in a given file
	public static int wordCount(File file) {
		int count = 0;
		try {
			sc = new Scanner(file);

			while (sc.hasNext()) {
				count++;
				sc.next();
			}
		} catch (FileNotFoundException e) {
			errorWindow("Output file not found.");
		}
		return count;
	}

	// Counts the lines in a given file
	public static int lineCount(File file) {
		int count = 0;
		try {
			sc = new Scanner(file);

			while (sc.hasNextLine()) {
				count++;
				sc.nextLine();
			}
		} catch (FileNotFoundException e) {
			errorWindow("Output file not found.");
		}
		return count;
	}
}