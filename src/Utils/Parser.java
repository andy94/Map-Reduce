/**********************************************************************
 * Tema        : 2 - APD                                              *
 * Autor       : Andrei Ursache                                       *
 * Grupa       : 332 CA                                               *
 * Data        : 21.11.2015                                           *
 **********************************************************************/

package Utils;

/**
 * Pentru identificare delimitatori
 *
 */
public class Parser {

	/**
	 * Lungime maxima cuvant
	 */
	public static int maxWordLength = 100;

	public static char[] delimiters = { ',', '.', '^', '@', '`', '*', ' ',
			'\t', '_', '\'', '/', '#', '$', '\\', '+', '-', '&', '=', '<', '>',
			'=', '%', '(', ')', '{', '}', '"', '[', ']', ';', ':', '?', '!',
			'~', '\n', '\r' };

	public static String delim = new String(delimiters);

	public static boolean[] notDelim; // false - e ok | true - e delim

	public static boolean isDelimiter(byte b) {
		for (char c : delimiters) {
			if (b == c) {
				return true;
			}
		}
		if (b == '\n' || b == '\r' || b == 3) {
			return true;
		}
		return false;
	}
}
