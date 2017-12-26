package io.raffi.kings.corner;

/**
 * CardPile.java - This class acts like a card pile but also like a players hand.  There are
 * numerous functions that interact with the card pile class and are able to manipulate the linked
 * list that the card pile points to.
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @project     Project #01 - Kings Corner
 * @package     io.raffi.kings.corner
 * @category    Card Pile
 * @author      Rafael Grigorian
 * @license     The MIT License
 */
class CardPile {

	// String declarations for coloring cards
	protected static final String RED = "\033[107;31m";
	protected static final String BLACK = "\033[107;30m";
	protected static final String RESET = "\033[49;39m";
	protected static final String REDTEXT = "\033[31;49m";

	/**
	 * This data member points to a card object that is on the top of the pile
	 * @var     Card        top         Points to the top of the card pile
	 */
	protected Card top;

	/**
	 * This data member points to a card object that is on the bottom of the pile
	 * @var     Card        bottom      Points to the bottom of the card pile
	 */
	protected Card bottom;

	/**
	 * This data member keeps track of how large the card pile is.
	 * @var     int         size        How many cards are in the card pile
	 */
	protected int size;

	/**
	 * This data member stores the name of the pile, this is initialized within the constructor
	 * function.
	 * @var     String      name        Name of the deck
	 */
	protected String name;

	/**
	 * This constructor takes in a name argument which names the deck, and simply sets all pointers
	 * to null as well as initializes the card pile size to zero.
	 * @var     String      name        Name of the deck to apply
	 */
	protected CardPile ( String name ) {
		// Initialize pointers
		this.top = null;
		this.bottom = null;
		// Initialize data members
		this.size = 0;
		this.name = name;
	}

	/**
	 * This function returns the card at a specified index, if the index is out of range, then
	 * null is returned.  This function simple returns the pointer to the object and does not
	 * uncouple the card from the card pile.  It is simply returned.
	 * @param   int         index       The index number of the card we want to get from the pile
	 * @return  Card                    Returns the card at the specified index, or -1 if invalid
	 */
	protected Card index ( int index ) {
		// Initialize card to null
		Card card = null;
		// Check that the index is within our bounds
		if ( index >= 0 && index < this.size ) {
			// Initialize counter to zero, and set card to the top of the pile
			int count = 0;
			card = this.top;
			// Loop through until the counter matches the passed index
			while ( count < index && card != null ) {
				// Increment counter variable as well as node pointer
				card = card.next;
				count++;
			}
		}
		// Return the desired card, or null if invalid argument
		return card;
	}

	/**
	 * This function sorts the card pile in descending order, where the primary sort condition
	 * depends on the cards rank, and the secondary condition depends on the cards suit, which
	 * is also in descending order where clubs are the lowest, and spades are the highest.  This
	 * algorithm is essentially bubble sort, but it doesn't matter because we will only use this
	 * function to sort player's hands.
	 * @return  void
	 */
	protected void sort () {
		// Initially set flag to true
		boolean flag = true;
		// Initialize a temporary card to hold data fro swapping
		Card temporary = new Card ( 0, 0, 0 );
		// Loop through while flag is true
		while ( flag ) {
			// Set flag to false
			flag = false;
			// Loop though the whole card pile
			for ( int j = 0; j < this.size - 1; j++ ) {
				// Get the card values for current and next index
				Card cardOne = this.index ( j );
				Card cardTwo = this.index ( j + 1 );
				// For style, initialize boolean conditions
				boolean checkRanks = cardOne.Rank.ordinal () < cardTwo.Rank.ordinal ();
				boolean checkSuits = cardOne.Rank.ordinal () == cardTwo.Rank.ordinal () &&
									 cardOne.Suit.ordinal () < cardTwo.Suit.ordinal ();
				// Check whether any of the two booleans are true
				if ( checkRanks || checkSuits ) {
					// Set the temporary card values to the values of card one
					temporary.Color = cardOne.Color;
					temporary.Suit = cardOne.Suit;
					temporary.Rank = cardOne.Rank;
					// Set card one values to card two values
					cardOne.Color = cardTwo.Color;
					cardOne.Suit = cardTwo.Suit;
					cardOne.Rank = cardTwo.Rank;
					// Set card two values to temporary card values
					cardTwo.Color = temporary.Color;
					cardTwo.Suit = temporary.Suit;
					cardTwo.Rank = temporary.Rank;
					// Reset flag to true
					flag = true;
				}
			}
		}
	}

	/**
	 * This function appends a card to the bottom of the deck and increments the card pile size.
	 * @param   Card        card        The card we want to append to the card pile
	 * @return  void
	 */
	protected void append ( Card card ) {
		// If we have an empty card pile
		if ( this.size == 0 ) {
			// Set both the top and bottom to the passed card
			this.top = card;
			this.bottom = card;
			// Set next and previous to null
			card.previous = null;
			card.next = null;
		}
		// Otherwise append it to the bottom of the card pile
		else {
			// Adjust pointers to append to the bottom of the card pile
			this.bottom.next = card;
			card.previous = this.bottom;
			this.bottom = card;
			card.next = null;
		}
		// Increment card pile size
		this.size++;
	}

	/**
	 * This function removes a card from the card pile, given the card rank and suit which are
	 * passed as parameters to the function.  If no card of such specification was found, then we
	 * will return null.
	 * @param   Rank        rank        Enum value associated with the rank of the card
	 * @param   Suit        suit        Enum value associated with the suit of the card
	 * @return  Card                    Will return the removed card, or null if none found
	 */
	protected Card remove ( Rank rank, Suit suit ) {
		// Initialize iterator variable
		Card current = this.top;
		// Loop through the whole card pile
		while ( current != null ) {
			// Check to see if the current card has the same rank and suit
			if ( current.Rank == rank && current.Suit == suit ) {
				// Check to see that current card is not on top of pile
				if ( current.previous != null ) {
					// Link previous card to the next card relative to current
					current.previous.next = current.next;
				}
				// Otherwise treat current like it's on top of the pile
				else {
					// set the top of the pile to the next card relative to current
					this.top = current.next;
				}
				// Check to see that current isn't the last card in the pile
				if ( current.next != null ) {
					// Set the previous value of the next card to the previous card
					current.next.previous = current.previous;
				}
				// Otherwise, treat current like it's on the bottom of the pile
				else {
					// Point the bottom of the pile to be the previous card relative to current
					this.bottom = current.previous;
				}
				// Set all pointers inside current to be null ( unattached from pile )
				current.next = null;
				current.previous = null;
				// Decrement pile count
				this.size--;
				// Return the removed card
				return current;
			}
			// Iterate through to the next card in the card pile
			current = current.next;
		}
		// Return null if nothing is found
		return current;
	}

	/**
	 * This function renders a card pile and prints it out to standard output.  It traversed
	 * through the deck while building up three strings associated with three line numbers.
	 * There is also an option to print this out with the card information hidden for
	 * implementations such as the deck and computers hand.
	 * @param   boolean         hidden          Should we hide the contents of the card
	 */
	protected void render ( boolean hidden ) {
		// Initialize the lines we will use to generate the card pile, and print out name
		String lineOne = "           ";
		String lineTwo = REDTEXT + this.name +
			new String ( new char [ 11 - this.name.length () ] ).replace ( "\0", " " );
		String lineThree = "           ";
		// Initialize iterative variable
		Card card = this.top;
		// Loop through all cards in pile except the last one
		while ( card != null && card.next != null && this.size > 1 ) {
			// If we choose to hide the cards
			if ( hidden ) {
				// Append to each line
				lineOne += RED + "  " + BLACK + "|" + RESET;
				lineTwo += RED + " U" + BLACK + "|" + RESET;
				lineThree += RED + "  " + BLACK + "|" + RESET;
			}
			// If we choose to display the cards
			else {
				// Construct suit string and suit with rank string
				String suit = Color.which ( card.Suit ) + Suit.which ( card.Suit );
				String both = Color.which ( card.Suit ) +
							  Rank.which ( card.Rank ) +
							  Suit.which ( card.Suit );
				// Append to each line
				lineOne += BLACK + both + BLACK + "|" + RESET;
				lineTwo += BLACK + "  " + BLACK + "|" + RESET;
				lineThree += BLACK + " " + suit + BLACK + "|" + RESET;
			}
			// Iterate card to next node
			card = card.next;
		}
		// For the last card, if its hidden, then hide contents
		if ( hidden && this.size > 0 ) {
			// Append to each line
			lineOne += RED + "     " + RESET;
			lineTwo += RED + " UIC " + RESET;
			lineThree += RED + "     " + RESET;
		}
		// Otherwise, render last card on pile
		else if ( card != null ) {
			// Construct suit string and suit with rank string
			String suit = Color.which ( card.Suit ) + Suit.which ( card.Suit );
			String both = Color.which ( card.Suit ) +
						  Rank.which ( card.Rank ) +
						  Suit.which ( card.Suit );
			// Append to each line
			lineOne += RED + both + " " + suit + " " + RESET;
			lineTwo += RED + "  " + suit + "  " + RESET;
			lineThree += RED + " " + suit + " " + suit + " " + RESET;
		}
		else if ( this.size == 0 ) {
			// Append to each line
			lineOne += RESET + "     " + RESET + RESET;
			lineTwo += RESET + "EMPTY" + RESET;
			lineThree += RESET + "     " + RESET + RESET;
		}
		// Finally return string array
		System.out.println ( lineOne );
		System.out.println ( lineTwo );
		System.out.println ( lineThree );
		System.out.println ("");
	}

}