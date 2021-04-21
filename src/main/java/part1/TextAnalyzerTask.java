package part1;

import java.util.HashMap;

public class TextAnalyzerTask extends BasicTask {

	private HashMap<String,String> wordsToDiscard;
	private WordFreqMap map;
	private Flag stopFlag;
	private String chunk;

	public TextAnalyzerTask(String id, Flag stopFlag, String chunk, HashMap<String,String> wordsToDiscard, WordFreqMap map)  {
		super("text-analyzer-" + id);
		this.wordsToDiscard = wordsToDiscard;
		this.map = map;
		this.stopFlag = stopFlag;
		this.chunk = chunk;
	}
	
	public void compute() {
		try {		    
		    String del = "[\\x{201D}\\x{201C}\\s'\", ?.@;:!-]+";
			if (!stopFlag.isSet()) {
				String[] words = chunk.split(del);
				for (String w: words) {
					String w1 = w.trim().toLowerCase();
					if (!w1.equals("") && !wordsToDiscard.containsKey(w1)) {
						map.add(w1);
					}
				}
			} else {
				log("stopped.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		log ("done");
	}
}
