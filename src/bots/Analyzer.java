//Boo!
package bots;

import main.Action;
import main.Game;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created 10/24/15
 *
 * @author TheNumberOne
 */
public class Analyzer implements Bot{

    @Override
    public String getName(){
        return "Analyzer";
    }

    @Override
    public Action takeTurn(int blue_levers, int red_levers, String history,
                           String memory, int roundNumber) {
        /*System.out.println(Game.totalScore(history) + " : " + history);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }*/
        int roundsLeft = 100 - roundNumber;
        int myScore = (Game.totalScore(history) + roundNumber) / 2; //My number of wins.
        int enemyScore = roundNumber - myScore;                     //Enemy's number of wins.
        Map<Integer, Double> bestRounds = new HashMap<>();
        int timeLimit = 0;

        Scanner scanner = new Scanner(memory);
        if (scanner.hasNext()){     //No memory, first turn.
            boolean triedTimeTravel = scanner.nextBoolean();
            if (triedTimeTravel){
                int time = scanner.nextInt();
                if (roundNumber > time) {     //Failed.
                    timeLimit = time;
                }
            }
            timeLimit = Math.max(timeLimit, scanner.nextInt());
            int size = scanner.nextInt();
            for (int i = 0; i < size; i++) {
                bestRounds.put(scanner.nextInt(), scanner.nextDouble());
            }
        } else {
            bestRounds.put(1, 0.5);
        }

        clean(bestRounds, roundNumber, timeLimit);
        double winningProb = computeWinningProbability(myScore, enemyScore, roundsLeft);
        String newMemory = computeMemory(bestRounds, roundNumber, winningProb);

        if (winningProb >= new double[]{1.5, .75, .7, .65, .6, .55}[blue_levers]){ //Ensure success ... slowly.
            return getAction(-1, newMemory, timeLimit, roundNumber);
        }

        int bestRound = bestRound(bestRounds);
        double bestRoundProb = bestRounds.get(bestRound);

        if ((winningProb <= bestRoundProb - .05 || winningProb < .5 && bestRoundProb > winningProb) && red_levers > 0){
            return getAction(bestRound, newMemory, timeLimit, roundNumber);  //Let's find the best past.
        } else {
            return getAction(0, newMemory, timeLimit, roundNumber); //Let's wait it out :)
        }
    }

    //Should be combined with computeMemory.
    private static Action getAction(int actionNum, String newMemory, int timeLimit, int roundNumber){
        if (actionNum == -1){
            timeLimit = Math.max(timeLimit, roundNumber);
            newMemory = "false " + timeLimit + " " + newMemory;
            return new Action(actionNum, newMemory);
        }
        if (actionNum == 0){
            return new Action(actionNum, "false " + timeLimit + " " + newMemory);
        }
        if (actionNum > 0){
            return new Action(actionNum, "true " + actionNum + " " + timeLimit + " " + newMemory);
        }
        return null;
    }

    private static int bestRound(Map<Integer, Double> bestRounds) {
        int best = 0;           //If no previous rounds ... just go forward a round.
        double bestScore = -1;
        for (Map.Entry<Integer, Double> entry : bestRounds.entrySet()){
            if (entry.getValue() > bestScore){
                best = entry.getKey();
                bestScore = entry.getValue();
            }
        }
        return best;
    }

    private static String computeMemory(Map<Integer, Double> map, int roundNumber, double winningProb) {
        StringBuilder builder = new StringBuilder();
        builder.append(map.size() + 1).append(" ");
        for (Map.Entry<Integer, Double> entry : map.entrySet()){
            builder.append(entry.getKey()).append(" ").append(entry.getValue()).append(" ");
        }
        builder.append(roundNumber + 1).append(" ").append(winningProb);
        return builder.toString();
    }

    private static void clean(Map<Integer, Double> data, int round, int timeLimit) {
        data
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() > round || entry.getKey() <= timeLimit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()).forEach(data::remove);
    }

    private static double computeWinningProbability(int myScore, int enemyScore, int roundsLeft){ //Too complex for IntelliJ
        int height = myScore - enemyScore;
        double total = 0.0;
        for (int i = Math.max(height - roundsLeft, 2); i <= height + roundsLeft; i += 2){
            total += prob(roundsLeft, height, i);
        }
        total += prob(roundsLeft, height, 0) / 2;
        return total;
    }

    private static double prob(int roundsLeft, int height, int i){
        double prob = 1;
        int up = i - height + (roundsLeft - Math.abs(i - height))/2;
        int down = roundsLeft - up;
        int r = roundsLeft;
        int p = roundsLeft;
        while (up > 1 || down > 1 || r > 1 || p > 0){  //Weird algorithm to avoid loss of precision.
            //Computes roundsLeft!/(2**roundsLeft*up!*down!)

            if ((prob >= 1.0 || r <= 1) && (up > 1 || down > 1 || p > 1)){
                if (p > 0){
                    p--;
                    prob /= 2;
                    continue;
                } else if (up > 1){
                    prob /= up--;
                    continue;
                } else if (down > 1){
                    prob /= down--;
                    continue;
                } else {
                    break;
                }
            }
            if (r > 1) {
                prob *= r--;
                continue;
            }
            break;
        }
        return prob;
    }

}