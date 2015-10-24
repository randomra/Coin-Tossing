package bots;

import main.Action;
import main.Game;

public final class RegretBot implements Bot {

    @Override
    public String getName() {
        return "RegretBot";
    }

    @Override
    public Action takeTurn(int blue_levers, int red_levers, String history, String memory, int roundNumber) {
        int actionNum = 0;
        if(roundNumber == 100) {
            // if it's the end of the game and we're losing, go back
            //  in time to the first loss, in hopes of doing better
            if(Game.totalScore(history)<=0 && red_levers > 0) {
                actionNum = history.indexOf("0")+1;
            }
            // if we're winning at the end, pull a blue lever if we can,
            //  to prevent our opponent from undoing our victory
            else if(blue_levers > 0) {
                actionNum = -1;
            }
        }
        // we don't need no stinkin' memory!
        return new Action(actionNum, null);
    }

}