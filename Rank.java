/**
 * Rank.java - This file contains an enum declaration that defines a statically interactive way to
 * address the rank of a card, from ace to king.  This enum declaration is used throughout the
 * whole program. This enum also contains a function that returns a string that represents the card
 * rank in short abbreviations.
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @package     Project #01 - Kings Corner
 * @category    Enum
 * @author      Rafael Grigorian
 * @license     GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
public enum Rank {
	ACE,
	TWO,
	THREE,
	FOUR,
	FIVE,
	SIX,
	SEVEN,
	EIGHT,
	NINE,
	TEN,
	JACK,
	QUEEN,
	KING;

	/**
	 * This function translates a passed rank enum into a string that you would see on a card.
	 * @param   Rank        rank        Enum that describes rank
	 * @return  String                  Returns card representation of passed enum
	 */
	protected static String which ( Rank rank ) {
		// Return Ace
		if ( rank == Rank.ACE ) {
			return "A";
		}
		// Return numbered ranks
		else if ( rank.ordinal () < 9 ) {
			return rank.ordinal () + 1 + "";
		}
		// Return Ten
		else if ( rank == Rank.TEN ) {
			return "T";
		}
		// Return Jack
		else if ( rank == Rank.JACK ) {
			return "J";
		}
		// Return Queen
		else if ( rank == Rank.QUEEN ) {
			return "Q";
		}
		// Return King
		else if ( rank == Rank.KING ) {
			return "K";
		}
		// Return joker otherwise
		return null;
	}

	/**
	 * This function is overloading the which function above.  It takes in the argument to be of
	 * type string and returns the appropriate Rank enum back.  If not a valid rank, then null
	 * will be returned.
	 * @param   String      rank        The ranks expressed by a character
	 * @return  Rank                    Returns rank enum, or null if invalid
	 */
	protected static Rank which ( String rank ) {
		// If we succeed, then it is a numerical rank
		try {
			// Try to convert into an integer
			int num = Integer.parseInt ( rank );
			// Return the proper rank
			return Rank.values () [ num - 1 ];
		}
		// Otherwise its a letter rank
		catch ( NumberFormatException error ) {
			// Switch to see which rank it is, return proper rank
			switch ( rank ) {
				case "t":
				case "T":
					return Rank.TEN;
				case "j":
				case "J":
					return Rank.JACK;
				case "q":
				case "Q":
					return Rank.QUEEN;
				case "k":
				case "K":
					return Rank.KING;
				case "a":
				case "A":
					return Rank.ACE;
				// If all fails, then we return null
				default:
					return null;
			}
		}
	}

}