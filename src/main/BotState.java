package main;

import java.util.ArrayList;
import java.util.List;

import bots.Bot;

public class BotState {

	Bot bot;
	int blue_left, red_left;
	List<String> states;
	long runtime;

	public BotState(Bot bot, int blue_left, int red_left) {
		this.bot = bot;
		this.blue_left = blue_left;
		this.red_left = red_left;
		states = new ArrayList<String>();
		states.add("");
		runtime = 0;
	}
}
