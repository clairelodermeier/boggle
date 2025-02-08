package view_controller;

/*
 * @author Claire Lodermeier
 */

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.BoggleGame;

public class BoggleGUI extends Application {

	private BoggleGame game = new BoggleGame();
	private ArrayList<String> userWords = new ArrayList<String>();

	private TextField input = new TextField();

	private Label inputLabel = new Label("Type or click to form words: ");

	private Label gameOverLabel = new Label("Game Over!");
	private Label scoreLabel = new Label("Score: ");

	private TextArea correctWords = new TextArea();
	private TextArea incorrectWords = new TextArea();
	private TextArea missedWords = new TextArea();

	private Label correctLabel = new Label("Correct Words:");
	private Label incorrectLabel = new Label("Incorrect Words:");
	private Label missedLabel = new Label("Missed Words:");

	private Button enter = new Button("Enter");

	private Button endGame = new Button("End Game");
	private Button newGame = new Button("New Game");

	private GridPane pane;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) {
		pane = new GridPane();
		pane.setHgap(1);
		pane.setVgap(1);

		styling();
		layout();
		setHandlers();

		stage.setTitle("Boggle");
		Scene scene = new Scene(pane, 500, 500);
		stage.setScene(scene);

		stage.show();
	}

	private void setHandlers() {
		EventHandler<ActionEvent> inputHandler = new inputHandler();
		input.setOnAction(inputHandler);
		enter.setOnAction(inputHandler);

		EventHandler<ActionEvent> endGameHandler = new endGameHandler();
		endGame.setOnAction(endGameHandler);

		EventHandler<ActionEvent> newGameHandler = new newGameHandler();
		newGame.setOnAction(newGameHandler);

	}

	// layout for the window
	private void layout() {

		pane.add(inputLabel, 57, 50);
		pane.add(input, 57, 51);
		pane.add(enter, 57, 52);

		pane.add(endGame, 53, 105, 4, 2);

		createLetters();

	}

	private void styling() {
		pane.setStyle("-fx-background-color: SteelBlue");
		inputLabel.setStyle("-fx-text-fill: white;");
		Font font = Font.font("Times", 14);
		inputLabel.setFont(font);
		Font fontMono = Font.font("Monospaced", FontWeight.BOLD, 16);
		input.setFont(fontMono);
		endGame.setStyle("-fx-border-color: Peru;" + "-fx-border-style: solid;" + "-fx-border-width: 2px;"
				+ "-fx-border-radius: 3px;" + "-fx-text-fill: White;" + "-fx-font-size: 18px;"
				+ "-fx-background-color: Chocolate;");
		input.setStyle("-fx-background-color: PaleGoldenRod;");

	}

	private void resultsStyling() {
		pane.setStyle("-fx-background-color: PaleGoldenRod");

		gameOverLabel.setStyle("-fx-text-fill: SteelBlue;");
		// edit fonts
		Font font = Font.font("Monospaced", FontWeight.BOLD, 18);
		scoreLabel.setFont(font);
		newGame.setFont(font);

		Font font2 = Font.font("Monospaced", FontWeight.BOLD, 16);
		correctWords.setFont(font2);
		incorrectWords.setFont(font2);
		missedWords.setFont(font2);

		Font font3 = Font.font("Monospaced", FontWeight.BOLD, 20);
		gameOverLabel.setFont(font3);

		newGame.setStyle("-fx-border-color: Peru;" + "-fx-border-style: solid;" + "-fx-border-width: 2px;"
				+ "-fx-border-radius: 3px;" + "-fx-text-fill: White;" + "-fx-font-size: 18px;"
				+ "-fx-background-color: Chocolate;");

	}

	private void createLetters() {
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				Button currentButton = new Button("" + game.getBoard()[r][c]);
				EventHandler<ActionEvent> letterHandler = new letterHandler("" + game.getBoard()[r][c]);

				currentButton.setOnAction(letterHandler);
				currentButton.setMinWidth(50);
				currentButton.setMaxWidth(50);
				currentButton.setMinHeight(50);
				currentButton.setMaxHeight(50);

				Font font = Font.font("Monospaced", FontWeight.BOLD, 20);
				currentButton.setFont(font);
				currentButton.setStyle("-fx-border-color: GoldenRod;" + "-fx-border-width: 2px;"
						+ "-fx-border-radius: 3px;" + "-fx-background-color: PaleGoldenRod;" + "-fx-text-fill: Black;");
				pane.add(currentButton, r + 50, c + 50);

			}
		}
	}

	private class letterHandler implements EventHandler<ActionEvent> {

		private String letter;

		letterHandler(String letter) {
			this.letter = letter;
		}

		@Override
		public void handle(ActionEvent event) {
			input.setText(input.getText() + letter);
		}
	}

	private class inputHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			String inputText = input.getText().strip();
			// check for invalid words
			if (inputText.length() < 3) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(" Words must be at least 3 letters. ");
				alert.show();
			} else if (userWords.contains(inputText.toLowerCase())) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(" Word already entered. ");
				alert.show();
			} else {
				addWord(inputText.toLowerCase());
			}
		}

		private void addWord(String word) {
			userWords.add(word);
			input.setText("");
		}
	}

	private class endGameHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {

			game.calculateResults(userWords);
			resultsStyling();
			displayResults();
		}

		// show results
		private void displayResults() {
			// remove game play nodes
			pane.getChildren().clear();

			// display labels at top
			scoreLabel.setText("Score: " + game.getScore());
			gameOverLabel.setMinWidth(125);
			scoreLabel.setMinWidth(125);

			pane.add(gameOverLabel, 50, 30, 5, 2);
			pane.add(scoreLabel, 50, 32, 5, 2);

			// set results text
			correctWords.setText(getCorrectStr());
			incorrectWords.setText(getIncorrectStr());
			missedWords.setText(getMissedStr());

			// adjust sizing
			correctWords.setMaxWidth(200);
			incorrectWords.setMaxWidth(200);
			correctWords.setMaxHeight(150);
			incorrectWords.setMaxHeight(150);
			missedWords.setMaxWidth(200);
			missedWords.setMinHeight(250);

			// display results
			pane.add(correctLabel, 50, 54);
			pane.add(correctWords, 50, 55);

			pane.add(incorrectLabel, 50, 56);
			pane.add(incorrectWords, 50, 57);

			pane.add(missedLabel, 90, 54);
			pane.add(missedWords, 90, 55, 1, 5);

			// new game button
			newGame.setMinWidth(150);
			newGame.setMinHeight(50);

			pane.add(newGame, 90, 30, 4, 2);

			// text areas not editable
			correctWords.setEditable(false);
			incorrectWords.setEditable(false);
			missedWords.setEditable(false);

		}

		private String getCorrectStr() {
			String str = "";
			for (String word : game.getCorrectWords()) {
				str += word + "\n";
			}
			return str.strip();
		}

		private String getIncorrectStr() {
			String str = "";
			for (String word : game.getIncorrectWords()) {
				str += word + "\n";
			}
			return str.strip();
		}

		private String getMissedStr() {
			String str = "";
			for (String word : game.getPossibleWords()) {
				str += word + "\n";
			}
			return str.strip();
		}
	}

	private class newGameHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			pane.getChildren().clear();
			game = new BoggleGame();
			userWords.clear();
			styling();
			layout();
			setHandlers();

		}

	}

}