package com.example;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;  

public class Deck { // deck class, the whole 52 deck of cards and all operations with them rely on this  class

    Card[] deckOfCards = {
    new Card(Integer.valueOf(2), "Hearts"), new Card(Integer.valueOf(3), "Hearts"), new Card(Integer.valueOf(4), "Hearts"), new Card(Integer.valueOf(5), "Hearts"),
    new Card(Integer.valueOf(6), "Hearts"), new Card(Integer.valueOf(7), "Hearts"), new Card(Integer.valueOf(8), "Hearts"), new Card(Integer.valueOf(9), "Hearts"),
    new Card(Integer.valueOf(10), "Hearts"), new Card("Jack", "Hearts"), new Card("Queen", "Hearts"),
    new Card("King", "Hearts"), new Card("Ace", "Hearts"),

    new Card(Integer.valueOf(2), "Diamonds"), new Card(Integer.valueOf(3), "Diamonds"), new Card(Integer.valueOf(4), "Diamonds"), new Card(Integer.valueOf(5), "Diamonds"),
    new Card(Integer.valueOf(6), "Diamonds"), new Card(Integer.valueOf(7), "Diamonds"), new Card(Integer.valueOf(8), "Diamonds"), new Card(Integer.valueOf(9), "Diamonds"),
    new Card(Integer.valueOf(10), "Diamonds"), new Card("Jack", "Diamonds"), new Card("Queen", "Diamonds"),
    new Card("King", "Diamonds"), new Card("Ace", "Diamonds"),

    new Card(Integer.valueOf(2), "Clubs"), new Card(Integer.valueOf(3), "Clubs"), new Card(Integer.valueOf(4), "Clubs"), new Card(Integer.valueOf(5), "Clubs"),
    new Card(Integer.valueOf(6), "Clubs"), new Card(Integer.valueOf(7), "Clubs"), new Card(Integer.valueOf(8), "Clubs"), new Card(Integer.valueOf(9), "Clubs"),
    new Card(Integer.valueOf(10), "Clubs"), new Card("Jack", "Clubs"), new Card("Queen", "Clubs"),
    new Card("King", "Clubs"), new Card("Ace", "Clubs"),

    new Card(Integer.valueOf(2), "Spades"), new Card(Integer.valueOf(3), "Spades"), new Card(Integer.valueOf(4), "Spades"), new Card(Integer.valueOf(5), "Spades"),
    new Card(Integer.valueOf(6), "Spades"), new Card(Integer.valueOf(7), "Spades"), new Card(Integer.valueOf(8), "Spades"), new Card(Integer.valueOf(9), "Spades"),
    new Card(Integer.valueOf(10), "Spades"), new Card("Jack", "Spades"), new Card("Queen", "Spades"),
    new Card("King", "Spades"), new Card("Ace", "Spades")
};

                      
    int currentIndex;
    ArrayList<Card> deck = new ArrayList<Card>();                  

    public Deck() {
        currentIndex = 0;
        refreshDeck();
        shuffleDeck();
    }

    public Card dealCard() {
        Card dealt = deck.get(currentIndex);
        currentIndex++;
        return dealt;
    }

    public void refreshDeck() {
        deck.clear();
        for (int i = 0; i < deckOfCards.length; i++) {
            deck.add(deckOfCards[i]);
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(deck);  
    }
    
    public ArrayList<Card> getDeck(){
        return deck;
    }
         
    public static void main (String[] args){
        Deck d = new Deck(); 
        for (Card card : d.getDeck()){
            System.out.println(card.toString());  
        }
    }               

}

