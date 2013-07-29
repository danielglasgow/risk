package risk;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class StartMenu {
	
	public final MainGame game;
	public final JFrame startMenuFrame;
	
	public StartMenu(MainGame game) {
		this.game = game;
		startMenuFrame = new JFrame();
		startMenu(startMenuFrame);
	}

	private void startMenu(JFrame startMenu) {
		startMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startMenu.setLayout(new FlowLayout());
		JLabel prompt = new JLabel();
		prompt.setText("Choose number of players:");
		startMenu.add(prompt);
		for (int i = 2; i <= 6; i++) {
			JButton button = new JButton();
			button.addActionListener(new StartMenuListener(i, game, this));
			button.setText("" + i);
			startMenu.add(button);
		}
		startMenu.pack();
		startMenu.setVisible(true);	
	}
}
