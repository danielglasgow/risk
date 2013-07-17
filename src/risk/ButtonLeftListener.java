package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonLeftListener implements ActionListener {
	
	public MainGame game;
	private Turn turn;
	private Boolean firstPulse = true;

	
	public ButtonLeftListener(MainGame game) {
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
			if (!firstPulse) {
				synchronized (game.lock) {
					turn.phase = "attackFrom";
					game.lock.notifyAll();
				}
			}
			firstPulse = false;
		}
	}

}
