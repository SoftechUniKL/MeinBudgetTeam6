import java.util.Date;

/**
 * Posten in der Budgetplanung
 */
public class Posten {
	/**
	 * Datum, wann der Posten verbucht wurde
	 */
	private Date datum;
	/**
	 * Kurze Beschreibung
	 */
	private String bezeichnung;
	/**
	 * Hoehe des Postens
	 */
	private double betrag;
	
	private String kategorie;
	
	private int periodkey;

	/**
	 * Konstruktor
	 * 
	 * @param datum
	 *            Datum, wann der Posten verbucht wurde
	 * @param bezeichnung
	 *            Kurze Beschreibung
	 * @param betrag
	 *            Hoehe des Postens
	 */
	public Posten(Date datum, String bezeichnung, double betrag, String kategorie, int periodkey) {
		this.bezeichnung = bezeichnung;
		this.datum = datum;
		this.betrag = betrag;
		this.kategorie = kategorie;
		this.periodkey = periodkey;
	}

	public Date getDatum() {
		return datum;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public double getBetrag() {
		return betrag;
	}
	
	public String getKategorie() {
		return kategorie;
	}
	
	public int getperiodkey() {
		return periodkey;
	}
	
	public void printPosten(){
		System.out.println("Datum "+datum);
		System.out.println("Bezeichnung "+bezeichnung);
		System.out.println("Betrag "+ betrag);
		System.out.println("Kategorie "+ kategorie);
		System.out.println("PeriodKey "+periodkey);
		
	}
	
	public void setBetrag(double AvgBetrag){
		this.betrag = AvgBetrag;
	}
	
	public void setKategorie(String Kategorie){
		this.kategorie = Kategorie;
	}
}
