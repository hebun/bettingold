package betting;

import java.awt.Container;
import java.awt.EventQueue;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.DualMatch;
import util.BeanTableModel;
import util.XTable;

public class SwingMain extends JFrame {

	JButton but;
	XTable table, tempoTable;

	public SwingMain(final List<Map<String, String>> matches) {
		super("swing test");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = this.getContentPane();
		contentPane.setLayout(null);

		but = new JButton("blblbla");

		but.setLocation(20, 20);
		but.setSize(100, 20);

		// contentPane.add(but);

		setSize(1000, 600);
		contentPane.setSize(800, 500);
		setVisible(true);

		setLocationRelativeTo(null);
		table = new XTable();
		table.setSize(500, 400);
		table.setData(matches);
		tempoTable = new XTable();
		tempoTable.setSize(500, 400);
		tempoTable.setLocation(550, 20);
		contentPane.add(table);
		contentPane.add(tempoTable);
	}

	public SwingMain(BeanTableModel<DualMatch> beanTableModel) {
		super("swing test");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = this.getContentPane();
		contentPane.setLayout(null);

		but = new JButton("blblbla");

		but.setLocation(20, 20);
		but.setSize(100, 20);

		// contentPane.add(but);

		setSize(1000, 600);
		contentPane.setSize(800, 500);
		setVisible(true);

		setLocationRelativeTo(null);
		table = new XTable();
		table.setSize(800, 400);
		table.setModel(beanTableModel);
		tempoTable = new XTable();
		tempoTable.setSize(500, 400);
		tempoTable.setLocation(550, 20);
		contentPane.add(table);
		contentPane.add(tempoTable);
	}

	/**
	 * @param contentPane
	 */
	public void initComps() {

	}

	public static void main(String[] args) {
		/**
		 * CURRENT: getting lay 
		 * ISSUE: getdualmatch performace measure, performance issue in fetchmarketbook
		 */
		setLookAndFell();

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				App app = new App();
				
				//app.updateBetfair();
				 Betting betting = new Betting("");
				//
				 betting.setApp(app);
				 betting.refresh();


			//	SwingMain frame = new SwingMain(beanTableModel);

				// frame.setData(betting.getMatches());
				// frame.initComps();
				// ApiNGDemo.fetchEvents();
				// betting.fetchMarketBook(null);
				// frame.setTempoData(betting.getTempoMatches());
			}
		});

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

	public void setTempoData(final List<Map<String, String>> matches) {

		tempoTable.setData(matches);
	}

	private static final long serialVersionUID = 4741507128571219377L;
}
