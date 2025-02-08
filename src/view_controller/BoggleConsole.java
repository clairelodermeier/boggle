package view_controller;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * @author Claire Lodermeier
 */

import model.BoggleGame;

public class BoggleConsole {

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		// Implement a Console IO Game
		BoggleGame game = new BoggleGame();
		char[][] board = game.getBoard();
		printGame(board);

		// get user words
		ArrayList<String> userWords = new ArrayList<String>();
		boolean playing = true;
		while (playing) {
			String input = keyboard.next();
			if (input.equals("ZZ")) {
				playing = false;
			} else {
				userWords.add(input.strip());
			}
		}
		// get results
		game.calculateResults(userWords);
		printResults(game);
		keyboard.close();
	}

	// print game setup
	public static void printGame(char[][] board) {
		System.out.println("Play one game of Boggle: ");
		System.out.println();
		System.out.println(formatBoard(board));
		System.out.println("Enter words or ZZ to quit: ");
	}

	// format board for printing
	public static String formatBoard(char[][] board) {
		String formatted = "";
		for (int row = 0; row < 4; row++) {
			formatted += "    ";
			for (int col = 0; col < 4; col++) {
				formatted += board[row][col] + "  ";
			}
			formatted += "\n\n";
		}

		return formatted;
	}

	// print game results
	public static void printResults(BoggleGame game) {
		System.out.println();
		System.out.println("Your Score: " + game.getScore());
		System.out.println("Words you found:");
		System.out.println("================");
		System.out.println(game.getConsoleString(game.getCorrectWords()));
		System.out.println();

		System.out.println("Incorrect Words:");
		System.out.println("================");
		System.out.println(game.getConsoleString(game.getIncorrectWords()));
		System.out.println();

		System.out.println("You could have found " + (game.getPossibleWords().size() - game.getCorrectWords().size())
				+ " more words. ");
		System.out.println("The computer found all of your words plus these: ");
		System.out.println("================================================");
		System.out.println(game.getConsoleString(game.getPossibleWords()));

	}

}