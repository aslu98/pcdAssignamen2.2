package part1;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.RecursiveAction;

public class DocDiscovererTask extends BasicTask {

	private final File startDir;
	private final List<RecursiveAction> forks;
	private int nDocsFound;
	private final Flag stopFlag;
	private final WordFreqMap map;
	private final HashMap<String,String> wordsToDiscard;
	
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
			for (File f: Objects.requireNonNull(dir.listFiles())) {
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
