package risk;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InstructionPanel {
	
	public JPanel mainPanel;
	public JLabel textArea;

	public InstructionPanel() {
		mainPanel = new JPanel();
		textArea = new JLabel();
		
		Font font = new Font("Verdana", Font.BOLD, 12);
		textArea.setFont(font);
		textArea.setText("BLAH BLAH BLAH");
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(new JButton("cancel"), BorderLayout.EAST);
		mainPanel.add(textArea, BorderLayout.CENTER);
	}

}
