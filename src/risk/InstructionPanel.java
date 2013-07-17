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
	public JLabel instruction;
	public JLabel newIndicator;
	public JButton buttonNO;
	public JButton buttonYES;
	public MainGame game;
	
	public final String newVisible = "			NEW			";
	public final String newInvisible = "									";
				

	public InstructionPanel(MainGame game) {
		mainPanel = new JPanel();
		instruction = new JLabel();
		newIndicator = new JLabel();
		buttonNO = new JButton();
		buttonYES = new JButton();
		
		buttonNO.addActionListener(new ButtonNoListener(game));
		
		
		JPanel buttonArea = new JPanel(new GridLayout(1,2));
		buttonArea.add(buttonYES);
		buttonArea.add(buttonNO);
		
		Font instructionFont = new Font("size14", Font.PLAIN, 14);
		
		
		
		instruction.setFont(instructionFont);
		mainPanel.setLayout(new BorderLayout());
	
		
		newIndicator.setForeground(Color.red);
	
		mainPanel.add(newIndicator, BorderLayout.WEST);
		
		mainPanel.add(buttonArea, BorderLayout.EAST);
		mainPanel.add(instruction, BorderLayout.CENTER);
	}
	
	public void setText(String newIndicator, String instruction, String buttonYES, String buttonNO) {
		this.newIndicator.setText(newIndicator);
		this.instruction.setText(instruction);
		this.buttonYES.setText(buttonYES);
		this.buttonNO.setText(buttonNO);
		
		
	}
	
	public void setText(String instruction) {
		this.instruction.setText(instruction);
		this.newIndicator.setText(newVisible);
	}

}
