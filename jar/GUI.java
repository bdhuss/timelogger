import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

//TODO Add user name/password support. (Probably a new version).

@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener, WindowListener {
	// ||========== GLOBAL VARIABLES ==========||
	private EntryTableModel model;
	private int seconds, minutes, hours;
	private JLabel entryElapsedTime, entryTimeStarted, jobDateStarted, jobElapsedTime, jobTitle, jobNumEntries;
	private JMenu fileMenu, sortMenu, byDate, byElapsedTime;
	private JMenuItem exit, loadJob, saveJob, newJob;
	private JRadioButtonMenuItem dateNormal, dateReversed, elapsedTimeNormal, elapsedTimeReversed;
	private JMenuBar menuBar;
	private JButton startButton, pauseButton, editNotesButton;
	private JScrollPane scrollPane;
	private JTable table;
	private Timer timer;
	private Job currentJob;
	private Entry currentEntry;
	private final String PATH = System.getProperty("user.dir");
	private final String VERSION = new String("Version 1.09.08");
	private LoadJobWindow window;
	private NotesFrame notesFrame;
	private String[] fileNames;
	private boolean infoChanged = false;
	private int selectedRow;
	private final int DATE = 0;
	private final int DATE_REVERSED = 1;
	private final int ELAPSED_TIME = 2;
	private final int ELAPSED_TIME_REVERSED = 3;
	private int currentSortMethod = 0;

	// ||========== CONSTRUCTOR ==========||
	public GUI() {
		initComponents();
		setTitle("Time Logger");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setJMenuBar(getJMB());
		setResizable(false);
		setSize(460, 445);
		setLocation(getStartLocation(getWidth(), getHeight()));
		addWindowListener(this);
		setVisible(true);
	}

	// ||========== METHODS ==========||
	// Initializes and forms all the GUI information needed.
	private void initComponents() {
		// ***** Global Variable Initialization *****
		jobTitle = new JLabel();
		jobDateStarted = new JLabel();
		jobElapsedTime = new JLabel();
		jobNumEntries = new JLabel();
		entryTimeStarted = new JLabel();
		entryElapsedTime = new JLabel("0:00:00");
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		startButton.setEnabled(false);
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(this);
		pauseButton.setEnabled(false);
		editNotesButton = new JButton("Edit Notes");
		editNotesButton.addActionListener(this);
		editNotesButton.setEnabled(false);
		scrollPane = new JScrollPane();
		model = new EntryTableModel(new String[] {"#", "Date", "Time", "Notes" });
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setWidth(20);
		table.sizeColumnsToFit(0);
		table.getColumnModel().getColumn(1).setWidth(208);
		table.sizeColumnsToFit(1);
		table.getColumnModel().getColumn(2).setWidth(65);
		table.sizeColumnsToFit(2);
		table.getColumnModel().getColumn(3).setWidth(40);
		table.sizeColumnsToFit(3);
		table.setCellSelectionEnabled(false);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		seconds = minutes = hours = 0;
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				seconds++;
				setTime();
			}
		});

		// ***** Local Variables Initialization *****
		JPanel jobPanel = new JPanel();
		JPanel entryPanel = new JPanel();
		JLabel JOB_TITLE = new JLabel();
		JLabel DATE_STARTED = new JLabel();
		JLabel TOTAL_TIME_ELAPSED = new JLabel();
		JLabel ENTRY_TIME_STARTED = new JLabel();
		JLabel ENTRY_ELAPSED_TIME = new JLabel();
		JLabel VERSION_LABEL = new JLabel(VERSION);
		JLabel NUM_ENTRIES = new JLabel("# of Entries: ");


		// From here till end of method is NetBeans GUI layout nonsense. I admit I was to lazy to do the
		//GUI coding personally. Saves more time for making the program work, and a lot less frustration.
		jobPanel.setBorder(BorderFactory.createTitledBorder(null, "Job Details", 
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Lucida Sans", 1, 14)));

		JOB_TITLE.setFont(new Font("Lucida Sans", 1, 12));
		JOB_TITLE.setText("Title: ");

		jobTitle.setFont(new Font("DejaVu Sans", 0, 12));

		DATE_STARTED.setFont(new Font("Lucida Sans", 1, 12));
		DATE_STARTED.setText("Date Started: ");

		jobDateStarted.setFont(new Font("DejaVu Sans", 0, 12));

		TOTAL_TIME_ELAPSED.setFont(new Font("Lucida Sans", 1, 12)); 
		TOTAL_TIME_ELAPSED.setText("Total Elapsed Time: ");

		jobElapsedTime.setFont(new Font("DejaVu Sans", 0, 12));

		NUM_ENTRIES.setFont(new java.awt.Font("Lucida Sans", 1, 12));

		jobNumEntries.setFont(new java.awt.Font("DejaVu Sans", 0, 12));

		javax.swing.GroupLayout jobPanelLayout = new javax.swing.GroupLayout(jobPanel);
		jobPanel.setLayout(jobPanelLayout);
		jobPanelLayout.setHorizontalGroup(
				jobPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jobPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jobPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jobPanelLayout.createSequentialGroup()
										.addComponent(JOB_TITLE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jobTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
										.addGroup(jobPanelLayout.createSequentialGroup()
												.addComponent(DATE_STARTED)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jobDateStarted, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
												.addGroup(jobPanelLayout.createSequentialGroup()
														.addComponent(TOTAL_TIME_ELAPSED)
														.addGap(10, 10, 10)
														.addComponent(jobElapsedTime, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE))
														.addGroup(jobPanelLayout.createSequentialGroup()
																.addComponent(NUM_ENTRIES)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(jobNumEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
																.addContainerGap())
		);
		jobPanelLayout.setVerticalGroup(
				jobPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jobPanelLayout.createSequentialGroup()
						.addGroup(jobPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(JOB_TITLE)
								.addComponent(jobTitle))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jobPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(DATE_STARTED)
										.addComponent(jobDateStarted))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(jobPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
												.addComponent(NUM_ENTRIES)
												.addComponent(jobNumEntries))
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(jobPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jobElapsedTime)
														.addComponent(TOTAL_TIME_ELAPSED))
														.addContainerGap())
		);

		entryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Current Entry Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Sans", 1, 13))); // NOI18N

		ENTRY_TIME_STARTED.setFont(new java.awt.Font("Lucida Sans", 1, 12));
		ENTRY_TIME_STARTED.setText("Time Started: ");

		ENTRY_ELAPSED_TIME.setFont(new java.awt.Font("Lucida Sans", 1, 12));
		ENTRY_ELAPSED_TIME.setText("Elapsed Time: ");

		javax.swing.GroupLayout entryPanelLayout = new javax.swing.GroupLayout(entryPanel);
		entryPanel.setLayout(entryPanelLayout);
		entryPanelLayout.setHorizontalGroup(
				entryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(entryPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(entryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(entryPanelLayout.createSequentialGroup()
										.addComponent(ENTRY_TIME_STARTED)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(entryTimeStarted, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
										.addGroup(entryPanelLayout.createSequentialGroup()
												.addComponent(ENTRY_ELAPSED_TIME)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(entryElapsedTime)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(pauseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(editNotesButton)))
												.addContainerGap())
		);
		entryPanelLayout.setVerticalGroup(
				entryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(entryPanelLayout.createSequentialGroup()
						.addGroup(entryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(ENTRY_TIME_STARTED)
								.addComponent(entryTimeStarted))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(entryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(ENTRY_ELAPSED_TIME)
										.addComponent(entryElapsedTime)
										.addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(pauseButton)
										.addComponent(editNotesButton)))
		);

		scrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Time Entries", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Sans", 1, 13)));
		scrollPane.setViewportView(table);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(entryPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jobPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
								.addComponent(VERSION_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(jobPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(entryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(VERSION_LABEL)
						.addContainerGap(14, Short.MAX_VALUE))
		);

		pack();
		// End NetBeans nonsense.
	}
	// Initializes the menu bar
	private JMenuBar getJMB() {
		MenuListener menuListener = new MenuListener();

		// ***** JMenuItems *****
		newJob = new JMenuItem("New Job");
		newJob.setMnemonic(KeyEvent.VK_N);
		newJob.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
		newJob.addActionListener(menuListener);

		loadJob = new JMenuItem("Load Job");
		loadJob.setMnemonic(KeyEvent.VK_L);
		loadJob.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_MASK));
		loadJob.addActionListener(menuListener);

		saveJob = new JMenuItem("Save Job");
		saveJob.setMnemonic(KeyEvent.VK_S);
		saveJob.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		saveJob.addActionListener(menuListener);
		saveJob.setEnabled(false);

		exit = new JMenuItem("Exit");
		exit.setMnemonic(KeyEvent.VK_X);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK));
		exit.addActionListener(menuListener);

		// ***** JRadioButtonMenuItem *****
		dateNormal = new JRadioButtonMenuItem("Earliest -> Latest");
		dateNormal.setMnemonic(KeyEvent.VK_E);
		dateNormal.addActionListener(menuListener);
		dateNormal.setEnabled(false);

		dateReversed = new JRadioButtonMenuItem("Latest -> Earliest");
		dateReversed.setMnemonic(KeyEvent.VK_L);
		dateReversed.addActionListener(menuListener);
		dateReversed.setEnabled(false);

		elapsedTimeNormal = new JRadioButtonMenuItem("Longest -> Shortest");
		elapsedTimeNormal.setMnemonic(KeyEvent.VK_L);
		elapsedTimeNormal.addActionListener(menuListener);
		elapsedTimeNormal.setEnabled(false);

		elapsedTimeReversed = new JRadioButtonMenuItem("Shortest -> Longest");
		elapsedTimeReversed.setMnemonic(KeyEvent.VK_S);
		elapsedTimeReversed.addActionListener(menuListener);
		elapsedTimeReversed.setEnabled(false);

		// Button Group for JRadioButtonMenuItems
		ButtonGroup group = new ButtonGroup();
		group.add(dateNormal);
		group.add(dateReversed);
		group.add(elapsedTimeNormal);
		group.add(elapsedTimeReversed);

		// ***** JMenus *****
		byDate = new JMenu("By date..");
		byDate.setMnemonic(KeyEvent.VK_D);
		byDate.add(dateNormal);
		byDate.add(dateReversed);

		byElapsedTime = new JMenu("By elapsed time..");
		byElapsedTime.setMnemonic(KeyEvent.VK_T);
		byElapsedTime.add(elapsedTimeNormal);
		byElapsedTime.add(elapsedTimeReversed);

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(newJob);
		fileMenu.addSeparator();
		fileMenu.add(loadJob);
		fileMenu.add(saveJob);
		fileMenu.addSeparator();
		fileMenu.add(exit);

		sortMenu = new JMenu("Sort");
		sortMenu.setMnemonic(KeyEvent.VK_S);
		sortMenu.add(byDate);
		sortMenu.add(byElapsedTime);

		// ***** JMenu *****
		menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(sortMenu);

		return menuBar;
	}
	// Receives the screen dimensions and centers the application. NOTE: Will display application
	//in center of multi-screen displays with disregard to screen positions.
	private java.awt.Point getStartLocation(int frameWidth, int frameHeight) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		java.awt.Dimension screen = tk.getScreenSize();
		java.awt.Point location = new java.awt.Point((screen.width/2)-(frameWidth/2), 
				(screen.height/2)-(frameWidth/2));
		return location;
	}
	// Displays time for currentEntry and total job time;
	private void setTime() {
		entryElapsedTime.setText(timeToString(formTime()));

		int[] temp = currentJob.getElapsedTime();
		temp[2]++;
		if (temp[2] == 60) {  temp[1]++; temp[2] = 0;  }
		if (temp[1] == 60) {  temp[0]++; temp[1] = 0;  }
		jobElapsedTime.setText(timeToString(temp));
	}
	// Makes sure the integers stay within a time range (IE: seconds & minutes are > 60).
	private int[] formTime() {
		if (seconds == 60) {  minutes++; seconds = 0;  }
		if (minutes == 60) {  hours++; minutes = 0;  }
		return new int[] {hours, minutes, seconds};
	}
	// Turns the integer array to a string with proper format.
	private String timeToString(int[] time) {
		String s = new String(time[0]+":");
		if (time[1] < 10) {  s+=("0"+time[1]+":");  }
		else {  s+=time[1]+":";  }
		if (time[2] < 10) {  s+=("0"+time[2]);  }
		else {  s+=time[2];  }
		return s;
	}
	// Returns parent frame.
	private JFrame getFrame() {
		return this;
	}
	// Creates a 2-Dimensional Object array that holds the current job entry info.
	//Then passes the Object[][] to the table model. The table model then fires a 
	//"all rows & columns changed" event to update the entire table. Also notes that
	//the information for the current job has been changed for saving purposes.
	private void updateTable(ArrayList<Entry> entries) {
		Object[][] data = new Object[currentJob.getEntries().size()][4];
		for (int x=0; x<currentJob.getEntries().size(); x++) {
			data[x][0] = new String("#"+(x+1));
			data[x][1] = currentJob.getEntries().get(x).getDate().toString();
			data[x][2] = currentJob.getEntries().get(x).getTime();
			data[x][3] = new Boolean(currentJob.getEntries().get(x).areNotes());
		}
		model.setData(data);
		infoChanged = true;
	}
	// Updates the main GUI information after a new job is created or loaded.
	private void updateUI() {
		jobTitle.setText(currentJob.getTitle());
		jobDateStarted.setText(currentJob.getDate().toString());
		jobElapsedTime.setText(currentJob.getTime());
		jobNumEntries.setText(""+currentJob.getEntries().size());
		entryTimeStarted.setText("");
		entryElapsedTime.setText("0:00:00");
		saveJob.setEnabled(true);
		startButton.setEnabled(true);
		if (currentJob.getEntries().size() > 0) {  editNotesButton.setEnabled(true);  }
		else {  editNotesButton.setEnabled(false);  }
		dateNormal.setEnabled(true);
		dateReversed.setEnabled(true);
		elapsedTimeNormal.setEnabled(true);
		elapsedTimeReversed.setEnabled(true);
		hours = minutes = seconds = 0;
		sort(currentSortMethod);
		updateTable(currentJob.getEntries());
	}
	// Saves job data to file in working directory that the jar was executed in.
	private void save() {
		// Check to see if file exists
		if (currentJob != null && infoChanged) {
			File file = new File(PATH+currentJob.getFileName());
			boolean save = true;
			if (file.exists()) {
				int choice = JOptionPane.showConfirmDialog(getFrame(), "The Job "+currentJob.getTitle()+" already exists."+
						" Do you want to overwrite it?", "File exists.", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {  save = true;  }
				else {  save = false;  }
			}

			// Save file
			if (save) {
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(file));
					writer.write(currentJob.getTitle()); writer.newLine();
					writer.write(""+currentJob.getDate().getTime()); writer.newLine();
					int[] time = currentJob.getElapsedTime();
					writer.write(""+time[0]); writer.newLine();
					writer.write(""+time[1]); writer.newLine();
					writer.write(""+time[2]); writer.newLine();
					writer.write(""+currentSortMethod); writer.newLine();
					ArrayList<Entry> entries = currentJob.getEntries();
					writer.write(""+entries.size()); writer.newLine();
					for (int x=0; x<entries.size(); x++) {
						writer.write(""+entries.get(x).getDate().getTime()); writer.newLine();
						time = entries.get(x).getElapsedTime();
						writer.write(""+time[0]); writer.newLine();
						writer.write(""+time[1]); writer.newLine();
						writer.write(""+time[2]); writer.newLine();
						writer.write(entries.get(x).getNotes()); writer.newLine();
					}
					writer.close();
				}catch (Exception e) {  e.printStackTrace();  }
			}
			infoChanged = false;
		}
	}
	// Gathers job files from working directory then passes them to load job screen.
	private void load() {
		confirmSave(false);
		File dir = new File(PATH);
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".job");
			}
		};
		fileNames = dir.list(filter);
		if (fileNames.length == 0) {
			JOptionPane.showMessageDialog(getFrame(), "There are no locally saved jobs.", "ERROR", JOptionPane.WARNING_MESSAGE);
		}
		else {
			window = new LoadJobWindow(fileNames, getFrame());
			JButton[] buttons = window.getButtons();
			LoadWindowListener lwl = new LoadWindowListener();
			for (int x=0; x<buttons.length; x++) {  buttons[x].addActionListener(lwl);  }
			window.setVisible();
		}
	}
	// Checks to see if file needs to be saved.
	private void confirmSave(boolean exit) {
		if (currentJob != null && infoChanged) {
			// 1 = NO_OPTION
			int choice = 1;
			String message = new String("Would you like to save");
			if (exit) {  message+=" before you exit?";  }
			else {  message+="?";  }
			choice = JOptionPane.showConfirmDialog(getFrame(), message, "Save?", JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			if (choice == JOptionPane.YES_OPTION) {  save();  }
		}
	}
	// Selection sort used to sort Array List of entries.
	private void sort(int method) {
		// If active job holds less than 2 entries, don't sort.
		currentSortMethod = method;
		if (currentJob.getEntries().size() < 2) {/* do nothing */}
		// Else sort.
		else {
			Entry[] array = new Entry[currentJob.getEntries().size()];
			for (int x=0; x<array.length; x++) {  
				array[x] = currentJob.getEntries().get(x);
			}

			for (int i=0; i<array.length-1; i++) {
				int min = i;
				for (int j=i+1; j<array.length; j++) {
					if (method == DATE) {  if (array[j].getDate().before(array[min].getDate())) {  min = j;  }  }
					else if (method == DATE_REVERSED) {  if (array[j].getDate().after(array[min].getDate())) {  min = j;  }  }
					else if (method == ELAPSED_TIME) {
						if (isLarger(array[j].getElapsedTime(), array[min].getElapsedTime())) {  min = j;  } 
					}
					else if (method == ELAPSED_TIME_REVERSED) {
						if (!isLarger(array[j].getElapsedTime(), array[min].getElapsedTime())) {  min = j;  }
					}
				}
				if (i != min) {
					Entry swap = array[i];
					array[i] = array[min];
					array[min] = swap;
				}
			}

			ArrayList<Entry> temp = new ArrayList<Entry>();
			for (int x=0; x<array.length; x++) {  temp.add(array[x]);  }
			currentJob.setEntries(temp);
		}
	}
	// Compares int[] for which is a larger time. Return true if j > min.
	private boolean isLarger(int[] j, int[] min) {
		boolean test = false;
		if (j[0] > min[0]) {  test = true;  }
		else if (j[0] == min[0] && j[1] > min[1]) {  test = true;  }
		else if (j[0] == min[0] && j[1] == min[1] && j[2] > min[2]) {  test = true;  }
		return test;
	}
	private void stopTimer() {
		timer.stop();
		if (hours == 0 && minutes == 0 && seconds ==0) {  /* do nothing */  }
		else {
			currentEntry.setElapsedTime(hours, minutes, seconds);
			currentJob.addEntry(currentEntry);
			updateUI();
		}
		startButton.setText("Start");
		pauseButton.setEnabled(false);
	}
	private void enterNotes(String notes) {
		currentJob.getEntries().get(selectedRow).setNotes(notes);
		selectedRow = -1;
		updateUI();
	}

	// ||========== ACTION LISTENERS ==========||
	@Override
	public void actionPerformed(ActionEvent ae) {
		// ***** START BUTTON *****
		if (ae.getSource().equals(startButton)) {
			infoChanged = true;
			JButton temp = (JButton) ae.getSource();
			if (temp.getText().equals("Start")) {
				hours = minutes = seconds = 0;
				entryElapsedTime.setText("0:00:00");
				currentEntry = new Entry();
				entryTimeStarted.setText(currentEntry.getDate().toString());
				timer.start();
				startButton.setText("Stop");
				pauseButton.setEnabled(true);
			}
			else {  stopTimer();  }
		}
		// ***** PAUSE BUTTON *****
		else if (ae.getSource().equals(pauseButton)) {
			JButton temp = (JButton) ae.getSource();
			if (temp.getText().equals("Pause")) {
				timer.stop();
				pauseButton.setText("Unpause");
			}
			else {
				timer.start();
				pauseButton.setText("Pause");
			}
		}
		// ***** ADD NOTES BUTTON *****
		else if (ae.getSource().equals(editNotesButton)) {
			selectedRow  = table.getSelectedRow();
			if (selectedRow != -1) {
				notesFrame = new NotesFrame(getLocation(), currentJob.getEntries().get(selectedRow).getNotes());
				JButton[] buttons = notesFrame.getButtons();
				NotesFrameListener nfl = new NotesFrameListener();
				for (int x=0; x<buttons.length; x++) {  buttons[x].addActionListener(nfl);  }
				notesFrame.setVisible(true);
			}
			else {
				JOptionPane.showMessageDialog(getFrame(), "No row is selected. Please select a row to edit the notes.",
						"ERROR", JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
	// Menu Listener
	public class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			// ***** NEW JOB *****
			if (ae.getSource().equals(newJob)) {
				if (currentJob != null) {
					if (timer.isRunning()) {  stopTimer();  }
					save();
				}
				String title = new String("");
				title = JOptionPane.showInputDialog("Job Title:");
				if (title == null || title.equals("")) {  
					JOptionPane.showMessageDialog(getFrame(), "Malformed title error.", "Error", JOptionPane.PLAIN_MESSAGE);  
				}
				else {  
					currentJob = new Job(title);
					currentSortMethod = 0;
					infoChanged = true;
					updateUI();  
				}
			}
			// ***** SAVE/LOAD/EXIT *****
			else if (ae.getSource().equals(loadJob)) {
				if (timer.isRunning()) {  stopTimer();  }
				load();
			}
			else if (ae.getSource().equals(saveJob)) {  
				if (timer.isRunning()) {  stopTimer();  }
				save();
			}
			else if (ae.getSource().equals(dateNormal)) {  sort(DATE); updateUI();  }
			else if (ae.getSource().equals(dateReversed)) {  sort(DATE_REVERSED); updateUI();  }
			else if (ae.getSource().equals(elapsedTimeNormal)) {  sort(ELAPSED_TIME); updateUI();  }
			else if (ae.getSource().equals(elapsedTimeReversed)) {  sort(ELAPSED_TIME_REVERSED); updateUI();  }
			// else == Exit
			else {  dispose();  }
		}
	}
	// Load Window Listener. Used to add an ActionListener to the load windows buttons.
	public class LoadWindowListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			JButton tempB = (JButton) ae.getSource();
			int choice = -1;
			// ***** CANCEL *****
			if (tempB.getName().equals("Cancel")) { window.close(); }
			// ***** ANY OTHER BUTTON ON LOAD WINDOW *****
			else {  choice = Integer.parseInt(tempB.getName());  }
			if (choice != -1) {
				window.close();
				File file = new File(PATH+"/"+fileNames[choice]);

				try {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					currentJob = new Job(reader.readLine());
					currentJob.setDate(new Date(Long.parseLong(reader.readLine())));
					currentJob.setElapsedTime(Integer.parseInt(reader.readLine()), Integer.parseInt(reader.readLine()),
							Integer.parseInt(reader.readLine()));
					currentSortMethod = Integer.parseInt(reader.readLine());
					int numEntries = Integer.parseInt(reader.readLine());
					String notes = new String();
					long nextDate = 0;
					for (int x=0; x<numEntries; x++) {
						Entry temp;
						if (x == 0) {
							temp = new Entry(new Date(Long.parseLong(reader.readLine())), Integer.parseInt(reader.readLine()),
									Integer.parseInt(reader.readLine()), Integer.parseInt(reader.readLine()));
						}
						else {
							temp = new Entry(new Date(nextDate), Integer.parseInt(reader.readLine()),
									Integer.parseInt(reader.readLine()), Integer.parseInt(reader.readLine()));
						}
						// If its the last entry...
						if (x == (numEntries-1)) {
							notes = reader.readLine();
							while (reader.ready()) {  notes+="\n"+reader.readLine();  }
							temp.setNotes(notes);
						}
						else {
							notes = reader.readLine();
							nextDate = 0;
							while (nextDate == 0) {
								String nextLine = reader.readLine();
								// Attempt to parse the string to a long.
								try {  nextDate = Long.parseLong(nextLine);  }
								// If fail: notes aren't done.
								catch (Exception e) {  notes+="\n"+nextLine;  }
							}
							temp.setNotes(notes);
						}
						currentJob.addEntry(temp);
					}
					reader.close();
				}catch (Exception e) {  e.printStackTrace();  }
				startButton.setEnabled(true);
				infoChanged = false;
				updateUI();
			}
		}
	}
	public class NotesFrameListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			JButton tempB = (JButton) ae.getSource();
			if (tempB.getName().equals("Cancel")) {  notesFrame.dispose();  }
			else {
				enterNotes(notesFrame.getNotes());
				notesFrame.dispose();
			}
		}
	}

	// ||========== WINDOW LISTENER ==========||
	public void windowActivated(WindowEvent ignored) {}
	public void windowClosed(WindowEvent we) {  stopTimer(); confirmSave(true);  }
	public void windowClosing(WindowEvent we) {  stopTimer(); confirmSave(true);  }
	public void windowDeactivated(WindowEvent ignored) {}
	public void windowDeiconified(WindowEvent ignored) {}
	public void windowIconified(WindowEvent ignored) {}
	public void windowOpened(WindowEvent ignored) {}
}