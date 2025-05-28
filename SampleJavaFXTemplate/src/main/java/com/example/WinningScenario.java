// package com.example;
// import java.util.*;


// public class WinningScenario{

//     private TreeSet<Card> playerCards;
//     private TreeSet<Card> communityCards;


//     public WinningScenario(Player p, TreeSet<Card> c ){
//         playerCards = p.getPlayerCards();
//         communityCards = c;
//     } 

//     public String getScenario() {
//         TreeSet<Card> allCards = new TreeSet<Card>();
//         allCards.addAll(communityCards);
//         allCards.addAll(playerCards);

//         ArrayList<Card> cardList = new ArrayList<Card>(allCards);
//         Collections.sort(cardList); // ascending
//         Collections.reverse(cardList); // descending

//         Map<Integer, Integer> rankCounts = new HashMap<Integer, Integer>();
//         Map<String, ArrayList<Card>> suitMap = new HashMap<String, ArrayList<Card>>();

//         for (int i = 0; i < cardList.size(); i++) {
//             Card card = cardList.get(i);
//             int rank = card.getRank();
//             String suit = card.getSuit();

//             if (rankCounts.containsKey(rank)) {
//                 rankCounts.put(rank, rankCounts.get(rank) + 1);
//             } else {
//                 rankCounts.put(rank, 1);
//             }

//             if (!suitMap.containsKey(suit)) {
//                 suitMap.put(suit, new ArrayList<Card>());
//             }
//             suitMap.get(suit).add(card);
//         }

//         ArrayList<Card> flushCards = null;
//         for (Iterator<String> it = suitMap.keySet().iterator(); it.hasNext();) {
//             String suit = it.next();
//             ArrayList<Card> suitedCards = suitMap.get(suit);
//             if (suitedCards.size() >= 5) {
//                 flushCards = new ArrayList<Card>(suitedCards);
//                 Collections.sort(flushCards);
//                 Collections.reverse(flushCards);
//                 break;
//             }
//         }

//         TreeSet<Integer> uniqueRanksSet = new TreeSet<Integer>(rankCounts.keySet());
//         ArrayList<Integer> uniqueRanks = new ArrayList<Integer>(uniqueRanksSet);

//         boolean isStraight = false;
//         for (int i = 0; i <= uniqueRanks.size() - 5; i++) {
//             int r0 = uniqueRanks.get(i);
//             int r1 = uniqueRanks.get(i + 1);
//             int r2 = uniqueRanks.get(i + 2);
//             int r3 = uniqueRanks.get(i + 3);
//             int r4 = uniqueRanks.get(i + 4);
//             if (r1 - r0 == 1 && r2 - r1 == 1 && r3 - r2 == 1 && r4 - r3 == 1) {
//                 isStraight = true;
//                 break;
//             }
//         }
//         if (!isStraight && uniqueRanks.contains(14) && uniqueRanks.contains(2)
//                 && uniqueRanks.contains(3) && uniqueRanks.contains(4) && uniqueRanks.contains(5)) {
//             isStraight = true;
//         }

//         if (flushCards != null) {
//             ArrayList<Integer> flushRanks = new ArrayList<Integer>();
//             for (int i = 0; i < flushCards.size(); i++) {
//                 flushRanks.add(flushCards.get(i).getRank());
//             }

//             Collections.sort(flushRanks);
//             Collections.reverse(flushRanks);

//             for (int i = 0; i <= flushRanks.size() - 5; i++) {
//                 int r0 = flushRanks.get(i);
//                 int r1 = flushRanks.get(i + 1);
//                 int r2 = flushRanks.get(i + 2);
//                 int r3 = flushRanks.get(i + 3);
//                 int r4 = flushRanks.get(i + 4);
//                 if (r1 - r0 == -1 && r2 - r1 == -1 && r3 - r2 == -1 && r4 - r3 == -1) {
//                     if (r0 == 14) return "Royal Flush";
//                     return "Straight Flush";
//                 }
//             }

//             if (flushRanks.contains(14) && flushRanks.contains(2)
//                     && flushRanks.contains(3) && flushRanks.contains(4) && flushRanks.contains(5)) {
//                 return "Straight Flush";
//             }
//         }

//         int four = 0, three = 0, pairs = 0;
//         for (Iterator<Integer> it = rankCounts.values().iterator(); it.hasNext();) {
//             int count = it.next();
//             if (count == 4) four++;
//             else if (count == 3) three++;
//             else if (count == 2) pairs++;
//         }

//         if (four == 1) return "Four of a Kind";
//         if (three >= 1 && pairs >= 1) return "Full House";
//         if (flushCards != null) return "Flush";
//         if (isStraight) return "Straight";
//         if (three == 1) return "Three of a Kind";
//         if (pairs >= 2) return "Two Pair";
//         if (pairs == 1) return "One Pair";

//         return "High Card";
//     }


//     public Card getHighestCard() {
//         Card highest = null;
        
//         for (Card card : playerCards) {
//             if (highest == null || card.compareTo(highest) > 0) {
//                 highest = card;
//             }
//         }

//         for (Card card : communityCards) {
//             if (highest == null || card.compareTo(highest) > 0) {
//                 highest = card;
//             }
//         }

//         return highest;
//     }

// }
