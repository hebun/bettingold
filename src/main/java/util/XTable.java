package util;

import java.awt.Dimension;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class XTable extends JPanel {

	private static final long serialVersionUID = 6908667529782919971L;
	private JTable table;
	private TableModel model;
	int height = 400;
	int width = 400;
	JScrollPane pane;

	public XTable() {

		super.setSize(width, height);
		this.setLocation(20, 20);
		pane = new JScrollPane();
		setLookAndFell();
	}

	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		width = w;
		height = h;

	}

	public void setData(final List<Map<String, String>> data) {

		model = new AbstractTableModel() {

			private static final long serialVersionUID = 1705882815380673349L;

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {

				Object object = data.get(rowIndex).values().toArray()[columnIndex];
				// System.out.println("valu" + object);
				return object;
			}

			@Override
			public int getRowCount() {

				return data.size();
			}

			@Override
			public int getColumnCount() {

				try {
					return data.get(0).size();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return 0;
				}
			}

			@Override
			public String getColumnName(int col) {
				String object = data.get(0).keySet().toArray()[col].toString();
				return object;
			}
		};
		table = new JTable(model);

		pane.setViewportView(table);
		pane.setSize(width, height);
		pane.setLocation(20, 20);
		pane.setPreferredSize(new Dimension(width, height));
		this.add(pane);
		table.setFillsViewportHeight(true);
		// table.repaint();

	}

	private static void setLookAndFell() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {

			e.printStackTrace();
		}
	}

	public void setModel(TableModel beanTableModel) {

		model = beanTableModel;
		table = new JTable(model);

		pane.setViewportView(table);
		pane.setSize(width, height);
		pane.setLocation(20, 20);
		pane.setPreferredSize(new Dimension(width, height));
		this.add(pane);
		table.setFillsViewportHeight(true);
	}
}
