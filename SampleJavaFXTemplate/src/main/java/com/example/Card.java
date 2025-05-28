package com.example;

public class Card implements Comparable<Card> {
    private Object value; // Can be Integer or String (for face cards)
    private String suit;
    
    public Card(Object value, String suit) {
        this.value = value;
        this.suit = suit;
    }
    
    public Object getValue() {
        return value;
    }
    
    public String getSuit() {
        return suit;
    }
    
    public int getNumericValue() {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            String strValue = (String) value;
            switch (strValue) {
                case "Jack": return 11;
                case "Queen": return 12;
                case "King": return 13;
                case "Ace": return 14; // High ace
                default: return 0;
            }
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return value + " of " + suit;
    }
    
    @Override
    public int compareTo(Card other) {
        return Integer.compare(this.getNumericValue(), other.getNumericValue());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return value.equals(card.value) && suit.equals(card.suit);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode() * 31 + suit.hashCode();
    }
}