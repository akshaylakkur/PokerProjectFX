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
        return "Blank";
    }
    






}



