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
	
	


	private JFrame hm = new JFrame();
	private JPanel pan = new JPanel();
	private JPanel pan2 = new JPanel();

	JScrollPane scroll = new JScrollPane(pan);
	GridBagLayout gb = new GridBagLayout();
	
	//Buttons
	private JButton btnEingabe;
	private JButton btnGrafik;
	private JButton btnDV;
	private JButton btnProg;
	private JButton btnSpar;
	private JButton btnExit;

	public Hauptmenu(){
		hm.setBounds(100, 100, 800, 600);
		hm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		hm.setLayout(new BorderLayout());
		hm.setVisible(true);
		pan.setLayout(gb);
		setupHM();
	}
	
	public void setupHM(){
		//Labels
		JLabel hl = headline("Hauptmenü");
		hm.add(hl, BorderLayout.NORTH);
				
		//Buttons
		schalt();
		hm.add(scroll, BorderLayout.CENTER);
		
		//Beenden
		hm.validate();
	}
	
	public void schalt(){
		btnEingabe = new JButton("Datensatz hinzufügen");
		btnEingabe.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				hm.setVisible(false); //Hauptmenü unsichtbar
				Eingabe window = new Eingabe(hm);
			}
		});
		addComp(pan,  gb,			btnEingabe,			1,			1,	2,	1);

		
		btnGrafik = new JButton("Darstellung");
		btnGrafik.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				BudgetPlanModel budget = new BudgetPlanModel(); // Modell
				BudgetPlanGUI Darstellung = new BudgetPlanGUI(budget); // View und Controller
				hm.setVisible(false); //Hauptmenü unsichtbar
			}
		});
		addComp(pan,  gb,			btnGrafik,			1,			2,	2,	1);
		
		btnDV = new JButton("Datensatz löschen");
		btnDV.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				BudgetPlanModel budget = new BudgetPlanModel();
				DatenVerwaltung ver = new DatenVerwaltung( budget, hm); // Modell
				hm.setVisible(false); //Hauptmenü unsichtbar
			}
		});
		addComp(pan,  gb,			btnDV,				1,				3,	2,	1);
		
		//PrognoseFunktion
		btnProg = new JButton("Prognosefunktion");
		btnProg.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				BudgetPlanModel budget = new BudgetPlanModel(); // Modell
				Prognosefunktion Darstellung = new Prognosefunktion(budget); // View und Controller
				hm.setVisible(false); //Hauptmenü unsichtbar
				
			}
		});
		addComp(pan,  gb,			btnProg,				1,				4,	2,	1);
		
		//SparFunktion
		btnSpar = new JButton("Sparfunktion");
		btnSpar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("Spar");
				hm.setVisible(false); //Hauptmenü unsichtbar
				Sparfunktion window = new Sparfunktion(hm);
			}
		});
		addComp(pan,  gb,			btnSpar,				1,				5,	2,	1);

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
