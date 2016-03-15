/**********************************************************************
 * Tema        : 2 - APD                                              *
 * Autor       : Andrei Ursache                                       *
 * Grupa       : 332 CA                                               *
 * Data        : 21.11.2015                                           *
 **********************************************************************/

package Tasks;

/**
 * Un Task Contine numele documentului
 *
 */
public abstract class Task {
	protected String documentName;
	protected boolean isFinished;

	public Task(String documentName) {
		this.documentName = documentName;
		isFinished = false;
	}

	public String getDocumentName() {
		return documentName;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void finishTask() {
		this.isFinished = true;
	}

	/**
	 * Proceseaza Task
	 */
	public abstract void processTask();

}
