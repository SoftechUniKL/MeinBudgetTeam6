import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

	
public class Menu {

	GridBagConstraints gbc = new GridBagConstraints();

	public Menu(){
	}
	
	
	protected JLabel headline(String s){
		JLabel headline = new JLabel(s);
		headline.setFont(new Font("Arial", Font.BOLD, 45));
		headline.setHorizontalAlignment(JLabel.LEFT);
		return headline;
	}
	
	protected void addComp(JPanel form, GridBagLayout gb, JComponent c,  int x, int y, int width, int height){
		gbc.gridx=x;
		gbc.gridy=y;
		gbc.gridwidth=width;
		gbc.gridheight=height;
		gbc.insets=new Insets(0,0,10,0);
		form.add(c, gbc);
	}
}
