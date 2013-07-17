package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonYesListener implements ActionListener {
	
	public MainGame game;
	
	public ButtonYesListener(MainGame game) {
		this.game = game;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (game.phase.equals("placeArmies")) {
			synchronized (game.lock) {
				game.nextPhase = true;
				game.lock.notifyAll();
			}
		}
		if (game.phase.equals("attackTo")) {
			synchronized (game.lock) {
				game.nextPhase = true;
				game.lock.notifyAll();
			}
		}
	}

}
