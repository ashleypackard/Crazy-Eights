/**
 * @author Mike Kucharski & Ashley Packard
 * Spring 2013 | COMP 310 - Data Structures
 */

package crazy8s;

// class represents node class
public class Card implements GameConstants
{
	private int rank, suit;
	private Card next, prev; // keep track of next and previous nodes
	
	private String[] possibleRanks = allRanks;
	private String[] possibleSuits = allSuits;
	
	public Card(int rank, int suit){
		this(rank, suit, null, null);
	}
	
	public Card(int rank, int suit, Card next, Card prev){
		this.rank = rank;
		this.suit = suit;
		this.next = next;
		this.prev = prev;
	}
	
	// mutator methods
	public void setRank(int rank)  {this.rank = rank;}
	public void setSuit(int suit)  {this.suit = suit;}
	public void setNext(Card next) {this.next = next;}
	public void setPrev(Card prev) {this.prev = prev;}
	
	//accessor methods
	public int getRank()           {return rank;}
	public int getSuit()           {return suit;}
	public String getRankType(int r)   {return possibleRanks[r];}
	public String getSuitType(int s)   {return possibleSuits[s];}
	public Card	getNext()          {return next;}
	public Card	getPrev()          {return prev;}
	
	// prints out the string form of the card
	public String toString()
	{
		return getRankType(getRank()) + " of " + getSuitType(getSuit());
	}
	
	// tests if two cards have the same rank and suit
	public boolean equals(Card test){
		return (test.getRank() == this.rank 
			   && test.getSuit() == this.suit) ? true : false;
	}
	
	
}
