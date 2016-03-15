/**********************************************************************
 * Tema        : 2 - APD                                              *
 * Autor       : Andrei Ursache                                       *
 * Grupa       : 332 CA                                               *
 * Data        : 21.11.2015                                           *
 **********************************************************************/

package Results;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Rezultatul uni task de tip Map
 *
 */
public class MapResult {
	private HashMap<Integer, Integer> map;
	private HashSet<String> maximalWords;
	private int maximalLength;

	public MapResult() {
		map = new HashMap<Integer, Integer>();
		maximalWords = new HashSet<String>();
		maximalLength = 0;
	}

	public int getMaximalLenghth() {
		return maximalLength;
	}

	public HashSet<String> getMaximalWords() {
		return maximalWords;
	}

	public HashMap<Integer, Integer> getMap() {
		return map;
	}

	/**
	 * Updateaza hashMapul pentru cuvantul dat
	 */
	public void processWord(String word) {
		int length = word.length();

		if (map.containsKey(length)) {
			map.put(length, map.get(length) + 1);
		} else {
			map.put(length, 1);
		}
		if (length == maximalLength) {
			maximalWords.add(word);
			return;
		}
		if (length > maximalLength) {
			maximalLength = length;
			maximalWords.clear();
			maximalWords.add(word);
		}

	}
}
