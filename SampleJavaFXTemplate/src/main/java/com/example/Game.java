package com.example;
import java.util.*;

public class Game {

	public HashMap<String, Player> players;
	public HashMap<String, Integer> amount;
	public int pot;
	public Scanner scan;
	public Random rand = new Random();
	public Player SBPlayer;
	public Player BBPlayer;
	public Player dealer;    
	public int highestBet;
	public int lowestBet;    
	public int randbet = (int) (rand.nextInt()*200 + 50);
	public Deck deck = new Deck();
	public TreeSet<Card> communityCards = new TreeSet<Card>();
	public String playerN = "";

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
		playerN = name;
		Player p = new Player(name, 1000);
		players.put(name, p);
	}

	public int fold(String name) {
		players.remove(name);
		players.get(name).fold();
		return amount.get(name);
	}

	public String assignDealer() {
		int l = players.keySet().size();
		Random rand = new Random();
		int index = rand.nextInt(l);
		String dealer = players.keySet().toArray(new String[0])[index];
		System.out.println("Player " + dealer + " is the dealer!");
		return dealer;
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


    public String randBotMove(){
		int x = rand.nextInt(9) + 1;

		if (x == 1){
			return "fold";
		} else if (x >= 2 && x < 7){
			return "raise";
		} else {
			return "call";
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
		System.out.println("Now each person gets 2 cards!");
		for (String e : players.keySet()){
			players.get(e).addCard(deck.dealCard());
			players.get(e).addCard(deck.dealCard());
		}
		System.out.println("Its time for the small blind and big blind to make the bets ");
		if (!players.get(playerN).isSmallBlind() && !players.get(playerN).isBigBlind()){   
			SBPlayer.makeMove("bet",randbet,highestBet);
		System.out.println("The small blind has made bet of $" + SBPlayer.getBet());
		updatePot();
		BBPlayer.makeMove("bet", highestBet * 2, highestBet);
		System.out.println("The big blind has made bet of $" + SBPlayer.getBet());
		updatePot();
			} else if (players.get(playerN).isSmallBlind()){
		System.out.println("Enter your SB bet:");
		int num = scan.nextInt();
		players.get(playerN).makeMove("bet", num, highestBet);
		System.out.println("The small blind has made bet of $" + SBPlayer.getBet());
		updatePot();
		BBPlayer.makeMove("bet", highestBet * 2, highestBet);
		System.out.println("The big blind has made bet of $" + SBPlayer.getBet());
		updatePot();
		} else if (players.get(playerN).isBigBlind()){
		SBPlayer.makeMove("bet",randbet,highestBet);
		System.out.println("The small blind has made bet of $" + SBPlayer.getBet());
		updatePot();
		System.out.println("Enter your BB bet (it should be higher than SB's bet):");
		int num2 = scan.nextInt();
		players.get(playerN).makeMove("bet", num2, highestBet);
		System.out.println("The big blind has made bet of $" + SBPlayer.getBet());
		updatePot();
		}
    	System.out.println("Now the small and big blinds have made the bets lets do it for others");

		for (String x : players.keySet()){
			if (!players.get(x).equals(players.get(playerN)) && players.get(x).getBet() == 0) {
				players.get(x).makeMove(randBotMove(),randbet, highestBet);
				updatePot();
			} else if (players.get(x).equals(players.get(playerN))){  
				System.out.println(players.get(playerN).getName() + " please enter your move");
				String move = scan.nextLine();
				System.out.println(players.get(playerN).getName() + "please enter your change amount (if you are doing call it doesnt matter)" ); 
				int am = scan.nextInt();
				players.get(playerN).makeMove(move, am, highestBet);
				updatePot();
			}     

			System.out.println("Now it is time for the flop! Let the first 3 cards be dealt!");   
			communityCards.add(deck.dealCard());
			communityCards.add(deck.dealCard());  
			communityCards.add(deck.dealCard());  
        
			System.out.println("Time for #2 round betting "); 
			for(String p : players.keySet()){
				if (players.get(p).equals(players.get(playerN))){
					System.out.println(players.get(playerN).getName() + "please enter your move"); 
					String secMove = scan.nextLine();
					System.out.println(players.get(playerN).getName() + ", please enter your bet amount");
					int bet2 = scan.nextInt();
					players.get(playerN).makeMove(secMove, bet2, highestBet);
					updatePot();
				} else if (!players.get(p).equals(players.get(playerN))){     
					players.get(p).makeMove(randBotMove(), randbet, highestBet);
					updatePot();
				}
			}

			
	
		}

     

	}

}
