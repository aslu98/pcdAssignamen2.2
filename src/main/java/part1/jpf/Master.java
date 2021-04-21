package part1.jpf;

import gov.nasa.jpf.vm.Verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Master extends BasicTask {

	private final File dir;
	private final int numMostFreqWords;
	private WordFreqMap map;

	public Master(File dir, int numMostFreqWords) {
		super("Master");
		this.dir = dir;
		this.numMostFreqWords = numMostFreqWords;
		System.out.println("ciao2");
	}

	public void compute() {
		try {
			log("started ");
			map = new WordFreqMap();
			log("map created ");

			DocDiscovererTask masterTask = new DocDiscovererTask(dir, map);
			log("task created ");
			masterTask.fork();
			log("task launched");
			masterTask.join();

			log("completed");
			
			elabMostFreqWords();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void elabMostFreqWords() {
		Verify.beginAtomic();
		Set<Map.Entry<String, Integer>> set = map.getWords().entrySet();

		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>();
		list.addAll(set);

		list.sort((Map.Entry<String,Integer> e1, Map.Entry<String,Integer> e2) -> {
			return Integer.compare(e2.getValue().intValue(), e1.getValue().intValue());
		});

		for (int i = 0; i < numMostFreqWords && i < list.size(); i++) {
			String key = list.get(i).getKey();
			System.out.println(" " + (i+1) + " - " +  key + " " + list.get(i).getValue());
		}

		ModelCheckingData.getInstance().checkResults(list);

		Verify.endAtomic();
	}
}
