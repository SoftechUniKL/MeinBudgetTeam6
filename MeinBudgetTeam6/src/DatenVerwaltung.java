import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;



import javax.swing.JButton;
import javax.swing.JFrame;
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


	public DatenVerwaltung(BudgetPlanModel budget, JFrame m1) {
		//super("BudgetPlan");
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);
		this.m1 = m1;
		this.budget = budget;
		initWindow(); // Initialisierung des Frameinhalts
		addBehavior(); // Verhalten der GUI Elemente dieses Frames
	}
	

	protected void initWindow() {


		JLabel hl = headline("Datenverwaltung");
		frame.add(hl, BorderLayout.NORTH);
		
		// Tabelle mit Uebersicht der Ausgaben
		line = new Object[] { "Datum", "Bezeichnung","Betrag", "Kategorie", "Periodisch anfallend" };
		dtm = new DefaultTableModel(setupTable(),line);
		table = new JTable(dtm);
		
		scrollpane = new JScrollPane(table);

		// Button
		JButton btnDelete = new JButton("Löschen!");
		JButton btnAbbruch = new JButton ("Zurück ins Hauptmenü!");
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					int row = table.getSelectedRow();
					budget.remove(row);
				
				
					budget.initialize();
					dtm = new DefaultTableModel(setupTable(),line);
					table.setModel(dtm);
	
				
			}

		});
		
		btnAbbruch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m1.setVisible(true);
				frame.dispose();
	
				
			}

		});
		// Elemente dem Fenster hinzufuegen:
		frame.add(scrollpane, BorderLayout.CENTER);
		panel = new JPanel();
		panel.add(btnDelete);
		panel.add(btnAbbruch);
		frame.add(panel, BorderLayout.SOUTH);


		frame.validate();
	}
	
	
	public Object[][] setupTable(){
		Object[][] data = new Object[budget.gesamt.size()][5];
		int i = 0;
		for (Posten p : budget.gesamt) {
			data[i][0] = new SimpleDateFormat("dd/MM/yyyy")
					.format(p.getDatum());
			data[i][1] = p.getBezeichnung();
			data[i][2] = String.format("%.2f", p.getBetrag());
			data[i][3] = p.getKategorie();
			data[i][4] = p.getperiodkey();
			i++;
		}
		return data;
	}
	
	
	// Verhalten hinzufuegen
	public void addBehavior() {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		
		


	}
	

}