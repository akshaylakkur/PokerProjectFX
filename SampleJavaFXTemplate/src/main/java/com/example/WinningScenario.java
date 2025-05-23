package com.example;
import java.util.*;


public class WinningScenario{

    private TreeSet<Card> playerCards;
    private TreeSet<Card> communityCards;


    public WinningScenario(Player p, TreeSet<Card> c ){
        playerCards = p.getPlayerCards();
        communityCards = c;
    } 

    public String getScenario() {
    TreeSet<Card> allCards = new TreeSet<Card>();
    allCards.addAll(communityCards);
    allCards.addAll(playerCards);

    ArrayList<Card> cardList = new ArrayList<Card>(allCards);

    // descending sort cuz its easier   
    for (int i = 0; i < cardList.size() - 1; i++) {
        for (int j = i + 1; j < cardList.size(); j++) {
            if (cardList.get(j).getRank() > cardList.get(i).getRank()) {
                Card temp = cardList.get(i);
                cardList.set(i, cardList.get(j));
                cardList.set(j, temp); 
            }    
        }
    }

    Map<Integer, Integer> rankCounts = new HashMap<Integer, Integer>(); // mapped by ranks number 
    Map<String, ArrayList<Card>> suitMap = new HashMap<String, ArrayList<Card>>(); // mapped by suit (both of these are here to make it easier to compare )

    for (int i = 0; i < cardList.size(); i++) {
        Card card = cardList.get(i);
        int rank = card.getRank();
        String suit = card.getSuit();

        // Count ranks
        if (rankCounts.containsKey(rank)) {
            rankCounts.put(rank, rankCounts.get(rank) + 1);
        } else {
            rankCounts.put(rank, 1);
        }

        // Group by suit
        if (!suitMap.containsKey(suit)) {
            suitMap.put(suit, new ArrayList<Card>());
        }
        suitMap.get(suit).add(card);
    }

    // Check for flush
    ArrayList<Card> flushCards = null;
    for (String suit : suitMap.keySet()) {
        ArrayList<Card> suitedCards = suitMap.get(suit);
        if (suitedCards.size() >= 5) {
            flushCards = suitedCards;
            break;
        }
    }

    // Check for straight
    TreeSet<Integer> uniqueRanksSet = new TreeSet<Integer>();
    for (Integer r : rankCounts.keySet()) {
        uniqueRanksSet.add(r);
    }

    List<Integer> uniqueRanks = new ArrayList<Integer>(uniqueRanksSet);

    boolean isStraight = false;
    int consecutive = 1;
    for (int i = 1; i < uniqueRanks.size(); i++) {
        if (uniqueRanks.get(i) - uniqueRanks.get(i - 1) == 1) {
            consecutive++;
            if (consecutive >= 5) {
                isStraight = true;
                break;
            }
        } else {
            consecutive = 1;
        }
    }

    // Special case: A-2-3-4-5
    if (!isStraight && uniqueRanks.contains(14) && uniqueRanks.contains(2)
            && uniqueRanks.contains(3) && uniqueRanks.contains(4) && uniqueRanks.contains(5)) {
        isStraight = true;
    }

    // staright flusha nd royale flush check, (these are hella rare)
    if (flushCards != null) {
        TreeSet<Integer> flushRanksSet = new TreeSet<Integer>();
        for (int i = 0; i < flushCards.size(); i++) {
            flushRanksSet.add(flushCards.get(i).getRank());
        }
        List<Integer> flushRanks = new ArrayList<Integer>(flushRanksSet);

        consecutive = 1;
        for (int i = 1; i < flushRanks.size(); i++) {
            if (flushRanks.get(i) - flushRanks.get(i - 1) == 1) {
                consecutive++;
                if (consecutive >= 5) {
                    if (flushRanks.get(i) == 14) return "Royal Flush";
                    return "Straight Flush";
                }
            } else {
                consecutive = 1;
            }
        }

        // Special straight flush case: A-2-3-4-5
        if (flushRanks.contains(14) && flushRanks.contains(2)
                && flushRanks.contains(3) && flushRanks.contains(4) && flushRanks.contains(5)) {
            return "Straight Flush";
        }
    }

    // if all the big stuff doesnt work it counts to find the more likely small stuff
    // this part is the most important the other stuff above are only for extreme cases
    int four = 0, three = 0, pairs = 0;
    for (Integer count : rankCounts.values()) {
        if (count == 4) four++;
        else if (count == 3) three++;
        else if (count == 2) pairs++;
    }

    if (four == 1) return "Four of a Kind";
    if (three == 1 && pairs >= 1) return "Full House";
    if (flushCards != null) return "Flush";
    if (isStraight) return "Straight";
    if (three == 1) return "Three of a Kind";
    if (pairs >= 2) return "Two Pair";
    if (pairs == 1) return "One Pair";

    return "High Card";
}


    






}



