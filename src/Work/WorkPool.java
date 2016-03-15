/**********************************************************************
 * Tema        : 2 - APD                                              *
 * Autor       : Andrei Ursache                                       *
 * Grupa       : 332 CA                                               *
 * Data        : 21.11.2015                                           *
 **********************************************************************/

package Work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import MapReduce.MapReduce;
import Results.MapResult;
import Tasks.MapTask;
import Tasks.ReduceTask;
import Tasks.Task;

/**
 * Clasa ce implementeaza un "work pool" conform modelului "replicated workers".
 * Clasa generica pentru Task
 */
public class WorkPool<T extends Task> {
	int nThreads; // Numar total workeri
	int nWaiting = 0; // Numar workeri in asteptare

	public boolean readyToStart = false; // flag de start

	/**
	 * Flag-uri pentru determinare stare
	 */
	public boolean doneMap = false;
	public boolean doneRed = false;

	int size;

	/**
	 * Rezultate
	 */
	public LinkedList<T> results = new LinkedList<T>();

	/**
	 * Task-uri
	 */
	public LinkedList<T> tasks = new LinkedList<T>();

	public LinkedList<T> getResults() {
		return results;
	}

	public WorkPool(int nThreads) {
		this.nThreads = nThreads;
		this.size = 0;
	}

	/**
	 * Functie care incearca obtinera unui task din workpool. Daca nu sunt
	 * task-uri disponibile, functia se blocheaza pana cand poate fi furnizat un
	 * task sau pana cand rezolvarea problemei este complet terminata
	 * 
	 * @return Un task de rezolvat, sau null daca rezolvarea problemei s-a
	 *         terminat
	 */
	public synchronized T getWork() {

		/**
		 * Inca nu a inceput
		 */
		if (!readyToStart) {
			try {
				this.wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (tasks.size() == 0) { // workpool gol
			nWaiting++;

			if ((nWaiting == nThreads) && readyToStart
					&& size == results.size() && doneMap) {
				doneRed = true;
				/**
				 * Problema s-a terminat, anunt toti ceilalti workeri
				 * */
				notifyAll();
				return null;
			} else {
				while ((!doneRed && tasks.size() == 0)) {
					try {
						/**
						 * Asteapta
						 */
						this.wait();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (doneRed) {
					/* S-a terminat prelucrarea */
					notifyAll();
					return null;
				}
				nWaiting--;
			}
		}

		if (!tasks.isEmpty() && !doneRed) {
			return tasks.remove();
		} else {
			return null;
		}

	}

	/**
	 * Functie care introduce un task in workpool.
	 * 
	 * @param t
	 *            - task-ul care trebuie introdus
	 */
	public synchronized void putWork(T t) {
		tasks.add(t);
		size++;
		this.notify();

	}

	/**
	 * Schimba starea in Reduce Ia rezultatele si "transforma-le" in taskuri
	 * Adauga in lista de taskuri Goleste lista de rezultate
	 */
	@SuppressWarnings("unchecked")
	public synchronized void preceedToReduce() {
		size = 0;
		HashMap<String, ArrayList<MapResult>> mapForFile = new HashMap<String, ArrayList<MapResult>>();
		for (T r : results) {
			if (mapForFile.containsKey(r.getDocumentName())) {
				mapForFile.get(r.getDocumentName()).add(
						((MapTask) r).getMapResult());
			} else {
				ArrayList<MapResult> res = new ArrayList<MapResult>();
				res.add(((MapTask) r).getMapResult());
				mapForFile.put(r.getDocumentName(), res);
			}
		}
		for (Map.Entry<String, ArrayList<MapResult>> entry : mapForFile
				.entrySet()) {
			putWork((T) new ReduceTask(entry.getKey(), entry.getValue()));
		}

		results.clear();

	}

	synchronized void putResult(T t) {
		results.add(t);

		/**
		 * Verifica terminarea celor doua stari
		 */
		if (results.size() == size && !doneMap) {
			doneMap = true;
			preceedToReduce();

			System.out.println("MAP\tfinished");
		} else if (results.size() == size && doneMap && !doneRed) {
			doneRed = true;
			MapReduce.writeResults(results);

			System.out.println("REDUCE\tfinished");
		}

		this.notify();
	}

	/**
	 * Start
	 */
	public synchronized void makeReady() {
		readyToStart = true;
	}

}
