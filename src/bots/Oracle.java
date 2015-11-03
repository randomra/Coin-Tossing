package bots;

import java.util.*;
import java.util.Map.Entry;
import main.*;

public class Oracle implements Bot {

    @Override
    public String getName() {
        return "Oracle";
    }

    @Override
    public Action takeTurn(int blue_levers, int red_levers, String history, String memory, int roundNumber) {
        int roundsLeft = 100 - roundNumber;
        Map<Integer, Integer> rounds = new HashMap<>();
        int myScore = (Game.totalScore(history) + roundNumber) / 2;
        int difference = myScore*2 - roundNumber;
        int highestBlockedRound = -1;
        int bestScore = 0;
        boolean hasUsedBlueLever = false;

        Scanner scanner = new Scanner(memory);
        if (scanner.hasNext()) {
            //timeTravel toRound highestBlockedRound hasUsedBlueLever bestScore rounds round1 percent1 round2 percent2 round3 percent3...
            boolean triedTravel = scanner.nextBoolean();
            int time = scanner.nextInt();
            if (triedTravel){
                if (roundNumber > time) {
                    highestBlockedRound = time;
                }
            }
            highestBlockedRound = Math.max(highestBlockedRound, scanner.nextInt());

            hasUsedBlueLever = scanner.nextBoolean();
            bestScore = scanner.nextInt();

            int size = scanner.nextInt();
            for (int i = 0; i < size && i < roundNumber; i++) {
                int number = scanner.nextInt();
                int diff = scanner.nextInt();
                if (number < roundNumber) {
                    rounds.put(number, diff);
                }
            }
        }
        rounds.put(roundNumber, difference);
        final int blockedRound = highestBlockedRound;

        int roundToRevert = 0;
        if (rounds.size() > 2) {
            Optional<Entry<Integer, Integer>> bestRound = rounds.entrySet()
                    .stream()
                    .filter(x -> x.getKey() >= blockedRound && x.getKey() <= roundNumber)
                    .sorted(Comparator
                        .comparingInt((Entry<Integer, Integer> x) -> x.getValue()*-1)
                        .thenComparingInt(x -> x.getKey()))
                    .findFirst();
            if (bestRound.isPresent()) {
                roundToRevert = bestRound.get().getKey();
            }
        }

        if (roundsLeft + Game.totalScore(history) <= 0 && red_levers > 0) {
            roundToRevert = highestBlockedRound+1;
        } else if (blue_levers > 0 && roundToRevert == roundNumber && ((hasUsedBlueLever && difference >= bestScore*1.5) || (!hasUsedBlueLever && difference > 1))) {
            roundToRevert = -1;
            hasUsedBlueLever = true;
            bestScore = difference;
            highestBlockedRound = roundNumber;
        } else if (red_levers > 0 && roundToRevert > 0 && rounds.get(roundToRevert) > difference+2) {
            roundToRevert += 1;
        } else {
            roundToRevert = 0;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(roundToRevert > 0).append(' ');
        sb.append(roundToRevert).append(' ');
        sb.append(highestBlockedRound).append(' ');
        sb.append(hasUsedBlueLever).append(' ');
        sb.append(bestScore).append(' ');
        sb.append(rounds.size()).append(' ');
        rounds.entrySet().stream().forEach((entry) -> {
            sb.append(entry.getKey()).append(' ').append(entry.getValue()).append(' ');
        });
        String mem = sb.toString().trim();
        scanner.close();
        return new Action(roundToRevert, mem);
    }
}

