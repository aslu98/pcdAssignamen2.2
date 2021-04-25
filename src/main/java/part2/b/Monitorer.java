package part2.b;

import io.vertx.core.Future;
import part2.a.TrainAPI;
import part2.b.view.View;

public class Monitorer extends BasicAgent {

	private View view;
	private Flag stopped;
	private RealTimeSubject subject;
	private Future<?> fut;
	private String code;
	
	protected Monitorer(View view, Flag stopped, RealTimeSubject subject, String code) {
		super("monitorer");
		this.view = view;
		this.stopped = stopped;
		this.subject = subject;
		this.code = code;
	}

	public void start() {
		while (!stopped.isSet()) {
			try {
				this.getNewInfo();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		this.getNewInfo();
	}

	private void getNewInfo(){
		if (subject == RealTimeSubject.STATION){
			fut = TrainAPI.getRealTimeStationInfo(code);
		} else {
			fut = TrainAPI.getRealTimeTrainInfo(code);
		}

		fut.onSuccess(res -> {
			view.updateMonitoring(res.toString());
		}).onFailure((Throwable t) -> {
			log("failure: " + t.getMessage());
			view.errorOccurred(t.getMessage());
		}).onComplete(res -> {
			log("Monitoring query completed.");
		});
	}
}
