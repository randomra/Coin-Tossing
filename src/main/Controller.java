package main;

import bots.*;

/**
 * INSTRUCTIONS: Add your bot to the `bots` array
 */

public class Controller {
	public static final int NUMBER_OF_GAMES = 10;
	public static final boolean VERBOSE = true;
	public static final long TIMEOUT = 10 * 1000000; // Nanoseconds per Turn (10
														// ms)
	public static final int BLUE_LEVER_COUNT = 5;
	public static final int RED_LEVER_COUNT = 20;
	public static final int COINTOSS_ROUNDS = 100;

	// Add your Bot class in this array.
	Bot[] bots = new Bot[] { /*new TestBot(), new TestBot(),*/ new OldscoolBot(),
			new RandomBot() };
	int[][][] res;

	Controller() {
		res = new int[bots.length][bots.length][NUMBER_OF_GAMES];
	}

	/**
	 * Runs all the games
	 */
	public void playGames() {
		Game game = new Game(BLUE_LEVER_COUNT, RED_LEVER_COUNT,
				COINTOSS_ROUNDS, TIMEOUT, VERBOSE);
		print("Playing games.");
		// for each unique pairing of bots
		for (int i = 0; i < bots.length; i++) {
			for (int j = i + 1; j < bots.length; j++) {
				int sum = 0;
				for (int k = 0; k < NUMBER_OF_GAMES; k++) {
					res[i][j][k] = game.play(bots[i], bots[j]);
					sum += res[i][j][k];
				}
				print("Total score: " + bots[i].getName() + " "
						+ Integer.toString(sum) + " - "
						+ Integer.toString(2 * NUMBER_OF_GAMES - sum) + " "
						+ bots[j].getName());
			}
		}
	}

	public void printTable() {
		String[] legend = new String[bots.length];
		for (int j = 0; j < bots.length; j++) {
			legend[j] = Character.toString((char) (j + 65));
		}
		// print bot ids
		print("");
		print("Legend:");
		for (int j = 0; j < bots.length; j++) {
			print(legend[j] + ": " + bots[j].getName());
		}

		print("");
		print("Results of the games (" + Integer.toString(NUMBER_OF_GAMES)
				+ " rounds):");

		// store chart
		int[][] chart = new int[bots.length][bots.length];
		int sum = 0;
		for (int i = 0; i < bots.length; i++) {
			for (int j = 0; j < bots.length; j++) {
				if (j > i) {
					sum = 0;
					for (int k = 0; k < NUMBER_OF_GAMES; k++) {
						sum += res[i][j][k];
					}
					chart[i][j] = sum;
					chart[j][i] = 2 * NUMBER_OF_GAMES - sum;
				} else if (j == i) {
					chart[i][j] = 0;
				}
			}
		}

		// print chart
		// header
		System.out.print("  ");
		for (int j = 0; j < bots.length; j++) {
			System.out.print("    " + legend[j] + ":");
		}
		print("  (opponents)");
		// body
		for (int i = 0; i < bots.length; i++) {
			System.out.print(legend[i] + ":");
			for (int j = 0; j < bots.length; j++) {
				System.out.print(String.format("%6d", chart[i][j]));
			}
			print("");
		}

		print("");
		System.out.print("Total Scores:");
		for (int i = 0; i < bots.length; i++) {
			print("");
			System.out.print(bots[i].getName() + ": ");
			// looping over bots (each bots a column)
			sum = 0;
			for (int j = 0; j < bots.length; j++) {
				sum += chart[i][j];
			}
			System.out.print(sum);
		}
	}// print table

	public static void main(String[] args) {
		Controller c = new Controller();
		c.playGames();
		c.printTable();
	}// main

	/**
	 * print helper function
	 */
	public static void print(String s) {
		System.out.println(s);
	}

}
