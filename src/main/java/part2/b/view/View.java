package part2.b.view;

import part2.a.model.train.TrainState;

/**
 * Class representing the view part of the application.
 * 
 * @author aricci
 *
 */
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
	
	public void update(TrainState state) {
		frame.update(state);
	}
	
	public void done() {
		frame.done();
	}

}
	
