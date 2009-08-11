import java.util.Date;

public class Entry {
	// ||========== GLOBAL VARIABLES ==========||
	private Date date;
	private int[] elapsedTime = new int[3];
	private String notes;
	private boolean areNotes = false;
	
	// ||========== CONSTRUCTOR ==========||
	public Entry() {
		this(new Date());
	}
	public Entry(Date date) {
		this.date = date;
		notes = new String();
	}
	public Entry(Date date, int[] elapsedTime) {
		this.date = date;
		this.elapsedTime = elapsedTime;
		notes = new String();
	}
	public Entry(Date date, int hours, int minutes, int seconds) {
		this.date = date;
		elapsedTime[0] = hours;
		elapsedTime[1] = minutes;
		elapsedTime[2] = seconds;
		notes = new String("");
	}
	
	// ||========== GETTERS/SETTERS ==========||
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int[] getElapsedTime() {
		return elapsedTime;
	}
	public String getTime() {
		String s = new String(elapsedTime[0]+":");
		if (elapsedTime[1] < 10) {  s+=("0"+elapsedTime[1]+":");  }
		else {  s+=elapsedTime[1]+":";  }
		if (elapsedTime[2] < 10) {  s+=("0"+elapsedTime[2]);  }
		else {  s+=elapsedTime[2];  }
		return s;
	}
	public void setElapsedTime(int[] elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public void setElapsedTime(int hours, int minutes, int seconds) {
		this.elapsedTime = new int[] {hours, minutes, seconds};
	}
	public void setNotes(String notes) {
		this.notes = notes;
		if (this.notes.equals("")) {  areNotes = false;  }
		else {  areNotes = true;  }
	}
	public String getNotes() {
		return notes;
	}
	public boolean areNotes() {
		return areNotes;
	}
}