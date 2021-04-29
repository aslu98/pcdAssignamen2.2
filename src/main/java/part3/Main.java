package part3;

import io.reactivex.rxjava3.subjects.PublishSubject;

import java.io.File;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		PublishSubject<Input> inputStream = PublishSubject.create();
		View view = new View(inputStream);
		new Master(view, inputStream);

		view.display();
	}
}
