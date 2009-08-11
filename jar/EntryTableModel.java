import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class EntryTableModel extends AbstractTableModel {
	// ||========== GLOBAL VARIABLES ==========||
	private String[] columnNames;
	private Object[][] data;
	
	// ||========== CONSTRUCTOR ==========||
	public EntryTableModel(String[] columnNames) {
		this(columnNames, new Object[0][0]);
	}
	public EntryTableModel(String[] columnNames, Object[][] data) {
		this.columnNames = columnNames;
		this.data = data;
	}
	
	// ||========== GETTERS/SETTERS ==========||
	public int getColumnCount() {
		return columnNames.length;
	}
	public int getRowCount() {
		return data.length;
	}
	public String getColumnName(int col) {
		return columnNames[col];
	}
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	@SuppressWarnings("unchecked")
	public Class getColumnClass(int col) {
		return getValueAt(0, col).getClass();
	}
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	public void setData(Object[][] data) {
		this.data = data;
		fireTableDataChanged();
	}
}
