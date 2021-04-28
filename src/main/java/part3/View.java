package part3;
import java.util.Map;

public class View {

	private ViewFrame frame;

	public View(){
		frame = new ViewFrame();
	}
	
	public void addListener(InputListener l){
		frame.addListener(l);
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
	
