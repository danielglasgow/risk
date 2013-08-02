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
	public final JLabel newIndicator; // Note should not be public!
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

		buttonArea = new JPanel(new GridLayout(1, 2));
		buttonArea.add(buttonLeft);
		buttonArea.add(buttonRight);

		Font instructionFont = new Font("size14", Font.PLAIN, 14);
		instruction.setFont(instructionFont);
		instruction.setText("test");
		mainPanel.setLayout(new BorderLayout());

		newIndicator.setForeground(Color.red);
		mainPanel.add(newIndicator, BorderLayout.WEST);
		mainPanel.add(buttonArea, BorderLayout.EAST);
		mainPanel.add(instruction, BorderLayout.CENTER);
	}

	private void addStandardButtons() {
		buttonArea.add(buttonLeft);
		buttonArea.add(buttonRight);
	}

	public void removeButtons() {
		buttonArea.removeAll();
	}

	public void setText(String newIndicator, String instruction,
			String buttonLeft, String buttonRight) {
		removeButtons();
		this.newIndicator.setText(newIndicator);
		this.instruction.setText(instruction);
		this.buttonLeft.setText(buttonLeft);
		this.buttonRight.setText(buttonRight);
		addStandardButtons();
	}

	public void addCustomButtons(String newIndicator, String instruction,
			JButton leftButton, JButton rightButton) {
		removeButtons();
		buttonArea.add(leftButton);
		buttonArea.add(rightButton);
		this.newIndicator.setText(newIndicator);
		this.instruction.setText(instruction);
	}

	/**
	 * Returns the main panel of the instructions.
	 */
	public JPanel getMainPanel() {
		return mainPanel;
	}

	public void addCustomButtons(String newIndicator, String instruction,
			JButton button) {
		removeButtons();
		buttonArea.add(button);
		this.newIndicator.setText(newIndicator);
		this.instruction.setText(instruction);
	}
}
