

import java.io.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;


import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.Date;
import java.text.*;

import javax.swing.JTextField;


	public class Eingabe {
		
		private int ein_aus = 0; // Eingabe = 1; Ausgabe = 2;
		
		private static final String sep = System.getProperty("line.separator"); // /r/n

		private JFrame frame;
		
		private JRadioButton rdbtnEinnahme;
		private JRadioButton rdbtnAusgabe;
	
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
			ButtonGroup g = new ButtonGroup(); // ButtonGroup erlaubt grob gesagt nur ein ausgew‰hltes Element der Gruppe 
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
			String[] kateg_string = {"Fixe Kosten", "Lebensmittel", "Haushalt", "KFZ"};
			JComboBox kategorie = new JComboBox(kateg_string);
			kategorie.setEditable(true);
			frame.getContentPane().add(kategorie);
			
			//Speichern
			JButton btnSpeichern = new JButton("Speichern");
			frame.getContentPane().add(btnSpeichern);
			
			btnSpeichern.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					ein_aus_checker();
					schreibeDaten(tdate.getText(),tBezeichnung.getText(),tBetrag.getText(),(String)kategorie.getSelectedItem());
				}
			});
		}
	
		
	private void ein_aus_checker(){ //wandelt markierte Checkbox in Wert aus...
		if (rdbtnEinnahme.isSelected() == true){
			ein_aus = 1;
		} if (rdbtnAusgabe.isSelected()== true){
			ein_aus = 2;
		}
	}
		
	private boolean schreibeDaten(String sdatum, String sbeschreibung, String sbetrag, String kateg_name){
	   	boolean status = false; //Check ob gespeichert werden kann.. momentan noch auﬂen vor
        String save ="Error";
        
        if (ein_aus == 1){
        	sbetrag = "+"+sbetrag;
        } else if ( ein_aus == 2){
        	sbetrag = "-"+sbetrag;
        }
        
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("data/budget.csv", true)); 
            	
               	save = sdatum +"," + sbeschreibung+ ","+ sbetrag+ "," +kateg_name + sep;
 
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
	
