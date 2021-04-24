package part2.b.view;

import java.io.File;

public interface InputListener {

	void findSolutions(File dir, File wordsFile, int nMostFreqWords);
	
	void stopped();
}
