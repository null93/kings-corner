package io.raffi.kings.corner;

import java.io.*;
import java.util.*;

/**
 * Deck.java - This class extends from the CardPile class, and it adds addition functionality to
 * that class as well as a different constructor.  This class contains deck specific functionality.
 * On construct time, it populates it's pile with all possible cards in a normal deck of playing
 * cards.  In addition it also has the option to shuffle the pile.
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @project     Project #01 - Kings Corner
 * @package     io.raffi.kings.corner
 * @category    Card Pile
 * @author      Rafael Grigorian
 * @license     The MIT License
 */
class Deck extends CardPile {

	/**
	 * This constructor triggers the CardPile constructor, as well as populates the deck with all
	 * possible types of cards found in a deck of playing cards.
	 * @param   String      name        Name of the deck, passed to CardPile constructor
	 * @see     CardPile.java
	 */
	protected Deck ( String name ) {
		// Call the CardPile class constructor
		super ( name );
		// Loop though all ranks as if they were enumerated from the Ranks enum
		for ( int rank = 0; rank < 13; rank++ ) {
			// Loop though all suits as if they were enumerated from the Suits enum
			for ( int suit = 0; suit < 4; suit++ ) {
				// Construct a new card from the suit and rank of current iteration
				Card card = new Card ( ( suit == 1 || suit == 2 ) ? 1 : 0, suit, rank );
				// Append this card to the deck
				this.append ( card );
			}
		}
	}

	/**
	 * This function shuffles the deck by swapping two random cards in the deck a variable amount
	 * of times.  This magnitude of shuffling can be controlled by the magnitude parameter.
	 * @param   int         magnitude   How well we want to shuffle the deck
	 * @return  void
	 */
	protected void shuffle ( int magnitude ) {
		// No need to shuffle if we have less than two cards in the deck
		if ( this.size > 1 ) {
			// Swap two random cards a variable amount of times
			for ( int swap = 0; swap < magnitude; swap++ ) {
				// Pick two random indexes in our deck
				int indexOne = ( int ) ( Math.random () * ( this.size - 1 ) );
				int indexTwo = ( int ) ( Math.random () * ( this.size - 1 ) );
				// No use swapping if they are the same index
				if ( indexOne != indexTwo ) {
					// Get the cards in each specified index
					Card cardOne = this.index ( indexOne );
					Card cardTwo = this.index ( indexTwo );
					// Save card one's data
					Color color = cardOne.Color;
					Suit suit = cardOne.Suit;
					Rank rank = cardOne.Rank;
					// Swap the data between both cards
					cardOne.Color = cardTwo.Color;
					cardOne.Suit = cardTwo.Suit;
					cardOne.Rank = cardTwo.Rank;
					cardTwo.Color = color;
					cardTwo.Suit = suit;
					cardTwo.Rank = rank;
				}
			}
		}
	}

	/**
	 * This function pops the first card off the top of the deck if any are there, and returns it.
	 * It also decrements the deck size by one.
	 * @return  Card                    Returns the card on top of the deck, or null if empty deck
	 */
	protected Card deal () {
		// Initially set card to return to null
		Card card = null;
		// If the deck is not empty precede
		if ( this.size > 0 ) {
			// Remove the top card off the deck and save it
			card = this.remove ( this.top.Rank, this.top.Suit );
		}
		// Return card from the top of the deck, or null
		return card;
	}

}