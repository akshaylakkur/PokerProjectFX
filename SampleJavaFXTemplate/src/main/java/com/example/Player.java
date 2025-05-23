package com.example;
import java.util.*;
import java.util.Set;
import java.util.TreeSet;

public class Player{
    private int cardsRecieved = 2;
    private String name;
    private TreeSet<Card> cards = new TreeSet<Card>();
    private int currentBet;
    private boolean folded;
    private boolean allIn;
    private int money;
    private boolean dealer;
    private boolean smallBlind;
    private boolean bigBlind;
    
    public Player(String name, int money){
        this.name = name;
        folded = false;
        allIn = false;
        currentBet = 0;
        this.money = money;       
    }

    public void makeMove(String move) {
        move = move.toLowerCase();
        if (move.equals("allin")) {
            currentBet += money;
            money = 0;
            allIn = true;
        } else if (move.equals("fold")) {
            fold();
        }
    }
    public void makeMove(String move, int amt, int currentHighestBet) {
        move = move.toLowerCase();
        if (move.equals("bet")) {
            bet(amt);
        } else if (move.equals("call")) {
            call(currentHighestBet);
        } else if (move.equals("raise")) {
            raise(amt, currentHighestBet);
        }  else if (move.equals("check")) {
            canCheck(currentHighestBet);
        }
    }

    public void raise(int raiseAmount, int highestBet) {
        int toCall = highestBet - currentBet;
        int totalAmount = toCall + raiseAmount;

        if (totalAmount >= money) {
            currentBet += money;
            money = 0;
            allIn = true;
        } else {
            money -= totalAmount;
            currentBet += totalAmount;
        }
    }

    public void bet(int amount) {
        if (amount >= money) {
            currentBet += money;
            money = 0;
            allIn = true;
        } else {
            money -= amount;
            currentBet += amount;
        }
    }

    public void call(int highestBet) {
        int toCall = highestBet - currentBet;
        if (toCall >= money) {
            currentBet += money;
            money = 0;
            allIn = true;
        } else {
            money -= toCall;
            currentBet += toCall;
        }
    }

    public void fold(){
        folded = true;
        money -= currentBet;
        currentBet = 0;
    }

    public boolean canCheck(int highestBet) {
        return currentBet == highestBet;
    }

    public int getMoney(){
        return money;
    }
    
    public int getBet(){
        return currentBet;
    }

    public boolean isBetOverBalance(int bet){
        return (bet - money > 0);
    }

    public boolean isBankrupt(){
        return !(money > 0);
    }

    public boolean isFolded(){
        return folded;
    }

    public boolean isAllIn(){
        return allIn;
    }

    public boolean isDealer(){
        return dealer;
    }
    
    public boolean isSmallBlind(){
        return smallBlind;
    }

    public boolean isBigBlind(){
        return bigBlind;
    }

    public TreeSet<Card> getPlayerCards(){
        return cards;
    }

    public void setBB(){
        bigBlind = true;
    }

    public void setSB(){
        smallBlind = true;
    }

    public void setDealer(){
        dealer = true;
    }

    public void reset() {
        cards.clear();
        folded = false;
        allIn = false;
        currentBet = 0;
    }
}