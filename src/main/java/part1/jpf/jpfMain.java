package part1.jpf;
import java.util.concurrent.ForkJoinPool;

public class jpfMain {
	public static void main(String[] args) {
		try {
			final ForkJoinPool forkJoinPool = new ForkJoinPool();
			int numMostFreqWords = 10;
			File dir = ModelCheckingData.getInstance().genMockDirData();
			System.out.println("ciao1");
			forkJoinPool.invoke(new Master(dir, numMostFreqWords));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
