/**
 * Suit.java - This file contains an enum declaration that defines a statically interactive way to
 * address the suit of a card, whether it be hearts, diamonds, clubs, or spades.  This enum
 * declaration is used throughout the whole program.
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @package     Project #01 - Kings Corner
 * @category    Enum
 * @author      Rafael Grigorian
 * @license     GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
public enum Suit {
	SPADES,
	HEARTS,
	DIAMONDS,
	CLUBS;

	/**
	 * This function returns an ASCII coded symbol that is equivalent to a card suit in a deck of
	 * cards.
	 * @param   Suit        suit        Enum representation of a suit
	 * @return  String                  Ascii representation of said suit
	 */
	protected static String which ( Suit suit ) {
		// Determine if the enum is spades
		if ( suit == Suit.SPADES ) {
			return "♠";
		}
		// Determine if the enum is hearts
		else if ( suit == Suit.HEARTS ) {
			return "♥";
		}
		// Determine if the enum is diamonds
		else if ( suit == Suit.DIAMONDS ) {
			return "♦";
		}
		// otherwise it is clubs
		else {
			return "♣";
		}
	}

	/**
	 * This function is overloading the which function above.  It takes in the argument to be of
	 * type string and returns the appropriate Suit enum back.  If not a valid rank, then null
	 * will be returned.
	 * @param   String      suit        The suit expressed by a character
	 * @return  Suit                    Returns suit enum, or null if invalid
	 */
	protected static Suit which ( String suit ) {
		// Switch to see which rank it is, return proper rank
		switch ( suit ) {
			case "s":
			case "S":
				return Suit.SPADES;
			case "H":
			case "h":
				return Suit.HEARTS;
			case "d":
			case "D":
				return Suit.DIAMONDS;
			case "c":
			case "C":
				return Suit.CLUBS;
			// If all fails, then we return null
			default:
				return null;
		}
	}

}