package risk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class provides an interface for interaction between a human player and
 * the risk game. The instruction panel appears at the bottom of the risk board,
 * with a line of text prompting the user about what he must do, and one or more
 * buttons for the user to input his decision.
 */

public class InstructionPanel {
    private final JPanel mainPanel;
    private final JLabel instruction;
    private final JLabel newIndicator;
    private final JButton buttonRight;
    private final JButton buttonLeft;
    private final JPanel buttonArea;

    public static final String NEW_VISIBLE = "          NEW         ";
    public static final String NEW_INVISIBLE = "                                    ";

    public InstructionPanel() {
        mainPanel = new JPanel();
        instruction = new JLabel();
        newIndicator = new JLabel();
        buttonRight = new JButton();
        buttonLeft = new JButton();

        buttonArea = new JPanel(new GridLayout(1, 5));
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

    public void setText(String newIndicator, String instruction, String buttonLeft,
            String buttonRight) {
        removeButtons();
        this.newIndicator.setText(newIndicator);
        this.instruction.setText(instruction);
        this.buttonLeft.setText(buttonLeft);
        this.buttonRight.setText(buttonRight);
        addStandardButtons();
    }

    public void addCustomButtons(String newIndicator, String instruction, JButton leftButton,
            JButton rightButton) {
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

    public void addCustomButtons(String newIndicator, String instruction, JButton button) {
        removeButtons();
        buttonArea.add(button);
        this.newIndicator.setText(newIndicator);
        this.instruction.setText(instruction);
    }

    public void addCustomButtons(String newIndicator, String instruction, JButton button1,
            JButton button2, JButton button3, JButton button4, JButton button5) {
        removeButtons();
        buttonArea.add(button1);
        buttonArea.add(button2);
        buttonArea.add(button3);
        buttonArea.add(button4);
        buttonArea.add(button5);
        this.newIndicator.setText(newIndicator);
        this.instruction.setText(instruction);
    }

    public JLabel getNewIndicator() {
        return newIndicator;
    }
}
