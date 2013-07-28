package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonRightListener implements ActionListener {
	
	public MainGame game;
	private PlayerTurn turn;
	
	
	public ButtonRightListener(MainGame game) {
		this.game = game;
		this.turn = game.playerTurn;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (turn.phase.equals("placeArmies")) {
			synchronized (turn.lock) {
				turn.restartPlaceArmies = true;
				turn.lock.notifyAll();
			}
		} else if (turn.phase.equals("attackTo") || (turn.phase.equals("attack") && !turn.attackWon)) {
			synchronized (turn.lock) {
				turn.phase = "attackFrom";
				turn.lock.notifyAll();
			}
		} else if (turn.phase.equals("wonTerritory")) {
			synchronized (turn.lock) {
				turn.phase = "attackFrom";
				turn.lock.notifyAll();
			}
		} else if (turn.phase.equals("fortifySelection") || turn.phase.equals("fortify")) {
			synchronized (turn.lock) {
				turn.phase = "endTurn";
				turn.lock.notifyAll();
			}
		}
	}

}
