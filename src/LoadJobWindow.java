import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class LoadJobWindow {
	// ||========== GLOBAL VARIABLES ==========||
	private String[] fileNames;
	private JButton[] buttons;
	private JLabel[] labels;
	private JWindow window;
	
	// ||========== CONSTRUCTOR ==========||
	public LoadJobWindow(String[] fileNames, JFrame owner) {
		// Initialize global variables.
		this.fileNames = fileNames;
		buttons = new JButton[fileNames.length+1];
		labels = new JLabel[fileNames.length];
		window = new JWindow(owner);
		window.add(initComponents());
		window.pack();
		//window.setLocation(getStartLocation(window.getWidth(), window.getHeight()));
		window.setLocation(getStartLocation(owner.getLocation(), new Point(owner.getSize().width, owner.getSize().height)));
	}
	
	// ||========== GETTERS/SETTERS ==========||
	private Point getStartLocation(Point frameLocation, Point frameSize) {
		Point location = new Point(((frameSize.x/2)-(window.getWidth()/2))+frameLocation.x, 
				((frameSize.y/2)-(window.getHeight()/2))+frameLocation.y);;
		return location;
	}
	public JButton[] getButtons() {
		return buttons;
	}
	public int getFileCount() {
		return fileNames.length;
	}
	public String getFileName(int pos) {
		return fileNames[pos];
	}
	
	// ||========== METHODS ==========||
	private JPanel initComponents() {
		// JPanel array
		JPanel[] panelArray = new JPanel[fileNames.length+1];
		
		// Initialize JButton and JLabel array.
		for (int x=0; x<fileNames.length; x++) {
			buttons[x] = new JButton("Select File");
			buttons[x].setName(""+x);
			labels[x] = new JLabel(":: "+fileNames[x]);
			panelArray[x] = new JPanel(new GridLayout(1,2));
			panelArray[x].add(buttons[x]);
			panelArray[x].add(labels[x]);
		}
		// Cancel button.
		buttons[panelArray.length-1] = new JButton("Cancel");
		buttons[panelArray.length-1].setName("Cancel");
		panelArray[panelArray.length-1] = new JPanel();
		panelArray[panelArray.length-1].add(buttons[panelArray.length-1]);
		
		// Wrapper panel
		JPanel wrapper = new JPanel(new GridLayout(panelArray.length, 1));
		for (int x=0; x<panelArray.length; x++) {  wrapper.add(panelArray[x]);  }
		return wrapper;
	}
	public void setVisible() {
		window.setVisible(true);
	}
	public void close() {
		window.dispose();
	}
}
