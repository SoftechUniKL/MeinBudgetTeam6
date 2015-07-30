import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;


public class Datenerfassung extends Menu {
	//Variablen
	private int ein_aus = 0; // Eingabe = 1; Ausgabe = 2;
	private int perkey; 
	
	private String hl;
	private static final String sep = System.lineSeparator(); // /r/n

	//GUI Elemente

	private JFrame frame;
	private JPanel buttons, form;
	private JRadioButton rdbtnEinnahme, rdbtnAusgabe, rdbtnPer;
	private JTextField tdate, tBezeichnung, tBetrag;
	private JComboBox kategorie, per;
	private JScrollPane scrollpane;
	private JTable table;
	private BudgetPlanModel bpm = new BudgetPlanModel();
	
	//Konstruktor - default
	public Datenerfassung() {
	}
	
	public Datenerfassung(int mode) {
		ein_aus = mode;
		
		if( mode == 1) { //switch zwischen Ein und Auszahlung in der headline
			hl ="Einzahlung";
		} 
		else{
			hl="Auszahlung";
		}
		
		frame = new JFrame();
		createClientPanel(frame, hl);
		setup();
		
	}
		
	private void setup() {
		GridBagLayout gb = new GridBagLayout();
		
		buttons = new JPanel();
		form = new JPanel();
		form.setLayout(gb);
		
		scrollpane = new JScrollPane(form);
		
		//Datum
		JLabel lbl_tdate = nline("Datum");
		JLabel lbl_tdate_hinweise = notice("Format: 31.12.2014");
		tdate = new JTextField();
		tdate.setColumns(10);
		addComp(form,  gb,			lbl_tdate,				0,			1,	1,	1,  GridBagConstraints.WEST);
		addComp(form,  gb,			tdate,					1,			1,	1,	1, GridBagConstraints.CENTER);
		addComp(form,  gb,			lbl_tdate_hinweise,		2,			1,	1,	1, GridBagConstraints.WEST);
		
		//Bezeichnung
		JLabel lbl_tBezeichnung = nline("Bezeichnung");
		addComp(form,  gb,			lbl_tBezeichnung,		0,			2,	1,	1,  GridBagConstraints.WEST);
		tBezeichnung= new JTextField();
		tBezeichnung.setColumns(10);
		addComp(form,  gb,			tBezeichnung,			1,			2,	1,	1, GridBagConstraints.CENTER);
		
		//Betrag
		JLabel lbl_tBetrag = nline("Betrag");
		JLabel lbl_tBetrag_hinweise = notice("Kommazahlen mit Punkt trennen!");
		tBetrag= new JTextField();
		tBetrag.setColumns(10);
		addComp(form,  gb,			lbl_tBetrag,			0,			3,	1,	1, GridBagConstraints.WEST);
		addComp(form,  gb,			tBetrag,				1,			3,	1,	1, GridBagConstraints.CENTER);
		addComp(form,  gb,			lbl_tBetrag_hinweise,	2,			3,	1,	1, GridBagConstraints.WEST);
		
		//Kategorie
		JLabel lbl_kateg = nline("Kategorie");
		JLabel lbl_kateg_hinweise = notice("Alternativ: Neu ausfüllen");
		List<String> kat =bpm.getCategorys();
		String[] kateg_string = new String[kat.size()];
		kat.toArray(kateg_string);
		kategorie = new JComboBox<String>(kateg_string);
		kategorie.setEditable(true);
		addComp(form,  gb,			lbl_kateg,				0,			4,	1,	1, GridBagConstraints.WEST);
		addComp(form,  gb,			kategorie,				1,			4,	1,	1, GridBagConstraints.CENTER);
		addComp(form,  gb,			lbl_kateg_hinweise,		2,			4,	1,	1, GridBagConstraints.WEST);
		
		//periodisch anfallende Größen 
		JLabel lbl_per_hinweise = notice("nach dem Speichern öffnet sich das Verwaltungsfenster für regelmäßige Zahlungen");

		JLabel lbl_per = nline("Regelmäßig anfallend?");
		rdbtnPer = new JRadioButton("ja");
		
		addComp(form,  gb,			lbl_per,				0,			5,	1,	1, GridBagConstraints.WEST);
		addComp(form,  gb,			rdbtnPer,				1,			5,	1,	1, GridBagConstraints.CENTER);
		addComp(form,  gb,			lbl_per_hinweise,		1,			6,	1,	1, GridBagConstraints.WEST);


		
		//Speichern
		JButton btnSpeichern = new JButton("Speichern");
		buttons.add(btnSpeichern);
		btnSpeichern.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				save();
			}	
		});
		
		//Schließen
		JButton btnAbbruch = new JButton("Abbrechen");
		buttons.add(btnAbbruch);
				btnAbbruch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				backHM();
			}
		});
		
		
		frame.add(buttons, BorderLayout.SOUTH);
		frame.getContentPane().add(scrollpane);
		frame.validate();
	}
	
	private void save(){
		if (bpm.isDate(tdate.getText()) == true){
			if((bpm.isZahl(tBetrag.getText()) == true)){	
				bpm.save(tdate.getText(),tBezeichnung.getText(),tBetrag.getText(),(String)kategorie.getSelectedItem(), 0, ein_aus);
				bpm.refresh(); //Posten werden neu eingelesen
				if (rdbtnPer.isSelected() == true) {
					int position = bpm.gesamt.size()-1; 
					windows  window = new windows("Bestätigen", bpm, position);
					frame.dispose(); // frame schließen
				}else {backHM();}
			
			}
			else {JOptionPane.showMessageDialog(null, "Bitte eine gültige Zahl als Betrag eingeben!");}
		}
		else {JOptionPane.showMessageDialog(null, "Bitte ein gültiges Datum eingeben!");}			
		
	}


	//ab hier Hilfsmethoden
	private void per_checker(String tocheck){
		switch (tocheck){
		case "täglich": //Werte erstmal frei gewählt
			perkey = 1; 
			break;
		case "monatlich":
			perkey = 30;
			break;
		case "halbjährlich":
			perkey = 180;
			break;
		case "jährlich":
			perkey = 365;
			break;
		default:
			perkey = 0;
		}
		 
		
	}
	
	private void backHM() {
		hm_ref.revisible();
		frame.dispose();
	}

}

