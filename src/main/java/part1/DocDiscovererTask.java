package part1;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class DocDiscovererTask extends BasicTask {

	private File startDir;
	private List<RecursiveAction> forks;
	private int nDocsFound;
	private Flag stopFlag;
	private WordFreqMap map;
	private HashMap<String,String> wordsToDiscard;
	
	public DocDiscovererTask(File dir, Flag stopFlag, HashMap<String,String> wordsToDiscard, WordFreqMap map) {
		super("doc-discoverer");
		this.startDir = dir;	
		this.forks = new LinkedList<>();
		this.stopFlag = stopFlag;
		this.map = map;
		this.wordsToDiscard = wordsToDiscard;
	}

	@Override
	public void compute() {
		log("started.");
		nDocsFound = 0;
		if (startDir.isDirectory()) {
			explore(startDir);
			if (stopFlag.isSet()) {
				log("job done - " + nDocsFound + " docs found.");
			} else {
				log("stopped.");
			}
		}

		for (RecursiveAction task : forks) {
			task.join();
		}
	}
	
	private void explore(File dir) {
		if (!stopFlag.isSet()) {
			for (File f: dir.listFiles()) {
				if (f.isDirectory()) {
					DocDiscovererTask task = new DocDiscovererTask(f, stopFlag, wordsToDiscard, map);
					forks.add(task);
					task.fork();
				} else if (f.getName().endsWith(".pdf")) {
					try {
						log("found a new doc: " + f.getName());
						DocLoaderTask task = new DocLoaderTask("" + nDocsFound, stopFlag, f, wordsToDiscard, map);
						forks.add(task);
						task.fork();
						nDocsFound++;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}

	public int getNDocsFound(){
		return nDocsFound;
	}
}
