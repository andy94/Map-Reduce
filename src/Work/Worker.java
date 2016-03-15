/**********************************************************************
 * Tema        : 2 - APD                                              *
 * Autor       : Andrei Ursache                                       *
 * Grupa       : 332 CA                                               *
 * Data        : 21.11.2015                                           *
 **********************************************************************/

package Work;

import Tasks.Task;

/**
 * Clasa pentru Worker generic
 *
 * @param <T>
 *            - tipul Task-ului
 */
public class Worker<T extends Task> extends Thread {
	WorkPool<T> workPool;

	public Worker(WorkPool<T> workpool) {
		this.workPool = workpool;
	}

	/**
	 * Proceseaza Task
	 */
	void processTask(T task) {
		if (!task.isFinished()) {
			task.processTask();
		}
		task.finishTask();
		/**
		 * Pune rezultat in workPool
		 */
		workPool.putResult(task);
	}

	/**
	 * Ruleaza
	 */
	public void run() {
		while (true) {
			T task = null;
			task = workPool.getWork();
			if (task == null) {
				break;
			}
			processTask(task);
		}
	}

}
