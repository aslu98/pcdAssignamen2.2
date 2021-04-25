package part2.a;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import part2.a.model.solution.Solution;
import part2.a.model.solution.SolutionsWrapper;
import part2.a.model.station.StationState;
import part2.a.model.train.TrainState;

import java.util.List;

public class APIMain {
	public static void main(String[] args) {
		/*Future<SolutionsWrapper> fut = TrainAPI.getTrainSolutions("cesena","bologna centrale","18/04/2021", "11");

		fut.onSuccess((SolutionsWrapper res) -> {
			log("success: " + res.toString());
		}).onFailure((Throwable t) -> {
			log("failure: " + t.getMessage());
		}).onComplete((AsyncResult<SolutionsWrapper> res) -> {
			log("reacting to completion - " + res.succeeded());
		});

		log("dopofut");*/

		/*Future<TrainState> fut = TrainAPI.getRealTimeTrainInfo("3988");

		fut.onSuccess((TrainState res) -> {
			log("success: " + res.getActualDelay() + "\n" + res.toString());
		}).onFailure((Throwable t) -> {
			log("failure: " + t.getMessage());
		}).onComplete((AsyncResult<TrainState> res) -> {
			log("reacting to completion - " + res.succeeded());
		});

		log("dopofut");*/

		Future<StationState> fut = TrainAPI.getRealTimeStationInfo("S05043");

		fut.onSuccess((StationState res) -> {
			log("success: " + res.toString());
		}).onFailure((Throwable t) -> {
			log("failure: " + t.getMessage());
		}).onComplete((AsyncResult<StationState> res) -> {
			log("reacting to completion - " + res.succeeded());
		});

		log("dopofut");
	}

	private static void log(String msg) {
		System.out.println("[Main] " + msg);
	}
}
