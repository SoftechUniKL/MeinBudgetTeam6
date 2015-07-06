import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.opencsv.CSVReader;
import com.opencsv.CSVParser;

/**
 * Datenmodell des Budgetplaners
 * 
 * Die Daten werden in der Datei data/budget.csv abgespeichert als CSV-Datei.
 * 
 */
public class BudgetPlanModel {
	List<Posten> gesamt;
	
	private static final String sep = System.lineSeparator();
	
	public BudgetPlanModel() {
		initialize();
	}
	
	public void initialize(){
		this.gesamt = new ArrayList<Posten>();
		try {
			// Zeilenweises Einlesen der Daten
			CSVReader reader = new CSVReader(new FileReader("data/budget.csv"));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN);
				Date datum = df.parse(nextLine[0]);
				String bezeichnung = nextLine[1];
				double betrag = Double.parseDouble(nextLine[2]);
				String kategorie = nextLine[3];
				int periodkey = Integer.parseInt(nextLine[4]);
				gesamt.add(new Posten(datum, bezeichnung, betrag, kategorie, periodkey));
			}
			reader.close();

		} catch (FileNotFoundException e) {
			System.err
					.println("Die Datei data/budget.csv wurde nicht gefunden!");
			System.exit(1);
		} catch (IOException e) {
			System.err
					.println("Probleme beim Oeffnen der Datei data/budget.csv!");
			System.exit(1);
		} catch (ParseException e) {
			System.err
					.println("Formatfehler: Die Datei konnte nicht eingelesen werden!");
			System.exit(1);
		}
	}
	
	 public static void clearCsv() {
		 try{
			 BufferedWriter bw = new BufferedWriter(new FileWriter("data/budget.csv", true)); 
	         bw.flush();
	         bw.close(); 
		 }
		 catch (Exception e){ // noch nichts
	     }
	}
	
	public void remove(int n){
		clearCsv();
		gesamt.remove(n);
			
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("data/budget.csv", false)); 
            	
        		for (Posten p : gesamt) {
        			String d = dateToString(p.getDatum());
        			String b = p.getBezeichnung();
        			Double be = p.getBetrag();
        			String k = p.getKategorie();
        			int pk = p.getperiodkey();
    			
        			String save = d +"," + b+ ","+ be+ "," +k +","+ pk + sep;
    				
        			bw.write(save); //schreiben
        		}
                bw.flush(); // Puffer von BW leeren
                bw.close();//BufferWriter schliessen
        }
        catch (Exception e){ // noch nichts
        }
		}

	

	private String dateToString(Date d){
		String format = "dd.MM.yyyy";
		Date date = d;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String stringDate = sdf.format(date );
		return stringDate;
	}
}

	
