import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class NotesFrame extends JFrame {
	// ||========== GLOBAL VARIABLES ==========||
	private JButton save, cancel;
	private JTextArea textArea;
	private String notes;
	
	// ||========== CONSTRUCTOR ==========||
	public NotesFrame(Point ownerLocation, String notes) {
		this.notes = notes;
		setTitle("Notes");
		setSize(400,200);
		setLocation(getStartLocation(ownerLocation, new Point(getWidth(),getHeight())));
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(textArea(), BorderLayout.CENTER);
		getContentPane().add(buttonPanel(), BorderLayout.SOUTH);
	}
	
	// ||========== METHODS ==========||
	private Point getStartLocation(Point frameLocation, Point frameSize) {
		Point location = new Point(((frameSize.x/2)-(getWidth()/2))+frameLocation.x, 
				((frameSize.y/2)-(getHeight()/2))+frameLocation.y);;
		return location;
	}
	private JScrollPane textArea() {
		textArea = new JTextArea(this.notes);
		JScrollPane scrollPane = new JScrollPane(textArea);
		return scrollPane;
	}
	private JPanel buttonPanel() {
		//TODO Button Panel
		save = new JButton("Save");
		save.setName("Save");
		cancel = new JButton("Cancel");
		cancel.setName("Cancel");
		JPanel panel = new JPanel();
		panel.add(save);
		panel.add(cancel);
		return panel;
	}
	public JButton[] getButtons() {
		return new JButton[] {save, cancel};
	}
	public String getNotes() {
		return textArea.getText();
	}
}
