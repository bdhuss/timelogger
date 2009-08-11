import java.util.ArrayList;
import java.util.Date;


public class Job {
	// ||========== GLOBAL VARIABLES ==========||
	private ArrayList<Entry> entries = new ArrayList<Entry>();
	private Date date;
	private String title;
	private int[] elapsedTime = new int[3];
	
	// ||========== CONSTRUCTOR ==========||
	public Job(String title) {
		this.title = title;
		date = new Date();
		for (int x=0; x<elapsedTime.length; x++) {  elapsedTime[x] = 0;  }
	}
	
	// ||========== GETTERS/SETTERS ==========||
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
	public void setElapsedTime(int hours, int minutes, int seconds) {
		this.elapsedTime[0] = hours;
		this.elapsedTime[1] = minutes;
		this.elapsedTime[2] = seconds;
	}
	public int[] getElapsedTime() {
		return elapsedTime;
	}
	public ArrayList<Entry> getEntries() {
		return entries;
	}
	public void setEntries(ArrayList<Entry> entries) {
		this.entries = entries;
	}
	public String getTime() {
		String s = new String(elapsedTime[0]+":");
		if (elapsedTime[1] < 10) {  s+=("0"+elapsedTime[1]+":");  }
		else {  s+=elapsedTime[1]+":";  }
		if (elapsedTime[2] < 10) {  s+=("0"+elapsedTime[2]);  }
		else {  s+=elapsedTime[2];  }
		return s;
	}
	public String getFileName() {
		return ("/"+this.title+".job");
	}
	
	// ||========== METHODS ==========||
	public void addEntry(Entry entry) {
		entries.add(entry);
		calcElapsedTime();
	}
	public void addEntry(Date date, int[] elapsedTime) {
		entries.add(new Entry(date, elapsedTime));
		calcElapsedTime();
	}
	private void calcElapsedTime() {
		int[] total = new int[3];
		for (int x=0; x<entries.size(); x++) {
			int[] temp = entries.get(x).getElapsedTime();
			total[0] = total[0] + temp[0];
			total[1] = total[1] + temp[1];
			total[2] = total[2] + temp[2];
		}
		while (total[2] >= 60) {  total[1]++; total[2]-=60;  }
		while (total[1] >=60) {  total[0]++; total[1]-=60;  }
		elapsedTime = total;
	}
}