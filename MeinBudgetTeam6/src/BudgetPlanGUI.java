import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
 * Graphische Benutzeroberflaeche des BudgetPlaners
 * 
 */
public class BudgetPlanGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * Tabelle mit Uebersicht der Ausgaben 
	 */

	private DefaultTableModel dtmIn, dtmOut;
	private Object[] line;

	private JTable tableIn, tableOut;
	/**
	 * Scrollelemente, das die Tabelle umfasst
	 */
	private JScrollPane scrollpaneIn, scrollpaneOut;
	
	ChartPanel panel, panel2, panel3, panel4;
	/**
	 * Schaltflaeche, die beim Klicken einen Dialog anzeigt
	 */
	private JButton buttonAll, buttonMonth, buttonYear, buttonBalken;

	/**
	 * Modell der Daten
	 */
	private BudgetPlanModel budget;

	/**
	 * Konstruktor fuer die GUI.
	 * 
	 * Hier wird das Hauptfenster initialisiert und aktiviert.
	 * 
	 * @param budget
	 *            Modell der Daten
	 */
	public BudgetPlanGUI(BudgetPlanModel budget) {
		super("BudgetPlan");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(0,1));

		this.budget = budget;
		initWindow(); // Initialisierung des Frameinhalts
		addBehavior(); // Verhalten der GUI Elemente dieses Frames
		setBounds(200, 200, 1000, 1000); // Groesse des Frames
		setVisible(true); // Frame wird sichtbar
	}

	// Initialisieren des Fensters
	protected void initWindow() {

		// Tabelle mit Uebersicht der Ausgaben
		line = new Object[] { "Datum", "Bezeichnung", "Betrag", "Kategorie" };
		dtmIn = new DefaultTableModel(setupTable(true), line);
		dtmOut = new DefaultTableModel(setupTable(false), line);
		tableIn = new JTable(dtmIn);
		tableOut = new JTable(dtmOut);

		// table = new JTable(setupTable(), new Object[] { "Datum",
		// "Bezeichnung",
		// "Betrag", "Kategorie" });
		scrollpaneIn = new JScrollPane(tableIn);
		scrollpaneOut = new JScrollPane(tableOut);
		
		JPanel tablePanel = new JPanel(new GridLayout(1, 2));
		
		tablePanel.add(scrollpaneIn);
		tablePanel.add(scrollpaneOut);
		// Kreisdiagramm
		DefaultPieDataset pd = new DefaultPieDataset();
		for (Posten p : budget.gesamt) {
			if (p.getBetrag() <= 0) {
				pd.setValue(p.getBezeichnung(), Math.abs(p.getBetrag()));
			}

		}
		DefaultPieDataset pd2 = new DefaultPieDataset();
		for (Posten p : budget.gesamt) {
			if(p.getBetrag() >0) {
				pd2.setValue(p.getBezeichnung(), Math.abs(p.getBetrag()));
			}
		}
		
		
		JFreeChart pie = ChartFactory.createPieChart("Ausgaben", pd);
		panel = new ChartPanel(pie);
		JFreeChart pie2 = ChartFactory.createPieChart("Einnahmen", pd2);
		panel2 = new ChartPanel(pie2);

		// Button
		buttonAll = new JButton("Gesamt");
		buttonMonth = new JButton("Monat");
		buttonYear = new JButton("Jahr");
		buttonBalken = new JButton("Als Balkendiagramm");
		
		JPanel controlPanel = new JPanel();
		controlPanel.add(new JLabel("Zeitraum"));
		controlPanel.add(buttonAll);
		controlPanel.add(buttonYear);
		controlPanel.add(buttonMonth);
		controlPanel.add(new JLabel("Weitere Darstellungsmöglichkeiten"));
		controlPanel.add(buttonBalken);
		// Elemente dem Fenster hinzufuegen:
		getContentPane().add(controlPanel);
		getContentPane().add(tablePanel);
		getContentPane().add(panel2);
		getContentPane().add(panel);
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		JFreeChart balken = ChartFactory.createBarChart("Einnahmen","Kategorie","Betrag",dataset,PlotOrientation.VERTICAL,true,true,false);
		panel3 = new ChartPanel(balken);
		getContentPane().add(panel3);
		panel3.setVisible(false);
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		
		JFreeChart balken2 = ChartFactory.createBarChart("Ausgaben","Kategorie","Betrag",dataset2,PlotOrientation.VERTICAL,true,true,false);
		panel4 = new ChartPanel(balken2);
		getContentPane().add(panel4);
		panel4.setVisible(false);
		
	}

	public Object[][] setupTable() {
		Object[][] data = new Object[budget.gesamt.size()][4];
		int i = 0;
		for (Posten p : budget.gesamt) {
			data[i][0] = new SimpleDateFormat("dd/MM/yyyy")
					.format(p.getDatum());
			data[i][1] = p.getBezeichnung();
			data[i][2] = String.format("%.2f", p.getBetrag());
			data[i][3] = p.getKategorie();
			i++;
		}
		return data;
	}

	public Object[][] setupTable(boolean einkommen) {
		
		Object[][] data = new Object[budget.gesamt.size()][4];
		int i = 0;
		for (Posten p : budget.gesamt) {
			if ((einkommen && p.getBetrag() >= 0)
					|| (!einkommen && p.getBetrag() < 0)) {
				data[i][0] = new SimpleDateFormat("dd/MM/yyyy").format(p
						.getDatum());
				data[i][1] = p.getBezeichnung();
				data[i][2] = String.format("%.2f", p.getBetrag());
				data[i][3] = p.getKategorie();
				i++;
			}
		}
		return data;
	}

	public Object[][] setupTable(boolean einkommen, Date von) {
		Object[][] data = new Object[budget.gesamt.size()][4];
		int i = 0;
		for (Posten p : budget.gesamt) {
			if (((einkommen && p.getBetrag() >= 0)
					|| (!einkommen && p.getBetrag() < 0)) && (p.getDatum().after(von) || p.getDatum().equals(von))) {
				data[i][0] = new SimpleDateFormat("dd/MM/yyyy").format(p
						.getDatum());
				data[i][1] = p.getBezeichnung();
				data[i][2] = String.format("%.2f", p.getBetrag());
				data[i][3] = p.getKategorie();
				i++;
			}
		}
		return data;
	} 

	// Verhalten hinzufuegen
	public void addBehavior() {
		// registriere den ActionListener fuer den Button als anonyme Klasse
//		button.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				JOptionPane.showMessageDialog(BudgetPlanGUI.this,
//						"Sie sollten Ihre Finanzplanung ueberdenken!",
//						"Hinweis", JOptionPane.PLAIN_MESSAGE);
//			}
//
//		});
		ActionListener aListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource() == buttonAll){
					dtmIn = new DefaultTableModel(setupTable(true), line);
					dtmOut = new DefaultTableModel(setupTable(false), line);
					tableIn.setModel(dtmIn);
					tableOut.setModel(dtmOut);
					dtmIn.fireTableDataChanged();
					dtmOut.fireTableDataChanged();
					
				} else if (e.getSource() == buttonYear){
					dtmIn = new DefaultTableModel(setupTable(true, getYear()), line);
					dtmOut = new DefaultTableModel(setupTable(false, getYear()), line);
					tableIn.setModel(dtmIn);
					tableOut.setModel(dtmOut);
					dtmIn.fireTableDataChanged();
					dtmOut.fireTableDataChanged();
				} else if (e.getSource() == buttonMonth){
					dtmIn = new DefaultTableModel(setupTable(true, getMonth()), line);
					dtmOut = new DefaultTableModel(setupTable(false, getMonth()), line);
					tableIn.setModel(dtmIn);
					tableOut.setModel(dtmOut);
					dtmIn.fireTableDataChanged();
					dtmOut.fireTableDataChanged();
					
				} else if (e.getSource() == buttonBalken) {
					if (panel.isVisible()){
					buttonBalken.setText("Als Kreisdiagramm");
					panel.setVisible(false);
					panel2.setVisible(false);
					panel3.setVisible(true);
					panel4.setVisible(true);
					}
					else{
						buttonBalken.setText("Als Balkendiagramm");
						panel.setVisible(true);
						panel2.setVisible(true);
						panel3.setVisible(false);
						panel4.setVisible(false);
						}
				}
				
		};
	};
		buttonAll.addActionListener(aListener);
		buttonYear.addActionListener(aListener);
		buttonMonth.addActionListener(aListener);
		buttonBalken.addActionListener(aListener);
		
	}
		
	Date getMonth(){
		// TODO
		return new Date(115, 4, 1);
	}
	Date getYear(){
		return new Date(115, 0, 1);
	}
}
