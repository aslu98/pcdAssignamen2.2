package part1;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

/**
 * part1.Controller part of the application - passive part.
 * 
 * @author aricci
 *
 */
public class Controller implements InputListener {

	private Flag stopFlag;
	private View view;
	private final ForkJoinPool forkJoinPool;

	public Controller(View view){
		this.stopFlag = new Flag();
		this.view = view;
		this.forkJoinPool = new ForkJoinPool();
	}

	public synchronized void started(File dir, File wordsFile, int nMostFreqWords){
		stopFlag.reset();
		forkJoinPool.submit(new MasterTask(wordsFile, dir, nMostFreqWords,stopFlag, view));
	}

	public synchronized void stopped() {
		stopFlag.set();
	}

}
