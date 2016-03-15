/**********************************************************************
 * Tema        : 2 - APD                                              *
 * Autor       : Andrei Ursache                                       *
 * Grupa       : 332 CA                                               *
 * Data        : 21.11.2015                                           *
 **********************************************************************/

package Tasks;

import java.util.ArrayList;

import Results.MapResult;
import Results.ReduceResult;

/**
 * Task de tip Reduce
 *
 */
public class ReduceTask extends Task {
	private ArrayList<MapResult> mapResults;
	private ReduceResult reduceResult;

	public ReduceTask(String documentName, ArrayList<MapResult> mapResult) {
		super(documentName);
		this.mapResults = new ArrayList<MapResult>(mapResult);
		this.reduceResult = new ReduceResult();
	}

	public ArrayList<MapResult> getMapResults() {
		return mapResults;
	}

	public ReduceResult getReduceResult() {
		return reduceResult;
	}

	/**
	 * Proceseaza task
	 */
	public void processTask() {
		/* Merge map results */
		reduceResult.processMap(mapResults);

	}
}
