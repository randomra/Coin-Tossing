package bots;
public interface Bot {
	public String getName();

	/**
	 * Makes a move based only based on the input information.
	 * 
	 * @return 0 for no action; -1 to pull a blue lever; x to pull a red lever
	 *         and try to revert back to round x
	 */
	public Action takeTurn(int blue_levers, int red_levers, String history,
			String memory, int roundNumber);
}
	