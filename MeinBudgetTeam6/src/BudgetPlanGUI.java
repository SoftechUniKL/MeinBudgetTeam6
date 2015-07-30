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

public class BudgetPlanGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private DefaultTableModel dtmIn, dtmOut;
	private DefaultPieDataset pieAus, pieEin;
	private DefaultCategoryDataset barEin, barAus;
	private Object[] line;
	private JTable tableIn, tableOut;
	private JFreeChart barEinnahme, barAusgabe, pieAusgabe, pieEinnahme;

	private JScrollPane scrollpaneIn, scrollpaneOut;
	ChartPanel pAus, pEin, bEin, bAus;
	
	private JButton buttonAll, buttonMonth, buttonYear, buttonBalken, buttonClose;
	private JPanel charts;
	private BudgetPlanModel budget;
	private List<Posten> sortedListIn, sortedListOut;
	
	public BudgetPlanGUI(BudgetPlanModel budget) {
		super("BudgetPlan");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.budget = budget;
		initWindow();
		addBehavior();
		setBounds(400, 400, 800, 800);
		setVisible(true);
	}

	protected void initWindow() {

		sortList();
		line = new Object[] { "Datum", "Bezeichnung", "Betrag", "Kategorie" };
		dtmIn = new DefaultTableModel(setupTable(true), line);
		dtmOut = new DefaultTableModel(setupTable(false), line);
		tableIn = new JTable(dtmIn);
		tableOut = new JTable(dtmOut);

		scrollpaneIn = new JScrollPane(tableIn);
		scrollpaneOut = new JScrollPane(tableOut);
		JPanel tablePanel = new JPanel(new GridLayout(1, 2));
		tablePanel.add(scrollpaneIn);
		tablePanel.add(scrollpaneOut);
		
		buttonAll = new JButton("Gesamt");
		buttonMonth = new JButton("Monat");
		buttonYear = new JButton("Jahr");
		buttonBalken = new JButton("Als Balkendiagramm");
		buttonClose = new JButton("Zum Hauptmen\u00fc");

		JPanel controlPanel = new JPanel();
		controlPanel.add(new JLabel("Zeitraum"));
		controlPanel.add(buttonAll);
		controlPanel.add(buttonYear);
		controlPanel.add(buttonMonth);
		controlPanel.add(new JLabel("Darstellung"));
		controlPanel.add(buttonBalken);
		controlPanel.add(buttonClose);
		buttonClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();
				Hauptmenu m1 = new Hauptmenu();
				}
			});
		
		getContentPane().add(controlPanel);
		getContentPane().add(tablePanel);

		setupDatasets();
		
		charts = new JPanel();
		charts.setLayout(new GridLayout(1, -1));
		getContentPane().add(charts);
		pieAusgabe = ChartFactory.createPieChart("Ausgaben", pieAus);
		pAus = new ChartPanel(pieAusgabe);
		//getContentPane().add(pAus);
		pieEinnahme = ChartFactory.createPieChart("Einnahmen", pieEin);
		pEin = new ChartPanel(pieEinnahme);
		//getContentPane().add(pEin);
		
		barEinnahme = ChartFactory.createBarChart("Einnahmen", "Kategorie",
		"Betrag", barEin, PlotOrientation.VERTICAL, true, true, false);
		bEin = new ChartPanel(barEinnahme);
		//getContentPane().add(bEin);
		bEin.setVisible(false);
		barAusgabe = ChartFactory
		.createBarChart("Ausgaben", "Kategorie", "Betrag", barAus,
		PlotOrientation.VERTICAL, true, true, false);
		bAus = new ChartPanel(barAusgabe);
		//getContentPane().add(bAus);
		bAus.setVisible(false);
		
		charts.add(pAus);
		charts.add(pEin);
	}

	public void setupDatasets() {
		pieAus = new DefaultPieDataset();
		pieEin = new DefaultPieDataset();
		barEin = new DefaultCategoryDataset();
		barAus = new DefaultCategoryDataset();
		for (Posten p : sortedListOut) {
			pieAus.setValue(p.getBezeichnung(), Math.abs(p.getBetrag()));
			barAus.addValue(Math.abs(p.getBetrag()), p.getBezeichnung(), "");
		}
		for (Posten p : sortedListIn) {
			pieEin.setValue(p.getBezeichnung(), Math.abs(p.getBetrag()));
			barEin.addValue(Math.abs(p.getBetrag()), p.getBezeichnung(), "");
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
		pieAusgabe = ChartFactory.createPieChart("Ausgaben", pieAus);
		pAus.setChart(pieAusgabe);
		pieEinnahme = ChartFactory.createPieChart("Einnahmen", pieEin);
		pEin.setChart(pieEinnahme);
		barEinnahme = ChartFactory.createBarChart("Einnahmen", "Kategorie",
		"Betrag", barEin, PlotOrientation.VERTICAL, true, true, false);
		bEin.setChart(barEinnahme);
		barAusgabe = ChartFactory
		.createBarChart("Ausgaben", "Kategorie", "Betrag", barAus,
		PlotOrientation.VERTICAL, true, true, false);
		bAus.setChart(barAusgabe);
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

	public void addBehavior() {

		ActionListener aListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == buttonAll) {
					sortList();
					refreshTables();
					refreshDiagrams();
				} else if (e.getSource() == buttonYear) {
					sortYear();
					refreshTables();
					refreshDiagrams();
				} else if (e.getSource() == buttonMonth) {
					sortMonth();
					refreshTables();
					refreshDiagrams();
				} else if (e.getSource() == buttonBalken) {
					if (pAus.isVisible()) {
						buttonBalken.setText("Als Kreisdiagramm");
						pAus.setVisible(false);
						pEin.setVisible(false);
						charts.remove(pEin);
						charts.remove(pAus);
						bEin.setVisible(true);
						bAus.setVisible(true);
						charts.add(bEin);
						charts.add(bAus);
					} else {
						buttonBalken.setText("Als Balkendiagramm");
						pAus.setVisible(true);
						pEin.setVisible(true);
						bEin.setVisible(false);
						bAus.setVisible(false);
						charts.add(pEin);
						charts.add(pAus);
						charts.remove(bEin);
						charts.remove(bAus);
						
					}
					getContentPane().validate();
					getContentPane().repaint();
				}
			}
		};

		buttonAll.addActionListener(aListener);
		buttonYear.addActionListener(aListener);
		buttonMonth.addActionListener(aListener);
		buttonBalken.addActionListener(aListener);

	}
	
	public void sortMonth(){
		sortedListIn = new ArrayList<Posten>();
		sortedListOut = new ArrayList<Posten>();
		
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		
		for (Posten p : budget.gesamt) {
			cal.setTime(p.getDatum());
			int comp_month = cal.get(Calendar.MONTH);
			int comp_year = cal.get(Calendar.YEAR);
			if (comp_month == month && comp_year == year) {
				if (p.getBetrag() >= 0)
					sortedListIn.add(p);
				else
					sortedListOut.add(p);
			}
		}
	}
	
	public void sortYear() {
		sortedListIn = new ArrayList<Posten>();
		sortedListOut = new ArrayList<Posten>();
		
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		int year = cal.get(Calendar.YEAR);
		
		for (Posten p : budget.gesamt) {
			cal.setTime(p.getDatum());
			int comp_year = cal.get(Calendar.YEAR);
			if (comp_year == year) {
				if (p.getBetrag() >= 0)
					sortedListIn.add(p);
				else
					sortedListOut.add(p);
			}
		}		
	}
}
