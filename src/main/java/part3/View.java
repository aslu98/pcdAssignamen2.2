package part3;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

public class View {

	private final ViewFrame frame;

	public View(){
		frame = new ViewFrame();
	}
	
	public void addListener(InputListener l){
		frame.addListener(l);
	}

	public void display() {
        javax.swing.SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
	
	public void update(Pair<Map<String, Integer>, Pair<Integer, Integer>> info) {
		frame.update(info);
	}
}
	
