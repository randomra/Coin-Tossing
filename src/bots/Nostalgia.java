package bots;

import main.Action;
import main.Game;

public class Nostalgia implements Bot {

    @Override
    public String getName() {
        return "Nostalgia";
    }

    @Override
    public Action takeTurn(int blue_levers, int red_levers, String history,
            String memory, int roundNumber) {

        int current_score = Game.totalScore(history);

        // wait until the end to use blue levers
        if (current_score > 0 && blue_levers >= (100 - roundNumber)) {
            return new Action(-1, memory);
        }

        // become increasingly likely to go back as the gap between the good old days
        // and the horrible present increases
        if (current_score < 0 && red_levers > 0) {
            //identify the best time to travel back to
            int best_score = -100;
            int good_old_days = 1;
            int past_score = 0;

            int unreachable_past = 0;
            if(memory != "") {
              unreachable_past = Integer.parseInt(memory, 10);
            }

            for(int i = unreachable_past; i<roundNumber ; i++) {
              if(history.charAt(i) == '1') {
                past_score += 1;
                if(past_score > best_score) {
                  best_score = past_score;
                  good_old_days = i + 1;
                }
              }
              else {
                past_score -= 1;
              }
            }
            if(roundNumber >= 95 || Math.random() < (best_score - current_score) / 100.0) {
              return new Action(good_old_days, Integer.toString(good_old_days));
            }
        }

        // if neither action was needed do nothing
        return new Action(0, memory);
    }
}