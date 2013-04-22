/**
 * @author Mike Kucharski & Ashley Packard
 * Spring 2013 | COMP 310 - Data Structures
 */

package crazy8s;

public class Player {
	// define a player by their hand and name
	private Hand playerHand;
	private String name;
	
	// default constructor
	public Player(){
		playerHand = null;
		name = "Unknown";
	}
	
	// constructor
	public Player(String name, Hand playerHand){
		this.name = name;
		this.playerHand = playerHand;
	}
	
	// mutator methods
	public void setName(String name){this.name = name;}
	public void setHand(Hand playerHand){this.playerHand = playerHand;}
	
	// accessor methods
	public String getName() {return name;}
	public Hand getHand() {return playerHand;}
	public int getHandSize(){return playerHand.getSize();}

}
