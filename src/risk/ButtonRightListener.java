package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonRightListener implements ActionListener {

	public MainGame game;
	private HumanStrategy turn;

	public ButtonRightListener(MainGame game) {
		this.game = game;
		this.turn = null; // game.playerTurn;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (turn.phase == Phase.ATTACK_TO
				|| (turn.phase == Phase.ATTACK && !turn.attackWon)) {
			synchronized (turn.lock) {
				turn.phase = Phase.ATTACK_FROM;
				turn.lock.notifyAll();
			}
		} else if (turn.phase == Phase.WON_TERRITORY) {
			synchronized (turn.lock) {
				turn.phase = Phase.ATTACK_FROM;
				turn.lock.notifyAll();
			}
		} else if (turn.phase == Phase.FORTIFY_SELECTION
				|| turn.phase == Phase.FORTIFY) {
			synchronized (turn.lock) {
				turn.phase = Phase.END_TURN;
				turn.lock.notifyAll();
			}
		}
	}

}
