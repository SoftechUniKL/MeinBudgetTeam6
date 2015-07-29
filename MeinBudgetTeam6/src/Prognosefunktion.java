
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

public class Prognosefunktion extends JFrame {

	class Vergleich{
		String kategorie;
		double betrag;
		double dw;
		int anzahl;
		
		Vergleich(String kategorie, double betrag, int anzahl, double dw){
			this.kategorie = kategorie;
			this.betrag = betrag;
			this.anzahl = anzahl;
			this.dw = dw;
		}
		
		private void setBetrag(double add){
			this.betrag+=add;
		}
		
		private void incAnzahl(){
			anzahl++;
		}
		
		private void bildeDw(){
			if(anzahl == 0){dw=0;} else{
			dw = betrag / anzahl;
			}
		}
		
		private double getMw(){
			return dw;
		}
		
		private String getKat(){
			return kategorie;
		}
		
	}
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * Tabelle mit Uebersicht der Ausgaben
	 * 
	 */

	private DefaultTableModel dtmIn, dtmOut; // Table
	private DefaultPieDataset pd, pd2; // Piechart
	private DefaultCategoryDataset dataset, dataset2; // Balken
	private Object[] line,line2;
	private JTable tableIn, tableOut;
	private JFreeChart balken, balken2, pie, pie2;

	private JScrollPane scrollpaneIn, scrollpaneOut;
	ChartPanel panel, panel2, panel3, panel4;



	private JButton buttonAll, buttonMonth, buttonYear, buttonBalken, buttonClose;


	private BudgetPlanModel budget;
	private List<Posten> sortedListIn, sortedListOut,categoryListIn, categoryListOut,categoryListIn2,categoryListOut2;

	
	public Prognosefunktion(BudgetPlanModel budget) {
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
		line2 = new Object[] { "Kategorie", "Betrag"};
		dtmIn = new DefaultTableModel(setupTable1(), line2);
		//dtmOut = new DefaultTableModel(setupTable(false), line);
		tableIn = new JTable(dtmIn);
		//tableOut = new JTable(dtmOut);

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


	public Object[][] setupTable1() {
		List<Vergleich> listToUse=mwBilden();
		
		int i = 0;
		Object[][] data = new Object[listToUse.size()][2];
		for (Vergleich v : listToUse) {
			data[i][0] =v.getKat();
			data[i][1] = v.getMw();
			i++;
		}
		return data;
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

	
	public List<Vergleich> mwBilden(){
	
		categoryListIn = new ArrayList<Posten>();
		categoryListOut = new ArrayList<Posten>();
	
		Vergleich bindestrich = new Vergleich("-",0.0,0,0.0);
		Vergleich lebensmittel = new Vergleich("Lebensmittel",0.0,0,0.0);
		Vergleich haushalt = new Vergleich("Haushalt",0.0,0,0.0);
		Vergleich KFZ = new Vergleich("KFZ",0.0,0,0.0);
		
		for (Posten p : budget.gesamt) { 
			if (p.getBetrag() >= 0){
				categoryListIn.add(p);
			}
			else
				categoryListOut.add(p);
		}
		
		for (Posten p : categoryListIn){
			switch (p.getKategorie()){
			case"-":
				bindestrich.setBetrag(p.getBetrag());
				bindestrich.incAnzahl();
				break;
			case"Lebensmittel":
				lebensmittel.setBetrag(p.getBetrag());
				lebensmittel.incAnzahl();
				break;
			case"Haushalt":
				haushalt.setBetrag(p.getBetrag());
				haushalt.incAnzahl();
				break;
			case"KFZ":
				KFZ.setBetrag(p.getBetrag());
				KFZ.incAnzahl();
				break;
				
				
			}
		}
		
		bindestrich.bildeDw();
		lebensmittel.bildeDw();
		haushalt.bildeDw();
		KFZ.bildeDw();
		
		List<Vergleich> uebersicht = new ArrayList<Vergleich>();
		uebersicht.add(bindestrich);
		uebersicht.add(lebensmittel);
		uebersicht.add(haushalt);
		uebersicht.add(KFZ);
		
		return uebersicht;
		
	}
	
	public void sortList() {
		double MittelwertBetrag=0;
		double SummeBetrag=0;
		int AnzahlKategorie=0;
		String tmpKategorie= "";
		
		categoryListIn = new ArrayList<Posten>();
		categoryListOut = new ArrayList<Posten>();
		
		categoryListIn2 = new ArrayList<Posten>();
		categoryListOut2 = new ArrayList<Posten>();
		
		
		sortedListIn = new ArrayList<Posten>();
		sortedListOut = new ArrayList<Posten>();
		
	
		for (Posten p : budget.gesamt) { 
			if (p.getBetrag() >= 0){
				categoryListIn.add(p);
				categoryListIn2.add(p);
			}
			else
				categoryListOut.add(p);
				categoryListOut2.add(p);
		}
		
		int initialCounter = 0;
		
		
		for (Posten in : categoryListIn) {
			AnzahlKategorie = 0;
			SummeBetrag=0;
			tmpKategorie=in.getKategorie();
			
			for (Posten in2 : categoryListIn2){
				if (in2.getKategorie().equals(tmpKategorie))
				{
					System.out.println(in.getKategorie()+in2.getKategorie());
					AnzahlKategorie=AnzahlKategorie+1;
					SummeBetrag = SummeBetrag+in2.getBetrag();
				}
			}
			
			MittelwertBetrag = SummeBetrag/AnzahlKategorie;
			in.setBetrag(MittelwertBetrag);
			in.setKategorie(tmpKategorie);
			sortedListIn.add(in);
		}
	}
		/*
		 
		for (Posten in : categoryListIn) {
			
			if (initialCounter == 0){
			sortedListIn.add(in);
			initialCounter =1;}
			
			AnzahlKategorie = 0;
			SummeBetrag=0;
			tmpKategorie="";
			
			for (Posten intmp : sortedListIn){
				if (intmp.getKategorie().equals(in.getKategorie()))
				{break;}
				else
				{
					for (Posten intmp2 : categoryListIn){
						if (intmp2.getKategorie().equals(in.getKategorie())){
							AnzahlKategorie=AnzahlKategorie+1;
							SummeBetrag = SummeBetrag+intmp2.getBetrag();
							tmpKategorie = intmp2.getKategorie();
							System.out.println("True");
						}
					}
					
					MittelwertBetrag = SummeBetrag/AnzahlKategorie;
					
					in.setBetrag(MittelwertBetrag);
					in.setKategorie(tmpKategorie);
					
					System.out.println();
					
					sortedListIn.add(in);
				
					break;}
			}
			 }
		sortedListIn.remove(0);
		}
		*/
		/*
		 for (Posten p : budget.gesamt) {
		 
			if (p.getBetrag() >= 0)
				sortedListIn.add(p);
			else
				sortedListOut.add(p);
			}
			
			
					this.bezeichnung = bezeichnung;
					this.datum = datum;
					this.betrag = betrag;
					this.kategorie = kategorie;
					this.periodkey = periodkey;
			
			*/


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
