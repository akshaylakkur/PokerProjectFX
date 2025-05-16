package com.example;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;  

public class Deck { // deck class, the whole 52 deck of cards and all operations with them rely on this  class

    Card[] deckOfCards = {
        new Card(2, "Hearts"), new Card(3, "Hearts"), new Card(4, "Hearts"), new Card(5, "Hearts"),
        new Card(6, "Hearts"), new Card(7, "Hearts"), new Card(8, "Hearts"), new Card(9, "Hearts"),
        new Card(10, "Hearts"), new Card("Jack", "Hearts"), new Card("Queen", "Hearts"),
        new Card("King", "Hearts"), new Card("Ace", "Hearts"),

        new Card(2, "Diamonds"), new Card(3, "Diamonds"), new Card(4, "Diamonds"), new Card(5, "Diamonds"),
        new Card(6, "Diamonds"), new Card(7, "Diamonds"), new Card(8, "Diamonds"), new Card(9, "Diamonds"),
        new Card(10, "Diamonds"), new Card("Jack", "Diamonds"), new Card("Queen", "Diamonds"),
        new Card("King", "Diamonds"), new Card("Ace", "Diamonds"),

        new Card(2, "Clubs"), new Card(3, "Clubs"), new Card(4, "Clubs"), new Card(5, "Clubs"),
        new Card(6, "Clubs"), new Card(7, "Clubs"), new Card(8, "Clubs"), new Card(9, "Clubs"),
        new Card(10, "Clubs"), new Card("Jack", "Clubs"), new Card("Queen", "Clubs"),
        new Card("King", "Clubs"), new Card("Ace", "Clubs"),

        new Card(2, "Spades"), new Card(3, "Spades"), new Card(4, "Spades"), new Card(5, "Spades"),
        new Card(6, "Spades"), new Card(7, "Spades"), new Card(8, "Spades"), new Card(9, "Spades"),
        new Card(10, "Spades"), new Card("Jack", "Spades"), new Card("Queen", "Spades"), // copy array to arraylist when game starts (acts as refresh) then shuffles the deck
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

