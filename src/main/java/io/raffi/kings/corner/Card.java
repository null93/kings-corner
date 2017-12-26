package io.raffi.kings.corner;

/**
 * Card.java - This class holds enum values that are associated with card suit, rank, and color.
 * This class also acts like the node part of a linked list.  It has a next and previous card
 * pointer and the deck class also implements this class since it acts like the front and back
 * pointer to the card nodes.
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @project     Project #01 - Kings Corner
 * @package     io.raffi.kings.corner
 * @category    Card
 * @author      Rafael Grigorian
 * @license     The MIT License
 */
class Card {

	/**
	 * This data member holds the value of the enum associated with the color of the card.
	 * @var     Color       Color       Color of card as defined in Color enum
	 * @see                             Color.java
	 */
	protected Color Color;

	/**
	 * This data member holds the value of the enum associated with the suit of the card.
	 * @var     Suit        Suit        Suit of card as defined in Suit enum
	 * @see                             Suit.java
	 */
	protected Suit Suit;

	/**
	 * This data member holds the value of the enum associated with the rank of the card.
	 * @var     Rank        Rank        Rank of card as defined in Rank enum
	 * @see                             Rank.java
	 */
	protected Rank Rank;

	/**
	 * This data member points to the next card in the card pile.  Remember that cards in this
	 * implementation act like a node in a linked list.
	 * @var     Card        next        Points to next card in card pile
	 */
	protected Card previous;

	/**
	 * This data member points to the next card in the card pile.  Remember that cards in this
	 * implementation act like a node in a linked list.
	 * @var     Card        next        Points to next card in card pile
	 */
	protected Card next;

	/**
	 * This constructor is overloaded and is used to pass enum values as arguments.  It sets it's
	 * appropriate data members based on those arguments.
	 * @param   Color       color       Enum associated with the Color enum
	 * @param   Suit        suit        Enum associated with the Suit enum
	 * @param   Rank        rank        Enum associated with the Rank enum
	 */
	protected Card ( Color color, Suit suit, Rank rank ) {
		// Save passed enum types internally
		this.Color = color;
		this.Suit = suit;
		this.Rank = rank;
		// Initialize next and previous data members
		this.previous = null;
		this.next = null;
	}

	/**
	 * This constructor is overloaded and is used to pass integer based values associated with
	 * enums into the arguments.  This constructor then sets the appropriate data into the data
	 * members based on the integer based index given that is associated with an enum with a
	 * corresponding type.
	 * @param   int         color       Color enum associated with this integer value.
	 * @param   int         suit        Suit enum associated with this integer value.
	 * @param   int         rank        Rank enum associated with this integer value.
	 */
	protected Card ( int color, int suit, int rank ) {
		// Save corresponding enum types internally
		this.Color = Color.values () [ color ];
		this.Suit = Suit.values () [ suit ];
		this.Rank = Rank.values () [ rank ];
		// Initialize next and previous data members
		this.previous = null;
		this.next = null;
	}

}