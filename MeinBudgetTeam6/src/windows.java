import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;



import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class windows extends Menu{

	private JFrame frame;
	private JTable table;
	private JPanel panel, centerpanel, buttons;
	private JComboBox kategorie;
	private JTextField dauer;
	private JScrollPane tablepane;
	private Object[] line = new Object[] { "Datum", "Bezeichnung","Betrag", "Kategorie", "Periodisch anfallend" };
	private DefaultTableModel dtm;
	private int position;
	private BudgetPlanModel bpm;
	private List<Posten> repeatlist;
	
	public windows(String s, BudgetPlanModel bpm, int position){
		frame = new JFrame();
		createClientPanel(frame,s);
		this.position = position;
		this.bpm = bpm;
		repeatAuswahl();
		
	}
	
	public void repeatAuswahl(){
		GridBagLayout gb = new GridBagLayout();
		JLabel lbl_kateg = new JLabel("Wiederholen?");
		String[] kateg_string = {"jeden Monat","jedes halbe Jahr","jedes Jahr"};
		JLabel lbl_dauer = new JLabel("Wie lange?(Endjahr)");
		dauer = new JTextField(4);
		kategorie = new JComboBox<String>(kateg_string);
		kategorie.setEditable(false);
		panel = new JPanel();
		
		dtm = new DefaultTableModel(bpm.setupTable(bpm.gesamt,position),line);
		table = new JTable(dtm);
		
		addComp(panel,  gb,			table,					0,			1,	1,	1);
		addComp(panel,  gb,			lbl_kateg,				0,			2,	1,	1);
		addComp(panel,  gb,			kategorie,				1,			2,	1,	1);
		addComp(panel,  gb,			lbl_dauer,				0,			3,	1,	1);
		addComp(panel,  gb,			dauer,					1,			3,	1,	1);
		centerpanel = new JPanel();
		centerpanel.setLayout(new BorderLayout());
		centerpanel.add(panel, BorderLayout.NORTH);
		
		buttons = new JPanel();
		JButton btnVorschau = new JButton("Vorschau");
		buttons.add(btnVorschau);
		JButton btnDelete = new JButton("Löschen!");
		buttons.add(btnDelete);
		JButton btnSave = new JButton("Speichern!");
		buttons.add(btnSave);
		
		btnSave.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e){
				bpm.addto(repeatlist);
				frame.dispose();
				hm_ref.revisible();
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					int row = table.getSelectedRow();
					repeatlist.remove(row);
					for (Posten p : repeatlist){
						p.printPosten();
					}
					
					dtm = new DefaultTableModel(bpm.setupTable(repeatlist),line);
					table.setModel(dtm);
					tablepane.validate();
			}
		});
				
		
		btnVorschau.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int t = Integer.parseInt(dauer.getText());
				String s = (String)kategorie.getSelectedItem();
				centerpanel.removeAll();
				centerpanel.add(panel, BorderLayout.NORTH);
				
				repeatControl(position, bpm, t, rename(s));
				frame.validate();
			}	
		});
		frame.add(centerpanel, BorderLayout.CENTER);
		frame.add(buttons, BorderLayout.SOUTH);
				
	}
	
	public int rename(String toCheck){
		int i = 0;
		switch(toCheck){
			case "jeden Monat":
				i = 1;
				return i;
			case "jedes halbe Jahr":
				i = 6;
				return i;
			case "jedes Jahr":
				i = 12;
				return i;
			default:
				return i;
		}
	}
	
	public void repeatControl(int position, BudgetPlanModel bpm, int dauer, int intervall){
		repeatlist = new ArrayList<Posten>(); //Elemente die wiederholt werden
		Posten pos = bpm.gesamt.get(position);
		
		repeatlist = bpm.repeatx(1,pos,dauer,intervall);
		
		
		
		//line = new Object[] { "Datum", "Bezeichnung","Betrag", "Kategorie", "Periodisch anfallend" };
		dtm = new DefaultTableModel(bpm.setupTable(repeatlist),line);
		table = new JTable(dtm);
		
		tablepane = new JScrollPane(table);
		centerpanel.add(tablepane, BorderLayout.CENTER);
		frame.add(centerpanel, BorderLayout.CENTER);
		
	}
	
	
	
}
