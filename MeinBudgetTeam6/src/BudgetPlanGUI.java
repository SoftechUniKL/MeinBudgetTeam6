
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static java.lang.Math.abs;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 * 
 * Graphische Benutzeroberflaeche des BudgetPlaners
 * 
 * 
 * 
 */

public class BudgetPlanGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * Tabelle mit Uebersicht der Ausgaben
	 * 
	 */

	private DefaultTableModel dtmIn, dtmOut; // Table
	private DefaultPieDataset pd, pd2; // Piechart
	private DefaultCategoryDataset dataset, dataset2; // Balken
	private Object[] line;
	private JTable tableIn, tableOut;
	private JFreeChart balken, balken2, pie, pie2;
	/**
	 * 
	 * Scrollelemente, das die Tabelle umfasst
	 * 
	 */

	private JScrollPane scrollpaneIn, scrollpaneOut;
	ChartPanel panel, panel2, panel3, panel4;

	/**
	 * 
	 * Schaltflaeche, die beim Klicken einen Dialog anzeigt
	 * 
	 */

	private JButton buttonAll, buttonMonth, buttonYear, buttonBalken, buttonClose;

	/**
	 * 
	 * Modell der Daten
	 * 
	 */

	private BudgetPlanModel budget;
	private List<Posten> sortedListIn, sortedListOut;

	/**
	 * 
	 * Konstruktor fuer die GUI.
	 * 
	 * 
	 * 
	 * Hier wird das Hauptfenster initialisiert und aktiviert.
	 * 
	 * 
	 * 
	 * @param budget
	 * 
	 *            Modell der Daten
	 * 
	 */

	public BudgetPlanGUI(BudgetPlanModel budget) {
		super("BudgetPlan");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(0, 1));
		this.budget = budget;
		initWindow(); // Initialisierung des Frameinhalts
		addBehavior(); // Verhalten der GUI Elemente dieses Frames
		setBounds(400, 400, 800, 800); // Groesse des Frames
		setVisible(true); // Frame wird sichtbar

	}

	// Initialisieren des Fensters

	protected void initWindow() {

		sortList();
		// Tabelle mit Uebersicht der Ausgaben
		line = new Object[] { "Datum", "Bezeichnung", "Betrag", "Kategorie" };
		dtmIn = new DefaultTableModel(setupTable(true), line);
		dtmOut = new DefaultTableModel(setupTable(false), line);
		tableIn = new JTable(dtmIn);
		tableOut = new JTable(dtmOut);

		// inits sortedList with copy of gesamt

		scrollpaneIn = new JScrollPane(tableIn);
		scrollpaneOut = new JScrollPane(tableOut);
		JPanel tablePanel = new JPanel(new GridLayout(1, 2));
		tablePanel.add(scrollpaneIn);
		tablePanel.add(scrollpaneOut);
		// Button

		buttonAll = new JButton("Gesamt");
		buttonMonth = new JButton("Monat");
		buttonYear = new JButton("Jahr");
		buttonBalken = new JButton("Als Balkendiagramm");
		buttonClose = new JButton("Zum Hauptmenu");

		JPanel controlPanel = new JPanel();
		controlPanel.add(new JLabel("Zeitraum"));
		controlPanel.add(buttonAll);
		controlPanel.add(buttonYear);
		controlPanel.add(buttonMonth);
		controlPanel.add(new JLabel("Weitere Darstellungsmöglichkeiten"));
		controlPanel.add(buttonBalken);
		controlPanel.add(buttonClose);
		buttonClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();
				Hauptmenu m1 = new Hauptmenu();
				}
			});
		

		// Elemente dem Fenster hinzufuegen:

		getContentPane().add(controlPanel);
		getContentPane().add(tablePanel);

		// Kreisdiagramm

		setupDatasets();
		pie = ChartFactory.createPieChart("Ausgaben", pd);
		panel = new ChartPanel(pie);
		getContentPane().add(panel);
		pie2 = ChartFactory.createPieChart("Einnahmen", pd2);
		panel2 = new ChartPanel(pie2);
		getContentPane().add(panel2);
		
		balken = ChartFactory.createBarChart("Einnahmen", "Kategorie",
		"Betrag", dataset, PlotOrientation.VERTICAL, true, true, false);
		panel3 = new ChartPanel(balken);
		getContentPane().add(panel3);
		panel3.setVisible(false);
		balken2 = ChartFactory
		.createBarChart("Ausgaben", "Kategorie", "Betrag", dataset2,
		PlotOrientation.VERTICAL, true, true, false);
		panel4 = new ChartPanel(balken2);
		getContentPane().add(panel4);
		panel4.setVisible(false);
	}

	public void setupDatasets() {
		pd = new DefaultPieDataset();
		pd2 = new DefaultPieDataset();
		dataset = new DefaultCategoryDataset();
		dataset2 = new DefaultCategoryDataset();
		for (Posten p : sortedListOut) {
			pd.setValue(p.getBezeichnung(), Math.abs(p.getBetrag()));
			dataset2.addValue(Math.abs(p.getBetrag()), p.getBezeichnung(), "");
		}
		for (Posten p : sortedListIn) {
			pd2.setValue(p.getBezeichnung(), Math.abs(p.getBetrag()));
			dataset.addValue(Math.abs(p.getBetrag()), p.getBezeichnung(), "");
		}
	}

	public void refreshTables() {
		dtmIn = new DefaultTableModel(setupTable(true), line);
		dtmOut = new DefaultTableModel(setupTable(false), line);
		tableIn.setModel(dtmIn);
		tableOut.setModel(dtmOut);
		dtmIn.fireTableDataChanged();
		dtmOut.fireTableDataChanged();
	}

	public void refreshDiagrams() {
		setupDatasets();
		pie = ChartFactory.createPieChart("Ausgaben", pd);
		panel.setChart(pie);
		pie2 = ChartFactory.createPieChart("Einnahmen", pd2);
		panel2.setChart(pie2);
		balken = ChartFactory.createBarChart("Einnahmen", "Kategorie",
		"Betrag", dataset, PlotOrientation.VERTICAL, true, true, false);
		panel3.setChart(balken);
		balken2 = ChartFactory
		.createBarChart("Ausgaben", "Kategorie", "Betrag", dataset2,
		PlotOrientation.VERTICAL, true, true, false);
		panel4.setChart(balken2);
	}


	public Object[][] setupTable(boolean einkommen) {
		List<Posten> listToUse;
		if (einkommen)
			listToUse = sortedListIn;
		else
			listToUse = sortedListOut;
		int i = 0;
		Object[][] data = new Object[listToUse.size()][4];
		for (Posten p : listToUse) {
			data[i][0] = new SimpleDateFormat("dd/MM/yyyy")
			.format(p.getDatum());
			data[i][1] = p.getBezeichnung();
			data[i][2] = String.format("%.2f", p.getBetrag());
			data[i][3] = p.getKategorie();
			i++;
		}
		return data;
	}


	public void sortList() {
		sortedListIn = new ArrayList<Posten>();
		sortedListOut = new ArrayList<Posten>();
		for (Posten p : budget.gesamt) {
			if (p.getBetrag() >= 0)
				sortedListIn.add(p);
			else
				sortedListOut.add(p);
			}
	}


	public void sortList(Date von) {
		sortedListIn = new ArrayList<Posten>();
		sortedListOut = new ArrayList<Posten>();
		for (Posten p : budget.gesamt) {
			if (p.getDatum().compareTo(von) >= 0) {
				if (p.getBetrag() >= 0)
					sortedListIn.add(p);
				else
					sortedListOut.add(p);
			}
			}
	}

	// Verhalten hinzufuegen

	public void addBehavior() {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		// button.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// JOptionPane.showMessageDialog(BudgetPlanGUI.this,
		// "Sie sollten Ihre Finanzplanung ueberdenken!",
		// "Hinweis", JOptionPane.PLAIN_MESSAGE);
		// }
		//
		// });

		ActionListener aListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource() == buttonAll) {
					sortList();
					refreshTables();
					refreshDiagrams();
					// erst sortList(para), dann aufrufen mit setupTable()
					// analog für Diagramme
				} else if (e.getSource() == buttonYear) {
					sortList(getYear());
					refreshTables();
					refreshDiagrams();
				} else if (e.getSource() == buttonMonth) {
					sortList(getMonth());
					refreshTables();
					refreshDiagrams();
				} else if (e.getSource() == buttonBalken) {
					if (panel.isVisible()) {
						buttonBalken.setText("Als Kreisdiagramm");
						panel.setVisible(false);
						panel2.setVisible(false);
						panel3.setVisible(true);
						panel4.setVisible(true);
					} else {
						buttonBalken.setText("Als Balkendiagramm");
						panel.setVisible(true);
						panel2.setVisible(true);
						panel3.setVisible(false);
						panel4.setVisible(false);
					}
				}
			}
		};

		buttonAll.addActionListener(aListener);
		buttonYear.addActionListener(aListener);
		buttonMonth.addActionListener(aListener);
		buttonBalken.addActionListener(aListener);

	}

	Date getMonth() {
		// TODO
		return new Date(115, 4, 1);
	}
	Date getYear() {
		return new Date(115, 0, 1);
	}
}
