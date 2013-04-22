/**
 * @author Mike Kucharski & Ashley Packard
 * Spring 2013 | COMP 310 - Data Structures
 */

package crazy8s;

// used to denote a players collection of cards
public class Hand extends Pile{
	
	// simply call the parent's add function
	public void addToHand(Card c){
		super.add(c);
	}

}
