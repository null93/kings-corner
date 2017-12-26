package io.raffi.kings.corner;

import java.io.*;
import java.util.*;

/**
 * Board.java - This class holds all the game data.  It also holds the players and the deck and
 * the pile. It also contains functions that relate to the board, suck as rendering a game board
 * as well as checking if the game or the round is over.
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @project     Project #01 - Kings Corner
 * @package     io.raffi.kings.corner
 * @category    Board
 * @author      Rafael Grigorian
 * @license     The MIT License
 */
class Board {

	/**
	 * This data member holds the main draw deck.  The Deck class extends the CardPile class.
	 * @var     Deck        Deck        The main draw card pile
	 */
	protected Deck Deck;

	/**
	 * This data member hold the Player class as a reference to that object.
	 * @var     Player      Player      The player controlled by the user
	 */
	protected Player Player;

	/**
	 * This data member hold the Computer class as a reference to that object.
	 * @var     Computer    Computer    The player controlled by the computer
	 */
	protected Computer Computer;

	/**
	 * This data member is an array of CardPile instances, and will act like the board on which
	 * we will play on.
	 * @var     CardPile[]  Piles       The eight main piles in which we will use to play
	 */
	protected CardPile [] Piles;

	/**
	 * This data member will be used to keep track of who's turn it is to play. It must be
	 * different from the variable that keeps track of who deals first because that is switched
	 * every round.
	 * @var     boolean     turn        True means it is the Player's turn
	 */
	protected boolean turn;

	/**
	 * This data member keeps track of who is dealing the cards out.  Initially when the program
	 * is launched, the computer deals first and the player goes first since who ever deals cannot go first the next round.
	 * @var     boolean     dealer      True means it is the Player's turn
	 */
	protected boolean dealer;

	/**
	 * This string variable will be used to output errors as well as the computers turn.  It is
	 * like a message board for the user to see warnings, and what the computer decided to play.
	 * @var     String      message     Shows warning and computers play history
	 */
	protected String message;

	/**
	 * This data member will be set in this class' constructor and will be used to determine when
	 * a player has won a game.
	 * @var     int         scoreLimit  Score threshold needed to win the game
	 */
	protected int scoreLimit;

	/**
	 * This data member will determine how many cards are initially dealt to each player in the
	 * beginning of each round.
	 * @var     int         initialHandCount    Initial cards in the beginning of each round
	 */
	protected int initialHandCount;

	/**
	 * The constructor for this class will set the default variables, declares all data members,
	 * but does not begin to deal out cards to the first four piles and does not hand out the
	 * initial card amount, because that is done in Board::go().
	 * @param   int         numCards        Number of initial cards in players hand
	 * @param   int         scoreThreshold  Score required to win the game
	 * @see                 Board::go ()
	 */
	protected Board ( int numCards, int scoreThreshold ) {
		// Initialize data members to default and passed values
		this.initialHandCount = numCards;
		this.scoreLimit = scoreThreshold;
		this.turn = true;
		this.dealer = false;
		this.message = "Dealing out cards...";
		// Initialize players
		this.Player = new Player ( this );
		this.Computer = new Computer ( this );
		// Initialize our game draw deck and shuffle with variable magnitude
		this.Deck = new Deck ("Main Deck");
		this.Deck.shuffle ( 2000 );
		// Initialize our card pile
		this.Piles = new CardPile [ 8 ];
		// Loop through card piles and initiate CardPile instances
		for ( int pile = 0; pile < 8; pile++ ) {
			this.Piles [ pile ] = new CardPile ( "Pile #" + ( pile + 1 ) );
		}
	}

	/**
	 * This function initiates the dealing of the card dealing etc.  This is meant to separate the
	 * initiation from the start of the game.
	 * @return  void
	 */
	protected void go () {
		// Render board being empty
		this.render ();
		// Loop through again, and deal a card out to the first four decks
		for ( int pile = 0; pile < 4; pile++ ) {
			// Add a card to the pile
			this.Piles [ pile ].append ( this.Deck.deal () );
			// Render board
			this.render ();
		}
		// Next based on the dealer turn, deal cards out
		this.deal ();
	}

	/**
	 * This function will return a new instance of the board class and choose whether or not to
	 * copy over critical game data into new board class.
	 * @param   boolean         copy        True is a soft reset, while false is a hard reset
	 * @return  Board                       A new board instance
	 */
	protected Board reset ( boolean copy ) {
		// Create new board instance
		Board board = new Board ( this.initialHandCount, this.scoreLimit );
		// If we want to do a soft reset
		if ( copy ) {
			// Copy score over
			board.Player.score = this.Player.score;
			board.Computer.score = this.Computer.score;
		}
		// Change dealer, and who goes first
		board.dealer = !this.dealer;
		board.turn = !board.dealer;
		// Return the new board
		return board;
	}

	/**
	 * This function checks whether any winners are on the boards by checking if the score
	 * threshold was breached.
	 * @return  boolean                     Return whether there is a winner or not, true is yes
	 */
	protected boolean checkWinner () {
		// Check if the player reached the score threshold
		if ( this.Player.score >= this.scoreLimit ) {
			// Return true
			return true;
		}
		// Check if the computer reached the score threshold
		else if ( this.Computer.score >= this.scoreLimit ) {
			// Return true
			return true;
		}
		// Otherwise return false
		return false;
	}

	/**
	 * This function checks if the round is still in play or if someone has won by checking the
	 * players hands and analyzing the board for possible moves.
	 * @return  boolean                     Returns true if round is done
	 */
	protected boolean checkRoundEnd () {
		// Check if the player has an empty hand
		if ( this.Player.Hand.size == 0 ) {
			// Render who has won the round
			this.render ();
			// Calculate penalty points
			this.calculatePenalty ();
			return true;
		}
		// Check if the computer has an empty hand
		else if ( this.Computer.Hand.size == 0 ) {
			// Render who has won the round
			this.render ();
			// Calculate penalty points
			this.calculatePenalty ();
			return true;
		}
		// Return false otherwise
		return false;
	}

	/**
	 * This function calculates the penalty points in the end of the game for each player and adds
	 * it to the score in the players data member section.
	 * @return  void
	 */
	protected void calculatePenalty () {
		// Sum up points for player
		Card card = this.Player.Hand.top;
		int score = 0;
		// Loop through hand
		while ( card != null ) {
			// If the card is a king, then add ten points
			if ( card.Rank == Rank.KING ) {
				score += 10;
			}
			// Otherwise add one point
			else {
				score += 1;
			}
			// Iterate to next card
			card = card.next;
		}
		// Set penalty points for player
		this.Player.score += score;
		// Sum up points for computer
		card = this.Computer.Hand.top;
		score = 0;
		// Loop through hand
		while ( card != null ) {
			// If the card is a king, then add ten points
			if ( card.Rank == Rank.KING ) {
				score += 10;
			}
			// Otherwise add one point
			else {
				score += 1;
			}
			// Iterate to next card
			card = card.next;
		}
		// Set penalty points to computer
		this.Computer.score += score;
	}

	/**
	 * This function renders out the board including the main deck, eight piles, and both players
	 * hands.  It also sleeps after it renders for a variable amount of time, for animation effects.
	 * @return  void
	 */
	protected void render () {
		// Clear the screen
		System.out.print("\033[2J\033[1;1H\n");
		// Display the Deck as hidden
		this.Deck.render ( true );
		// Loop through piles and print each deck out
		for ( int pile = 0; pile < 8; pile++ ) {
			// Render pile
			this.Piles [ pile ].render ( false );
		}
		// Render computer's hand hidden
		this.Computer.Hand.render ( true );
		// Render player's hand visible
		this.Player.Hand.render ( false );
		// Render scores
		System.out.println (
			CardPile.REDTEXT + "Your Score: " + CardPile.RESET + this.Player.score +
			CardPile.REDTEXT + "\tAI's Score: " + CardPile.RESET + this.Computer.score
		);
		// Render message area
		System.out.println ( this.message + "\n" );
		// Try to put thread to sleep
		try {
			// Put the thread to sleep for variable seconds
			Thread.sleep ( 200 );
		// Catch any exceptions that might occur
		} catch ( InterruptedException ex ) {
			// Interrupt through otherwise
			Thread.currentThread ().interrupt ();
		}
	}

	/**
	 * This function deals out cards in to correct order of who's turn it is.  It uses the Players
	 * class deal function to get a card from the deck.
	 * @return  void
	 */
	protected void deal () {
		// If the dealer is the Player
		if ( this.dealer ) {
			// Loop through to how many initial cards we need
			for ( int i = 0; i < this.initialHandCount; i++ ) {
				// Deal to the Computer first
				this.Computer.deal ();
				// Render board
				this.render ();
				// Deal to the Player
				this.Player.deal ();
				// Render board
				this.render ();
			}
		}
		// If the dealer is the Computer
		else {
			// Loop through to how many initial cards we need
			for ( int i = 0; i < this.initialHandCount; i++ ) {
				// Deal to the Player first
				this.Player.deal ();
				// Render board
				this.render ();
				// Deal to the Computer
				this.Computer.deal ();
				// Render board
				this.render ();
			}
		}
		// Change message and render
		this.message = "Lets start the round!";
		this.render ();
	}

	/**
	 * This function runs when a user chooses to quit on their turn. It additionally verifies if
	 * the player wants to quit.
	 * @return  void
	 */
	protected void quit () {
		// Check message on board and render
		this.message = "Are you sure you want to quit? (Y/N)";
		this.render ();
		// Initialize scanner
		Scanner reader = new Scanner ( System.in );
		// Initialize local variables
		String command;
		// Get player input, split it into segments
		System.out.print ("> ");
		command = reader.nextLine ();
		if ( command.equals ("Y") || command.equals ("y") ) {
			// GTFO
			System.out.println ("");
			System.exit ( 0 );
		}
		// Render out board again
		this.message = "Glad you stayed ;)";
		this.render ();
	}

	/**
	 * This function prints out information about me and information about the program.  Nothing
	 * special.
	 * @return  void
	 */
	protected void about () {
		// Print out about menu
		System.out.println (
			CardPile.REDTEXT + "\tVersion:" + CardPile.RESET + "\tv1.0.0\n" +
			CardPile.REDTEXT + "\tUniversity:" + CardPile.RESET + "\tUniversity of Illinois at Chicago\n" +
			CardPile.REDTEXT + "\tCourse:" + CardPile.RESET + "\t\tCS342 - Software Design\n" +
			CardPile.REDTEXT + "\tPackage:" + CardPile.RESET + "\tProject #01 - Kings Corner\n" +
			CardPile.REDTEXT + "\tAuthor:" + CardPile.RESET + "\t\tRafael Grigorian\n" +
			CardPile.REDTEXT + "\tLicense:" + CardPile.RESET + "\tThe MIT License"
		);
	}

	/**
	 * This function prints out the help menu along with a brief description of the game.  Nothing
	 * else happens.
	 * @return  void
	 */
	protected void help () {
		// Print out help
		System.out.println (
			"\n\tThis is the help menu and it shows some of the commands that can be used within " +
			"the\n\tgame console. All commands can be expressed in both lower and upper case\n\n" +
			CardPile.REDTEXT + "\tQ\t\t\t" + CardPile.RESET + "Quit the Kings Corner game.\n" +
			CardPile.REDTEXT + "\tH\t\t\t" + CardPile.RESET +"Displays this menu.\n" +
			CardPile.REDTEXT + "\tA\t\t\t" + CardPile.RESET +"Shows author and information " +
				"about the game.\n" +
			CardPile.REDTEXT + "\tD\t\t\t" + CardPile.RESET +"Takes a card from the deck if " +
				"deck is not empty, otherwise\n\t\t\t\tskips user's turn\n" +
			CardPile.REDTEXT + "\tL  <Card>  <Pile>\t" + CardPile.RESET +"Uses the first " +
				"argument, which is a made up from two characters\n\t\t\t\tthe first being the " +
				"rank of your card and the second being the\n\t\t\t\tsuit of your card, and the " +
				"second argument being which pile to\n\t\t\t\tput your card on.\n" +
			CardPile.REDTEXT + "\tM  <From>  <To>\t\t" + CardPile.RESET +"Moves the 'from' " +
				"pile, first argument, to the 'to' pile which\n\t\t\t\tis the second argument."
		);
		// Print out instructions
		System.out.println (
			"\n\tThis game is fairly simple to play.  Each player gets 7 cards initially and the " +
			"computers\n\tdeals first. Between rounds, dealers are switched, whoever didn't deal " +
			"goes first.  At\n\tthe start of the round, a card is placed on pile 1 - 4.  Only " +
			"kings can be laid down onto\n\tpiles 5 - 8.  A player wins a round when they have " +
			"no cards in their hand.  Place cards on\n\tcards on the piles as long as the rank " +
			"is one less then the bottom of the pile and of a\n\tdifferent color.  Once the " +
			"round is done, the hands are summed up and added to the players\n\tscore.  Kings " +
			"are worth 10 points otherwise every other card is work 1 point.  Once a\n\tscore " +
			"of 25 is reached from either player, the player that has a lower score wins " +
			"the\n\tgame. Have fun!\n"
		);
	}

}