/**********************************************************************
 * Tema        : 2 - APD                                              *
 * Autor       : Andrei Ursache                                       *
 * Grupa       : 332 CA                                               *
 * Data        : 21.11.2015                                           *
 **********************************************************************/

package Tasks;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;

import Results.MapResult;
import Utils.Parser;

/**
 * Task de tip Map
 *
 */
public class MapTask extends Task {
	private int offset, fragmentDimension;
	long fileDim;
	private MapResult mapResult;

	public MapTask(String documentName, int offset, int fragmentDimension,
			long fileDim) {
		super(documentName);
		mapResult = new MapResult();
		this.offset = offset;
		this.fragmentDimension = fragmentDimension;
		this.mapResult = new MapResult();
		this.fileDim = fileDim;
	}

	public int getOffset() {
		return this.offset;
	}

	public int getFragmentDimension() {
		return this.fragmentDimension;
	}

	public MapResult getMapResult() {
		return mapResult;
	}

	/**
	 * Proceseaza Task
	 */
	public void processTask() {
		try {
			/* Deschide fisier */
			RandomAccessFile raf = new RandomAccessFile(documentName, "r");
			if (offset >= fileDim) {
				raf.close();
				return;
			}

			boolean full = false;
			int toRead; // cat trebuie sa citesc

			/* Verifica final */
			if ((offset + fragmentDimension) >= (int) fileDim) {
				toRead = (int) fileDim - offset;
				full = true;
			} else {
				toRead = fragmentDimension;
			}

			/* Verifica daca elimn primul cuvant */
			boolean back = false;
			boolean needToRemoveFirstWord = false;
			if (offset - 1 >= 0) {
				back = true;
			}
			if (back == true) {
				raf.seek(offset - 1);
				byte before = raf.readByte();
				byte first = raf.readByte();
				byte[] b = { before };
				byte[] f = { first };
				String bs = new String(b, "UTF-8");
				String fs = new String(f, "UTF-8");

				if (!Parser.delim.contains(bs) && !Parser.delim.contains(fs)) {
					needToRemoveFirstWord = true;
				}
			}

			/* Citeste continut */
			raf.seek(offset);
			byte[] text = new byte[toRead];
			raf.read(text, 0, toRead);

			/* Decodifica */
			String decoded = new String(text, "UTF-8");

			/* Verifica daca citesc ultimul cuvant complet */
			boolean needToCheckLastWord = false;
			String last = "";
			last += decoded.charAt(decoded.length() - 1);
			if (!Parser.delim.contains(last)) {// daca e litera ultimul
				needToCheckLastWord = true;
			}

			StringTokenizer tokenizer = new StringTokenizer(decoded,
					Parser.delim);

			String word = "";
			int count = tokenizer.countTokens();
			if (needToRemoveFirstWord && tokenizer.hasMoreTokens()) {
				tokenizer.nextToken();
				count--;
			}

			while (count > 1) {
				/* Updateaza rezultat */
				mapResult.processWord(tokenizer.nextToken());
				count--;
			}
			if (tokenizer.hasMoreTokens()) {
				word = tokenizer.nextToken();
			}
			/* Citeste si continuarea ultimului cuvant */
			if (!full) {
				int pos = offset + fragmentDimension;
				int dim = Parser.maxWordLength;
				if (pos + dim > fileDim) {
					dim = (int) fileDim - pos;
				}

				text = new byte[dim];
				raf.read(text, 0, dim);

				decoded = new String(text, "UTF-8");
				last = "";
				last += decoded.charAt(0);
				tokenizer = new StringTokenizer(decoded, Parser.delim);
				if (!Parser.delim.contains(last) && needToCheckLastWord) {
					word += tokenizer.nextToken();

				}
			}

			if (word != "") {
				mapResult.processWord(word);
			}
			raf.close();

		} catch (FileNotFoundException e) {
			System.out.println("No such file: " + documentName);
		} catch (IOException e) {
			System.out.println("IO Error: " + documentName);
		}
	}
}