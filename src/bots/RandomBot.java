package bots;

import main.Game;

public class RandomBot implements Bot {

	@Override
	public String getName() {
		return "Random";
	}

	@Override
	public Action takeTurn(int blue_levers, int red_levers, String history,
			String memory, int roundNumber) {

		// if in the lead and has blocks left, blocks with a 10% chance
		if (Game.totalScore(history) > 0 && blue_levers > 0
				&& Math.random() > 0.9) {
			return new Action(-1, null);
		}

		// if behind and has travels left, travel back the current step to
		// replay it with a 10% chance
		if (Game.totalScore(history) < 0 && red_levers > 0
				&& Math.random() > 0.9) {
			return new Action(roundNumber, null);
		}

		// if neither action were needed do nothing
		return new Action(0, null);
	}
}
