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
	public JButton buttonRight;
	public JButton buttonLeft;
	public MainGame game;
	public JPanel buttonArea2;
	public JPanel buttonArea1;
	
	public final String newVisible = "			NEW			";
	public final String newInvisible = "									";
				

	public InstructionPanel(MainGame game) {
		mainPanel = new JPanel();
		instruction = new JLabel();
		newIndicator = new JLabel();
		buttonRight = new JButton();
		buttonLeft = new JButton();
		
		buttonRight.addActionListener(new ButtonRightListener(game));
		buttonLeft.addActionListener(new ButtonLeftListener(game));
		
		//failed two vs one button interface...
		buttonArea2 = new JPanel(new GridLayout(1,2));
		buttonArea2.add(this.buttonLeft);
		buttonArea2.add(this.buttonRight);
		
		buttonArea1 = new JPanel(new GridLayout(1,1));

		
		Font instructionFont = new Font("size14", Font.PLAIN, 14);
		
		
		
		instruction.setFont(instructionFont);
		mainPanel.setLayout(new BorderLayout());
	
		
		newIndicator.setForeground(Color.red);
	
		mainPanel.add(newIndicator, BorderLayout.WEST);
		
		mainPanel.add(buttonArea2, BorderLayout.EAST);
		mainPanel.add(instruction, BorderLayout.CENTER);
	}
	
	public void setText(String newIndicator, String instruction, String buttonLeft, String buttonRight) {
		//buttonArea2.add(this.buttonLeft);
		//buttonArea2.add(this.buttonRight);
		//mainPanel.add(buttonArea2, BorderLayout.EAST);
		this.newIndicator.setText(newIndicator);
		this.instruction.setText(instruction);
		this.buttonLeft.setText(buttonLeft);
		this.buttonRight.setText(buttonRight);
		
		
	}
	
	public void setText(String newIndicator, String instruction, String buttonLeft) {
		//buttonArea1.add(this.buttonLeft);
		//mainPanel.add(buttonArea1, BorderLayout.EAST);
		this.newIndicator.setText(newIndicator);
		this.instruction.setText(instruction);
		this.buttonLeft.setText(buttonLeft);
		
	}

}
