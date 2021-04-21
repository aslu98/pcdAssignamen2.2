package part1.jpf;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class DocLoaderTask extends BasicTask {

	private WordFreqMap map;
	private List<RecursiveAction> forks;
	private File doc;
	
	public DocLoaderTask(String id, File f, WordFreqMap map) throws Exception  {
		super("doc-loader-" + id);
		this.map = map;
		this.doc = f;
		this.forks = new LinkedList<>();
	}

	@Override
	public void compute(){
		try {
			loadDoc(doc);
		} catch (Exception ex) {
		}

		for (RecursiveAction task : forks) {
			task.join();
		}
	}
	
	private void loadDoc(File doc) {
		String[] mockChunks = ModelCheckingData.getInstance().genMockChunks();
		for (String chunk: mockChunks) {
			TextAnalyzerTask task = new TextAnalyzerTask("" + forks.size(), chunk, map);
			forks.add(task);
			task.fork();
		}
  	}
}
