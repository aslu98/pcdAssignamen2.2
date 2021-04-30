package part2.b;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import part2.a.TrainAPI;
import part2.a.model.solution.SolutionsWrapper;
import part2.b.view.View;

public class Controller implements InputListener {

	private static final int MAX_CONSECUTIVE_ERRORS = 20;
	private final Flag stopFlag;
	private final View view;
	private long startTime;
	private int errors;
	private boolean lastWasError;
	private final TrainAPI api;
	
	public Controller(View view){
		this.stopFlag = new Flag();
		this.view = view;
		this.api = new TrainAPI();
		errors = 0;
		lastWasError = false;
	}

	@Override
	public void findSolutions(String from, String to, String date, String time) {
		startTime = System.currentTimeMillis();
		Future<SolutionsWrapper> fut = api.getTrainSolutions(from, to, date, time);
		fut.onSuccess((SolutionsWrapper res) -> view.updateSolutions(res.toString())).onFailure((Throwable t) -> {
			log("failure: " + t.getMessage());
			view.blockingErrorOccurred(t.getMessage());
		}).onComplete((AsyncResult<SolutionsWrapper> res) -> log("findsolutions completed in " + (System.currentTimeMillis() - startTime) + " millis."));
	}

	@Override
	public void startMonitoring(String code, RealTimeSubject subject) {
		stopFlag.reset();
		this.getNewInfo(code, subject);
	}

	@Override
	public void stopMonitoring() {
		stopFlag.set();
	}

	private void getNewInfo(String code, RealTimeSubject subject){
		startTime = System.currentTimeMillis();
		if (!stopFlag.isSet()) {
			Future<?> fut;
			if (subject == RealTimeSubject.STATION){
				fut = api.getRealTimeStationInfo(code);
			} else {
				fut = api.getRealTimeTrainInfo(code);
			}

			fut.onSuccess(res -> {
				errors = 0;
				lastWasError = false;
				view.updateMonitoring(res.toString());
				this.getNewInfo(code, subject);
			}).onFailure((Throwable t) -> {
				log("failure: " + t.getMessage());
				if (lastWasError && errors > MAX_CONSECUTIVE_ERRORS){
					view.blockingErrorOccurred(t.getMessage());
				} else {
					errors += 1;
					lastWasError = true;
					this.getNewInfo(code, subject);
				}
			}).onComplete(res -> {
				log("Monitoring query completed in " + (System.currentTimeMillis() - startTime) + " millis.");
				//this.getNewInfo();
			});
		}
	}

	protected void log(String msg) {
		System.out.println("[Controller] " + msg);
	}
}
