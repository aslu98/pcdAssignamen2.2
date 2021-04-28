package part1;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

public class Controller implements InputListener {

	private Flag stopFlag;
	private View view;
	private ForkJoinPool forkJoinPool;

	public Controller(View view){
		this.stopFlag = new Flag();
		this.view = view;
		this.forkJoinPool = new ForkJoinPool();
	}

	public synchronized void started(File dir, File wordsFile, int nMostFreqWords){
		stopFlag.reset();
		this.forkJoinPool = new ForkJoinPool();
		forkJoinPool.submit(new MasterTask(wordsFile, dir, nMostFreqWords,stopFlag, view));
	}

	public synchronized void stopped() {
		stopFlag.set();
	}

}
