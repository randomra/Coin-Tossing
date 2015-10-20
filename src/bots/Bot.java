package bots;

import main.Action;

public interface Bot {

	/**
	 * Returns the name of your bot.
	 */
	public String getName();

	/**
	 * Makes a move based only on the input information.
	 * 
	 * @param blue_levers
	 *            number of time travel stopping levers left for you
	 * 
	 * @param red_levers
	 *            number of time reverter levers left for you
	 * 
	 * @param history
	 *            results of previous rounds, the first char is the first round,
	 *            '1' marks win, '0' marks loss
	 * 
	 * @param memory
	 *            your returned memory state from the previously perceived round
	 *            (the last (roundNumner-1)th round)
	 * 
	 * @param roundNumber
	 *            the current round number (first is 1)
	 * 
	 * @return 0 for no action; -1 to pull a blue lever; x to pull a red lever
	 *         and try to revert back to where round x will be the next round
	 */
	public Action takeTurn(int blue_levers, int red_levers, String history,
			String memory, int roundNumber);
}
