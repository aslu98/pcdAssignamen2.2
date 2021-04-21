package part1;
import java.util.concurrent.RecursiveAction;

public abstract class BasicTask extends RecursiveAction {

	private String name;

	protected BasicTask(String name) {
		this.name = name;
	}

	protected void log(String msg) {
		System.out.println("[ " + this.name +"] " + Thread.currentThread() + msg);
	}
}
