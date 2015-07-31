import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Hauptmenu extends Menu {
	
	

	private JLabel kontostand, lbl_kontostand;
	private JFrame hm = new JFrame();
	private JPanel pan = new JPanel();
	private JPanel center = new JPanel();
	private JPanel pan2 = new JPanel();

	JScrollPane scroll = new JScrollPane(pan);
	GridBagLayout gb = new GridBagLayout();
	BudgetPlanModel budget = new BudgetPlanModel();
	
	//Buttons
	private JButton btnEingabe;
	private JButton btnAusgabe;
	private JButton btnGrafik;
	private JButton btnDV;
	private JButton btnProg;
	private JButton btnSpar;
	private JButton btnExit;

	
	
	public Hauptmenu(){
		createClientPanel(hm,"Hauptmenü");
		pan.setLayout(gb);
		setupHM();
	}
	
	public void revisible(){
		hm.setVisible(true);
		updateKontostand();
		
	}
	
	public void updateKontostand(){
		budget.refresh();
		kontostand.setText(Double.toString(budget.getKontostandM()));
	}
	
	public void setupHM(){
		//Buttons
		
		kontostand = lb_konto(Double.toString(budget.getKontostandM())+"€");
		lbl_kontostand = nline("Kontostand aktueller Monat:");
		center.setLayout(new BorderLayout());
		JPanel pan3 = new JPanel();
		pan3.add(lbl_kontostand);
		pan3.add(kontostand);
		center.add(pan3, BorderLayout.NORTH);
		center.add(scroll, BorderLayout.CENTER);
					
		schalt();
		hm.add(center, BorderLayout.CENTER);
		hm.validate();
	}
	
	public void schalt(){
		btnEingabe = new JButton("Einnahmen hinzufügen");
		btnEingabe.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				hm.setVisible(false); //Hauptmenü unsichtbar
				Datenerfassung d_erf = new Datenerfassung(1);
			}
		});
		addComp(pan,  gb,			btnEingabe,			1,			1,	2,	1);
		
		btnAusgabe = new JButton("Ausgaben hinzufügen");
		btnAusgabe.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				hm.setVisible(false); //Hauptmenü unsichtbar
				Datenerfassung d_erf = new Datenerfassung(2);
			}
		});
		addComp(pan,  gb,			btnAusgabe,			1,			2,	2,	1);
		

		
		btnGrafik = new JButton("Darstellung");
		btnGrafik.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				BudgetPlanModel budget = new BudgetPlanModel(); // Modell
				BudgetPlanGUI Darstellung = new BudgetPlanGUI(budget); // View und Controller
				hm.setVisible(false); //Hauptmenü unsichtbar
			}
		});
		addComp(pan,  gb,			btnGrafik,			1,			3,	2,	1);
		
		btnDV = new JButton("Datensatz löschen");
		btnDV.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				BudgetPlanModel budget = new BudgetPlanModel();
				DatenVerwaltung ver = new DatenVerwaltung( budget); // Modell
				hm.setVisible(false); //Hauptmenü unsichtbar
			}
		});
		addComp(pan,  gb,			btnDV,				1,				4,	2,	1);
		
		//PrognoseFunktion
		btnProg = new JButton("Prognosefunktion");
		btnProg.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				BudgetPlanModel budget = new BudgetPlanModel(); // Modell
				Prognose Darstellung = new Prognose(); // View und Controller
				hm.setVisible(false); //Hauptmenü unsichtbar
				
			}
		});
		addComp(pan,  gb,			btnProg,				1,				5,	2,	1);
		
		//SparFunktion
		btnSpar = new JButton("Sparfunktion");
		btnSpar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("Spar");
				hm.setVisible(false); //Hauptmenü unsichtbar
				Sparfunktion window = new Sparfunktion(hm);
			}
		});
		addComp(pan,  gb,			btnSpar,				1,				6,	2,	1);

		JButton btnExit = new JButton("Beenden");
		btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
			System.exit(0);
			}
		});
		
		pan2.add(btnExit);
		hm.add(pan2, BorderLayout.SOUTH);
		
	}
	


	
}
