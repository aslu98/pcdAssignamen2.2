package part2.b.view;

import part2.b.InputListener;

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

    public void blockingErrorOccurred(String err){
		frame.blockingErrorOccurred(err);
	}

    public void updateSolutions(String state){
		frame.updateSolutions(state);
	}
	
	public void updateMonitoring(String state) {
		frame.updateMonitoring(state);
	}

	public void stopMonitoring() {
		frame.stopMonitoring();
	}

}
	
