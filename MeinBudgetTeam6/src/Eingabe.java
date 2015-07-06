

import java.io.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import javax.swing.JComponent;
import javax.swing.JScrollPane;



import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.text.SimpleDateFormat;



	public class Eingabe extends Menu{
		
		private int ein_aus = 0; // Eingabe = 1; Ausgabe = 2;
		private int perkey; 
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		private static final String sep = System.lineSeparator(); // /r/n

		private JFrame frame;
		private JFrame m1;
		
		private JPanel buttons = new JPanel();
		JPanel form = new JPanel();
		
		private JRadioButton rdbtnEinnahme;
		private JRadioButton rdbtnAusgabe;
		private JRadioButton rdbtnPer;
		
		
		

	
		JScrollPane scroll = new JScrollPane(form);
		
		public Eingabe() {
			init();
			setup();
		}
		
		public Eingabe(JFrame m1) {
			this.m1=m1;
			init();
			setup();
		}
		private void init(){
			frame = new JFrame();
			frame.setBounds(100, 100, 800, 600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new BorderLayout());
			frame.setVisible(true);
		}
		
		private void setup() {
			GridBagLayout gb = new GridBagLayout();
			form.setLayout(gb);
			
			JLabel hl = headline("Datenerfassung");
			frame.add(hl, BorderLayout.NORTH);
			
			//Eingabe oder Ausgabe
			ButtonGroup g = new ButtonGroup(); // nur ein ausgew‰hltes Element pro Gruppe 
			rdbtnEinnahme = new JRadioButton("Einnahme");
			rdbtnAusgabe = new JRadioButton("Ausgabe");
			g.add(rdbtnEinnahme);
			g.add(rdbtnAusgabe);
			JLabel lbl_einaus = new JLabel("Handelt es sich um eine Ein- oder Ausgabe?");
			addComp(form , gb,			lbl_einaus,				0,			0,	1,	1);
			addComp(form,  gb,			rdbtnEinnahme,			1,			0,	1,	1);
			addComp(form,  gb,			rdbtnAusgabe,			2,			0, 	1,	1);
						
			//Datum
			JLabel lbl_tdate = new JLabel("Bitte das Datum eingeben");
			JTextField tdate = new JTextField();
			tdate.setColumns(10);
			addComp(form,  gb,			lbl_tdate,				0,			1,	1,	1);
			addComp(form,  gb,			tdate,					1,			1,	2,	1);
			
			//Bezeichnung
			JLabel lbl_tBezeichnung = new JLabel("Bezeichnung");
			addComp(form,  gb,			lbl_tBezeichnung,		0,			2,	1,	1);
			JTextField tBezeichnung= new JTextField();
			tBezeichnung.setColumns(10);
			addComp(form,  gb,			tBezeichnung,			1,			2,	1,	1);
			
			//Betrag
			JLabel lbl_tBetrag = new JLabel("Betrag");
			JTextField tBetrag= new JTextField();
			tBetrag.setColumns(10);
			addComp(form,  gb,			lbl_tBetrag,			0,			3,	1,	1);
			addComp(form,  gb,			tBetrag,				1,			3,	1,	1);
			
			//Kategorie
			JLabel lbl_kateg = new JLabel("Kategorie ausw‰hlen");
			String[] kateg_string = {"-", "Lebensmittel", "Haushalt", "KFZ"};
			JComboBox<String> kategorie = new JComboBox<String>(kateg_string);
			kategorie.setEditable(true);
			addComp(form,  gb,			lbl_kateg,				0,			4,	1,	1);
			addComp(form,  gb,			kategorie,				1,			4,	1,	1);
			
			//periodisch anfallende Grˆﬂen
			rdbtnPer = new JRadioButton("periodisch anfallend");
			String[] per_gr = {"t‰glich", "monatlich", "halbj‰hrlich", "j‰hrlich"};
			JComboBox<String> per = new JComboBox<String>(per_gr);
			addComp(form,  gb,			rdbtnPer,				0,			5,	1,	1);
			addComp(form,  gb,			per,					1,			5,	1,	1);
				
			//Speichern
			JButton btnSpeichern = new JButton("Speichern");
			buttons.add(btnSpeichern);
			//addComp(form,  gb,			btnSpeichern,			0,			6,	2,	1);
						
			btnSpeichern.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if (isDate(tdate.getText()) == true){
						if((isZahl(tBetrag.getText()) == true)){	
							ein_aus_checker();
							if (rdbtnPer.isSelected() == true) {
								per_checker((String) per.getSelectedItem());
							} else {perkey = 0;}
							schreibeDaten(tdate.getText(),tBezeichnung.getText(),tBetrag.getText(),(String)kategorie.getSelectedItem(), perkey);
							backHM();
						}
						else {JOptionPane.showMessageDialog(null, "Betrag bitte als Zahl eingeben");}
					}
					else {JOptionPane.showMessageDialog(null, "falsches Datum");}
				}
			});
			
			//Schlieﬂen
			JButton btnAbbruch = new JButton("Abbrechen");
			buttons.add(btnAbbruch);
			
			//addComp(form,  gb,			btnAbbruch,			1,			6,	2,	1);
			
			btnAbbruch.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					backHM();
				}
			});
			
			
			frame.add(buttons, BorderLayout.SOUTH);
			frame.getContentPane().add(scroll);
			frame.validate();
		}
	
		
	private boolean schreibeDaten(String sdatum, String sbeschreibung, String sbetrag, String kateg_name, int per){
	   	boolean status = false; //Check ob gespeichert werden kann.. momentan noch auﬂen vor
        String save ="Error";
        
        if (ein_aus == 1){
        	sbetrag = "+"+sbetrag;
        } else if ( ein_aus == 2){
        	sbetrag = "-"+sbetrag;
        }
        
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("data/budget.csv", true)); 
            	
               	save = sdatum +"," + sbeschreibung+ ","+ sbetrag+ "," +kateg_name + "," + per +sep;
 
                bw.write(save); //schreiben
                bw.flush(); // Puffer von BW leeren
            bw.close();//BufferWriter schliessen
            status=true;
        }
        catch (Exception e){ // noch nichts
        }
        
        return status;
    }
	
	//ab hier Hilfsmethoden
	private void per_checker(String tocheck){
		switch (tocheck){
		case "t‰glich": //Werte erstmal frei gew‰hlt
			perkey = 1; 
			break;
		case "monatlich":
			perkey = 30;
			break;
		case "halbj‰hrlich":
			perkey = 180;
			break;
		case "j‰hrlich":
			perkey = 365;
			break;
		default:
			perkey = 0;
		}
		 
		
	}
		
	private void ein_aus_checker(){ //wandelt markierte Checkbox in Wert um...
		if (rdbtnEinnahme.isSelected() == true){
			ein_aus = 1;
		} if (rdbtnAusgabe.isSelected()== true){
			ein_aus = 2;
		}
	}
	
	private boolean isZahl(String s){
		try{
			double eingabe = Double.parseDouble(s);
			return true;
		}
		catch (Exception e){
			return false;
		}
	}
	
	private boolean isDate(String s){
		SimpleDateFormat dformat = new SimpleDateFormat("dd.MM.yyyy");
		dformat.setLenient(false);
		
		try{
			dformat.parse(s);
			return true;
		} catch (Exception e) {
			//kein Datum
			return false;
		}
	}
	
	private void backHM() {
		m1.setVisible(true);
		frame.dispose();
	}
	
}
	
