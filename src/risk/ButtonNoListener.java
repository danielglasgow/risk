package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonNoListener implements ActionListener {
	
	public MainGame game;
	
	public ButtonNoListener(MainGame game) {
		this.game = game;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (game.phase.equals("placeArmies")) {
			synchronized (game.lock) {
				game.restartPlaceArmies = true;
				game.lock.notifyAll();
			}
		}
		
	}

}
