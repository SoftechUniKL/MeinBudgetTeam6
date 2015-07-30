import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

	
public abstract class Menu {
	protected static Hauptmenu hm_ref;

	GridBagConstraints gbc = new GridBagConstraints();

	protected static void createClientPanel(final JFrame frame, String s){
       	frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);
		frame.add(headline(s), BorderLayout.NORTH);
    }

	protected static JLabel headline(String s){
		JLabel headline = new JLabel(s);
		headline.setFont(new Font("Arial", Font.BOLD, 45));
		headline.setHorizontalAlignment(JLabel.LEFT);
		return headline;
	}
	
	protected static JLabel lb_konto(String s){
		JLabel konto = new JLabel(s);
		konto.setFont(new Font("Arial", Font.BOLD, 28));
		konto.setHorizontalAlignment(JLabel.CENTER);
		return konto;
	}
	
	protected static JLabel notice(String s){
		JLabel notice = new JLabel(s);
		notice.setFont(new Font("Arial", Font.ITALIC, 10));
		return notice;
	}
	
	protected static JLabel nline(String s){
		JLabel nline = new JLabel(s);
		nline.setFont(new Font("Arial", Font.BOLD, 16));
		nline.setHorizontalAlignment(JLabel.RIGHT);
		return nline;
	}
	
	protected void addComp(JPanel form, GridBagLayout gb, JComponent c,  int x, int y, int width, int height){
		gbc.gridx=x;
		gbc.gridy=y;
		gbc.gridwidth=width;
		gbc.gridheight=height;
		gbc.insets=new Insets(0,0,10,0);
		form.add(c, gbc);
	}
	protected void addComp(JPanel form, GridBagLayout gb, JComponent c,  int x, int y, int width, int height, int anchor){
		gbc.gridx=x;
		gbc.gridy=y;
		gbc.gridwidth=width;
		gbc.gridheight=height;
		gbc.insets=new Insets(0,0,10,0);
		gbc.anchor=anchor;
		form.add(c, gbc);
	}
}
