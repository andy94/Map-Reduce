/**********************************************************************
 * Tema        : 2 - APD                                              *
 * Autor       : Andrei Ursache                                       *
 * Grupa       : 332 CA                                               *
 * Data        : 21.11.2015                                           *
 **********************************************************************/

package Results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Rezultatul unui task de tip Reduce
 *
 */
public class ReduceResult {
	private HashMap<Integer, Integer> map;
	private int maximalCount;
	private int maximalLength;
	private double rank;

	public ReduceResult() {
		map = new HashMap<Integer, Integer>();
		maximalCount = 0;
		maximalLength = 0;
		rank = 0;
	}

	public long getFibo(int n) {
		long f = 0;
		long g = 1;

		for (int i = 1; i <= n; i++) {
			f = f + g;
			g = f - g;
		}
		return f;
	}

	/**
	 * Proceseaza o lista de rezultate de tip Map
	 */
	public void processMap(ArrayList<MapResult> mapResults) {
		HashSet<String> maximalWords = new HashSet<String>();
		int totalWords = 0;
		for (MapResult m : mapResults) {
			for (Map.Entry<Integer, Integer> entry : m.getMap().entrySet()) {
				if (map.containsKey(entry.getKey())) {
					int count = map.get(entry.getKey());
					totalWords += entry.getValue();
					count += entry.getValue();
					map.put(entry.getKey(), count);
				} else {
					map.put(entry.getKey(), entry.getValue());
					totalWords += entry.getValue();
				}
			}

			if (maximalLength < m.getMaximalLenghth()) {
				maximalWords.clear();
				maximalLength = m.getMaximalLenghth();
				maximalWords.addAll(m.getMaximalWords());
			} else if (maximalLength == m.getMaximalLenghth()) {
				maximalWords.addAll(m.getMaximalWords());
			}
		}
		maximalCount = maximalWords.size();
		long sum = 0;

		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			sum += getFibo(entry.getKey() + 1) * entry.getValue();
		}
		rank = (double) sum / (double) totalWords;
	}

	public HashMap<Integer, Integer> getMap() {
		return map;
	}

	public void setMap(HashMap<Integer, Integer> map) {
		this.map = map;
	}

	public int getMaximalCount() {
		return maximalCount;
	}

	public int getMaximalLength() {
		return maximalLength;
	}

	public void setMaximalCount(int maximalCount) {
		this.maximalCount = maximalCount;
	}

	public void setMaximalLength(int maximalLength) {
		this.maximalLength = maximalLength;
	}

	public double getRank() {
		return rank;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}
}
