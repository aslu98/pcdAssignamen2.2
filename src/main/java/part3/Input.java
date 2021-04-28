package part3;

import java.io.File;

public class Input {
    private final File dir;
    private final File configFile;
    private final int numMostFreqWords;

    public Input(){
        String absolutePath = new File("").getAbsolutePath() + "/src/main/resources/";
        String directoryPath = "PDF";
        String ignoreFilePath = "ignored/empty.txt";
        this.dir = new File(absolutePath + directoryPath);
        this.configFile = new File(absolutePath + ignoreFilePath);
        this.numMostFreqWords = 10;
    }

    public Input(final File dir, final File configFile, final int numMostFreqWords){
        this.dir = dir;
        this.configFile = configFile;
        this.numMostFreqWords = numMostFreqWords;
    }

    public File getDir() {
        return dir;
    }

    public File getConfigFile() {
        return configFile;
    }

    public int getNumMostFreqWords() {
        return numMostFreqWords;
    }

}
