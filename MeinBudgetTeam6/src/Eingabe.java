

import java.io.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;


import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JTextField;


	public class Eingabe {
		
		private int ein_aus = 0; // Eingabe = 1; Ausgabe = 2;
		private int perkey; 
		
		private static final String sep = System.getProperty("line.separator"); // /r/n

		private JFrame frame;
		
		private JRadioButton rdbtnEinnahme;
		private JRadioButton rdbtnAusgabe;
		private JRadioButton rdbtnPer;
	
		public Eingabe() {
			initialize();
		}

		private void initialize() {
			frame = new JFrame();
			frame.setBounds(100, 100, 800, 600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new FlowLayout());
			frame.setVisible(true);
			
			//Eingabe oder Ausgabe
			ButtonGroup g = new ButtonGroup(); // nur ein ausgewähltes Element pro Gruppe 
			rdbtnEinnahme = new JRadioButton("Einnahme");
			rdbtnAusgabe = new JRadioButton("Ausgabe");
			g.add(rdbtnEinnahme);
			g.add(rdbtnAusgabe);
			frame.getContentPane().add(rdbtnEinnahme);
			frame.getContentPane().add(rdbtnAusgabe);
						
			//Datum
			JLabel lbl_tdate = new JLabel("Datum");
			frame.getContentPane().add(lbl_tdate);
			JTextField tdate = new JTextField();
			frame.getContentPane().add(tdate);
			tdate.setColumns(10);
			
			//Bezeichnung
			JLabel lbl_tBezeichnung = new JLabel("Bezeichnung");
			frame.getContentPane().add(lbl_tBezeichnung);
			JTextField tBezeichnung= new JTextField();
			frame.getContentPane().add(tBezeichnung);
			tBezeichnung.setColumns(10);
			
			//Betrag
			JLabel lbl_tBetrag = new JLabel("Betrag");
			frame.getContentPane().add(lbl_tBetrag);
			JTextField tBetrag= new JTextField();
			frame.getContentPane().add(tBetrag);
			tBetrag.setColumns(10);
			
			//Kategorie
			String[] kateg_string = {"-", "Lebensmittel", "Haushalt", "KFZ"};
			JComboBox<String> kategorie = new JComboBox<String>(kateg_string);
			kategorie.setEditable(true);
			frame.getContentPane().add(kategorie);
			
			//periodisch anfallende Größen
			rdbtnPer = new JRadioButton("periodisch anfallend");
			String[] per_gr = {"täglich", "monatlich", "halbjährlich", "jährlich"};
			JComboBox<String> per = new JComboBox<String>(per_gr);
			frame.getContentPane().add(rdbtnPer);
			frame.getContentPane().add(per);
			
			//Speichern
			JButton btnSpeichern = new JButton("Speichern");
			frame.getContentPane().add(btnSpeichern);
			
			btnSpeichern.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					ein_aus_checker();
					if (rdbtnPer.isSelected() == true) {
						per_checker((String) per.getSelectedItem());
					} else {perkey = 0;}
					schreibeDaten(tdate.getText(),tBezeichnung.getText(),tBetrag.getText(),(String)kategorie.getSelectedItem(), perkey);
				}
			});
			
			//Hinweise
			JLabel hinweise = new JLabel("Kommazahlen Bitte mit Punkt trennen");
			frame.getContentPane().add(hinweise);
		}
	
		
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
		
	private void ein_aus_checker(){ //wandelt markierte Checkbox in Wert um...
		if (rdbtnEinnahme.isSelected() == true){
			ein_aus = 1;
		} if (rdbtnAusgabe.isSelected()== true){
			ein_aus = 2;
		}
	}
		
	private boolean schreibeDaten(String sdatum, String sbeschreibung, String sbetrag, String kateg_name, int per){
	   	boolean status = false; //Check ob gespeichert werden kann.. momentan noch außen vor
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
	
}
	
