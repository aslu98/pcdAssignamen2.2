package part2.b;
import part2.b.view.View;

public class Main {
	public static void main(String[] args) {
		View view = new View();

		Controller controller = new Controller(view);
		view.addListener(controller);

		view.display();
	}
}
