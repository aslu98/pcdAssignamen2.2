package part3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class ViewFrame extends JFrame implements ActionListener {

	private Input input;
	private JButton startButton;
	private JButton stopButton;
	private JButton chooseDir;
	private JButton chooseFile;
	private JTextField nMostFreqWords;
	private JTextField state;
	private JLabel selectedDir;
	private JLabel selectedFile;
	private JTextArea wordsFreq;
	private JFileChooser startDirectoryChooser;
	private JFileChooser wordsToDiscardFileChooser;
	
	private File dir;
	private File wordsToDiscardFile;
	
	private ArrayList<InputListener> listeners;

	public ViewFrame(){
		super(".:: Words Freq ::.");
		setSize(1000,400);
		listeners = new ArrayList<InputListener>();
		input = new Input();
		
		startButton = new JButton("start");
		stopButton = new JButton("stop");
		chooseDir = new JButton("select dir");
		chooseFile = new JButton("select file");

		String dirPath = input.getDir().getPath();
		selectedDir = new JLabel(dirPath);
		selectedDir.setSize(400,14);
		String filePath = input.getConfigFile().getPath();
		selectedFile = new JLabel(filePath);
		selectedFile.setSize(400,14);

		nMostFreqWords = new JTextField(input.getNumMostFreqWords() + "");
		
		JPanel controlPanel11 = new JPanel();
		controlPanel11.add(chooseDir);
		controlPanel11.add(selectedDir);

		JPanel controlPanel12 = new JPanel();
		controlPanel12.add(chooseFile);
		controlPanel12.add(selectedFile);
		
		JPanel controlPanel2 = new JPanel();
		controlPanel2.add(new JLabel("Num words"));
		controlPanel2.add(nMostFreqWords);
		controlPanel2.add(Box.createRigidArea(new Dimension(40,0)));
		controlPanel2.add(startButton);
		controlPanel2.add(stopButton);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.add(controlPanel11);
		controlPanel.add(controlPanel12);
		controlPanel.add(controlPanel2);
		
		JPanel wordsPanel = new JPanel();
		wordsFreq = new JTextArea(15,40);
		wordsPanel.add(wordsFreq);
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
		
		startButton.addActionListener(this);
		stopButton.addActionListener(this);
		chooseDir.addActionListener(this);
		chooseFile.addActionListener(this);
		
		this.startButton.setEnabled(true);
		this.stopButton.setEnabled(false);
		chooseDir.setEnabled(true);
		chooseFile.setEnabled(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void addListener(InputListener l){
		listeners.add(l);
	}
	
	public void actionPerformed(ActionEvent ev){
		Object src = ev.getSource();
		if (src == chooseDir) {
			startDirectoryChooser = new JFileChooser();
			startDirectoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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
		} else if (src == startButton) {
			File dir = new File(selectedDir.getText());
			File configFile = new File(selectedFile.getText());
			int numMostFreqWords = Integer.parseInt(nMostFreqWords.getText());			
			this.notifyStarted(new Input(dir, configFile, numMostFreqWords));
			this.state.setText("Processing...");

			chooseDir.setEnabled(false);
			chooseFile.setEnabled(false);
			this.stopButton.setEnabled(true);
			
		} else if (src == stopButton) {
			this.notifyStopped();
			this.state.setText("Stopped.");

			chooseDir.setEnabled(true);
			chooseFile.setEnabled(true);
			this.stopButton.setEnabled(false);
		}

	}

	private void notifyStarted(Input input){
		for (InputListener l: listeners){
			l.started(input);
		}
	}
	
	private void notifyStopped(){
		for (InputListener l: listeners){
			l.stopped();
		}
	}
	
	public void update(Map<String, Integer> freqs) {
		SwingUtilities.invokeLater(() -> {
			wordsFreq.setText("");
			freqs.forEach((k,v) -> wordsFreq.append(k + " -> " + v + " times\n"));
		});
	}

}
	
