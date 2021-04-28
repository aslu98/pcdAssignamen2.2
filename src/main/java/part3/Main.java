package part3;
public class Main {
	public static void main(String[] args) throws InterruptedException {

		View view = new View();
		InputListener master = new Master(view);

		view.addListener(master);
		view.display();
	}
}
