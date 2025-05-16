package com.example;

public class Card implements Comparable<Card>
{
    private Integer rank;
    private String suit;

    /*
     * Precondition: 0 < rank < 15 and suit is either spades, hearts, clubs, or diamonds
     */
    public Card (Integer rank, String suit)
    {
        this.rank = rank;
        this.suit = suit;
    }

    /*
     * Precondition: Rank is either jack, queen, king, or ace
     */
    public Card (String rank, String suit)
    {
        if (rank.toLowerCase() == "jack")
        {
            this.rank = 11;
        }
        else if (rank.toLowerCase() == "queen")
        {
            this.rank = 12;
        }
        else if (rank.toLowerCase() == "king")
        {
            this.rank = 13;
        }
        else if (rank.toLowerCase() == "ace")
        {
            this.rank = 14;
        }
        this.suit = suit;
    }

    public Integer getRank()
    {
        return rank;
    }

    public String getSuit()
    {
        return suit;
    }

    public int compareTo(Card other)
    {
        return (rank - other.getRank());
    }

    public String toString()
    {
        return ("Suit: " + suit + "\nRank: " + rank.toString());
    }
}
