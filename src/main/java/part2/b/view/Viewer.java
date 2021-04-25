package part2.b.view;

public class Viewer extends BasicAgent {

	private View view;
	private Flag done;
	
	protected Viewer(View view, Flag done) {
		super("viewer");
		this.view = view;
		this.done = done;
	}

	public void start() {
		while (!done.isSet()) {
			try {
				//view.update(map.getCurrentMostFreq());
				//aggiorna monitoraggio
				Thread.sleep(10);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		//aggiorna monitoraggio
		//view.update(map.getCurrentMostFreq());
	}
}
