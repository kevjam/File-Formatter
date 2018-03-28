import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class FileFormatter extends Application {
	private static Scanner sc;

	// remove once implemented as user input
	public static final int linelength = 80;

	public static final int width = 350;
	public static final int height = 150;
	public static final int minLength = 20;
	public static final int defaultLength = 80;
	public static final int maxLength = 999;
	public static final int maxDigits = 3;

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

		// Line Length input field
		// Spinner(minimum, maximum, default, increment by)
		Spinner<Integer> lengthField = new Spinner<Integer>(minLength, maxLength, defaultLength, 1);
		lengthField.setEditable(true);
		Text prompt = new Text("Line Length: \t");
		HBox lengthBox = new HBox(prompt, lengthField);

		// Justification Selection
		RadioButton left = new RadioButton("Left (default)");
		RadioButton right = new RadioButton("Right");
		RadioButton full = new RadioButton("Full");
		HBox justification = radioButtonBox("Justification: \t", left, right, full);

		// Run program / analysis
		HBox operations = new HBox();
		Button runButton = new Button("Run Formatter");
		CheckBox analysisCheckBox = new CheckBox("Show Analysis");
		operations.setSpacing(20);
		operations.getChildren().addAll(runButton, analysisCheckBox);

		// Root pane
		VBox root = new VBox();
		VBox txtFields = new VBox();
		txtFields.getChildren().addAll(lengthBox, inputBox, outputBox);
		root.setSpacing(5);
		root.setPadding(new Insets(10));
		root.getChildren().addAll(txtFields, justification, operations);

		// Primary Stage
		primaryStage.setTitle("File Formatter");
		primaryStage.setScene(new Scene(root, width, height));
		primaryStage.setResizable(false);
		primaryStage.show();

		// Handles input for lengthField
		lengthField.getEditor().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (arg2.length() > maxDigits) // reset to old value if too many digits
					lengthField.getEditor().setText(arg1);
				else if (arg2.length() > arg1.length())
					for (char c = 0; c < arg2.length(); c++) { // prevent strings/non-numerical chars
						if (arg2.charAt(c) < '0' || arg2.charAt(c) > '9') {
							lengthField.getEditor().setText(arg1);
							System.out.println(c);
						}
					}
			}
		});

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
				if (left.isSelected())
					justified = "left";
				else
					justified = "right";

				String oldFileName = inputField.getText();
				String newFileName = outputField.getText();
				File oldFile = new File(oldFileName);
				File newFile = new File(newFileName);

				// Write to file if not in the same path
				if (oldFileName.equals(""))
					errorWindow("No input file selected.");
				else if (newFileName.equals(""))
					errorWindow("No output file selected.");
				else if (samePath(oldFile, newFile))
					errorWindow("Same input and output path.");
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
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setToggleGroup(group);
			radioButtonBox.getChildren().add(buttons[i]);
		}
		buttons[0].setSelected(true);
		radioButtonBox.setSpacing(10);
		return radioButtonBox;
	}

	// Takes a string as a message and creates an error popup
	public static void errorWindow(String message) {
		Stage errorPopup = new Stage();
		errorPopup.setTitle("Error!");
		VBox errorBox = new VBox();
		errorBox.setPadding(new Insets(10));
		errorBox.setSpacing(10);
		Text error = new Text(message);
		error.setFill(Color.RED);
		Button OK = new Button("OK");
		errorBox.getChildren().addAll(error, OK);
		errorPopup.setScene(new Scene(errorBox, width, height / 2));
		errorPopup.setResizable(false);
		errorPopup.show();

		OK.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				errorPopup.close();
			}
		});
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
		analysisPopup.setScene(new Scene(analysisReport, width - 100, height));
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
						newFile.writeToFile(line.trim(), true, justification, linelength);
						line = next + " ";
					}
				}
				newFile.writeToFile(line.trim(), false, justification, linelength);
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
				totalLineLength += sc.nextLine().length();
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