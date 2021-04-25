package part2.b;

public interface InputListener {

	void findSolutions(String from, String to, String date, String time);

	void startMonitoring(String code, RealTimeSubject subject);

	void stopMonitoring();
}
