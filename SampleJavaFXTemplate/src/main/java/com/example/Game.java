package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Game {

	private HashMap<String, Player> players;
	private HashMap<String, Integer> amount;
	private int pot;
	private Scanner scan;
	private Random rand = new Random();
	private Player SBPlayer;
	private Player BBPlayer;
	private Player dealer;    
	private Player mc;  
	private int highestBet;
	private int lowestBet;    
	private int x = (int) (rand.nextInt()*200 + 50);

	public Game() {
		Player cpu1 = new Player("cpu1", 1000);
		Player cpu2 = new Player("cpu2", 1000);
		Player cpu3 = new Player("cpu3", 1000);
		players.put("cpu1", cpu1);
		players.put("cpu2", cpu2);
		players.put("cpu3", cpu3);
		Scanner scan = new Scanner(System.in);
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

	public void updatePot() {

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
			BBPlayer.makeMove("bet",x,highestBet);
			    
		}

	}

}
