package part2.b.view;

import part2.b.InputListener;
import part2.b.RealTimeSubject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewFrame extends JFrame implements ActionListener {

	private final JButton findButton;
	private final JButton trainMonitorButton;
	private final JButton stationMonitorButton;
	private final JButton stopMonitoringButton;
	private final JTextField fromField;
	private final JTextField toField;
	private final JTextField dateField;
	private final JTextField timeField;
	private final JTextField codeField;
	private final JTextField state;
	private final JTextArea textArea;
	
	private final ArrayList<InputListener> listeners;

	public ViewFrame(){
		super(".:: Trains ::.");
		setSize(800,800);
		listeners = new ArrayList<>();
		
		findButton = new JButton("find");
		trainMonitorButton = new JButton("monitor train");
		stationMonitorButton = new JButton("monitor station");
		stopMonitoringButton = new JButton("stop monitoring");
		fromField = new JTextField("  Ancona  ");
		toField = new JTextField("  Bologna Centrale  ");
		dateField = new JTextField("  25/04/2021  ");
		timeField = new JTextField("  11.30  ");
		codeField = new JTextField("  S07810  ");

		JPanel controlSolutionsPanel = new JPanel();
		controlSolutionsPanel.add(Box.createRigidArea(new Dimension(20,0)));
		controlSolutionsPanel.add(new JLabel("From: "));
		controlSolutionsPanel.add(fromField);
		controlSolutionsPanel.add(Box.createRigidArea(new Dimension(20,0)));
		controlSolutionsPanel.add(new JLabel("To: "));
		controlSolutionsPanel.add(toField);
		controlSolutionsPanel.add(Box.createRigidArea(new Dimension(20,0)));
		controlSolutionsPanel.add(new JLabel("Date: "));
		controlSolutionsPanel.add(dateField);
		controlSolutionsPanel.add(Box.createRigidArea(new Dimension(20,0)));
		controlSolutionsPanel.add(new JLabel("Time: "));
		controlSolutionsPanel.add(timeField);
		controlSolutionsPanel.add(Box.createRigidArea(new Dimension(40,0)));
		controlSolutionsPanel.add(findButton);
		controlSolutionsPanel.add(Box.createRigidArea(new Dimension(20,0)));
		controlSolutionsPanel.setLayout(new BoxLayout(controlSolutionsPanel, BoxLayout.X_AXIS));

		JPanel controlMonitoringPanel = new JPanel();
		controlSolutionsPanel.add(Box.createRigidArea(new Dimension(20,0)));
		controlMonitoringPanel.add(new JLabel("     CODE of train/station to monitor: "));
		controlMonitoringPanel.add(codeField);
		controlMonitoringPanel.add(Box.createRigidArea(new Dimension(20,0)));
		controlMonitoringPanel.add(trainMonitorButton);
		controlMonitoringPanel.add(Box.createRigidArea(new Dimension(20,0)));
		controlMonitoringPanel.add(stationMonitorButton);
		controlMonitoringPanel.add(Box.createRigidArea(new Dimension(20,0)));
		controlMonitoringPanel.add(stopMonitoringButton);
		controlSolutionsPanel.add(Box.createRigidArea(new Dimension(20,0)));
		controlMonitoringPanel.setLayout(new BoxLayout(controlMonitoringPanel, BoxLayout.X_AXIS));

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.add(controlSolutionsPanel);
		controlPanel.add(controlMonitoringPanel);

		JPanel textAreaPanel = new JPanel();
		textArea = new JTextArea(35,40);
		textAreaPanel.add(textArea);
		JScrollPane scrollTextAreaPane=new JScrollPane(textAreaPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JPanel infoPanel = new JPanel();
		state = new JTextField("Ready.",40);
		state.setSize(700, 14);
		infoPanel.add(state);
		
		JPanel cp = new JPanel();
		LayoutManager layoutCP = new BorderLayout();
		cp.setLayout(layoutCP);
		cp.add(BorderLayout.NORTH,controlPanel);
		cp.add(BorderLayout.CENTER,scrollTextAreaPane);
		cp.add(BorderLayout.SOUTH, infoPanel);
		setContentPane(cp);		
		
		findButton.addActionListener(this);
		trainMonitorButton.addActionListener(this);
		stationMonitorButton.addActionListener(this);
		stopMonitoringButton.addActionListener(this);

		initializeButtons();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	

	public void addListener(InputListener l){
		listeners.add(l);
	}
	
	public void actionPerformed(ActionEvent ev){
		Object src = ev.getSource();
		if (src == findButton) {
			String from = fromField.getText().trim();
			String to = toField.getText().trim();
			String date = dateField.getText().trim();
			String time = timeField.getText().trim();
			this.state.setText("Finding solutions...");
			this.notifyFindSolutions(from, to, date, time);
			this.findButton.setEnabled(false);
		} else if (src == trainMonitorButton){
			String code = codeField.getText().trim();
			this.state.setText("Monitoring train " + code);
			this.notifyStartMonitoringTrain(code);
			monitoringButtons();
		} else if (src == stationMonitorButton){
			String code = codeField.getText().trim();
			this.state.setText("Monitoring station " + code);
			this.notifyStartMonitoringStation(code);
			monitoringButtons();
		} else if (src == stopMonitoringButton){
			this.state.setText("Ready.");
			this.notifyStopMonitoring();
			this.stationMonitorButton.setEnabled(true);
			this.trainMonitorButton.setEnabled(true);
			this.stopMonitoringButton.setEnabled(false);
			this.findButton.setEnabled(true);
		}

	}

	private void monitoringButtons(){
		this.stationMonitorButton.setEnabled(false);
		this.trainMonitorButton.setEnabled(false);
		this.stopMonitoringButton.setEnabled(true);
		this.findButton.setEnabled(false);
	}

	private void initializeButtons(){
		this.findButton.setEnabled(true);
		this.trainMonitorButton.setEnabled(true);
		this.stationMonitorButton.setEnabled(true);
		this.stopMonitoringButton.setEnabled(false);
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

	public void blockingErrorOccurred(String err){
		this.textArea.setText(err);
		this.state.setText("Ready.");
		initializeButtons();
	}
	
	public void updateSolutions(String state) {
		SwingUtilities.invokeLater(() -> {
			this.textArea.setText(state);
			this.state.setText("Ready.");
			this.findButton.setEnabled(true);
		});
	}

	public void updateMonitoring(String state) {
		SwingUtilities.invokeLater(() -> textArea.setText(state.trim()));
	}

}
	
