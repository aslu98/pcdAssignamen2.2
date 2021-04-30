package part1;

import java.util.*;

public class WordFreqMap {

	private final HashMap<String, Integer> freqs;
	private final LinkedList<AbstractMap.SimpleEntry<String, Integer>> mostFreq;
	private int analyzedWords;

	public WordFreqMap(int nMostFreq) {
		freqs = new HashMap<>();
		mostFreq = new LinkedList<>();
		for (int i = 0; i < nMostFreq; i++) {
			mostFreq.add(new AbstractMap.SimpleEntry<>("", 0));
		}
	}

	public synchronized void add(String word) {
		Integer nf = freqs.get(word);
		int freq = 1;
		if (nf == null) {
			freqs.put(word, 1);
		} else {
			freqs.put(word, nf + 1);
			freq = nf + 1;
		}
		analyzedWords++;

		int index = 0;
		boolean alreadyPresent = false;
		Iterator<AbstractMap.SimpleEntry<String, Integer>> it = mostFreq.iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> el = it.next();
			if (el.getKey().equals(word)) {
				it.remove();
				alreadyPresent = true;
			} else if (freq > el.getValue()) {
				index++;
			} else {
				break;
			}
		}

		if (alreadyPresent) {
			AbstractMap.SimpleEntry<String, Integer> el = new AbstractMap.SimpleEntry<>(word, freq);
			mostFreq.add(index, el);
		} else if (index > 0) {
			AbstractMap.SimpleEntry<String, Integer> el = new AbstractMap.SimpleEntry<>(word, freq);
			mostFreq.add(index, el);
			mostFreq.removeFirst();
		}
	}

	public synchronized Object[] getCurrentMostFreq() {
		return mostFreq.toArray();
	}

	public synchronized int getAnalyzedWords() {
		return analyzedWords;
	}
}
