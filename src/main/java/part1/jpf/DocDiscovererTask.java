package part1.jpf;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class DocDiscovererTask extends BasicTask {

	private File startDir;
	private List<RecursiveAction> forks;
	private WordFreqMap map;
	private int nDocsFound = 0;
	
	public DocDiscovererTask(File dir, WordFreqMap map) {
		super("doc-discoverer");
		this.startDir = dir;	
		this.forks = new LinkedList<>();
		this.map = map;
	}

	@Override
	public void compute() {
		if (startDir.isDirectory()) {
			explore(startDir);
		}

		for (RecursiveAction task : forks) {
			task.join();
		}
	}
	
	private void explore(File dir) {
			for (File f: dir.listFiles()) {
				if (f.isDirectory()) {
					DocDiscovererTask task = new DocDiscovererTask(f, map);
					forks.add(task);
					task.fork();
				} else if (f.getName().endsWith(".pdf")) {
					try {
						log("found a new doc: " + f.getName());
						DocLoaderTask task = new DocLoaderTask("" + nDocsFound, f, map);
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
