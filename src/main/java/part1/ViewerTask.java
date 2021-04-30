package part1;

public class ViewerTask extends BasicTask{

	private final WordFreqMap map;
	private final View view;
	private final Flag done;
	
	protected ViewerTask(WordFreqMap map, View view, Flag done) {
		super("viewer");
		this.map = map;
		this.view = view;
		this.done = done;
	}

	public void compute() {
		log("started.");
		while (!done.isSet()) {
			try {
				view.update(map.getCurrentMostFreq());
				Thread.sleep(10);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		view.update(map.getCurrentMostFreq());
	}
}
