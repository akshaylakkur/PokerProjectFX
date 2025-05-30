package com.example;

import java.util.ArrayList;

public class Player {
    public String name;
    public int money;
    public ArrayList<Card> cards = new ArrayList<Card>();
    public int currentBet;
    public boolean folded;
    public boolean allIn;
    public boolean smallBlind;
    public boolean bigBlind;
    public boolean hasActed;
    public boolean isBankrupt;
    
    public Player(String name, int startingMoney) {
        this.name = name;
        this.money = startingMoney;
        this.cards = new ArrayList<>();
        this.currentBet = 0;
        this.folded = false;
        this.allIn = false;
        this.smallBlind = false;
        this.bigBlind = false;
        this.hasActed = false;
        this.isBankrupt = false;
    }
    
    public void addCard(Card card) {
        cards.add(card);
    }
    
    public void fold() {
        this.folded = true;
        this.hasActed = true;
        System.out.println(name + " folds");
    }
    
    public boolean canCheck(int highestBet) {
        return currentBet == highestBet;
    }
    
    public void check(int highestBet) {
        if (canCheck(highestBet)) {
            this.hasActed = true;
            System.out.println(name + " checks");
        } else {
            System.out.println(name + " cannot check - must call or raise");
        }
    }
    
    public void call(int highestBet) {
        int callAmount = highestBet - currentBet;
        if (callAmount <= 0) {
            // Already matched the bet, just check
            check(highestBet);
            return;
        }
        
        if (callAmount >= money) {
            // All-in call
            currentBet += money;
            money = 0;
            allIn = true;
            System.out.println(name + " goes all-in calling with $" + currentBet + " total");
        } else {
            money -= callAmount;
            currentBet += callAmount;
            System.out.println(name + " calls $" + callAmount + " (total bet: $" + currentBet + ")");
        }
        this.hasActed = true;
    }
    
    public void raise(int raiseAmount, int highestBet) {
        if (raiseAmount <= 0) {
            System.out.println("Raise amount must be positive!");
            return;
        }
        
        int totalBet = highestBet + raiseAmount;
        int additionalAmount = totalBet - currentBet;
        
        if (additionalAmount >= money) {
            // All-in raise - bet all remaining money
            int actualRaise = money - (highestBet - currentBet);
            if (actualRaise <= 0) {
                // Can't raise, just call
                call(highestBet);
                return;
            }
            currentBet += money;
            money = 0;
            allIn = true;
            System.out.println(name + " goes all-in raising to $" + currentBet + " total");
        } else {
            money -= additionalAmount;
            currentBet = totalBet;
            System.out.println(name + " raises by $" + raiseAmount + " to $" + currentBet + " total");
        }
        this.hasActed = true;
    }
    
    public void bet(int amount) {
        if (amount <= 0) return;
        
        if (amount >= money) {
            currentBet += money;
            money = 0;
            allIn = true;
            System.out.println(name + " bets all-in: $" + currentBet);
        } else {
            money -= amount;
            currentBet += amount;
            System.out.println(name + " bets $" + amount + " (total: $" + currentBet + ")");
        }
        this.hasActed = true;
    }
    
    public void changeMoney(int amount) {
        money += amount;
        if (money < 0) money = 0; // Prevent negative money
    }
    
    public void resetForNewHand() {
        folded = false;
        allIn = false;
        smallBlind = false;
        bigBlind = false;
        hasActed = false;
        currentBet = 0;
        cards.clear();
    }
    
    public void resetForNewBettingRound() {
        hasActed = false;
        // currentBet stays - it gets collected into pot by game manager
    }
    
    public boolean isActive() {
        return !folded && money > 0;
    }
    
    public boolean needsToAct(int highestBet) {
        return !folded && !allIn && (!hasActed || currentBet < highestBet);
    }

    public boolean isBankrupt(){
        if (money <= 0){
            isBankrupt = true;
        }
        return isBankrupt;
    }

    public ArrayList<Card> getPlayerCards(){
        return cards;
    }
    
    @Override
    public String toString() {
        return name + " (Money: $" + money + ", Current Bet: $" + currentBet + 
               (folded ? ", FOLDED" : "") + (allIn ? ", ALL-IN" : "") + ")";
    }
}
