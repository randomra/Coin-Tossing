package main;

import bots.Bot;

public class Game {

	public int blue;
	public int red;
	public int turns;
	public long timeout;
	public boolean verbose;

	public Game(int blue, int red, int turns, long timeout, boolean verbose) {
		this.blue = blue;
		this.red = red;
		this.turns = turns;
		this.timeout = timeout;
		this.verbose = verbose;
	}

	/**
	 * plays one game
	 * 
	 * @return 1 if p1 wins, 0 if tie, -1 if p2 wins
	 */
	public int play(Bot p1, Bot p2) {
		BotState[] botStates = { new BotState(p1, blue, red),
				new BotState(p2, blue, red) };

		int turn = 1;
		int abs_turn = 1;
		String history = "";
		int block_turn = -1;
		int[] moves = new int[2];

		if (verbose) {
			print(p1.getName() + " (coin 1) vs " + p2.getName() + " (coin 0):");
		}

		while (turn < turns + 1) {

			// flip coin
			history += (Math.random() < 0.5 ? '1' : '0');

			// ask each bot for action
			int second = 0;
			for (BotState bs : botStates) {
				String relative_history = "";
				for (Character c : history.toCharArray()) {
					relative_history += ((c == '1' ^ second == 1) ? '1' : '0');
				}
				long startTime = System.nanoTime();// measure time
				Action action = null;
				try {
					action = bs.bot.takeTurn(bs.blue_left, bs.red_left,
							relative_history, bs.states.get(turn - 1), turn);
				} catch (Exception e) {
					print(bs.bot.getName()
							+ " should be disqualified! (runtime error)");
					action = new Action(0, "");
				}
				long endTime = System.nanoTime();// measure time
				bs.runtime += endTime - startTime;
				if (bs.states.size() < turn + 1) {
					bs.states.add(action.memory);
				} else {
					bs.states.set(turn, action.memory);
				}
				if (action.move == -1) {
					if (bs.blue_left > 0) {
						bs.blue_left -= 1;
					} else {
						// incorrect move disregarded
						action.move = 0;
					}
				}
				if (action.move >= 1 && action.move <= turn) {
					if (bs.red_left > 0) {
						bs.red_left -= 1;
					} else {
						// incorrect move disregarded
						action.move = 0;
					}
				}
				moves[second] = action.move;
				second = 1;
			}

			// blocking happened
			for (int move : moves) {
				if (move == -1) {
					block_turn = turn;
				}
			}

			int next_turn = turn + 1;

			// travel checks
			for (int move : moves) {
				// in range, in range, not blocked, earliest of the two travels
				if (move >= 1 && move <= turn && move > block_turn
						&& move < next_turn) {
					next_turn = move;
				}
			}

			// state update for reverter to keep state in the next round
			for (int i : new int[] { 0, 1 }) {
				if (moves[i] == next_turn) {
					botStates[i].states.set(next_turn - 1,
							botStates[i].states.get(turn));
				}
			}

			if (verbose) {
				print("score = " + Integer.toString(Game.totalScore(history))
						+ ", coins = " + history);
				for (int i : new int[] { 0, 1 }) {
					if (moves[i] == -1) {
						print(botStates[i].bot.getName() + " blocks.");
					}
					if (moves[i] > 0) {
						print(botStates[i].bot.getName()
								+ " tries to revert to round "
								+ Integer.toString(moves[i]) + ".");
					}
				}
				if (next_turn == turn + 1) {
					print("No revert. Next round is round "
							+ Integer.toString(next_turn) + ".");
				} else {
					print("Revert to round " + Integer.toString(next_turn)
							+ ".");
				}
			}

			turn = next_turn;

			history = history.substring(0, turn - 1);

			abs_turn += 1;

		}

		if (verbose) {
			print("Match result: "
					+ botStates[0].bot.getName()
					+ " "
					+ Integer
							.toString((int) Math.signum(totalScore(history)) + 1)
					+ " - "
					+ Integer.toString(1 - (int) Math
							.signum(totalScore(history))) + " "
					+ botStates[1].bot.getName()
					+ " (final coin difference is "
					+ Integer.toString(totalScore(history))
					+ " (P1's coins - P2's coins))");
		}
		for (BotState bs : botStates) {
			if (bs.runtime > timeout * abs_turn) {
				print(bs.bot.getName() + " should be disqualified! (timeout)");
			}
		}

		return (int) Math.signum(totalScore(history)) + 1;
	}

	/**
	 * Gives your score - opponent's score for a given history
	 * 
	 * @param history
	 *            string of 0's and 1's to evaluate
	 * @return number if 1's - number of 0's
	 */
	public static int totalScore(String history) {
		int score = 0;
		for (Character c : history.toCharArray()) {
			score += (c == '1' ? 1 : -1);
		}
		return score;
	}

	/**
	 * print helper function
	 */
	public static void print(String s) {
		System.out.println(s);
	}
}
