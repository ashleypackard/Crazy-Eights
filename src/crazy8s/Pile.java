/**
 * @author Mike Kucharski & Ashley Packard
 * Spring 2013 | COMP 310 - Data Structures
 */

package crazy8s;

// this class is a linked list of cards
public class Pile {
	// cards represent our nodes
	private Card topCard, bottomCard;
	private int numCards;
	
	// default constructor
	public Pile(){
		topCard = bottomCard = null;
		numCards = 0;
	}
	
	//accessor methods
	public int getSize()     {return numCards;}
	public Card getFirst()   {return topCard;}
	public Card getLast()    {return bottomCard;}
	
	public boolean isEmpty() {return getSize() == 0 ? true : false;}
	
	// append card to the end of list
	public void add(Card newCard){
		if(newCard == null) return; // error check for null argument pass
		
		if(isEmpty()){     // covers the case of adding to an empty list
			newCard.setNext(null);
			newCard.setPrev(null);
			topCard = bottomCard = newCard;
		}else{           // appends a card normally
			bottomCard.setNext(newCard);
			newCard.setPrev(bottomCard);
			newCard.setNext(null);
			bottomCard = newCard;
		}
		numCards++; // increment card count
	}
	
	// adds the element at the specified position aka inserts 'before' node currently at that position
	public void add(int index, Card toAdd){
		if(toAdd == null || index > getSize() - 1) {return;} // error check for invalid arguments
		
		if(index == 0){    // add to the head
			toAdd.setNext(topCard);
			toAdd.setPrev(null);
			topCard.setPrev(toAdd);
			topCard = toAdd;
		}else{           // add before any element except head
			Card insertBefore = get(index);
			toAdd.setNext(insertBefore);
			toAdd.setPrev(insertBefore.getPrev());
			insertBefore.getPrev().setNext(toAdd);
			insertBefore.setPrev(toAdd);
		}
		numCards++;
	}
	
	// if card passed in is in the list, remove it from the list and return that card
	public Card remove(Card cardToFind)
	{
		if(cardToFind == null || !contains(cardToFind)) {return null;} // error check for invalid arguments
		
		int position = indexOf(cardToFind);
		Card cardFound = get(position);
		Card toReturn = cardFound;
		
		if(getSize() == 1){
			topCard = bottomCard = null; 		// if the list contains only 1 element
		}else if(cardFound.getPrev() == null){ // if found == first element
			topCard = cardFound.getNext();
			topCard.setPrev(null);
			cardFound.setNext(null);
		}else if(cardFound.getNext() == null){ // if found == last element
			bottomCard = cardFound.getPrev();
			bottomCard.setNext(null);
			cardFound.setPrev(null);
		}else{		// element is in the middle of the list
			cardFound.getPrev().setNext(cardFound.getNext());
			cardFound.getNext().setPrev(cardFound.getPrev());
			cardFound.setNext(null);
			cardFound.setPrev(null);
		}
		
		numCards--;
		return toReturn;
	}
	
	// remove and return card at specified index
	public Card remove(int index)
	{
		if(index > getSize() - 1) {return null;}   // error check for invalid arguments
		Card toRemove = get(index);   // get the card at that index
		return remove(toRemove);	// simply call the remove function that passes in a card
	}
	
	// returns the card at the specified index. NOTE: does not remove it
	public Card get(int index){
		if(isEmpty() || index > getSize()-1) {return null;}
		// loop through list to find card
		int count = 0;
		for(Card iter = topCard; iter != null; iter = iter.getNext())
		{
			if (count == index) // if we find the card, return the reference the the card
				return iter;
			else 
				count++;  // if we didn't find the card, increment count
		}
		
		return null; // return null if nothing was found
	}
	
	// Return true is the list contains the element
	public boolean contains(Card findMe)
	{
		Card cardFound = null;
		for(Card iter = topCard; iter != null; iter = iter.getNext())
		{
			if(iter.equals(findMe))	{cardFound = iter;}
		}
		
		return (cardFound == null) ? false : true; // advanced if statement, returns true if the card was found
	}
	
	// return the index of the card specified
	public int indexOf(Card toFind){
		int index = -1;
		for(Card iter = topCard; iter != null; iter = iter.getNext())
		{
			index++;
			if(iter.equals(toFind))	{return index;}
		}
		return -1;
	}

	// recursively delete the list by removing the last node
	private void clear(int size) {
		if(size == 0){return;}
		remove(size-1);
		clear(size-1);
	}
	
	// Delete the list by calling recursive clear function
	public void clear()
	{
		clear(this.getSize());
	}
	
	// change the data of the card at the specified index to the data of newCard
	public Card set(int index, Card newCard){
		if(index > getSize() - 1)   {return null;}
		Card c = get(index);
		Card beforeChange = c;
		c.setRank(newCard.getRank());
		c.setSuit(newCard.getSuit());
		return beforeChange;
	}
	
	// standard swap function using a temp variable
	// NOTE: swaps the data not the nodes themselves
	public void swap(int first, int second){
		Card firstCard = get(first);
		Card secondCard = get(second);
		Card tempCard = new Card(firstCard.getRank(), firstCard.getSuit());
		set(first, secondCard);
		set(second, tempCard);
	}
	
	// prints out all the elements of our pile
	public String toString(){
		String printed = "";
		int i = 0;
		// for this card;       while there are more cards; i++
		for(Card iter = topCard; iter != null; iter = iter.getNext())
		{
			printed += i + ": " + iter.getRankType(iter.getRank()) + " of " + iter.getSuitType(iter.getSuit()) + "\n";
			i++;
		}
		return printed;
	}
	
}
