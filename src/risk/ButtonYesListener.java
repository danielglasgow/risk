package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonYesListener implements ActionListener {
	
	public MainGame game;
	private Turn turn;
	
	public ButtonYesListener(MainGame game) {
		this.game = game;
		this.turn = game.turn;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (turn.phase.equals("placeArmies") && turn.player.armiesToPlace == 0) {
			synchronized (game.lock) {
				turn.phase = "attackTo";
				game.lock.notifyAll();
			}
		}
		if (turn.phase.equals("attackTo")) {
			synchronized (game.lock) {
				turn.nextPhase = true;
				game.lock.notifyAll();
			}
		}
	}

}
