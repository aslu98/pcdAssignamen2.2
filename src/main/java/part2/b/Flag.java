package part2.b;

public class Flag {

	private boolean flag;
	
	public Flag() {
		flag = false;
	}
	
	public void reset() {
		flag = false;
	}
	
	public void set() {
		flag = true;
	}
	
	public Boolean isSet() {
		return flag;
	}
}
