package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * @author Claire Lodermeier
 */

public class BoggleGame {

	private ArrayList<String> dice;
	private char[][] board;
	private DiceTray tray;
	private int score;
	private ArrayList<String> possibleWords;
	private ArrayList<String> incorrectWords;
	private ArrayList<String> correctWords;

	public BoggleGame() {
		String[] diceArr = new String[] { "LRYTTE", "VTHRWE", "EGHWNE", "SEOTIS", "ANAEEG", "IDSYTT", "OATTOW",
				"MTOICU", "AFPKFS", "XLDERI", "HCPOAS", "ENSIEU", "YLDEVR", "ZNRNHL", "NMIHUQ", "OBBAOJ" };
		dice = new ArrayList<String>();
		for (String die : diceArr) {
			dice.add(die);
		}

		board = buildBoard();
		tray = new DiceTray(board);
		score = 0;
		possibleWords = new ArrayList<String>();
		incorrectWords = new ArrayList<String>();
		correctWords = new ArrayList<String>();
		findPossibleWords();

	}

	private void findPossibleWords() {
		File dictionary = new File("BoggleWords.txt");
		Scanner scanner;
		try {
			scanner = new Scanner(dictionary);
			while (scanner.hasNextLine()) {
				String word = scanner.nextLine();
				word = word.strip();
				if (tray.found(word)) {
					possibleWords.add(word);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
	}

	private int wordScore(int wordLength) {
		if (wordLength < 5) {
			return 1;
		} else if (wordLength == 5) {
			return 2;
		} else if (wordLength == 6) {
			return 3;
		} else if (wordLength == 7) {
			return 5;
		} else {
			return 11;
		}
	}

	public void calculateResults(ArrayList<String> userWords) {
		for (String userWord : userWords) {
			// invalid length word
			if (userWord.length() < 3 || userWord.length() > 16) {
				score += 0;
			}
			// duplicate word
			else if (correctWords.contains(userWord)) {
				score += 0;
			}
			// correct word
			else if (possibleWords.contains(userWord)) {
				possibleWords.remove(userWord);
				correctWords.add(userWord);
				score += wordScore(userWord.length());
			}
			// incorrect word
			else {
				incorrectWords.add(userWord);
			}
		}
	}

	private char[][] buildBoard() {
		char[][] board = new char[4][4];
		int row = 0;
		int col = 0;

		for (int i = 0; i < 16; i++) {
			// randomly select one of the remaining dice
			int dieIndex = (int) (Math.random() * (dice.size()));
			String currentDie = dice.get(dieIndex);
			dice.remove(currentDie);

			// randomly select a side of the dice
			int letterIndex = (int) (Math.random() * 6);

			// add the letter to the board
			board[row][col] = currentDie.charAt(letterIndex);

			// adjust row, col numbers
			row++;
			if (row == 4) {
				row = 0;
				col++;
			}
		}

		return board;
	}

	public char[][] getBoard() {
		return board;
	}

	public int getScore() {
		return score;
	}

	public ArrayList<String> getCorrectWords() {
		return correctWords;
	}

	public ArrayList<String> getIncorrectWords() {
		return incorrectWords;
	}

	public ArrayList<String> getPossibleWords() {
		return possibleWords;
	}

	public String getConsoleString(ArrayList<String> words) {
		int charNum = 0;
		String str = "";
		for (int i = 0; i < words.size(); i++) {
			String currentWord = words.get(i);
			str += currentWord + " ";
			charNum += currentWord.length() + 1;
			if (charNum > 50) {
				str += "\n";
				charNum = 0;
			}

		}
		return str;
	}
}
