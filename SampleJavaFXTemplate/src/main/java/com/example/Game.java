package com.example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Game{
    
  private HashMap<String, Player> players;
  private HashMap<String, Integer> amount;
  private int pot;
  private Scanner scan;
    
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


  public void executeGame(){
    System.out.println("Welcome to Poker! Please enter your name:");
    String name = scan.nextLine();
    addPlayers(name);
    players.get("cpu2");


  }





}
