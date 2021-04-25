package part2.b.view;

import io.vertx.core.AbstractVerticle;

public abstract class BasicAgent extends AbstractVerticle {

	private final String name;

	protected BasicAgent(String name) {
		this.name = name;
	}

	protected void log(String msg) {
		System.out.println("[" + name +"] " + msg);
	}
}
