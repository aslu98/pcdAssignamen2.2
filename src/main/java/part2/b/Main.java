package part2.b;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import part2.a.TrainAPI;
import part2.a.model.station.StationState;
import part2.b.view.View;

public class Main {
	public static void main(String[] args) {
		View view = new View();

		Controller controller = new Controller(view);
		view.addListener(controller);

		view.display();
	}

	private static void log(String msg) {
		System.out.println("[Main] " + msg);
	}
}
