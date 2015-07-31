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
	
	private long id;

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
	public Posten(Date datum, String bezeichnung, double betrag, String kategorie, long id) {
		this.bezeichnung = bezeichnung;
		this.datum = datum;
		this.betrag = betrag;
		this.kategorie = kategorie;
		this.id = id;
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
	
	public long getID() {
		return id;
	}
	
	public void printPosten(){
		System.out.println("Datum "+datum);
		System.out.println("Bezeichnung "+bezeichnung);
		System.out.println("Betrag "+ betrag);
		System.out.println("Kategorie "+ kategorie);
		System.out.println("PeriodKey "+id);
		
	}
	
	public void setBetrag(double AvgBetrag){
		this.betrag = AvgBetrag;
	}
	
	public void setKategorie(String Kategorie){
		this.kategorie = Kategorie;
	}
}
