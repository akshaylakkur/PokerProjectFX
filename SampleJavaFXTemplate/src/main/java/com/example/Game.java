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

	public String assignDealer() {
		int l = players.keySet().size();
		Random rand = new Random();
		int index = rand.nextInt(l);
		String dealer = players.keySet().toArray(new String[0])[index];
		System.out.println("Player " + dealer + " is the dealer!");
		return dealer;
	}

	public void assignBB() {
		ArrayList<Player> playerList = new ArrayList<>(players.values());
		Collections.shuffle(playerList);

		for (Player p : playerList) {
			if (!p.isDealer() && !p.isSmallBlind()) {
				p.setSB();
				SBPlayer = p;
				System.out.println("Player " + p.getName() + " is the small blind!");
				return;
			}
		}
	}

	public void assignSB() {
		ArrayList<Player> playerList = new ArrayList<>(players.values());
		Collections.shuffle(playerList);

		for (Player p : playerList) {
			if (!p.isDealer() && !p.isBigBlind()) {
				p.setSB();
				SBPlayer = p;
				System.out.println("Player " + p.getName() + " is the small blind!");
				return;
			}
		}
	}

	public void updatePot() {
		pot = 0;
		highestBet = 0;
		for (String x : players.keySet()) {
			pot += players.get(x).getBet();
			if (players.get(x).getBet() > highestBet) {
				highestBet = players.get(x).getBet();
			}
		}

		System.out.println("The pot has been updated, HighestBet: " + highestBet + "Pot: " + pot);
	}

	public String randBotMove() {
		int x = rand.nextInt(9) + 1;

		if (x == 1) {
			return "fold";
		} else if (x >= 2 && x < 7) {
			return "raise";
		} else {
			return "call";
		}
	}

	public void winner() {
		Map<String, String> playerScenarios = new HashMap<>();
		String bestScenario = null;
		List<String> winners = new ArrayList<>();

		List<String> ranks = new ArrayList<String>();
		ranks.add("High Card");
		ranks.add("One Pair");
		ranks.add("Two Pair");
		ranks.add("Three of a Kind");
		ranks.add("Straight");
		ranks.add("Flush");
		ranks.add("Full House");
		ranks.add("Four of a Kind");
		ranks.add("Straight Flush");
		ranks.add("Royal Flush");

		for (String name : players.keySet()) {
			Player p = players.get(name);
			String scenario = new WinningScenario(p, communityCards).getScenario();
			playerScenarios.put(name, scenario);

			if (bestScenario == null || ranks.indexOf(scenario) > ranks.indexOf(bestScenario)) {
				bestScenario = scenario;
				winners.clear();
				winners.add(name);
			} else if (ranks.indexOf(scenario) == ranks.indexOf(bestScenario)) {
				winners.add(name);
			}
		}

		if (winners.size() == 1) {
			String winnerName = winners.get(0);
			System.out.println("Winner is " + winnerName + " with " + bestScenario);
			players.get(winnerName).changeMoney(pot);
		} else {
			System.out.print("Tie between: ");
			for (String name : winners) {
				System.out.print(name + " ");
			}
			System.out.println("with " + bestScenario);
			int splitPot = pot / winners.size();
			for (String name : winners) {
				players.get(name).changeMoney(splitPot);
			}
		}
	}

	public void resetGame() {
		for (String x : players.keySet()) {
			players.get(x).reset();
			players.get(x).changeMoney(1000);
		}
		pot = 0;
		highestBet = 0;
		communityCards.clear();
		dealer = null;
		SBPlayer = null;
		BBPlayer = null;
	}

	public void executeGame() {
		boolean continueGame = true;
		System.out.println("Welcome to Poker! Please enter your name:");
		String name = scan.nextLine();
		addPlayers(name);
		while (continueGame) {
			resetGame();
			System.out.println("Time to assign the dealer and blinds!!");
			assignDealer();
			assignSB();
			assignBB();
			Deck deck = new Deck();
			System.out.println("Now each person gets 2 cards!");
			for (String e : players.keySet()) {
				players.get(e).addCard(deck.dealCard());
				players.get(e).addCard(deck.dealCard());
			}

			System.out.println("Its time for the small blind and big blind to make the bets ");
			if (!players.get(playerN).isSmallBlind() && !players.get(playerN).isBigBlind()) {
				int randBet = rand.nextInt(200) + 50;
				SBPlayer.makeMove("bet", randBet, highestBet);
				System.out.println("The small blind has made bet of $" + SBPlayer.getBet());
				updatePot();
				BBPlayer.makeMove("bet", highestBet * 2, highestBet);
				System.out.println("The big blind has made bet of $" + BBPlayer.getBet());
				updatePot();
			} else if (players.get(playerN).isSmallBlind()) {
				System.out.println("Enter your SB bet:");
				int num = scan.nextInt();
				scan.nextLine();
				players.get(playerN).makeMove("bet", num, highestBet);
				System.out.println("The small blind has made bet of $" + SBPlayer.getBet());
				updatePot();
				BBPlayer.makeMove("bet", highestBet * 2, highestBet);
				System.out.println("The big blind has made bet of $" + BBPlayer.getBet());
				updatePot();
			} else if (players.get(playerN).isBigBlind()) {
				int randBet = rand.nextInt(200) + 50;
				SBPlayer.makeMove("bet", randBet, highestBet);
				System.out.println("The small blind has made bet of $" + SBPlayer.getBet());
				updatePot();
				System.out.println("Enter your BB bet (it should be higher than SB's bet):");
				int num2 = scan.nextInt();
				scan.nextLine();
				players.get(playerN).makeMove("bet", num2, highestBet);
				System.out.println("The big blind has made bet of $" + BBPlayer.getBet());
				updatePot();
			}
			System.out.println("Now the small and big blinds have made the bets lets do it for others");

			for (String x : players.keySet()) {
				if (!players.get(x).equals(players.get(playerN)) && players.get(x).getBet() == 0) {
					int randBet = rand.nextInt(200) + 50;
					players.get(x).makeMove(randBotMove(), randBet, highestBet);
					updatePot();
				} else if (players.get(x).equals(players.get(playerN)) && players.get(x).getBet() == 0) {
					System.out.println(players.get(playerN).getName() + " please enter your move");
					String move = scan.nextLine();
					System.out.println(players.get(playerN).getName() + ", please enter your bet amount, it only matters if you choose to raise");
					int am = scan.nextInt();
					scan.nextLine();
					players.get(playerN).makeMove(move, am, highestBet);
					updatePot();
				}
			}

			System.out.println("Now it is time for the flop! Let the first 3 cards be dealt!");
			communityCards.add(deck.dealCard());
			communityCards.add(deck.dealCard());
			communityCards.add(deck.dealCard());

			System.out.println("Time for #2 round betting ");
			for (String p : players.keySet()) {
				if (players.get(p).equals(players.get(playerN)) && !players.get(p).isAllIn() && !players.get(p).isFolded()) {
					System.out.println(players.get(playerN).getName() + "please enter your move");
					String secMove = scan.nextLine();
					System.out.println(players.get(playerN).getName() + ", please enter your bet amount, it only matters if you choose to raise");
					int bet2 = scan.nextInt();
					scan.nextLine();
					players.get(playerN).makeMove(secMove, bet2, highestBet);
					updatePot();
				} else if (!players.get(p).equals(players.get(playerN)) && !players.get(p).isAllIn() && !players.get(p).isFolded()) {
					int randBet = rand.nextInt(200) + 50;
					players.get(p).makeMove(randBotMove(), randBet, highestBet);
					updatePot();
				}
			}

			System.out.println("Now it is time for the Turn! Let the 4th card be dealt");
			communityCards.add(deck.dealCard());
			System.out.println("Time for #3 round betting ");
			for (String p2 : players.keySet()) {
				if (players.get(p2).equals(players.get(playerN)) && !players.get(p2).isAllIn() && !players.get(p2).isFolded()) {
					System.out.println(players.get(playerN).getName() + " please enter your move");
					String thirdMove = scan.nextLine();
					System.out.println(players.get(playerN).getName() + ", please enter your bet amount, it only matters if you choose to raise");
					int bet3 = scan.nextInt();
					scan.nextLine();
					players.get(playerN).makeMove(thirdMove, bet3, highestBet);
					updatePot();
				} else if (!players.get(p2).equals(players.get(playerN)) && !players.get(p2).isAllIn() && !players.get(p2).isFolded()) {
					int randBet = rand.nextInt(200) + 50;
					players.get(p2).makeMove(randBotMove(), randBet, highestBet);
					updatePot();
				}
			}

			System.out.println("Now it is time for the River, The final Card! Let the 5th card be dealt");
			communityCards.add(deck.dealCard());
			System.out.println("Time for #4 round (final) betting ");
			for (String p3 : players.keySet()) {
				if (players.get(p3).equals(players.get(playerN)) && !players.get(p3).isAllIn() && !players.get(p3).isFolded()) {
					System.out.println(players.get(playerN).getName() + " please enter your move");
					String finalMove = scan.nextLine();
					System.out.println(players.get(playerN).getName() + ", please enter your bet amount, it only matters if you choose to raise");
					int bet4 = scan.nextInt();
					scan.nextLine();
					players.get(playerN).makeMove(finalMove, bet4, highestBet);
					updatePot();
				} else if (!players.get(p3).equals(players.get(playerN)) && !players.get(p3).isAllIn() && !players.get(p3).isFolded()) {
					int randBet = rand.nextInt(200) + 50;
					players.get(p3).makeMove(randBotMove(), randBet, highestBet);
					updatePot();
				}
			}

			System.out.println("Now that everyone has made their final bets, lets compare hands to determine the winner!");
			winner();

			System.out.println("The round is over! If you would like to start another round press Y else press N");
			String ans = scan.nextLine();
			if (ans.equals("N")) {
				continueGame = false;
			}

		}

	}

}
