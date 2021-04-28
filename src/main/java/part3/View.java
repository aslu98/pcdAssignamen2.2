package part3;

import part1.InputListener;

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
	
	public void update(Object[] freqs) {
		frame.update(freqs);
	}
	
	public void done() {
		frame.done();
	}

}
	
