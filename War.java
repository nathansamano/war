
/* Starter routines for 
 * War:  The old card game!
 * Nathan Samano (Sophmore Year)
 * December 6, 2013
 */

import javax.swing.*;   	// for ImageIcon and JOptionPane

import java.awt.*;		// for Container
import java.awt.event.*;	// for ActionListener

public class War extends JFrame implements ActionListener {
	
	//// static constants ////
	public static final boolean DEBUG = true;	// print debugging info if true
	public static final String IMAGEFILENAME_BACK = "backofcard.png";	// image file
	public static final String IMAGEFILENAME_STACK = "stackofcards.png";	// image file
	private static final long serialVersionUID = 1L;  // to make Eclipse happy
	
	//// data members ////
	private LinkedList player1 = new LinkedList();			// player 1's deck
	private LinkedList player2 = new LinkedList();			// player 2's deck
	private LinkedList player1InPlay = new LinkedList();		// cards player 1 has in play
	private LinkedList player2InPlay = new LinkedList();		// cards player 2 has in play
	private Card player1Card = new Card();		// player 1 faced up card
	private Card player2Card = new Card();		// player 2 faced up card
	private int iDeckSize = 0;			// initial size of a deck once generated
	private int player1Count = 0;			// number of Cards player 1 has left before collection
	private int player2Count = 0;			// number of Cards player 2 has left before collection
	
	//// change player names variables ////
	private JTextField field1 = new JTextField();  
	private JTextField field2 = new JTextField();
	private String player1Name = field1.getText();  
    private String player2Name = field2.getText(); 
	
	//// JFrame window-related variables ////
	private JPanel player1Panel;			// the panel for player 1
	private JPanel player2Panel;			// the panel for player 2
	private JPanel centerPanel;			// the center panel
	private JLabel stackOfCardsImage = null;	// the image for the stack of cards for player 1
	private JLabel stackOfCardsImage2 = null;	// the image for the stack of cards for player 2
	private JButton goButton = null;		// button to play next round
	private static final int FRAME_WIDTH = 400;	// initial width of JFrame window
	private static final int FRAME_HEIGHT = 200;	// initial height
 	private static final int FRAME_X_POS = 50;	// initial position of JFrame window, in x
	private static final int FRAME_Y_POS = 50;	// in y

	//// Constructor ////
	public War() {
		/////////////////////////////////////////
		// set up the image for stack of cards //
		/////////////////////////////////////////
	    java.net.URL imgURL = getClass().getResource(IMAGEFILENAME_STACK);
	    if (imgURL != null) {
	    	// need two separate JLabels, since it doesn't like to display the same JLabel
	    	// in two different locations
	        stackOfCardsImage = new JLabel(new ImageIcon(imgURL, "stack of cards"));
	        stackOfCardsImage2 = new JLabel(new ImageIcon(imgURL, "stack of cards"));
	    } else {
	        System.err.println("Couldn't find file: " + IMAGEFILENAME_STACK);
	    }
		
	    ////////////////////////////////////////////////////////////////////
		// set up the content pane -- the inner area of the JFrame Window //
	    ////////////////////////////////////////////////////////////////////
		Container contentPane = getContentPane(); 	// the content pane for the main window
		contentPane.setLayout(new BorderLayout(10,0));  // 10 pixel gap horizontally, 0 pixel gap vertically
		contentPane.setBackground(Color.BLACK);
		
		////////////////////////////////////////////////////
		// set up the player 1 panel, add to content pane //
		////////////////////////////////////////////////////
		player1Panel = new JPanel();	// for player 1's info
		player1Panel.setBorder(BorderFactory.createLoweredBevelBorder());
		player1Panel.setLayout(new BorderLayout());
		player1Panel.add(stackOfCardsImage);
		player1Panel.setBackground(Color.GREEN);
		contentPane.add(player1Panel, BorderLayout.WEST);  // on the left

		////////////////////////////////////////////////////
		// set up the player 2 panel, add to content pane //
		////////////////////////////////////////////////////
		player2Panel = new JPanel();	// for player 2's info
		player2Panel.setBorder(BorderFactory.createLoweredBevelBorder());
		player2Panel.setLayout(new BorderLayout());
		player2Panel.add(stackOfCardsImage2);
		player2Panel.setBackground(Color.GREEN);
		contentPane.add(player2Panel, BorderLayout.EAST);  // on the right

		//////////////////////////////////////////////////
		// set up the center panel, add to content pane //
		//////////////////////////////////////////////////
		centerPanel = new JPanel();	// for the two cards, and Go button
		centerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		centerPanel.setLayout(new FlowLayout());
		centerPanel.setBackground(Color.GREEN);
		contentPane.add(centerPanel, BorderLayout.CENTER); // in the center
	
		//////////////////////////
		// set up the Go button //
		//////////////////////////
		goButton = new JButton("Go!");		// the button for the next round of play
		goButton.addActionListener(this);   // we'll listen for the button to be pressed
		
		/////////////////////////////////////////////
		// set some other properties of the window //
		/////////////////////////////////////////////
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setTitle("The Game of War");
		setLocation(FRAME_X_POS, FRAME_Y_POS);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		///////////////////////////////////////////////////////////
		// set up the deck with one full deck of cards, shuffled //
		///////////////////////////////////////////////////////////
		
		// create a LinkedList object
		LinkedList deck = new LinkedList();
		deck.generateDeck(13,4);		// generate full 52 card deck
		//deck.generateDeck(3,4);		// generate 12 card deck
		//deck.generateDeck(3,2);		// generate 6 card deck
		System.out.println("deckSize = " + deck.getDeckSize());		// print out the deck size
		iDeckSize = deck.getDeckSize();					// set the size of the deck to a variable
		System.out.println(deck + "\n");				// print the generated deck
		deck = deck.shuffleDeck(deck.getDeckSize());			// shuffle the deck
		System.out.println(deck);					// print shuffled deck
		
		////////////////////////////////////////
		// set up players, deal out the cards //
		////////////////////////////////////////
		System.out.println(deck.getDeckSize());
		for (int i=0; i<(iDeckSize/2); i++) {		// pass out all the cards
			player1.enqueue(deck.dequeue());	// give player 1 a card
			player2.enqueue(deck.dequeue());	// give player 2 a card
		}
		/***** FOR DEBUGGING PURPOSES *****/
		/* How many cards in player decks */
		player1Count = player1.count();
		player2Count = player2.count();
		System.out.println("Player1 deck = " + player1 + " Count = " + player1.count());
		System.out.println("Player2 deck = " + player2 + " Count = " + player2.count());
	}
	
	///////////////////
	//// playRound ////
	///////////////////
	/* play one round, exiting if one player runs out of cards */
	public void playRound() {

		////////////////////
		// play the round //
		////////////////////
		
		int inPlayCount1 = 0;	// number of Cards player 1 has in play
		int inPlayCount2 = 0;	// number of Cards player 2 has in play
		
		//// flip one Card over in play ////
		if (player1.count() != 0 /*&& player1 != null*/) {	// as long as player deck is not empty they can play another card
			player1Card = new Card();		// player 1 faced up card
			player1Card = player1.dequeue();	System.out.println(player1);
			player1InPlay.enqueue(player1Card);	System.out.println("Player1InPlay: " + player1InPlay);
		} else {
			// /* must leave the same card in play due to not having anymore to play */
			// player1Card = player1InPlay.getRear();
			/* player 1 ran out of cards, therefore, player 2 wins */
			JOptionPane.showMessageDialog(null, "Player 2 Wins!", "Game Over", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}
		if (player2.count() != 0 /*&& player1 != null*/) {	// as long as player deck is not empty they can play another card
			player2Card = new Card();		// player 2 faced up card
			player2Card = player2.dequeue();	System.out.println(player2);
			player2InPlay.enqueue(player2Card);	System.out.println("Player2InPlay: " + player2InPlay);
		} else {
			/* must leave the same card in play due to not having anymore to play */
			// player2Card = player2InPlay.getRear();
			/* player 2 ran out of cards, therefore, player 1 wins */
			JOptionPane.showMessageDialog(null, "Player 1 Wins!", "Game Over", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}
		
		////////////////////////////////
		/////// update the frame ///////
		////////////////////////////////
		/* update the center panel */
		centerPanel.removeAll();			// remove old images
		centerPanel.add(player1Card.getImage());  	// add new card image
		centerPanel.add(player2Card.getImage());	// add new card image
		centerPanel.add(goButton);			// add the go button
		centerPanel.add(new JLabel("" + (player1InPlay.count() + player2InPlay.count()) + " cards in play"));
		centerPanel.validate();				// display the panel
		
		// set up the player 1 panel, add to content pane
		player1Panel.removeAll();	// remove old images
		player1Panel.add(new JLabel(player1Name), BorderLayout.NORTH);  // add new ones
		player1Panel.add(stackOfCardsImage, BorderLayout.CENTER);
		player1Panel.add(new JLabel("Cards: " + player1.count()), BorderLayout.SOUTH);
		player1Panel.validate();	// display the panel

		// set up the player 2 panel, add to content pane
		player2Panel.removeAll();
		player2Panel.add(new JLabel(player2Name), BorderLayout.NORTH);
		player2Panel.add(stackOfCardsImage2, BorderLayout.CENTER);
		player2Panel.add(new JLabel("Cards: " + player2.count()), BorderLayout.SOUTH);
		player2Panel.validate();
		
		// store the number of cards in each player's deck before determining round scenario
		player1Count = player1.count();
		player2Count = player2.count();
		
		/////////////////////////
		//// Round Scenarios ////
		/////////////////////////
		
		//// War ////
		if (player1Card.getPointValue() == player2Card.getPointValue()) {	// equal value cards in play
			JOptionPane.showMessageDialog(null,"War!");
			System.out.println("War has occured. player1.count()=" + player1.count() + " player2.count()=" + player2.count());
			if (player1.count() != 0) {	// if there are cards left in player's deck play one
				player1InPlay.enqueue(player1.dequeue());
			}
			if (player2.count() != 0) {	// if there are cards left in player's deck play one
				player2InPlay.enqueue(player2.dequeue());
			}
		} // if war
		
		//// Player 1 Wins Round ////
		// player 1's card is greater than player 2's card
		else if (player1Card.getPointValue() > player2Card.getPointValue()) {
			inPlayCount1 = player1InPlay.count();			// how many cards player 1 has in play
			for (int i=0; i<inPlayCount1; i++) {			// dequeue all cards player 1 has in play
				player2InPlay.enqueue(player1InPlay.dequeue());	// enqueue into one middle deck (just used
			} // for											// player 2's deck rather than making another one)
			
			/* shuffle cards before giving to round winner */
			inPlayCount2 = player2InPlay.count();	// recalculate number of cards middle deck has (player 2's in-play deck)
			player2InPlay = player2InPlay.shuffleDeck(inPlayCount2);
			
			for (int i=0; i<inPlayCount2; i++) {
				player1.enqueue(player2InPlay.dequeue());	// dequeue all cards in middle and give them to player 1
			} // for
		} // else if player 1 wins round
		
		//// Player 2 Wins Round ////
		else {	/* player1Card.getPointValue() < player2Card.getPointValue() */
			inPlayCount1 = player1InPlay.count();			// how many cards player 1 has in play
			for (int i=0; i<inPlayCount1; i++) {			// dequeue all cards player 1 has in play
				player2InPlay.enqueue(player1InPlay.dequeue());	// enqueue into one middle deck (just used
			} // for											// player 2's deck rather than making another one)
			
			/* shuffle cards before giving to round winner */
			inPlayCount2 = player2InPlay.count();			// recalculate number of cards middle deck has (player 2's in-play deck)
			player2InPlay = player2InPlay.shuffleDeck(inPlayCount2);
			
			for (int i=0; i<inPlayCount2; i++) {
				player2.enqueue(player2InPlay.dequeue());	// dequeue all cards in middle and give them to player 2
			} // for
		} // else player 2 wins round
		
		/////////////////////
		//// End of Game ////
		/////////////////////
		
		if (player1Card.getPointValue() != player2Card.getPointValue()) {	// cannot claim winner with war situation
			if (player1.count() == 0) {	// player 1 ran out of cards so player 2 wins
				JOptionPane.showMessageDialog(null, "Player 2 Wins!", "Game Over", JOptionPane.PLAIN_MESSAGE);
				System.exit(0);
			}
			if (player2.count() == 0) {	// player 2 ran out of cards so player 1 wins
				JOptionPane.showMessageDialog(null, "Player 1 Wins!", "Game Over", JOptionPane.PLAIN_MESSAGE);
				System.exit(0);
			}
		}
		/* If both players get war with no cards left in each of their decks then must either
		 * redistribute a shuffled version of their cards in play, or
		 * call the game a stalemate and start a new one otherwise will result in infinite war.
		 * Chances of this occurring in a standard 52 card deck is very slim although possible.
		 */
		if (/*player1.count()*/ player1Count == 0 && /*player2.count()*/ player2Count == 0) {
			JOptionPane.showMessageDialog(null, "Stalemate!", "Game Over", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}
		/* FOR DEBUGGING */
		System.out.println("Player1 Count: " + player1Count + " Player2 Count: " + player2Count + "\n");
	}
	
	public void setPlayerNames() {
		/* Googled how to set this up */
		Object[] message = { "Player 1 Name:", field1, "Player 2 Name:", field2, };  
		int option = JOptionPane.showConfirmDialog(null, message, "Set Player Names", JOptionPane.OK_CANCEL_OPTION);  
		if (option == JOptionPane.OK_OPTION) {  
		    player1Name = field1.getText();  
		    player2Name = field2.getText();   
		} else {
			player1Name = "Player 1";
			player2Name = "Player 2";
		}
	}
	
	//////////////
	//// main ////
	//////////////
	/* start the game, playing the first round */
	/* NOTE:  after the first round, the user will click the Go button to
	 * call playRound() another time. */
	public static void main (String[] args) {
		// create the window
		War war = new War();
		// set player names
		war.setPlayerNames();
		// display the window
		war.setVisible(true);
		// play the game
		war.playRound();
	}
	
	////////////////////////////////////
	//// the action listener method ////
	////////////////////////////////////
	public void actionPerformed(ActionEvent event) {
		/* make sure that we caught an action for the button that we want */
		if (event.getSource() instanceof JButton) {
			/* ok, it was a button...which one? (yea, we only have one!) */
			JButton clickedButton = (JButton) event.getSource();
			if (clickedButton == goButton) {
				if (DEBUG) System.out.println("Clicked Go!");
				/* play the next round */
				playRound();
	        } else {
				if (DEBUG) System.out.println("Clicked a button that wasn't one the go button.");
	        }
		} else { // the event source is something else
	    	if (DEBUG) System.out.println("Caught another event.");
	    }
	}
}
