package bots;

import main.Action;
import main.Game;

public class TestBot implements Bot {

	@Override
	public String getName() {
		return "Test";
	}

	@Override
	public Action takeTurn(int blue_levers, int red_levers, String history,
			String memory, int roundNumber) {

		String h = memory + "x";
		
		System.out.println("h is "+h);
		
		// if in the lead and has blocks left, blocks with a 10% chance
		if (Game.totalScore(history) > 0 && blue_levers > 0
				&& Math.random() > 0.1) {
			return new Action(-1, h);
		}

		// if behind and has travels left, travel back the current step to
		// replay it with a 10% chance
		if (Game.totalScore(history) < 0 && red_levers > 0
				&& Math.random() > 0.1) {
			return new Action(roundNumber, h);
		}

		// if neither action were needed do nothing
		return new Action(0, h);
	}
}
