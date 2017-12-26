package io.raffi.kings.corner;

/**
 * Computer.java - This class extends from the Player class and adds functionality to automate a
 * players turn.  Other functions in this class are modularized in order to help the automation
 * flow of the AI.
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @package     Project #01 - Kings Corner
 * @category    Computer
 * @author      Rafael Grigorian
 * @license     GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
class Computer extends Player {

	/**
	 * This constructor takes in a board that it will pass to the super constructor and will also
	 * change the card piles name.
	 * @param   Board       board       Pointer to the board instance
	 */
	protected Computer ( Board board ) {
		super ( board );
		this.Hand.name = "Ai Hand";
	}

	/**
	 * This function tries to see if the computer can put a king from it's hand onto the game pile.
	 * @return  boolean                 If anything happened to the board within this function
	 */
	protected boolean moveKingsFromHand () {
		// Initialize iterative variable
		Card card = this.Hand.top;
		// Loop through card pile
		while ( card != null ) {
			// Check if current card in hand is a king
			if ( card.Rank == Rank.KING ) {
				// Loop through king card piles
				for ( int pile = 4; pile < 8; pile++ ) {
					// Initialize current card pile
					CardPile current = this.Board.Piles [ pile ];
					// Check to see that the king pile is empty
					if ( current.size == 0 ) {
						// Remove card from hand and append to pile
						current.append ( this.Hand.remove ( card.Rank, card.Suit ) );
						// Append computers move to board message
						this.Board.message += "L " + Rank.which ( card.Rank );
						this.Board.message += Suit.which ( card.Suit ) + " to ";
						this.Board.message += ( pile + 1 ) + ", ";
						// Render board and return true
						this.Board.render ();
						return true;
					}
				}
			}
			// Iterate card to be the next card in hand
			card = card.next;
		}
		// Return false by default
		return false;
	}

	/**
	 * This function checks to see if there are any kings in standard piles and tries to move them
	 * to the king piles.  If anything happens it will return true.
	 * @return  boolean                 If anything happened to the board within this function
	 */
	protected boolean moveKingsFromPiles () {
		// Loop through regular card piles on board
		for ( int fromPileIndex = 0; fromPileIndex < 4; fromPileIndex++ ) {
			// Set current card pile to literary variable
			CardPile fromPile = this.Board.Piles [ fromPileIndex ];
			// If from pile is not empty and top of from pile is a king
			if ( fromPile.size != 0 && fromPile.top.Rank == Rank.KING ) {
				// Loop through all the boards king piles
				for ( int toPileIndex = 4; toPileIndex < 8; toPileIndex++ ) {
					// Save literary to pile
					CardPile toPile = this.Board.Piles [ toPileIndex ];
					// If the target king pile is empty
					if ( toPile.size == 0 ) {
						// Loop through while from pile is not empty
						while ( fromPile.size > 0 ) {
							// Append top of from pile to the bottom of to pile
							toPile.append ( fromPile.remove ( fromPile.top.Rank, fromPile.top.Suit ) );
							// Render board
							this.Board.render ();
						}
						// Append computers move to board message
						this.Board.message += "M " + ( fromPileIndex + 1 ) + " ";
						this.Board.message += ( toPileIndex + 1 ) + ", ";
						// Render board and return true
						this.Board.render ();
						return true;
					}
				}
			}
		}
		// Otherwise return false
		return false;
	}

	/**
	 * This function will merge two piles on the game board as long as they are compatible.  If
	 * this function changed anything on the board it will return true.
	 * @return  boolean                 If anything happened to the board within this function
	 */
	protected boolean mergePiles () {
		// Loop through regular piles on the board
		for ( int fromPileIndex = 0; fromPileIndex < 4; fromPileIndex++ ) {
			// Set current regular pile for iteration
			CardPile fromPile = this.Board.Piles [ fromPileIndex ];
			// Loop through all piles on the board
			for ( int toPileIndex = 0; toPileIndex < 8; toPileIndex++ ) {
				// Set to pile for iteration
				CardPile toPile = this.Board.Piles [ toPileIndex ];
				// Check that piles are not empty and that we are not trying to copy over it itself
				if ( fromPile.size > 0 && toPile.size > 0 && fromPileIndex != toPileIndex ) {
					// Set booleans for style
					boolean passRank = fromPile.top.Rank.ordinal () + 1 == toPile.bottom.Rank.ordinal ();
					boolean passColor = fromPile.top.Color != toPile.bottom.Color;
					// Check if piles are compatible
					if ( passRank && passColor ) {
						// Loop while from pile if not empty
						while ( fromPile.size > 0 ) {
							// Append card from the top for from pile to the bottom of to pile
							toPile.append ( fromPile.remove ( fromPile.top.Rank, fromPile.top.Suit ) );
							// Render board
							this.Board.render ();
						}
						// Construct message for board, render board, and return true
						this.Board.message += "M " + ( fromPileIndex + 1 ) + " ";
						this.Board.message += ( toPileIndex + 1 ) + ", ";
						this.Board.render ();
						return true;
					}
				}
			}
		}
		// By default return false
		return false;
	}

	/**
	 * This function places cards from the players hand to a card pile on the board.  If this
	 * function changes the board in any way, then return true.
	 * @return  boolean                 If anything happened to the board within this function
	 */
	protected boolean placeCardToPileFromHand () {
		// Set card for iteration
		Card card = this.Hand.top;
		// Loop through all cards in hand
		while ( card != null ) {
			// Loop though all piles on board
			for ( int pile = 0; pile < 8; pile++ ) {
				// Set to pile for iteration and booleans for comparisons
				CardPile toPile = this.Board.Piles [ pile ];
				boolean passRank = toPile.size > 0 && card.Rank.ordinal () + 1 == toPile.bottom.Rank.ordinal ();
				boolean passColor = toPile.size > 0 && card.Color != toPile.bottom.Color;
				// Check to see that card and pile are compatible
				if ( toPile.size > 0 && passRank && passColor ) {
					// Append the card to the end of the pile
					this.Board.Piles [ pile ].append ( this.Hand.remove ( card.Rank, card.Suit ) );
					// Push history, render, and return true
					this.Board.message += "L " + Rank.which ( card.Rank );
					this.Board.message += Suit.which ( card.Suit ) + " to " + ( pile + 1 ) + ", ";
					this.Board.render ();
					return true;
				}
			}
			// Iterate to next card
			card = card.next;
		}
		// Return false by default
		return false;
	}

	/**
	 * This function tries to place a card from players hand onto an empty regular pile.
	 * @return  boolean                 If anything happened to the board within this function
	 */
	protected boolean placeCardOnEmptyFromHand () {
		// Save top card from deck for iteration
		Card card = this.Hand.top;
		// Loop through whole deck
		while ( card != null ) {
			// Loop through regular card piles on board
			for ( int pile = 0; pile < 4; pile++ ) {
				// Initialize toPile for style
				CardPile toPile = this.Board.Piles [ pile ];
				// If the pile is empty
				if ( toPile.size == 0 ) {
					// Append card to the empty pile
					this.Board.Piles [ pile ].append ( this.Hand.remove ( card.Rank, card.Suit ) );
					// Push to history, render, and return true
					this.Board.message += "L " + Rank.which ( card.Rank );
					this.Board.message += Suit.which ( card.Suit ) + " to " + ( pile + 1 ) + ", ";
					this.Board.render ();
					return true;
				}
			}
			// Iterate to next card
			card = card.next;
		}
		// By default return true
		return false;
	}

	/**
	 * This function overloads the Player defined function and runs AI automation to determine
	 * which moves it can make.
	 * @return  void
	 */
	protected void turn () {
		// Initialize message for move history
		this.Board.message = "Ai's Move(s): ";
		// Try to move kings from hand to king piles
		while ( moveKingsFromHand () ) {};
		// Try to move kings from regular piles to king piles
		while ( moveKingsFromPiles () ) {};
		// Try to merge piles on board
		while ( mergePiles () ) {};
		// Try to place cards from hands to a card pile
		while ( placeCardToPileFromHand () ) {
			// If successful, then try to merge piles
			while ( mergePiles () ) {};
		};
		// Try to place a card from hand to an empty regular pile
		while ( placeCardOnEmptyFromHand () ) {};
		// Get a card from the deck if the game is not done
		if ( !this.Board.checkRoundEnd () ) {
			// Try to deal a card
			if ( this.deal () ) {
				// If there is a card, then append to history
				this.Board.message += "took a card, ";
			}
			// State that the turn is done
			this.Board.message += "your turn!";
		}
	}

}