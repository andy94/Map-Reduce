/**********************************************************************
 * Tema        : 2 - APD                                              *
 * Autor       : Andrei Ursache                                       *
 * Grupa       : 332 CA                                               *
 * Data        : 21.11.2015                                           *
 **********************************************************************/

package MapReduce;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import Results.ReduceResult;
import Tasks.MapTask;
import Tasks.ReduceTask;
import Tasks.Task;
import Utils.FileInfo;
import Work.WorkPool;
import Work.Worker;

/**
 * Comparator pentru FileInfo
 *
 */
class CustomComparator implements Comparator<FileInfo> {
	@Override
	public int compare(FileInfo o1, FileInfo o2) {
		return (int) (o1.rank * 100) > (int) (o2.rank * 100) ? -1
				: (int) (o1.rank * 100) == (int) (o2.rank * 100) ? 0 : 1;
	}
}

/**
 * Clasa de start pentru aplicatie
 *
 */
public class MapReduce {

	static int D = 0, ND = 0;
	static ArrayList<FileInfo> files = new ArrayList<FileInfo>();
	static String outputFileName;

	/**
	 * Citeste datele din fisierul cu numele dat la input
	 */
	static void readData(String inputFileName) {
		BufferedReader inputFileReader;
		try {
			inputFileReader = new BufferedReader(new FileReader(inputFileName));
			String line = inputFileReader.readLine();

			D = Integer.parseInt(line);
			line = inputFileReader.readLine();
			ND = Integer.parseInt(line);

			for (int i = 0; i < ND; ++i) {
				line = inputFileReader.readLine();
				files.add(new FileInfo(line));
			}
			inputFileReader.close();

		} catch (FileNotFoundException e) {
			System.out.println("Wrong input file name");
			return;
		} catch (IOException e) {
			return;
		}
	}

	/**
	 * Scrie in fisierul de out, informatiile despre fisiere
	 */
	static void writeData() {
		BufferedWriter outputFileWriter = null;
		try {
			File file = new File(outputFileName);
			outputFileWriter = new BufferedWriter(new FileWriter(file));
			for (FileInfo f : files) {
				outputFileWriter.write(f.toString());
				outputFileWriter.newLine();
			}
			outputFileWriter.close();
		} catch (IOException e) {
			System.out.println("IO Exception - write");
		}
	}

	/**
	 * Scrie rezultatele de la Reduce
	 */
	public static <T> void writeResults(LinkedList<T> results) {
		for (T t : results) {
			if (!(t instanceof ReduceTask)) {
				continue;
			}
			ReduceTask rt = (ReduceTask) t;
			ReduceResult rr = rt.getReduceResult();
			String name = rt.getDocumentName();

			FileInfo fi = null;
			for (int i = 0; i < files.size(); ++i) {
				if (files.get(i).name.equals(name)) {
					fi = files.get(i);
					break;
				}
			}
			fi.maximalCount = rr.getMaximalCount();
			fi.maximalLength = rr.getMaximalLength();
			fi.rank = rr.getRank();
		}

		Collections.sort(files, new CustomComparator());

		writeData();
	}

	/**
	 * Main:
	 */
	public static void main(String[] args) {

		/* Verificare parametri */
		if (args.length != 3) {
			System.out.println("Wrong arguments");
			return;
		}

		int numWorkers = Integer.parseInt(args[0]);
		String inputFileName = args[1];
		outputFileName = args[2];

		/* Citeste date */
		readData(inputFileName);

		/* Creeaza workPool */
		WorkPool<Task> workPool = new WorkPool<Task>(numWorkers);

		/* Creeaza workers */
		ArrayList<Worker<Task>> workers = new ArrayList<Worker<Task>>();
		for (int i = 0; i < numWorkers; ++i) {
			workers.add(new Worker<Task>(workPool));
		}

		/* Porneste workers */
		for (Worker<Task> worker : workers) {
			worker.start();
		}

		/* Adauga toate task-urile */
		for (FileInfo f : files) {
			int offset = 0;
			while (offset + D + 1 < f.dimension) {
				workPool.putWork(new MapTask(f.name, offset, D - 1, f.dimension));
				offset += D + 1;

			}
			int o = (int) f.dimension - offset - 1;
			if (o > 0) {
				workPool.putWork(new MapTask(f.name, offset, o, f.dimension));// [offset
																				// ,
			}
		}

		/* Start work */
		workPool.makeReady();
	}
}
