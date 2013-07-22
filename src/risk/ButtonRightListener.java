package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonRightListener implements ActionListener {
	
	public MainGame game;
	private Turn turn;
	
	
	public ButtonRightListener(MainGame game) {
		this.game = game;
		this.turn = game.turn;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (turn.phase.equals("placeArmies")) {
			synchronized (turn.lock) {
				turn.restartPlaceArmies = true;
				turn.lock.notifyAll();
			}
		}
		if(turn.phase.equals("attackFrom")) {
			synchronized (turn.lock) {
				turn.phase = "placeArmies";
				turn.restartPlaceArmies = true;
				turn.lock.notifyAll();
			}
		}
		if (turn.phase.equals("attackTo")) {
			synchronized (turn.lock) {
				turn.phase = "attackFrom";
				turn.lock.notifyAll();
			}
		}
	}

}
