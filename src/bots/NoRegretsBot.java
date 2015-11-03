package bots;

import main.Action;
import main.Game;

public final class NoRegretsBot implements Bot {

    @Override
    public String getName() {
        return "NoRegretsBot";
    }

    @Override
    public Action takeTurn(int blue_levers, int red_levers, String history, String memory, int roundNumber) {
        // every 20 turns, pull a blue lever to lock in the past
        // hopefully this will thwart some of those pesky time-travelers
        return new Action(roundNumber%20==0?-1:0, null);
    }

}