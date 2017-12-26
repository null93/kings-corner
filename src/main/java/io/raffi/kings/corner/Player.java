package io.raffi.kings.corner;

import java.io.*;
import java.util.*;

/**
 * Player.java - This class keeps all necessary information about a player including the score,
 * their hand, and all necessary function to make a move.  It also contains a function that
 * controls user input by parsing it.  The Computer class also inherits from this class.
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @package     Project #01 - Kings Corner
 * @category    Player
 * @author      Rafael Grigorian
 * @license     GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
class Player {

	/**
	 * This data member contains a pointer to the game board, it is necessary to save this pointer
	 * to use the boards functions like help, about, quit, as well as render.
	 * @var     Board       Board       A pointer to the game board instance
	 */
	protected Board Board;

	/**
	 * This data member contains the instance of this instance player's pile of cards, or hand.
	 * @var     CardPile    Hand        The player's CardPile instance
	 */
	protected CardPile Hand;

	/**
	 * This data member keeps track of the penalty points that the player has acquired.
	 * @var     int         score       The player's score
	 */
	protected int score;

	/**
	 * This constructor takes in an argument that is an instance of the game which it later saves
	 * that pointer to it.  It initializes the players score to zero and initializes the deck as
	 * well.
	 * @param   Board       board       Instance of game board
	 */
	protected Player ( Board board ) {
		// Save pointer to passed board
		this.Board = board;
		// Initialize players hand
		this.Hand = new CardPile ("Your Hand");
		// Set initial score to zero
		this.score = 0;
	}

	/**
	 * This function prompts user for input on the next move it can make.  It does basic syntax
	 * checking and then calls the specialized function for the task in hand.  That specialized
	 * function will then check for semantic errors and complete that task if possible.
	 * @return  void
	 */
	protected void turn () {
		// Check to see if round has ended
		if ( this.Board.checkRoundEnd () ) {
			// Return from this function
			return;
		}
		// Initialize scanner
		Scanner reader = new Scanner ( System.in );
		// Initialize local variables
		String command;
		// Get player input, split it into segments and get the length of the segments
		System.out.print ("> ");
		command = reader.nextLine ();
		String [] commands = command.split ("\\s+");
		int length = commands.length;
		// Switch between commands
		switch ( commands [ 0 ] ) {
			// Hidden feature that will finish off the round for me
			case "^":
				// Replace with computer, copy over data
				Computer comp = new Computer ( this.Board );
				this.Board.Player = comp;
				comp.Hand = this.Hand;
				comp.score = this.score;
				comp.turn ();
				break;
			// This command quits the game
			case "q":
			case "Q":
				// Check that one word was passed
				if ( length == 1 ) {
					// Call the specialized quit function from board class
					this.Board.quit ();
				}
				// Otherwise prep and render error
				else {
					// Set board message and render
					this.Board.message = "Error: Unknown additional parameters were passed!";
					this.Board.render ();
				}
				// Prompt player for input again
				this.turn ();
				break;
			// This command displays the about menu
			case "a":
			case "A":
				// Check that one word was passed
				if ( length == 1 ) {
					// Call the specialized about function from board class
					this.Board.about ();
				}
				// Otherwise prep and render error
				else {
					// Set board message and render
					this.Board.message = "Error: Unknown additional parameters were passed!";
					this.Board.render ();
				}
				// Prompt player for input again
				this.turn ();
				break;
			// This command displays the help menu
			case "h":
			case "H":
				// Check that one word was passed
				if ( length == 1 ) {
					// Call the specialized help function from board class
					this.Board.help ();
				}
				// Otherwise prep and render error
				else {
					// Set board message and render
					this.Board.message = "Error: Unknown additional parameters were passed!";
					this.Board.render ();
				}
				// Prompt player for input again
				this.turn ();
				break;
			// This command deals a card to the player
			case "d":
			case "D":
				// Check that one word was passed
				if ( length == 1 ) {
					// Try to take a card from the file, if deck is empty then...
					if ( !this.deal () ) {
						// Set board message and render board
						this.Board.message = "Deck is empty, your turn skipped!";
						this.Board.render ();
					}
				}
				// Otherwise prep and render error
				else {
					// Set board message and prompt player for further input
					this.Board.message = "Error: Unknown additional parameters were passed!";
					this.turn ();
				}
				// Render out the board either way
				this.Board.render ();
				break;
			// This command lays a card from the hand onto one of the game card piles
			case "l":
			case "L":
				// Check to see that argument is three in length and that arguments are in order
				if ( length == 3 && commands [ 1 ].length () == 2 && isInt ( commands [ 2 ] ) ) {
					// Save and parse arguments locally
					String card = commands [ 1 ];
					int pile = Integer.parseInt ( commands [ 2 ] );
					// Call the specialized lay function in player class
					if ( this.lay ( card, pile ) ) {
						// If successful, set game board message
						this.Board.message  = "Put card " + card.toUpperCase () + " onto pile ";
						this.Board.message += pile + "!";
					}
				}
				// Otherwise, prep and render error
				else {
					this.Board.message = "Error: Invalid or no arguments passed!";
				}
				// Render board and prompt user for input
				this.Board.render ();
				this.turn ();
				break;
			// This command moves one pile to another
			case "m":
			case "M":
				// Check that three arguments were passed, two of which are integers
				if ( length == 3 && isInt ( commands [ 1 ] ) && isInt ( commands [ 2 ] ) ) {
					// Convert arguments to integers
					int from = Integer.parseInt ( commands [ 1 ] );
					int to = Integer.parseInt ( commands [ 2 ] );
					// Run specialized function within player class
					if ( this.move ( from, to ) ) {
						// If successful, set error for game board
						this.Board.message = "Moved pile " + from + " to " + to + "!";
					}
				}
				// Otherwise, prep error message
				else {
					this.Board.message = "Error: Insufficient arguments passed to move function!";
				}
				// Render board and prompt player from input
				this.Board.render ();
				this.turn ();
				break;
			// Default error message will be displayed
			default:
				// Construct board message
				this.Board.message =  "Error: Unknown command '" + commands [ 0 ];
				this.Board.message += "' passed, type 'h' or 'H' for help!";
				// Render board and prompt user for input
				this.Board.render ();
				this.turn ();
		}
	}

	/**
	 * This function takes out a card from the top of the deck, if deck is not empty, and then will
	 * sort the hand.  If deck is empty then function will return false.
	 * @return  boolean                 True if deck is not empty and card was drawn
	 */
	protected boolean deal () {
		// Check to see if there are cards in deck
		if ( this.Board.Deck.size == 0 ) {
			// If there isn't we failed
			return false;
		}
		// Take a card from the deck and put it in players hand
		this.Hand.append ( this.Board.Deck.deal () );
		// Sort the deck after
		this.Hand.sort ();
		// Return true otherwise
		return true;
	}

	/**
	 * This specialized function will do semantic error checking and try to move one pile to
	 * another.  If the move was successfully executed the true will be returned.
	 * @param   int         from        None-index based integer for the from pile
	 * @param   int         to          None-index based integer for the to pile
	 * @return  boolean                 Returns true if move was successful
	 */
	protected boolean move ( int from, int to ) {
		// Check bounds for from and to, make sure they can represent a pile on game board
		if ( from <= 8 && to <= 8 && from > 0 && to > 0 ) {
			// Define card pile local variables for use
			CardPile fromPile = this.Board.Piles [ from - 1 ];
			CardPile toPile = this.Board.Piles [ to - 1 ];
			// Make sure that the from pile is not empty
			if ( fromPile.size > 0 ) {
				// If we are trying to move a king to a king pile
				if ( fromPile.top.Rank == Rank.KING && to > 4 && from < 5 ) {
					// Loop through from pile until its empty
					while ( fromPile.size > 0 ) {
						// Pop top card from from pile and append to back of to pile
						toPile.append ( fromPile.remove ( fromPile.top.Rank, fromPile.top.Suit ) );
					}
					// Return true for successful move
					return true;
				}
				// If it is a king but is not in targeted into one of the king piles
				else if ( fromPile.top.Rank == Rank.KING ) {
					this.Board.message = "Error: You can only move a king pile from (1-4) to (5-8)!";
				}
				// If the to pile is not empty
				else if ( toPile.size != 0 ) {
					// Set boolean variables for checking
					boolean passRank = toPile.bottom.Rank.ordinal () == fromPile.top.Rank.ordinal () + 1;
					boolean passColor = toPile.bottom.Color != fromPile.top.Color;
					// If appropriate cards are present
					if ( passRank && passColor  ) {
						// Loop through from pile until its empty
						while ( fromPile.size > 0 ) {
							// Pop top card from from pile and append to back of to pile
							toPile.append (
								fromPile.remove ( fromPile.top.Rank, fromPile.top.Suit )
							);
						}
						// Return true for successful move
						return true;
					}
					// Otherwise, report error saying that card is incompatible
					else {
						this.Board.message = "Error: Must be of one lower rank and different color!";
					}
				}
				// Otherwise, prep error to say that invalid locations were passed
				else {
					this.Board.message = "Error: Invalid move locations!";
				}
			}
			// Otherwise, report that the from pile is empty and command makes no sense
			else {
				this.Board.message = "Error: From pile is empty!";
			}
		}
		// Otherwise report error saying that arguments are out of bounds
		else {
			this.Board.message = "Error: Arguments to move function are out of bounds, use (1-8).";
		}
		// Render and return false
		this.Board.render ();
		return false;
	}

	/**
	 * This function is a specialized function that handles semantic errors and lays a card from a
	 * players hand onto a game card pile.  If successful, then return true.
	 * @param   String      card        String representing card rank and suit
	 * @param   int         pile        Pile to lay card onto
	 * @return  boolean                 Whether operation was successful
	 */
	protected boolean lay ( String card, int pile ) {
		// Split rank and suit into separate strings
		Rank rank = Rank.which ( card.charAt ( 0 ) + "" );
		Suit suit = Suit.which ( card.charAt ( 1 ) + "" );
		// If rank and suit is valid
		if ( rank != null && suit != null ) {
			// Make sure pile number is in bound
			if ( pile < 9 && pile > 0 ) {
				// Try to remove this card from players hand
				Card from = this.Hand.remove ( rank, suit );
				// Make sure that card exists in players hand
				if ( from != null ) {
					// Initialize variables to make life easy
					CardPile target = this.Board.Piles [ pile - 1 ];
					boolean isKing = from.Rank == Rank.KING;
					// If we are trying to put card into empty card pile
					if ( isKing && pile > 4 && target.size == 0 ) {
						// Append card to king pile and return true
						target.append ( from );
						return true;
					}
					// If it is not a king
					else if ( !isKing ) {
						// If its a regular empty pile
						if ( target.size == 0 && pile < 5 ) {
							// Append to empty regular pile and return true
							target.append ( from );
							return true;
						}
						// If its and empty kings card pile, report error
						else if ( target.size == 0 && pile > 4 ) {
							this.Board.message = "Error: Kings go into this piles (5-8) only!";
						}
						// If it's a compatible move
						else if ( target.bottom.Rank.ordinal () == from.Rank.ordinal () + 1 &&
								  from.Color != target.bottom.Color ) {
							// Append card to pile and return true
							target.append ( from );
							return true;
						}
						// Otherwise, report that the card is incompatible
						else {
							this.Board.message = "Error: Card cannot be laid onto this pile!";
						}
					}
					// Otherwise, report that only kings can be laid in piles 5 - 8
					else {
						this.Board.message = "Error: Kings can only lay in empty piles (5-8)!";
					}
				}
				// Otherwise, report that card is not in hand
				else {
					this.Board.message = "Error: Passed card is not in your hand!";
				}
				// If card is in hand and errors were found, then put it back in
				if ( from != null ) {
					// Push card back into hand and sort
					this.Hand.append ( from );
					this.Hand.sort ();
				}
			}
			// Otherwise, report that pile number is out of bounds
			else {
				this.Board.message = "Error: Passed pile number is out of range!";
			}
		}
		// Otherwise, report that rank and/or suit is invalid
		else {
			this.Board.message = "Error: Invalid card rank and/or suit passed!";
		}
		// Return false be default
		return false;
	}

	/**
	 * This is a helper function that will check if passed string can be represented as an integer.
	 * If it can it will return true.
	 * @param   String          string          The string to be examined
	 * @return  boolean                         If string can be represented as integer, then true
	 */
	public static boolean isInt ( String string ) {
		// Try to convert integer
		try {
			// Convert to integer and return true
			Integer.parseInt ( string );
			return true;
		// Otherwise catch exception and return false
		} catch ( NumberFormatException error ) {
			return false;
		}
	}

}