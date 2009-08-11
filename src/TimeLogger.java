
public class TimeLogger {
	public static void main(String[] args) {
		// COMMENT ADDED FOR GIT TEST!
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e) {  /* do nothing */  }
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GUI();
			}
		});
	}
}
