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
	private JButton trainMonitorButton;
	private JButton stationMonitorButton;
	private JButton stopMonitoringButton;
	private JTextField fromField;
	private JTextField toField;
	private JTextField dateField;
	private JTextField timeField;
	private JTextField codeField;
	private JTextField state;
	private JTextArea solutions;
	private JTextArea monitoring;
	
	private ArrayList<InputListener> listeners;

	public ViewFrame(){
		super(".:: Trains ::.");
		setSize(800,620);
		listeners = new ArrayList<InputListener>();
		
		findButton = new JButton("find");
		trainMonitorButton = new JButton("monitor train");
		stationMonitorButton = new JButton("monitor station");
		stopMonitoringButton = new JButton("stop monitoring");
		fromField = new JTextField("  Ancona  ");
		toField = new JTextField("  Bologna Centrale  ");
		dateField = new JTextField("  25/04/2021  ");
		timeField = new JTextField("  11.30  ");
		codeField = new JTextField("         ");

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

		JPanel solutionsPanel = new JPanel();
		solutions = new JTextArea(15,40);
		solutionsPanel.add(solutions);
		JScrollPane scrollSolutionsPane=new JScrollPane(solutionsPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

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

		JPanel monitoringPanel = new JPanel();
		monitoring = new JTextArea(15,40);
		monitoringPanel.add(monitoring);
		JScrollPane scrollMonitoringPanel=new JScrollPane(monitoringPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JPanel centralPanel = new JPanel();
		LayoutManager layout = new BorderLayout();
		centralPanel.setLayout(layout);
		centralPanel.add(BorderLayout.NORTH,scrollSolutionsPane);
		centralPanel.add(BorderLayout.CENTER,controlMonitoringPanel);
		centralPanel.add(BorderLayout.SOUTH, scrollMonitoringPanel);

		JPanel infoPanel = new JPanel();
		state = new JTextField("Ready.",40);
		state.setSize(700, 14);
		infoPanel.add(state);
		
		JPanel cp = new JPanel();
		LayoutManager layoutCP = new BorderLayout();
		cp.setLayout(layoutCP);
		cp.add(BorderLayout.NORTH,controlSolutionsPanel);
		cp.add(BorderLayout.CENTER,centralPanel);
		cp.add(BorderLayout.SOUTH, infoPanel);
		setContentPane(cp);		
		
		findButton.addActionListener(this);
		trainMonitorButton.addActionListener(this);
		stationMonitorButton.addActionListener(this);
		stopMonitoringButton.addActionListener(this);

		this.findButton.setEnabled(true);
		this.trainMonitorButton.setEnabled(true);
		this.stationMonitorButton.setEnabled(true);
		this.stopMonitoringButton.setEnabled(false);

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
			this.state.setText("Finding...");
			this.notifyFindSolutions(from, to, date, time);
			this.findButton.setEnabled(false);
		} else if (src == trainMonitorButton){
			String code = codeField.getText().trim();
			this.state.setText("Monitoring...");
			this.notifyStartMonitoringTrain(code);
			this.stationMonitorButton.setEnabled(false);
			this.trainMonitorButton.setEnabled(false);
			this.stopMonitoringButton.setEnabled(true);
		} else if (src == trainMonitorButton){
			String code = codeField.getText().trim();
			this.state.setText("Monitoring...");
			this.notifyStartMonitoringStation(code);
			this.stationMonitorButton.setEnabled(false);
			this.trainMonitorButton.setEnabled(false);
			this.stopMonitoringButton.setEnabled(true);
		} else if (src == trainMonitorButton){
			this.state.setText("Ready.");
			this.notifyStopMonitoring();
			this.stationMonitorButton.setEnabled(true);
			this.trainMonitorButton.setEnabled(true);
			this.stopMonitoringButton.setEnabled(false);
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
			monitoring.setText(state);
		});
	}

}
	
