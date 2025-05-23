package com.example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Game{
    
  private HashMap<String, Player> players;
  private HashMap<String, Integer> amount;
  private int pot;
  private Scanner scan;
  private Random rand = new Random();

    
  public Game(){
    Player cpu1 = new Player("cpu1", 1000);
    Player cpu2 = new Player("cpu2", 1000);
    Player cpu3 = new Player("cpu3", 1000);
    players.put("cpu1", cpu1);
    players.put("cpu2", cpu2);
    players.put("cpu3", cpu3);
    Scanner scan = new Scanner(System.in);
    pot = 0;
  }

  public void addPlayers(String name){
    Player p = new Player(name, 1000);
    players.put(name, p);
  }

  public int fold(String name){
    players.remove(name);
    players.get(name).fold();
    return amount.get(name);
  }

  public void updatePot(){
  
  }

  public void assignDealer(){
    int z = ((int) rand.nextInt()*(players.keySet().size()-1)) + 1;
    String key = "";
    int count = 0;
    for (String x : players.keySet()){
      if (count > z){
        break;
      }
      key = x;
      count++;
    }         

    Player d = players.get(key);
    
    if (!d.isBigBlind() && !d.isSmallBlind()){
      d.setDealer();
    } else {
      assignDealer();
    }
  }

  public void assignBB(){
    int z = ((int) rand.nextInt()*(players.keySet().size()-1)) + 1;
    String key = "";
    int count = 0;
    for (String x : players.keySet()){
      if (count > z){
        break;
      }
      key = x;
      count++;
    }         

    Player d = players.get(key);
    
    if (!d.isDealer() && !d.isSmallBlind()){
      d.setBB();
    } else {
      assignBB();
    }
  }

  public void assignSB(){
    int z = ((int) rand.nextInt()*(players.keySet().size()-1)) + 1;
    String key = "";
    int count = 0;
    for (String x : players.keySet()){
      if (count > z){
        break;
      }
      key = x;
      count++;
    }         

    Player d = players.get(key);
    
    if (!d.isDealer() && !d.isBigBlind()){
      d.setSB();
    } else {
      assignSB();
    }
        
  }

    public void executeGame(){
    System.out.println("Welcome to Poker! Please enter your name:");
    String name = scan.nextLine();
    addPlayers(name);
    players.get("cpu2");   


  }
                


}
