package part2.a;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import part1.Controller;
import part1.View;
import part2.a.model.TrainState;

import java.util.List;

public class Main {
	public static void main(String[] args) {
		/*Future<List<Solution>> fut = TrainAPI.getTrainSolutions("cesena","bologna centrale","18/04/2021", "11");

		fut.onSuccess((List<Solution> res) -> {
			log("reacting to timeout - success: " + res.get(0).getNumTreno());
		}).onFailure((Throwable t) -> {
			log("reacting to timeout - failure: " + t.getMessage());
		}).onComplete((AsyncResult<List<Solution>> res) -> {
			log("reacting to completion - " + res.succeeded());
		});

		log("dopofut");*/

		/*Future<TrainState> fut = TrainAPI.getRealTimeTrainInfo("3988");

		fut.onSuccess((TrainState res) -> {
			log("reacting to timeout - success: " + res.getActualDelay() + res.getLastStop().getArriveScheduled());
		}).onFailure((Throwable t) -> {
			log("reacting to timeout - failure: " + t.getMessage());
		}).onComplete((AsyncResult<TrainState> res) -> {
			log("reacting to completion - " + res.succeeded());
		});

		log("dopofut");*/

		Future<Integer> fut = TrainAPI.getRealTimeStationInfo("S05066");

		fut.onSuccess((Integer res) -> {
			log("reacting to timeout - success: " + res);
		}).onFailure((Throwable t) -> {
			log("reacting to timeout - failure: " + t.getMessage());
		}).onComplete((AsyncResult<Integer> res) -> {
			log("reacting to completion - " + res.succeeded());
		});

		log("dopofut");
	}

	private static void log(String msg) {
		System.out.println("[Main] " + msg);
	}
}
