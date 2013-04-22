/**
 * @author Mike Kucharski & Ashley Packard
 * Spring 2013 | COMP 310 - Data Structures
 */

package crazy8s;

import java.util.Random;
import java.util.Scanner;

public class Game implements GameConstants
{
	private Player[] players;
	private Deck deck;
	private Pile discards;
	private int turn;
	private Scanner userInput;
	
	// default constructor
	public Game()
	{
		deck = null;
		players = null;
		discards = null;
		turn = 0;
		userInput = null;
	}
	
	// includes all steps to run the game
	public void run()
	{
		while(true) // main game loop
		{
			displayRules();   // 0. Display rules
			initGameVars();   // 1. initialize the game variables
			takeTurns();	  // 2. cycle through player's turns until someone wins
			displayWinner();  // 3. Display the winner
			if(!playAgain())  // 4 ask to play again
				break;
		} // end game loop
		
		System.out.println("Thank you for playing, goodbye!");
	}
	
	// Display rules
	private void displayRules() {
		System.out.println(
				"|==========================  Welcome to Crazy 8's  ==========================|" +
				"\n| Each player takes a turn playing a card. A player can only play a card if" +
				"\n| it's suit or rank matches the card last played. 8's are wild and can be" +
				"\n| played at any time. The suit is then chosen by the player. If a player can't" +
				"\n| play any cards, they draw from the deck and their turn is skipped. The first" +
				"\n| player to remove all the cards in their hand is the winner. Have Fun. :D\n" +
				"+============================================================================|"
		);
	}
	
	// initialize the game variables
	private void initGameVars()
	{
		deck = new Deck();
		players = new Player[NUM_PLAYERS];
		turn = 0;
		discards = new Pile();
		userInput = new Scanner(System.in);
		
		//shuffle deck
		deck.shuffle();
		
		// initialize the first player as the user, with their unique name
		System.out.print("Enter your name: ");
		String name = userInput.nextLine().trim();
		players[0] = new Player(name, new Hand());
		
		// initialize the AI players 
		for(int i = 1; i < players.length; i++)
			players[i] = new Player("Player " + i, new Hand());
		
		// deal 5 cards to all the players
		for(int p = 0; p < players.length; p++){
			for(int cards = 0; cards < 5; cards++)
				players[p].getHand().addToHand(deck.deal());
		}
		
		// start the game by giving the discard pile a card off the deck
		discards.add(deck.deal());
	}
	
	// cycle through player's turns until someone wins
	@SuppressWarnings("static-access")
	private void takeTurns()  
	{
		while(true)	{
			// display useful information about game
			printPlayerStats();
			displayTopDiscard();
			
			// perform whose ever turn it is
			if(turn == 0)
				userTurn();
			else
				performAIturn(turn);
			
			// check for game over, i.e. a players hand is empty
			if (players[turn].getHand().isEmpty())
				break;

			// simulate waiting screen by making thread sleep
			System.out.println("Please wait, moving to next turn....\n");
			Thread sleeper = new Thread();
			try {
				// wait for 3 1/2 seconds
				sleeper.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			incTurn(); // advance to next turn
		}
	}
	
	// AI turn, plays a card or draws from the deck if not possible
	private void performAIturn(int turn) 
	{
		Hand hand = players[turn].getHand();
		
		// cycle through every card to find match
		for(int i = 0; i < hand.getSize(); i++)
		{
			if(hand.get(i).getRank() == 7) // ignore crazy eights at first
				continue;
			
			if( canPlay(hand.get(i), discards.getLast())){
				System.out.println("Player " + turn + " played a " + hand.get(i) + "\n");
				discards.add(hand.remove(i));
				return;
			}
		}
		
		// if no playable card was found, check if a crazy eight was found
		for(int i = 0; i < hand.getSize(); i++)
		{
			if(hand.get(i).getRank() == 7){
				discards.add(hand.remove(i));
				Random r = new Random();
				int newSuitNum = r.nextInt(4);
				discards.getLast().setSuit(newSuitNum);
				System.out.println("Player " + turn + " played a crazy 8 and declared it a "
									+ discards.getLast().getSuitType(newSuitNum) + "\n");
				return;
			}
		}
		
		// no matches found, draw from deck
		// if deck is empty, game over
		Card toAdd = deck.deal();
		if(!(toAdd == null) ){
			hand.addToHand(toAdd);
			System.out.println("Player " + turn + " drew a card from deck.");
		}else{
			System.out.println("Player " + turn + " passed their turn because the deck is empty.");
		}
		
	}
	
	private void userTurn() 
	{
		Hand hand = players[0].getHand();
		// display the user's hand
		displayHand(turn);
		
		String input = "";
		while (true) 
		{
			System.out.print("Which card would you like to play? (-1 to draw from deck): ");
			input = userInput.nextLine().trim();
			
			// validate user input
			int chosen = -1;
			try {
				chosen = Integer.parseInt( input );
			} catch (NumberFormatException e) {
				System.out.println("Please enter a number.");
				continue;
			}
			
			// -1 input draws a card from deck
			if(chosen == -1) 
			{
				Card toAdd = deck.deal();
				System.out.println("\nYou drew a " + toAdd + " from the deck.\n");
				if(!(toAdd == null) ){
					hand.addToHand(toAdd);
				}else{
					System.out.println("You passed your turn because the deck is empty.");
				}
				return;
			// if the card is a valid index, a canPlay card, or a crazy 8
			}else if ( (chosen >= 0 && chosen < players[0].getHandSize()) &&
				( hand.get(chosen).getRank() == 7  || canPlay(hand.get(chosen), discards.getLast())) )
			{
				int crazySuit = -1;
				// if card played was a crazy_eight
				if(hand.get(chosen).getRank() == 7 ) {
					crazySuit = getCrazyEightSuit();
					hand.get(chosen).setSuit(crazySuit);
				}
				System.out.print("You played a " + hand.get(chosen) + "\n");
				discards.add(hand.remove(chosen));
				break;
			}
			else {
				System.out.println("Invalid card! Please enter again.");
			}
		}
	}

	// prompts the user to play again
	private boolean playAgain() {
		System.out.print("Would you like to play again?(y/n): ");
		String answer = userInput.nextLine().trim();
		if( answer != null && ( (answer.charAt(0) == 'y') || (answer.charAt(0) == 'Y') ))
			return true;
		return false;
	}
	
	// print out winner
	private void displayWinner() {
		if(turn == 0)
			System.out.println("You won the game!");
		else
			System.out.println("Player " + turn + " won the game!");
	}

	// simply asks user for input on a suit number and validates it
	private int getCrazyEightSuit() {
		
		while (true) {
			System.out.println("Which suit would you like?\n" +
					"0: Clubs\n1: Hearts\n2: Spades\n3: Diamonds" );
			String input = userInput.nextLine().trim();
			
			int chosen = -1;
			try {
				chosen = Integer.parseInt( input );
			} catch (NumberFormatException e) {
				System.out.println("Please enter a number.");
				continue;
			}
			
			// if the number was not in a valid range
			if( (chosen < 0) || (chosen > 3) ){
				System.out.println("Please enter a valid number (0-3)"); 
				continue;
			}else{ // number was a valid input
				return chosen;
			}// end else
		}// end while
		
	}// end function

	// print out last card played
	private void displayTopDiscard()
	{
		System.out.println("*** Last Card Played: " + discards.getLast() + " ***\n");
	}
	
	// display players hands, AI for for testing
	private void displayHand(int turn)
	{
		if(turn == 0)
			System.out.println("Your Hand: \n" + players[turn].getHand());
		else
			System.out.println(players[turn].getName() + "'s Hand: \n" + players[turn].getHand());
	}
	
	// increment turn counter by 1, loops to 0 after 3
	private void incTurn()
	{	
		turn = (turn == 3) ? 0 : turn+1;
	}
	
	// tests if the two cards have either the same suit or rank
	private boolean canPlay(Card test, Card discard)
	{
		return (test.getRank() == discard.getRank() 
			   || test.getSuit() == discard.getSuit()) ? true : false;
	}
	
	// print out the number of cards in each player's hand
	private void printPlayerStats(){
		System.out.println("+-------Player's Cards-----+");
		
		for(int i = 0; i < players.length; i++)
			System.out.println("|     " + players[i].getName() + " : " + players[i].getHandSize() + " cards ");
		
		System.out.println("+--------------------------+");
	}
}
