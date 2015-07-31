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
import java.util.Calendar;
import java.util.Collections;

import javax.swing.JOptionPane;

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
	
	public void refresh(){
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
		} catch (NumberFormatException e) {
			int selected = JOptionPane.showConfirmDialog(null,
                    "Soll versucht werden den Fehler zu beheben, indem die letzten hinzugefügten Einträge gelöscht werden? Vorsicht Datenverlust!",
                    "NumberFormatException beim Öffnen der Datei",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
			if (selected == 0) {remove();} else {System.exit(1);}
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
	
	 public void remove(){
			clearCsv();
						
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

	public void save(String sdatum, String sbeschreibung, String sbetrag, String kateg_name, int per, int ein_aus){
		boolean status = false; //Check ob gespeichert wurde
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
		    catch (Exception e){ 
		    	System.out.println(e.getMessage());
		    	if (status == true){
		    		//int position = gesamt.size()+1;
		    		remove();
		    	}
		    	JOptionPane.showMessageDialog(null, "Fehler, Daten konnten nicht gespeichert werden!");
		    }
		    if (status == true){
		    	JOptionPane.showMessageDialog(null, "Erfolgreich gespeichert!");
		    } 
	    	
		
	}
	
	public double getKontostandM(){ //liefert aktuellen Kontostand zurück
		double kontostand = 0;
		
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		
		for (Posten p : gesamt) {
			cal.setTime(p.getDatum());
			int comp_month = cal.get(Calendar.MONTH);
			int comp_year = cal.get(Calendar.YEAR);
			if (comp_month == month && comp_year == year) {
				kontostand += p.getBetrag();
			}
		}
		return kontostand;
	}

	private String dateToString(Date d){
		String format = "dd.MM.yyyy";
		Date date = d;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String stringDate = sdf.format(date );
		return stringDate;
	}
	
	public boolean isZahl(String s){
		try{
			double eingabe = Double.parseDouble(s);
			return true;
		}
		catch (Exception e){
			return false;
		}
	}
	
	public boolean isDate(String s){
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
	
	public void addto(List<Posten> repeat){
		for(Posten p : repeat){
			gesamt.add(p);
		}
		remove();
		JOptionPane.showMessageDialog(null, "Gespeichert!");
	}
	
	public List<Posten> repeatx (int repeat, Posten p, int dauer, int intervall){
		Calendar cal = Calendar.getInstance();
		cal.setTime(p.getDatum());
		int month = cal.get(Calendar.MONTH)+1; // Januar = 0
		int year = cal.get(Calendar.YEAR);
		
		List<Posten> periodic = new ArrayList<Posten>();
		
		while (year < dauer){
		//for (int i = year; i<=dauer; i++){
			cal.add(Calendar.MONTH,intervall);
			Date datum = cal.getTime();
			periodic.add(new Posten(datum, p.getBezeichnung(), p.getBetrag(), p.getKategorie(), 0));
			System.out.println(year = cal.get(Calendar.YEAR));
		}
		return periodic;
	}
	
	public Object[][] setupTable(List<Posten> list){
		Object[][] data = new Object[list.size()][5];
		int i = 0;
		for (Posten p : list) {
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
	public Object[][] setupTable(List<Posten> list, int position){
		Object[][] data = new Object[1][5];
		Posten p = gesamt.get(position);
			data[0][0] = new SimpleDateFormat("dd/MM/yyyy").format(p.getDatum());
			data[0][1] = p.getBezeichnung();
			data[0][2] = String.format("%.2f", p.getBetrag());
			data[0][3] = p.getKategorie();
			data[0][4] = p.getperiodkey();
		return data;
	}
	
	public List<String> getCategorys(){
		List<String> strings = new ArrayList<String>();
			
			for (Posten p : gesamt){
				String kat = p.getKategorie();
				if(strings.contains(kat)==false){
					strings.add(kat);
				}
			
			}
			Collections.sort(strings);
			return strings;
	
	}
}
	
	
	

	
