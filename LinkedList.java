/* LinkedList: using book's implementation
 * NOTE: insertions are to the front of the list
 * Nathan Samano
 * October 2013 (Sophmore Year)
 */
import java.util.Random;

public class LinkedList {

	//// static constants ////
	
	//// data members ////
	private int nodeCount;		// count number of nodes in LinkedList
	private int deckSize;		// number of cards in deck
	private Card front;		// front of queue
	private Card rear;		// rear of queue
	
	//// Constructor ////
	public LinkedList() {
		// create a dummy node for the list
		front = new Card();
		// Note that the Card class initializes this to a dummy node
		rear = front;
	}
	
	public String toString() {
		String outString = new String("The linked list contains: ");
		// traverse the list
		Card temp;		// temporary pointer to a node
		// start at the beginning
		temp = front.getNext();		// skip the dummy node
		// run through every node in the list
		while(temp != null) {
			// "visit" each node
			//outString = outString + temp.getData() + " ";
			outString = outString + "(" + temp.getSuit() + ", " + temp.getCard() + ", " + temp.getPointValue() + ") ";
			// move temp to the next node
			temp = temp.getNext();
		}
		return outString;
	}
	
	//// Count total number of nodes in linked list
	public int count() {
		return nodeCount;
	}
	
	//// Getter for rear
	public Card getRear() {
		return rear;
	}
	
	//// Getter for deckSize
	public int getDeckSize() {
		return deckSize;
	}
	
	//// enqueue a specific card
 	public boolean enqueue(int newSuit, int newValue, int newPointValue) {
		// create a new Node object
		Card newestCard = new Card();
		// error check -- if we can't get memory, newestNode will be null
		if (newestCard == null) {
			// out of memory
			System.out.println("Out of memory error.");
			return false;
		}  else {
			System.out.println("Adding node...");
			nodeCount++; // increment nodeCount
			// fill in the data for the newest node, suit, value, point value, image
			newestCard.setSuit(newSuit);
			newestCard.setCard(newValue);
			newestCard.setPointValue(newPointValue);
			// link the newest node to the old first node
			newestCard.setNext(rear.getNext());
			// link the first (dummy) node to this newest node
			rear.setNext(newestCard);
			rear = newestCard;
			return true;
		}
	}
	
 	//// enqueue created card
	public boolean enqueue(Card newestCard) {
		// error check -- if we can't get memory, newestNode will be null
		if (newestCard == null) {
			// out of memory
			System.out.println("Out of memory error.");
			return false;
		}  else {
			System.out.println("Adding node...");
			nodeCount++; // increment nodeCount
			// link the newest node to the old first node
			newestCard.setNext(rear.getNext());
			// link the first (dummy) node to this newest node
			rear.setNext(newestCard);
			rear = newestCard;
			return true;
		}
	}
	
	//// dequeue a card
	public Card dequeue() {
		Card temp;	// a temporary reference node down the list
		Card previous;	// the node before this temp (dummy)
		
		// start at the beginning of the list
		temp = front.getNext();	// the first node that is not a dummy
		previous = front;	// at the dummy node (one before the first node)
		
		if (temp != null) {
			// delete it
			// set the previous node's next pointer to be the one AFTER temp
			previous.setNext(temp.getNext());
			nodeCount--; // decrement nodeCount
			System.out.println("Node being removed: " + temp);
			if (front.getNext() == null) { // if the entire list has been removed then need to start all over to add to the list
				front = new Card();
				rear = front;
			}
			return temp;
		} else {
			// else, return null (nothing to remove)
			System.out.println("No node to be removed");
			return null;
		}
	}
	
	//// create a specified sized deck of cards
	public void generateDeck(int maxValue, int maxSuit) {
		int pointValue = 0;				// point value for card
		for (int value=0; value<maxValue; value++) {	// loop through all card values
			for (int suit=0; suit<maxSuit; suit++) {	// loop through all suits
				switch (value) { // operations
					case 0:		pointValue = 11;	// ace
								break;
					case 1:		pointValue = 2;		// two
								break;
					case 2:  	pointValue = 3;		// three
								break;
					case 3:  	pointValue = 4;		// four
								break;		
					case 4:		pointValue = 5;		// five
								break;
					case 5:		pointValue = 6;		// six
								break;
					case 6:		pointValue = 7;		// seven
								break;
					case 7:		pointValue = 8;		// eight
								break;
					case 8:		pointValue = 9;		// nine
								break;
					case 9:		pointValue = 10;	// ten
								break;
					case 10:	pointValue = 10;	// jack
								break;
					case 11:	pointValue = 10;	// queen
								break;
					case 12:	pointValue = 10;	// king
								break;
					default: 	System.out.println("Invalid pointValue");
								break;
				} // switch
				enqueue(suit, value, pointValue);	// enqueue card to deck
				deckSize++;				// increment deckSize
			}
		}
	}
	
	//// shuffle the deck
	public LinkedList shuffleDeck(int iDeckLength) {
		LinkedList tempDeck = new LinkedList();			// temporary holding deck
		LinkedList shuffledDeck = new LinkedList();		// the end shuffled deck
		
		int deckLength = iDeckLength + 1;			// +1 due to the wanted exclusion of zero
		Random randomGenerator = new Random();			// RNG
	
		//// must remove every card in initial deck and put in a shuffled deck
		for (int i=0; i<iDeckLength; i++) {
			//// first generate a random number not zero
			int randomNum = randomGenerator.nextInt(deckLength);	System.out.println("randomNum = " + randomNum);
			while (randomNum == 0) {
				randomNum = randomGenerator.nextInt(deckLength);	System.out.println("randomNum was 0 it is now " + randomNum);
			} // while
			//// dequeue randomNum amount of cards and put in tempDeck
			for (int j=0; j<randomNum; j++) {
				/* 
				 * temp = dequeue();									System.out.println("Card being dequeued from main deck is " + temp);
				 * tempDeck.enqueue(temp);								System.out.println("Card enqueuing into tempDeck is " + temp);
				 */
				tempDeck.enqueue(dequeue());
			} // for j
			//// dequeue all but the last card in tempDeck back into the initial deck
			for (int j=0; j<randomNum-1; j++) {
				/*
				 * temp = tempDeck.dequeue();							System.out.println("Card being dequeued from tempDeck is " + temp);
				 * enqueue(temp);										System.out.println("Card enqueuing into main deck is " + temp);
				 */
				enqueue(tempDeck.dequeue());
			} // for j
			/*
			 * temp = tempDeck.dequeue();								System.out.println("Card being dequeued from tempDeck is " + temp);
			 * shuffledDeck.enqueue(temp);								System.out.println("Card enqueuing into shuffledDeck is " + temp);
			 */
			//// put that last card from tempDeck into the shuffledDeck
			shuffledDeck.enqueue(tempDeck.dequeue());
			deckLength--;  /* decrement number of cards in initial deck */	System.out.println("deckLength = " + deckLength);
		} // for i
		return shuffledDeck;
	} // shuffleDeck
	
	//// main, for testing ////
	public static void main(String[] args) {
		// create a LinkedList object
		LinkedList deck = new LinkedList();
		//deck.generateDeck(13,4);		// generate full 52 card deck
		deck.generateDeck(3,4);			// generate 12 card deck
		//deck.generateDeck(3,0);		// generate 4 card deck
		System.out.println("deckSize = " + deck.getDeckSize() + " Count = " + deck.count());	// print out the deck size
		System.out.println(deck + "\n");							// print the generated deck
		deck = deck.shuffleDeck(deck.getDeckSize());	// shuffle the deck
		System.out.println(deck);			// print the shuffled deck

	}
}
