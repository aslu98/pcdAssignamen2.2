package part2.b.view;

public class Viewer {

	private View view;
	private Flag done;
	
	protected Viewer(View view, Flag done) {
		super("viewer");
		this.view = view;
		this.done = done;
	}

	public void run() {
		while (!done.isSet()) {
			try {
				//view.update(map.getCurrentMostFreq());
				//aggiorna view con nuove info
				Thread.sleep(10);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		//aggiorna view un'ultima volta
		//view.update(map.getCurrentMostFreq());
	}
}
