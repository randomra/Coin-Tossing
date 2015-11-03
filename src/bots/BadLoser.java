package bots;

import main.Action;;

/**
 * Created 11/2/15
 *
 * @author TheNumberOne
 */
public class BadLoser implements Bot{
    @Override
    public String getName() {
        return "Bad Loser";
    }

    @Override
    public Action takeTurn(int blue_levers, int red_levers, String history, String memory, int roundNumber) {
        if (history.contains("00") && red_levers > 0){       //Subtract a zero for better performance against
            return new Action(history.indexOf("00") + 1, "");// Analyzer and Nostalgia, and worse performance 
                                                             // against everything else.
        }
        int wins = 0;
        for (char c : history.toCharArray()){
            wins += c - '0';
        }
        if (wins >= new int[]{101, 51, 40, 30, 20, 10}[blue_levers]){
            return new Action(-1, "");
        }
        return new Action(0, "");
    }
}