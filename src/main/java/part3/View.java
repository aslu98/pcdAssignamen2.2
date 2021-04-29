package part3;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.util.Map;

public class View {

	private ViewFrame frame;

	public View(PublishSubject<Input> inputStream){
		frame = new ViewFrame(inputStream);
	}

	public void display() {
        javax.swing.SwingUtilities.invokeLater(() -> {
        	frame.setVisible(true);
        });
    }
	
	public void update(Map<String, Integer> freqs) {
		frame.update(freqs);
	}

	public void done() {
		frame.done();
	}
}
	
