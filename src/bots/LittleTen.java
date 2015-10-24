package bots;

import main.Action;
import main.Game;

public class LittleTen implements Bot {
    @Override
    public String getName() {
        return "Little Ten";
    }

    @Override
    public Action takeTurn(int blue_levers, int red_levers, String history,
                           String memory, int roundNumber) {
        int score = Game.totalScore(history);
        char c = history.charAt(history.length() - 1);
        if (memory.isEmpty()) {
            if (c == '1' && score == 1)
                memory = "1:";
            else
                memory = "0:";
        }
        if (c == memory.charAt(0))
            memory = c + ":" + String.valueOf(roundNumber);

        if (roundNumber == 100) {
            if (score >= 0)
                // We're tied or ahead by the end of the match. Prevent time travel if
                // we can; otherwise whatever happens happens.
                return new Action(blue_levers > 0 ? -1 : 0, memory);
            else {
                // Travel to earlier rounds the farther behind we are if we can
                if (red_levers > 0) {
                    String[] x = memory.split(":");
                    int i = Integer.parseInt(x[1]);
                    int distance = score <= -9 ? i : 100 + ((100 - i) / (score * i));
                    return new Action(distance, memory);
                }
            }
        }
        else if (score >= 5 + roundNumber / 20 && blue_levers > 0 && roundNumber != 100) {
            // We're ahead; we don't want to lose our lead, especially if the match is
            // close to ending. But we don't want to use up our blue levers too quickly.
            int choice = (int) (Math.random() * 100),
                bound = (roundNumber / 20 + 1) * 10 - (int) ((6 - blue_levers) * 2.5 - 2);
            return new Action(choice < bound ? -1 : 0, memory);
        }
        else if (score <= -3) {
            // Possibly use a red lever if we're falling too far behind
            if (red_levers > 0) {
                int choice = (int) (Math.random() * 100),
                    bound = score <= -11 ? 90 : 10 * (-3 - score + 1);
                if (choice < bound) {
                    // Check the first round that is the lower multiple of ten and
                    // decide if we've been successful up to that point; if so, travel
                    // back to that round, otherwise go back 10 more
                    int round = roundNumber / 10 * 10;
                    if (round < 10)
                        return new Action(1, memory);
                    String seq = history.substring(0, round-1);
                    while (Game.totalScore(seq) <= 0 && round > 10) {
                        round -= 10;
                        seq = history.substring(0, round-1);
                    }
                    if (round == 0)
                        round = 1;
                    return new Action(round, memory);
                }
            }
        }
        return new Action(0, memory);
    }
}

