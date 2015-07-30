import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;




public class Prognose extends Menu {
	
	private JFrame hm = new JFrame();
	private JPanel pan = new JPanel();
	private JPanel pan2 = new JPanel();
	
	private Object[] line,line2;
	private DefaultTableModel dtmIn, dtmOut; // Table
	private JTable tableIn, tableOut;
	
	private JScrollPane scrollpane, scrollpane2;


	JScrollPane scroll = new JScrollPane(pan);
	GridBagLayout gb = new GridBagLayout();
	
	BudgetPlanModel budget = new BudgetPlanModel();
	List<Posten> categoryListIn, categoryListOut;
	
	//Hilfsklasse
	class Vergleich{
		String kategorie;
		double betrag;
		double dw;
		int anzahl;
		
		Vergleich(String kategorie, double betrag, int anzahl, double dw){
			this.kategorie = kategorie;
			this.betrag = betrag;
			this.anzahl = anzahl;
			this.dw = dw;
		}
		
		private void setBetrag(double add){
			this.betrag+=add;
		}
		
		private void incAnzahl(){
			anzahl++;
		}
		
		private void bildeDw(){
			if(anzahl == 0){dw=0;} else{
			dw = betrag / anzahl;
			}
		}
		
		private double getMw(){
			return dw;
		}
		
		private String getKat(){
			return kategorie;
		}
		
	}
	
	//Konstruktor
	Prognose(){
		hm = new JFrame();
		createClientPanel(hm,"Prognosefunktion");
		pan.setLayout(gb);
		EinAus();
		setup();
	}
	
	//Setup
	public void setup(){
		line2 = new Object[] { "Kategorie", "Betrag"};
		dtmIn = new DefaultTableModel(setupTable1(mwBilden()), line2);
		dtmOut = new DefaultTableModel(setupTable1(mwBildenNeg()), line2);
		tableIn = new JTable(dtmIn);
		tableOut = new JTable(dtmOut);
		scrollpane = new JScrollPane(tableIn);
		scrollpane2 = new JScrollPane(tableOut);
	
	
		JPanel tablePanel = new JPanel(new GridLayout(1, 2));
		tablePanel.add(scrollpane);
		tablePanel.add(scrollpane2);
		
		hm.add(tablePanel, BorderLayout.CENTER);
		
		JButton btnAbbruch = new JButton ("Zurück ins Hauptmenü!");
		btnAbbruch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				backHM();
			}
		});
		
		pan = new JPanel();
		pan.add(btnAbbruch);
		hm.add(pan, BorderLayout.SOUTH);
	}
	
	private void backHM() {
		hm_ref.revisible();
		hm.dispose();
	}
	
	//ab hier Methoden
	public Object[][] setupTable1(List<Vergleich> t) {
		List<Vergleich> listToUse= t;
		
		int i = 0;
		Object[][] data = new Object[listToUse.size()][2];
		for (Vergleich v : listToUse) {
			data[i][0] =v.getKat();
			data[i][1] = v.getMw();
			i++;
		}
		return data;
	}
	
	public void EinAus(){
		categoryListIn = new ArrayList<Posten>();
		categoryListOut = new ArrayList<Posten>();

		for (Posten p : budget.gesamt) { 
			if (p.getBetrag() >= 0){
				categoryListIn.add(p);
			}
			else
				categoryListOut.add(p);
		}
	}
	
	public List<Vergleich> mwBilden(){
		List<String> kats = budget.getCategorys();
		List<Vergleich> elem = new ArrayList<Vergleich>();
		
		for (String s : kats){
			elem.add(new Vergleich(s,0.0,0,0.0));
		}
	
		
		for (Posten p : categoryListIn){
			for(Vergleich v : elem){
				if(v.getKat().equals(p.getKategorie()) ){
					v.setBetrag(p.getBetrag());
					v.incAnzahl();
					v.bildeDw();
				}
			}
		}
		
		return elem;
		
	}
	
	public List<Vergleich> mwBildenNeg(){
		List<String> kats = budget.getCategorys();
		List<Vergleich> elem = new ArrayList<Vergleich>();
		
		for (String s : kats){
			elem.add(new Vergleich(s,0.0,0,0.0));
		}
	
		
		for (Posten p : categoryListOut){
			for(Vergleich v : elem){
				if(v.getKat().equals(p.getKategorie()) ){
					v.setBetrag(p.getBetrag());
					v.incAnzahl();
					v.bildeDw();
				}
			}
		}
		
		return elem;
		
	}
	

}
