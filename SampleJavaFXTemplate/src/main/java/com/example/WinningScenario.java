package com.example;
import java.util.*;


public class WinningScenario{

    private TreeSet<Card> playerCards;
    private TreeSet<Card> communityCards;
    private Player player;


    public WinningScenario(Player p, TreeSet<Card> c ){
        playerCards = p.getPlayerCards();
        communityCards = c;
        player = p;
    }

    public String getScenario(){
        TreeSet<Card> allCards = new TreeSet<Card>();
        Iterator<Card> x = communityCards.iterator();

        while (x.hasNext()){
            Card c = x.next();
            allCards.add(c);
        }

        Iterator<Card> y = player.getPlayerCards().iterator();

        while (y.hasNext()){
            Card l = y.next();
            allCards.add(l);
        }

        return "hi";
    }


    






}



