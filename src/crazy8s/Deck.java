/**
 * @author Mike Kucharski & Ashley Packard
 * Spring 2013 | COMP 310 - Data Structures
 */

package crazy8s;

import java.util.Random;

public class Deck extends Pile
{
	// constructor, adds 52 unique cards to the deck
	public Deck()
	{
		for (int suitNumber = 0; suitNumber < 4; suitNumber++) // for every suit
		{
			for (int rankNumber = 0; rankNumber < 13; rankNumber++) // for every rank
				this.add(new Card(rankNumber, suitNumber)); // add new card to deck
		}
	}
	
	// swaps the card values at two random indexes for getSize times
	public void shuffle(){
		Random r = new Random();
		
		// swap every element with another random element
		for(int i = 0; i < getSize(); i++)
			swap( r.nextInt(getSize()), r.nextInt(getSize()) );
	}
	
	// removes top card from deck
	public Card deal(){
		return this.remove(this.getSize()-1);
	}
}
