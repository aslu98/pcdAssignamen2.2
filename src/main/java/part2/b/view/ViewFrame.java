package part2.b.view;

import part2.a.model.train.TrainState;
import part2.b.InputListener;
import part2.b.RealTimeSubject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * 
 * GUI component of the view.
 * 
 * @author aricci
 *
 */
public class ViewFrame extends JFrame implements ActionListener {

	private JButton findButton;
	private JButton stopButton;
	private JButton chooseDir;
	private JButton chooseFile;
	private JTextField fromField;
	private JTextField toField;
	private JTextField dateField;
	private JTextField timeField;
	private JTextField state;
	private JLabel selectedDir;
	private JLabel selectedFile;
	private JTextArea solutions;
	private JFileChooser startDirectoryChooser;
	private JFileChooser wordsToDiscardFileChooser;
	
	private File dir;
	private File wordsToDiscardFile;
	
	private ArrayList<InputListener> listeners;

	public ViewFrame(){
		super(".:: Trains ::.");
		setSize(800,600);
		listeners = new ArrayList<InputListener>();
		
		findButton = new JButton("find");
		stopButton = new JButton("stop");
		chooseDir = new JButton("select dir");
		chooseFile = new JButton("select file");
		
		selectedDir = new JLabel("test/ass01/data-ita");
		selectedDir.setSize(200,14);
		selectedFile = new JLabel("test/ass01/data-ita/config.txt");
		selectedFile.setSize(200,14);

		fromField = new JTextField("  Ancona  ");
		toField = new JTextField("  Bologna Centrale  ");
		dateField = new JTextField("  25/04/2021  ");
		timeField = new JTextField("  11.30  ");

		JPanel controlPanel1 = new JPanel();
		controlPanel1.add(new JLabel("From: "));
		controlPanel1.add(fromField);
		controlPanel1.add(Box.createRigidArea(new Dimension(20,0)));
		controlPanel1.add(new JLabel("To: "));
		controlPanel1.add(toField);
		controlPanel1.add(Box.createRigidArea(new Dimension(20,0)));
		controlPanel1.add(new JLabel("Date: "));
		controlPanel1.add(dateField);
		controlPanel1.add(Box.createRigidArea(new Dimension(20,0)));
		controlPanel1.add(new JLabel("Time: "));
		controlPanel1.add(timeField);
		controlPanel1.add(Box.createRigidArea(new Dimension(40,0)));
		controlPanel1.add(findButton);
		
		JPanel controlPanel2 = new JPanel();

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.add(controlPanel1);
		controlPanel.add(controlPanel2);
		
		JPanel wordsPanel = new JPanel();
		solutions = new JTextArea(15,40);
		wordsPanel.add(solutions);
		JScrollPane scrollPane=new JScrollPane(wordsPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JPanel infoPanel = new JPanel();
		state = new JTextField("ready.",40);
		state.setSize(700, 14);
		infoPanel.add(state);
		
		JPanel cp = new JPanel();
		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);
		cp.add(BorderLayout.NORTH,controlPanel);
		cp.add(BorderLayout.CENTER,scrollPane);
		cp.add(BorderLayout.SOUTH, infoPanel);
		setContentPane(cp);		
		
		findButton.addActionListener(this);
		stopButton.addActionListener(this);
		chooseDir.addActionListener(this);
		chooseFile.addActionListener(this);
		
		this.findButton.setEnabled(true);
		this.stopButton.setEnabled(false);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	

	public void addListener(InputListener l){
		listeners.add(l);
	}
	
	public void actionPerformed(ActionEvent ev){
		Object src = ev.getSource();
		if (src == chooseDir) {
			startDirectoryChooser = new JFileChooser();
		    int returnVal = startDirectoryChooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		        dir = startDirectoryChooser.getSelectedFile();
		        selectedDir.setText(dir.getAbsolutePath());
		     }
		} else if (src == chooseFile) {
			wordsToDiscardFileChooser = new JFileChooser();
		    int returnVal = wordsToDiscardFileChooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	wordsToDiscardFile = wordsToDiscardFileChooser.getSelectedFile();
		        selectedFile.setText(wordsToDiscardFile.getAbsolutePath());
		     }		    
		} else if (src == findButton) {
			String from = fromField.getText().trim();
			String to = toField.getText().trim();
			String date = dateField.getText().trim();
			String time = timeField.getText().trim();
			this.state.setText("Finding...");
			this.notifyFindSolutions(from, to, date, time);
			this.findButton.setEnabled(false);
			
		} else if (src == stopButton) {
			//this.notifyStopped();
			this.state.setText("Stopped.");

			this.findButton.setEnabled(true);
			this.stopButton.setEnabled(false);
			chooseDir.setEnabled(true);
			chooseFile.setEnabled(true);
		}

	}

	private void notifyFindSolutions(String from, String to, String date, String time){
		for (InputListener l: listeners){
			l.findSolutions(from, to, date, time);
		}
	}
	
	private void notifyStartMonitoringTrain(String code){
		for (InputListener l: listeners){
			l.startMonitoring(code, RealTimeSubject.TRAIN);
		}
	}

	private void notifyStartMonitoringStation(String code){
		for (InputListener l: listeners){
			l.startMonitoring(code, RealTimeSubject.STATION);
		}
	}

	private void notifyStopMonitoring(){
		for (InputListener l: listeners){
			l.stopMonitoring();
		}
	}

	public void errorOccurred(String err){
		this.state.setText(err);
		this.findButton.setEnabled(true);
	}
	
	public void updateSolutions(String state) {
		SwingUtilities.invokeLater(() -> {
			this.solutions.setText(state);
			this.state.setText("Ready.");
			this.findButton.setEnabled(true);
		});
	}

	public void updateMonitoring(String state) {
		SwingUtilities.invokeLater(() -> {
			//update mon
		});
	}
	
	public void done() {
		SwingUtilities.invokeLater(() -> {
			this.findButton.setEnabled(true);
			this.stopButton.setEnabled(false);
			chooseDir.setEnabled(true);
			chooseFile.setEnabled(true);
			this.state.setText("Done.");
		});

	}

}
	
