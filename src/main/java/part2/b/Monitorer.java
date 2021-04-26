package part2.b;

import io.vertx.core.Future;
import part2.a.TrainAPI;
import part2.b.view.View;

public class Monitorer extends BasicAgent {

	private final View view;
	private Flag stopped;
	private RealTimeSubject subject;
	private Future<?> fut;
	private String code;
	private TrainAPI api;
	private long startTime;
	
	Monitorer(View view, Flag stopped, RealTimeSubject subject, String code) {
		super("monitorer");
		this.view = view;
		this.stopped = stopped;
		this.subject = subject;
		this.code = code;
		this.api = new TrainAPI();
	}

	public void start() {
		this.getNewInfo();
	}

	private void getNewInfo(){
		log(stopped.isSet().toString());
		if (!stopped.isSet()) {
			if (subject == RealTimeSubject.STATION){
				fut = api.getRealTimeStationInfo(code);
			} else {
				fut = api.getRealTimeTrainInfo(code);
			}

			fut.onSuccess(res -> {
				view.updateMonitoring(res.toString());
				this.getNewInfo();
			}).onFailure((Throwable t) -> {
				log("failure: " + t.getMessage());
				view.errorOccurred(t.getMessage());
			}).onComplete(res -> {
				log("Monitoring query completed.");
			});
		}
	}
}
