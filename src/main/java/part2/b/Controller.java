package part2.b;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import part2.a.TrainAPI;
import part2.a.agency.RealTimeTrainAPIAgent;
import part2.a.model.solution.SolutionsWrapper;
import part2.a.model.train.TrainState;
import part2.b.view.View;

/**
 * Controller part of the application - passive part.
 * 
 * @author aricci
 *
 */
public class Controller implements InputListener {

	private Flag stopFlag;
	private View view;
	
	public Controller(View view){
		this.stopFlag = new Flag();
		this.view = view;
	}

	@Override
	public void findSolutions(String from, String to, String date, String time) {
		Future<SolutionsWrapper> fut = new TrainAPI().getTrainSolutions(from, to, date, time);
		fut.onSuccess((SolutionsWrapper res) -> {
			view.updateSolutions(res.toString());
		}).onFailure((Throwable t) -> {
			log("failure: " + t.getMessage());
			view.blockingErrorOccurred(t.getMessage());
		}).onComplete((AsyncResult<SolutionsWrapper> res) -> {
			log("findSolutions completed");
		});
	}

	@Override
	public void startMonitoring(String code, RealTimeSubject subject) {
		stopFlag.reset();
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Monitorer(view, stopFlag, subject, code));
	}

	@Override
	public void stopMonitoring() {
		stopFlag.set();
	}

	protected void log(String msg) {
		System.out.println("[Controller] " + msg);
	}
}
