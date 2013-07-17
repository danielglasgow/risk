package risk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InstructionPanel {
	
	public JPanel mainPanel;
	public JLabel textArea;
	public JLabel placeHolder;
	public JButton buttonNO;
	public JButton buttonYES;
	
	public MainGame game;
				

	public InstructionPanel(MainGame game) {
		mainPanel = new JPanel();
		textArea = new JLabel();
		placeHolder = new JLabel();
		buttonNO = new JButton();
		buttonYES = new JButton();
		
		
		JPanel buttonArea = new JPanel(new GridLayout(1,2));
		buttonArea.add(buttonYES);
		buttonArea.add(buttonNO);
		
		Font instructionFont = new Font("size14", Font.PLAIN, 14);
		//Font redFont = new Font("red", Font.BOLD, 14);
		
		
		textArea.setFont(instructionFont);
		mainPanel.setLayout(new BorderLayout());
	
		
		placeHolder.setForeground(Color.red);
	
		mainPanel.add(placeHolder, BorderLayout.WEST);
		
		mainPanel.add(buttonArea, BorderLayout.EAST);
		mainPanel.add(textArea, BorderLayout.CENTER);
	}

}
