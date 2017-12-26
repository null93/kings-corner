package io.raffi.kings.corner;

import java.io.*;
import java.util.*;

/**
 * KingsCorner.java - This class is the driver class that contains the main function.  It will
 * initialize the game board and manage who's turn it is.  It will also reset the game when the
 * game is over and reinitialize the board when a round is done, while still maintaining the game
 * metadata.
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @package     Project #01 - Kings Corner
 * @category    Driver
 * @author      Rafael Grigorian
 * @license     GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
class KingsCorner {

	/**
	 * This function is the main driver function that initializes the board and drives the game
	 * play.
	 * @param   String []       args        Arguments passed to the game, not used
	 * @return  void
	 */
	public static void main ( String [] args ) {
		// Initialize board to play on, passing initial card count and winning threshold
		Board Board = new Board ( 7, 25 );
		Board.go ();
		// Loop through until we choose not to play again
		while ( true ) {
			// Loop through until a winner is crowned
			while ( !Board.checkWinner () ) {
				// Loop through until the round has ended
				while ( !Board.checkRoundEnd () ) {
					// Render the board after clearing screen
					Board.render ();
					// If it is the player's turn
					if ( Board.turn ) {
						// Prompt user for a turn
						Board.Player.turn ();
					}
					// Otherwise it is the computer's turn
					else {
						// Prompt computer for a turn
						Board.Computer.turn ();
					}
					// Change who's turn it is
					Board.turn = !Board.turn;
				}
				// If the game continues, then deal cards
				if ( !Board.checkWinner () ) {
					// Reset board and copy score and dealer data into board
					Board = Board.reset ( true );
					Board.go ();
				}
				// Ask user if they want to play again
				else {
					// Clear the screen
					System.out.print ("\033[2J\033[1;1H");
					// Render scores
					System.out.println (
						CardPile.REDTEXT + "Your Score: " + CardPile.RESET + Board.Player.score +
						CardPile.REDTEXT + "\tAI's Score: " + CardPile.RESET + Board.Computer.score
					);
					// Did the player win?
					if ( Board.Player.Hand.size == 0 && Board.Player.score < Board.Computer.score ) {
						System.out.println ("You won! Would you like to play again?\n");
					}
					// Then the computer won
					else {
						System.out.println ("You lost! Would you like to play again?\n");
					}
				}
			}
			// Check to see if the user wants to play again
			Scanner reader = new Scanner ( System.in );
			String command;
			System.out.print ("> ");
			command = reader.nextLine ();
			// See if user wants to play again
			if ( command.equals ("Y") || command.equals ("y") ) {
				// State who won the game, and ask user if they want to play again
				Board = Board.reset ( false );
				Board.go ();
			}
			// Otherwise break
			else {
				System.out.println ("");
				break;
			}
		}
	}

}