package bots;

import main.Action;

public class OldscoolBot implements Bot {

	@Override
	public String getName() {
		return "Oldschool";
	}

	@Override
	public Action takeTurn(int blue_levers, int red_levers, String history,
			String memory, int roundNumber) {		
		// never tries to block or travel at all
		return new Action(0, null);
	}
}
