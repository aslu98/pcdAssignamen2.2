package part2.b;

import io.vertx.core.Future;
import part2.a.TrainAPI;
import part2.b.view.View;

import java.util.Random;

public class Monitorer extends BasicAgent {

	private static final int MAX_CONSECUTIVE_ERRORS = 20;
	private final View view;
	private Flag stopped;
	private RealTimeSubject subject;
	private Future<?> fut;
	private String code;
	private TrainAPI api;
	private int errors;
	private boolean lastWasError;
	private Random rand;
	
	Monitorer(View view, Flag stopped, RealTimeSubject subject, String code) {
		super("monitorer");
		this.view = view;
		this.stopped = stopped;
		this.subject = subject;
		this.code = code;
		this.api = new TrainAPI();
		errors = 0;
		lastWasError = false;
		rand = new Random();
	}

	public void start() {
		this.getNewInfo();
	}

	private void getNewInfo(){
		if (!stopped.isSet()) {
			if (subject == RealTimeSubject.STATION){
				fut = api.getRealTimeStationInfo(code);
			} else {
				fut = api.getRealTimeTrainInfo(code);
			}

			fut.onSuccess(res -> {
				errors = 0;
				lastWasError = false;
				view.updateMonitoring(res.toString());
				this.getNewInfo();
			}).onFailure((Throwable t) -> {
				log("failure: " + t.getMessage());
				if (lastWasError && errors > MAX_CONSECUTIVE_ERRORS){
					view.blockingErrorOccurred(t.getMessage());
				} else {
					errors += 1;
					lastWasError = true;
					this.getNewInfo();
				}
			}).onComplete(res -> {
				log("Monitoring query completed." + rand.nextInt());
			});
		}
	}
}
