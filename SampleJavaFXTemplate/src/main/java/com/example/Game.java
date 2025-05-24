package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Game {

	public HashMap<String, Player> players;
	public HashMap<String, Integer> amount;
	public int pot;
	public Scanner scan;
	public Random rand = new Random();
	public Player SBPlayer;
	public Player BBPlayer;
	public Player dealer;    
	public Player mc;  
	public int highestBet;
	public int lowestBet;    
	public int x = (int) (rand.nextInt()*200 + 50);

	public Game() {
		this.players = new HashMap<String, Player>();
		this.amount = new HashMap<String, Integer>();
		Player cpu1 = new Player("cpu1", 1000);
		Player cpu2 = new Player("cpu2", 1000);
		Player cpu3 = new Player("cpu3", 1000);
		players.put("cpu1", cpu1);
		players.put("cpu2", cpu2);
		players.put("cpu3", cpu3);
		amount.put("cpu1", 1000);
		amount.put("cpu2", 1000);
		amount.put("cpu3", 1000);
		pot = 0;
		highestBet = 0;
		lowestBet = 0;
	}

	public void addPlayers(String name) {
		Player p = new Player(name, 1000);
		players.put(name, p);
	}

	public int fold(String name) {
		players.remove(name);
		players.get(name).fold();
		return amount.get(name);
	}

	public void assignDealer() {
		int z = ((int) rand.nextInt() * (players.keySet().size() - 1)) + 1;
		String key = "";
		int count = 0;
		for (String x : players.keySet()) {
			if (count > z) {
				break;
			}
			key = x;
			count++;
		}

		Player d = players.get(key);

		if (!d.isBigBlind() && !d.isSmallBlind()) {
			d.setDealer();
			System.out.println("Player " + d.getName() + " is the dealer!");
			dealer = d;
		} else {
			assignDealer();
		}
	}

	public void assignBB() {
		int z = ((int) rand.nextInt() * (players.keySet().size() - 1)) + 1;
		String key = "";
		int count = 0;
		for (String x : players.keySet()) {
			if (count > z) {
				break;
			}
			key = x;
			count++;
		}

		Player d = players.get(key);

		if (!d.isDealer() && !d.isSmallBlind()) {
			d.setBB();
			System.out.println("Player " + d.getName() + " is the big blind!");
			BBPlayer = d;
		} else {
			assignBB();
		}
	}

	public void assignSB() {
		int z = ((int) rand.nextInt() * (players.keySet().size() - 1)) + 1;
		String key = "";
		int count = 0;
		for (String x : players.keySet()) {
			if (count > z) {
				break;
			}
			key = x;
			count++;
		}

		Player d = players.get(key);

		if (!d.isDealer() && !d.isBigBlind()) {
			d.setSB();
			System.out.println("Player " + d.getName() + " is the small blind!");
			SBPlayer = d;
		} else {
			assignSB();
		}

	}

  public void updatePot(){
    pot = 0;
    highestBet = 0;
    for (String x : players.keySet()){
      pot += players.get(x).getBet();
      if (players.get(x).getBet() > highestBet){
        highestBet = players.get(x).getBet();
      }
    }

    System.out.println("The pot has been updated, HighestBet: " + highestBet + "Pot: " + pot);
  }

	public void executeGame() {
		System.out.println("Welcome to Poker! Please enter your name:");
		String name = scan.nextLine();
		addPlayers(name);
		System.out.println("Time to assign the dealer and blinds!!");
		assignDealer();
		assignSB();
		assignBB();
		System.out.println("Its time for the small blind and big blind to make the bets ");
		if (!mc.isSmallBlind() && !mc.isBigBlind()){   
			SBPlayer.makeMove("bet",x,highestBet);
      System.out.println("The small blind has made bet of $" + SBPlayer.getBet());
      updatePot();
      BBPlayer.makeMove("bet", highestBet * 2, highestBet);
      System.out.println("The big blind has made bet of $" + SBPlayer.getBet());
      updatePot();
		} else if (mc.isSmallBlind()){
      System.out.println("Enter your SB bet:");
      int num = scan.nextInt();
      mc.makeMove("bet", num, highestBet);
      System.out.println("The small blind has made bet of $" + SBPlayer.getBet());
      updatePot();
      BBPlayer.makeMove("bet", highestBet * 2, highestBet);
      System.out.println("The big blind has made bet of $" + SBPlayer.getBet());
      updatePot();
    } else if (mc.isBigBlind()){
      SBPlayer.makeMove("bet",x,highestBet);
      System.out.println("The small blind has made bet of $" + SBPlayer.getBet());
      updatePot();
      System.out.println("Enter your BB bet (it should be higher than SB's bet):");
      int num2 = scan.nextInt();
      mc.makeMove("bet", num2, highestBet);
      System.out.println("The big blind has made bet of $" + SBPlayer.getBet());
      updatePot();
    }
    System.out.println("Now the small and big blinds have made the bets lets do it for others");
    

	}

}
