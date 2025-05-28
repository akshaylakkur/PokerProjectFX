package com.example;

import java.util.*;

public class gameV2 {
    public HashMap<String, Player> players = new HashMap<>();
    public String[] playerNames;
    public Deck deck;
    public ArrayList<Card> communityCards;
    public int pot;
    public int highestBet;
    public int smallBlindAmount = 10;
    public int bigBlindAmount = 20;
    public String currentPlayerName;
    public int currentPlayerIndex;
    public ArrayList<String> turnOrder;
    public GameState gameState;
    public int dealerIndex;
    public boolean gameInProgress;
    public String humanPlayerName;
    public boolean waitingForHumanInput;
    
    public enum GameState {
        WAITING_TO_START, PRE_FLOP, FLOP, TURN, RIVER, SHOWDOWN, HAND_OVER
    }
    
    public gameV2(String humanPlayer) {
        this.humanPlayerName = humanPlayer;
        playerNames = new String[] {"cpu1", "cpu2", "cpu3", "cpu4", "cpu5"};
        
        // Add human player first
        players.put(humanPlayer, new Player(humanPlayer, 1000));
        
        // Add CPU players
        for (String name : playerNames) {
            players.put(name, new Player(name, 1000));
        }
        
        deck = new Deck();
        communityCards = new ArrayList<>();
        pot = 0;
        highestBet = 0;
        dealerIndex = 0;
        gameState = GameState.WAITING_TO_START;
        gameInProgress = false;
        waitingForHumanInput = false;
        
        // Create turn order starting with human player
        turnOrder = new ArrayList<>();
        turnOrder.add(humanPlayer);
        turnOrder.addAll(Arrays.asList(playerNames));
        
        currentPlayerIndex = 0;
        currentPlayerName = turnOrder.get(currentPlayerIndex);
    }
    
    public void startNewGame() {
        if (gameInProgress) {
            System.out.println("Game already in progress! Finishing current hand...");
            return;
        }
        
        System.out.println("=== NEW TEXAS HOLD'EM HAND STARTED ===");
        System.out.println("Players: " + players.keySet());
        
        gameInProgress = true;
        waitingForHumanInput = false;
        
        // Reset for new hand
        resetForNewHand();
        dealInitialCards();
        setBlinds();
        gameState = GameState.PRE_FLOP;
        
        // Start first betting round
        startBettingRound();
    }
    
    private void startBettingRound() {
        System.out.println("\n=== " + gameState + " BETTING ROUND ===");
        if (!communityCards.isEmpty()) {
            System.out.println("Community Cards: " + communityCards);
        }
        
        // Reset all players for new betting round
        for (Player player : players.values()) {
            player.resetForNewBettingRound();
        }
        
        // Find first player to act
        findFirstPlayerToAct();
        
        // If it's human's turn, wait for input
        if (currentPlayerName.equals(humanPlayerName) && players.get(humanPlayerName).needsToAct(highestBet)) {
            waitingForHumanInput = true;
            displayPlayerTurnInfo();
        } else {
            // Continue with CPU players
            processCPUTurns();
        }
    }
    
    private void findFirstPlayerToAct() {
        // For pre-flop, start after big blind. For other rounds, start after dealer
        if (gameState == GameState.PRE_FLOP) {
            currentPlayerIndex = (dealerIndex + 3) % turnOrder.size(); // After big blind
        } else {
            currentPlayerIndex = (dealerIndex + 1) % turnOrder.size(); // After dealer
        }
        
        // Find first active player
        int attempts = 0;
        while (attempts < turnOrder.size()) {
            currentPlayerName = turnOrder.get(currentPlayerIndex);
            Player currentPlayer = players.get(currentPlayerName);
            if (currentPlayer.needsToAct(highestBet)) {
                break;
            }
            currentPlayerIndex = (currentPlayerIndex + 1) % turnOrder.size();
            attempts++;
        }
        
        if (attempts >= turnOrder.size()) {
            // No one needs to act, move to next stage
            advanceGameState();
        }
    }
    
    private void processCPUTurns() {
        while (gameInProgress && !waitingForHumanInput && needMoreActions()) {
            Player currentPlayer = players.get(currentPlayerName);
            
            if (currentPlayer.needsToAct(highestBet)) {
                if (currentPlayerName.equals(humanPlayerName)) {
                    waitingForHumanInput = true;
                    displayPlayerTurnInfo();
                    return;
                } else {
                    makeCPUMove(currentPlayer);
                }
            }
            
            moveToNextPlayer();
        }
        
        // If we exit the loop and no one is waiting for input, advance game state
        if (!waitingForHumanInput && !needMoreActions()) {
            advanceGameState();
        }
    }
    
    private boolean needMoreActions() {
        List<Player> activePlayers = getActivePlayers();
        if (activePlayers.size() <= 1) {
            return false; // Only one player left
        }
        
        // Check if any active player still needs to act
        for (Player player : activePlayers) {
            if (player.needsToAct(highestBet)) {
                return true;
            }
        }
        return false;
    }
    
    private void displayPlayerTurnInfo() {
        Player currentPlayer = players.get(currentPlayerName);
        System.out.println("\n" + currentPlayerName + "'s turn to act");
        System.out.println("Your cards: " + currentPlayer.cards);
        System.out.println("Your money: $" + currentPlayer.money);
        System.out.println("Current bet: $" + currentPlayer.currentBet + " | Highest bet: $" + highestBet);
        System.out.println("Pot: $" + pot);
        
        if (currentPlayer.canCheck(highestBet)) {
            System.out.println("Options: Fold, Check, or Raise");
        } else {
            int callAmount = highestBet - currentPlayer.currentBet;
            System.out.println("Options: Fold, Call $" + callAmount + ", or Raise");
        }
    }
    
    public void processHumanMove(String action, int amount) {
        if (!gameInProgress || !currentPlayerName.equals(humanPlayerName) || !waitingForHumanInput) {
            System.out.println("It's not your turn or game is not in progress!");
            return;
        }
        
        Player humanPlayer = players.get(humanPlayerName);
        
        switch (action.toLowerCase()) {
            case "fold":
                humanPlayer.fold();
                break;
            case "check":
                if (humanPlayer.canCheck(highestBet)) {
                    humanPlayer.check(highestBet);
                } else {
                    System.out.println("Cannot check! Must call or raise.");
                    return;
                }
                break;
            case "call":
                humanPlayer.call(highestBet);
                updateHighestBet(humanPlayer.currentBet);
                break;
            case "raise":
                if (amount <= 0) {
                    System.out.println("Raise amount must be positive!");
                    return;
                }
                humanPlayer.raise(amount, highestBet);
                updateHighestBet(humanPlayer.currentBet);
                break;
            default:
                System.out.println("Invalid action: " + action);
                return;
        }
        
        waitingForHumanInput = false;
        moveToNextPlayer();
        
        // Continue with CPU turns or advance game state
        processCPUTurns();
    }
    
    private void makeCPUMove(Player cpu) {
        Random rand = new Random();
        
        // Simple CPU AI logic
        if (cpu.canCheck(highestBet)) {
            // Can check - 70% chance to check, 30% to raise
            if (rand.nextDouble() < 0.7) {
                cpu.check(highestBet);
            } else {
                int raiseAmount = rand.nextInt(50) + 20; // Raise 20-70
                cpu.raise(raiseAmount, highestBet);
                updateHighestBet(cpu.currentBet);
            }
        } else {
            // Must call or raise or fold
            double action = rand.nextDouble();
            if (action < 0.15) { // 15% chance to fold
                cpu.fold();
            } else if (action < 0.75) { // 60% chance to call
                cpu.call(highestBet);
                updateHighestBet(cpu.currentBet);
            } else { // 25% chance to raise
                int raiseAmount = rand.nextInt(100) + 25;
                cpu.raise(raiseAmount, highestBet);
                updateHighestBet(cpu.currentBet);
            }
        }
        
        // Small delay for better UX
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void moveToNextPlayer() {
        int attempts = 0;
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % turnOrder.size();
            currentPlayerName = turnOrder.get(currentPlayerIndex);
            attempts++;
        } while (attempts < turnOrder.size() && 
                (!players.get(currentPlayerName).needsToAct(highestBet)));
    }
    
    private void updateHighestBet(int bet) {
        if (bet > highestBet) {
            highestBet = bet;
            
            // Reset hasActed for players who now need to act again
            for (Player player : players.values()) {
                if (!player.folded && !player.allIn && player.currentBet < highestBet) {
                    player.hasActed = false;
                }
            }
        }
    }
    
    private void advanceGameState() {
        // Collect all bets into pot
        collectBetsIntoPot();
        
        List<Player> activePlayers = getActivePlayers();
        if (activePlayers.size() <= 1) {
            endHand();
            return;
        }
        
        switch (gameState) {
            case PRE_FLOP:
                dealFlop();
                gameState = GameState.FLOP;
                break;
            case FLOP:
                dealTurn();
                gameState = GameState.TURN;
                break;
            case TURN:
                dealRiver();
                gameState = GameState.RIVER;
                break;
            case RIVER:
                gameState = GameState.SHOWDOWN;
                showdown();
                return;
            default:
                endHand();
                return;
        }
        
        // Reset betting for new round
        highestBet = 0;
        startBettingRound();
    }
    
    private void collectBetsIntoPot() {
        System.out.println("Collecting bets into pot...");
        for (Player player : players.values()) {
            if (player.currentBet > 0) {
                pot += player.currentBet;
                System.out.println("Collected $" + player.currentBet + " from " + player.name);
                player.currentBet = 0;
            }
        }
        System.out.println("Total pot: $" + pot);
    }
    
    private List<Player> getActivePlayers() {
        List<Player> active = new ArrayList<>();
        for (Player player : players.values()) {
            if (!player.folded) {
                active.add(player);
            }
        }
        return active;
    }
    
    private void resetForNewHand() {
        deck = new Deck();
        communityCards.clear();
        pot = 0;
        highestBet = 0;
        waitingForHumanInput = false;
        
        // Reset all players for new hand
        for (Player player : players.values()) {
            player.resetForNewHand();
        }
    }
    
    private void dealInitialCards() {
        System.out.println("Dealing hole cards...");
        for (int i = 0; i < 2; i++) {
            for (String playerName : turnOrder) {
                Player player = players.get(playerName);
                if (player.money > 0) { // Only deal to players with money
                    player.addCard(deck.dealCard());
                }
            }
        }
        
        // Show human player's cards
        Player human = players.get(humanPlayerName);
        System.out.println("Your hole cards: " + human.cards);
    }
    
    private void setBlinds() {
        // Small blind
        int sbIndex = (dealerIndex + 1) % turnOrder.size();
        String sbPlayer = turnOrder.get(sbIndex);
        Player sbPlayerObj = players.get(sbPlayer);
        sbPlayerObj.bet(Math.min(smallBlindAmount, sbPlayerObj.money));
        sbPlayerObj.smallBlind = true;
        updateHighestBet(sbPlayerObj.currentBet);
        
        // Big blind
        int bbIndex = (dealerIndex + 2) % turnOrder.size();
        String bbPlayer = turnOrder.get(bbIndex);
        Player bbPlayerObj = players.get(bbPlayer);
        bbPlayerObj.bet(Math.min(bigBlindAmount, bbPlayerObj.money));
        bbPlayerObj.bigBlind = true;
        updateHighestBet(bbPlayerObj.currentBet);
        
        System.out.println(sbPlayer + " posts small blind: $" + sbPlayerObj.currentBet);
        System.out.println(bbPlayer + " posts big blind: $" + bbPlayerObj.currentBet);
    }
    
    private void dealFlop() {
        deck.dealCard(); // Burn card
        for (int i = 0; i < 3; i++) {
            communityCards.add(deck.dealCard());
        }
        System.out.println("FLOP: " + communityCards.subList(0, 3));
    }
    
    private void dealTurn() {
        deck.dealCard(); // Burn card
        communityCards.add(deck.dealCard());
        System.out.println("TURN: " + communityCards.get(3));
    }
    
    private void dealRiver() {
        deck.dealCard(); // Burn card
        communityCards.add(deck.dealCard());
        System.out.println("RIVER: " + communityCards.get(4));
    }
    
    private void showdown() {
        System.out.println("\n=== SHOWDOWN ===");
        List<Player> activePlayers = getActivePlayers();
        
        // Collect any remaining bets
        collectBetsIntoPot();
        
        // Display all active players' cards
        for (Player player : activePlayers) {
            System.out.println(player.name + "'s cards: " + player.cards);
        }
        System.out.println("Final community cards: " + communityCards);
        
        // Simple random winner selection (replace with actual hand evaluation)
        Random rand = new Random();
        Player winner = activePlayers.get(rand.nextInt(activePlayers.size()));
        
        System.out.println("\nWinner: " + winner.name + " wins $" + pot + "!");
        winner.changeMoney(pot);
        
        endHand();
    }
    
    private void endHand() {
        List<Player> activePlayers = getActivePlayers();
        
        // Collect any remaining bets
        collectBetsIntoPot();
        
        if (activePlayers.size() == 1 && pot > 0) {
            Player winner = activePlayers.get(0);
            System.out.println("\n" + winner.name + " wins $" + pot + " (all others folded)");
            winner.changeMoney(pot);
        }
        
        pot = 0;
        gameState = GameState.HAND_OVER;
        gameInProgress = false;
        waitingForHumanInput = false;
        
        // Move dealer button
        dealerIndex = (dealerIndex + 1) % turnOrder.size();
        
        System.out.println("\n=== HAND COMPLETE ===");
        displayPlayerChips();
        
        // Check if game should continue
        if (!isGameOver()) {
            System.out.println("Ready for next hand! Click 'Start Game' to continue.");
            gameState = GameState.WAITING_TO_START;
        } else {
            System.out.println("GAME OVER! Only one player has chips remaining.");
        }
    }
    
    private boolean isGameOver() {
        int playersWithMoney = 0;
        for (Player player : players.values()) {
            if (player.money > 0) {
                playersWithMoney++;
            }
        }
        return playersWithMoney <= 1;
    }
    
    private void displayPlayerChips() {
        System.out.println("\n=== CHIP COUNTS ===");
        for (String playerName : turnOrder) {
            Player player = players.get(playerName);
            System.out.println(player.name + ": $" + player.money);
        }
    }
    
    // Getters for GUI integration
    public String getCurrentPlayerName() {
        return currentPlayerName;
    }
    
    public boolean isHumanPlayerTurn() {
        return currentPlayerName.equals(humanPlayerName) && waitingForHumanInput;
    }
    
    public GameState getGameState() {
        return gameState;
    }
    
    public List<Card> getCommunityCards() {
        return new ArrayList<>(communityCards);
    }
    
    public int getPot() {
        return pot;
    }
    
    public int getHighestBet() {
        return highestBet;
    }
    
    public boolean isWaitingForHumanInput() {
        return waitingForHumanInput;
    }
    
    public boolean isGameInProgress() {
        return gameInProgress;
    }
    
    public Player getHumanPlayer() {
        return players.get(humanPlayerName);
    }
}