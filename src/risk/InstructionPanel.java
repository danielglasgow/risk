package risk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InstructionPanel {
	private final JPanel mainPanel;
	private final JLabel instruction;
	public final JLabel newIndicator;  // Note should not be public!
	private final JButton buttonRight;
	private final JButton buttonLeft;
	private final JPanel buttonArea;
	
	public static final String newVisible = "			NEW			";
	public static final String newInvisible = "									";
				
	public InstructionPanel(MainGame game) {
		mainPanel = new JPanel();
		instruction = new JLabel();
		newIndicator = new JLabel();
		buttonRight = new JButton();
		buttonLeft = new JButton();
		
		buttonRight.addActionListener(new ButtonRightListener(game));
		buttonLeft.addActionListener(new ButtonLeftListener(game));
		
		//failed two vs one button interface...
		buttonArea = new JPanel(new GridLayout(1,2));
		buttonArea.add(this.buttonLeft);
		buttonArea.add(this.buttonRight);
		
		Font instructionFont = new Font("size14", Font.PLAIN, 14);
		
		instruction.setFont(instructionFont);
		mainPanel.setLayout(new BorderLayout());
	
		newIndicator.setForeground(Color.red);
	
		mainPanel.add(newIndicator, BorderLayout.WEST);
		mainPanel.add(buttonArea, BorderLayout.EAST);
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
	
	/**
	 * Returns the main panel of the instructions.
	 */
	public JPanel getMainPanel() {
		return mainPanel;
	}
}
