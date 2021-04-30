package part3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class Input {
    private final File dir;
    private final File configFile;
    private final int numMostFreqWords;
    private List<String> wordsToDiscard;

    public Input(){
        String absolutePath = new File("").getAbsolutePath() + "/src/main/resources/";
        String directoryPath = "PDF";
        String ignoreFilePath = "ignored/empty.txt";
        this.dir = new File(absolutePath + directoryPath);
        this.configFile = new File(absolutePath + ignoreFilePath);
        this.numMostFreqWords = 10;
        this.loadWordsToDiscard();
    }

    public Input(final File dir, final File configFile, final int numMostFreqWords){
        this.dir = dir;
        this.configFile = configFile;
        this.numMostFreqWords = numMostFreqWords;
        this.loadWordsToDiscard();
    }

    public File getDir() {
        return dir;
    }

    public List<String> getWordsToDiscard(){
        return this.wordsToDiscard;
    }

    public int getNumMostFreqWords() {
        return numMostFreqWords;
    }

    public File getConfigFile(){return configFile;}

    private void loadWordsToDiscard() {
        try {
            this.wordsToDiscard = new LinkedList<>();
            FileReader fr = new FileReader(configFile);
            BufferedReader br = new BufferedReader(fr);
            br.lines().forEach(w -> wordsToDiscard.add(w));
            fr.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
