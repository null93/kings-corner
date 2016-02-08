/**
 * Color.java - This file contains an enum declaration that defines a statically interactive way to
 * address the color of a card, whether it be black or red.  This enum declaration is used
 * throughout the whole program.  This enum also contains a function that returns formatted strings
 * in color
 * @version     1.0.0
 * @university  University of Illinois at Chicago
 * @course      CS342 - Software Design
 * @package     Project #01 - Kings Corner
 * @category    Enum
 * @author      Rafael Grigorian
 * @license     GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
public enum Color {
	BLACK,
	RED;

	/**
	 * This function determines which color a suit should be and it returns a string that is
	 * formatted to be colored.
	 * @param   Suit        suit        The enum representation of the suit
	 * @return  String                  Color formatted string
	 */
	protected static String which ( Suit suit ) {
		// Determine if the suit is hearts or diamonds
		if ( suit == Suit.HEARTS || suit == Suit.DIAMONDS ) {
			// Return red with white background
			return "\033[107;31m";
		}
		// Return black with white background
		return "\033[107;30m";
	}
}