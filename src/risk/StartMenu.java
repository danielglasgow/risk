package risk;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class StartMenu {
	
	public MainGame game;
	
	public StartMenu(MainGame game) {
		this.game = game;
		chooseNumberPlayers();
	}

	private void startMenu(JFrame startMenu) {
		startMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startMenu.setLayout(new FlowLayout());
		JLabel prompt = new JLabel();
		prompt.setText("Choose number of players:");
		startMenu.add(prompt);
		for (int i = 2; i <=6; i++) {
			JButton button = new JButton();
			button.addActionListener(new StartMenuListener(i, game));
			button.setText("" + i);
			startMenu.add(button);
		}
		startMenu.pack();
		startMenu.setVisible(true);	
	}
	
	private void chooseNumberPlayers() {
		JFrame startMenuFrame = new JFrame();
		startMenu(startMenuFrame);
		synchronized (game.lock) {
			while(game.players.size() == 0) {
				try {
					game.lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		startMenuFrame.dispose();
	}

}
