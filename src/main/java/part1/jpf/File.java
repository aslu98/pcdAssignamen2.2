package part1.jpf;

public interface File {

	boolean isDirectory();
	File[] listFiles();
	String getName();
	
}
