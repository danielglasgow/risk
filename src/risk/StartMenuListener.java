package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenuListener implements ActionListener {
	
	public MainGame game;
	private int numPlayers;
	
	public StartMenuListener(int numPlayers, MainGame game) {
		this.game = game;
		this.numPlayers = numPlayers;
	}
	
	public void actionPerformed(ActionEvent event) {
		String[] colors = {"red", "blue", "green", "black", "yellow", "orange"};
		for (int i = 1; i <= numPlayers; i++) {
			Player player = new Player("Player" + i, colors[i-1], game);
			game.players.add(player);
		}
		synchronized (game.lock) {
			game.lock.notifyAll();
		}
		
		
	}

}
