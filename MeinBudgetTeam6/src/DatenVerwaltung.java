import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;



/**
 * Graphische Benutzeroberflaeche des BudgetPlaners
 * 
 */
public class DatenVerwaltung extends Menu {
	private static final long serialVersionUID = 1L;
	/**
	 * Tabelle mit Uebersicht der Ausgaben
	 */
   
	private DefaultTableModel dtm;
	private Object[] line;
	
	private JFrame frame;
	private JFrame m1;
	private JPanel panel;
	
	private JTable table;
	
	private JScrollPane scrollpane;

	private BudgetPlanModel budget;


	public DatenVerwaltung(BudgetPlanModel budget) {
		//super("BudgetPlan");
		frame = new JFrame();
		createClientPanel(frame, "Datenverwaltung");
		this.budget = budget;
		initWindow(); // Initialisierung des Frameinhalts
	}	
	

	protected void initWindow() {

		// Tabelle mit Uebersicht der Ausgaben
		line = new Object[] { "Datum", "Bezeichnung","Betrag", "Kategorie", "ID" };
		dtm = new DefaultTableModel(budget.setupTable(budget.gesamt),line);
		table = new JTable(dtm);
		
		scrollpane = new JScrollPane(table);
		
		
		// Button
		JButton btnSortDat = new JButton ("Nach Datum sortieren");
		btnSortDat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Collections.sort(budget.gesamt, new SortDate());
				dtm = new DefaultTableModel(budget.setupTable(budget.gesamt),line);
				table.setModel(dtm);	
			}

		});
		JButton btnSortID = new JButton ("Nach ID sortieren");
		btnSortID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Collections.sort(budget.gesamt, new SortID());
				dtm = new DefaultTableModel(budget.setupTable(budget.gesamt),line);
				table.setModel(dtm);	
			}

		});
		
		JButton btnDelete = new JButton("Löschen!");
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					int line_TR = table.getSelectedRow();
					int row = 4;
					long id_tR = (long) table.getValueAt(line_TR,row);
					budget.remove(line_TR);
				
				
					budget.initialize();
					dtm = new DefaultTableModel(budget.setupTable(budget.gesamt),line);
					table.setModel(dtm);
				} 
				catch (ArrayIndexOutOfBoundsException ex){
					JOptionPane.showMessageDialog(null, "Bitte eine Zeile zum Löschen auswählen");
				}
				
			}

		});
		JButton btnAbbruch = new JButton ("Zurück ins Hauptmenü!");
		btnAbbruch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hm_ref.revisible();
				frame.dispose();
	
				
			}

		});
		// Elemente dem Fenster hinzufuegen:
		frame.add(scrollpane, BorderLayout.CENTER);
		panel = new JPanel();
		panel.add(btnSortDat);
		panel.add(btnSortID);
		panel.add(btnDelete);
		panel.add(btnAbbruch);
		frame.add(panel, BorderLayout.SOUTH);
		
		frame.validate();
	}
	
}