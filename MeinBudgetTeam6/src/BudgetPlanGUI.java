import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import static java.lang.Math.abs;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable.*;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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
   
	private DefaultTableModel dtm;
	private Object[] line;
	
	private JTable table;
	/**
	 * Scrollelemente, das die Tabelle umfasst
	 */
	private JScrollPane scrollpane;
	/**
	 * Schaltflaeche, die beim Klicken einen Dialog anzeigt
	 */
	private JButton button;

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
		getContentPane().setLayout(new FlowLayout());

		this.budget = budget;
		initWindow(); // Initialisierung des Frameinhalts
		addBehavior(); // Verhalten der GUI Elemente dieses Frames
		setBounds(10, 10, 800, 800); // Groesse des Frames
		setVisible(true); // Frame wird sichtbar
	}
	
	// Initialisieren des Fensters
	protected void initWindow() {

		// Tabelle mit Uebersicht der Ausgaben
		line = new Object[] { "Datum", "Bezeichnung","Betrag", "Kategorie", "Periodisch anfallend" };
		dtm = new DefaultTableModel(setupTable(),line);
		table = new JTable(dtm);
		
		scrollpane = new JScrollPane(table);

		// Kreisdiagramm
		DefaultPieDataset pd = new DefaultPieDataset();
		for (Posten p : budget.gesamt) {
			if(p.getBetrag() <= 0){
				pd.setValue(p.getBezeichnung(),Math.abs(p.getBetrag()));
			}
		
		}
		JFreeChart pie = ChartFactory.createPieChart("Ausgaben", pd);
		ChartPanel panel = new ChartPanel(pie);

		// Button
		button = new JButton("Refresh!");


		// Elemente dem Fenster hinzufuegen:
		getContentPane().add(scrollpane);
		getContentPane().add(panel);
		getContentPane().add(button);

		// Berechnet Layout mit geringstem Platzbedarf
		pack();
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
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					//JOptionPane.showMessageDialog(BudgetPlanGUI.this,
					//	"Sie sollten Ihre Finanzplanung ueberdenken!",
					//	"Hinweis", JOptionPane.PLAIN_MESSAGE);
					
					budget.initialize();
					dtm = new DefaultTableModel(setupTable(),line);
					table.setModel(dtm);
	
				
			}

		});

	}
	

}