/**********************************************************************
 * Tema        : 2 - APD                                              *
 * Autor       : Andrei Ursache                                       *
 * Grupa       : 332 CA                                               *
 * Data        : 21.11.2015                                           *
 **********************************************************************/

package Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.StringTokenizer;

/**
 * Informatii despre un fisier
 *
 */
public class FileInfo {

	public String name;
	public long dimension;
	public double rank;
	public int maximalLength;
	public int maximalCount;

	public FileInfo(String name) {
		this.name = name;
		dimension = getSize();
		maximalLength = maximalCount = 0;
		rank = 0;
	}

	long getSize() {
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(name, "r");
			long l = raf.length();
			raf.close();
			return l;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;

	}

	public String getTwoDecimals(double val) {
		String sval = String.valueOf(val);
		StringTokenizer t = new StringTokenizer(sval, ".");
		String result = "0.";
		if (t.hasMoreTokens()) {
			result = t.nextToken() + ".";
		}
		String dec = "00";
		if (t.hasMoreTokens()) {
			dec = "";
			String aux = t.nextToken();
			dec += aux.charAt(0);
			dec += aux.charAt(1);
		}
		result += dec;
		return result;

	}

	public String toString() {
		return name + ";" + getTwoDecimals(rank) + ";[" + maximalLength + ","
				+ maximalCount + "]";
	}

	public String toStringComplete() {
		return name + ";"
				+ (new BigDecimal(rank).setScale(2, RoundingMode.HALF_EVEN))
				+ ";[" + maximalLength + "," + maximalCount + "]  " + dimension;
	}
}
