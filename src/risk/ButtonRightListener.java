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
			synchronized (game.lock) {
				turn.restartPlaceArmies = true;
				game.lock.notifyAll();
			}
		}
		if(turn.phase.equals("attackTo")) {
			synchronized (game.lock) {
				turn.phase = "placeArmies";
				turn.restartPlaceArmies = true;
				game.lock.notifyAll();
			}
		}
	}

}
