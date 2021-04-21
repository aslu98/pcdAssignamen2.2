package part1.jpf;

import java.util.HashMap;

public class TextAnalyzerTask extends BasicTask {

	private WordFreqMap map;
	private String chunk;

	public TextAnalyzerTask(String id, String chunk, WordFreqMap map)  {
		super("text-analyzer-" + id);
		this.map = map;
		this.chunk = chunk;
	}
	
	public void compute() {
		try {		    
		    String del = "[\\x{201D}\\x{201C}\\s'\", ?.@;:!-]+";
				String[] words = chunk.split(del);
				for (String w: words) {
					String w1 = w.trim().toLowerCase();
					if (!w1.equals("")) {
						map.add(w1);
					}
				}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
