package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenuListener implements ActionListener {
	
	public MainGame game;
	private int numPlayers;
	private StartMenu startMenu;
	
	public StartMenuListener(int numPlayers, MainGame game, StartMenu startMenu) {
		this.game = game;
		this.numPlayers = numPlayers;
		this.startMenu = startMenu;
	}
	
	public void actionPerformed(ActionEvent event) {
		String[] colors = {"red", "blue", "green", "black", "yellow", "orange"};
		for (int i = 1; i <= numPlayers; i++) {
			Player player = new Player("Player" + i, colors[i-1], game);
			MainGame.players.add(player);
		}
		
		startMenu.startMenuFrame.dispose();
		game.wait = false;
		synchronized (game.startMenuLock) {
			game.startMenuLock.notifyAll();
		}
		
		
	}

}
