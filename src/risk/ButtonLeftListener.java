package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ButtonLeftListener implements ActionListener {
	
	public MainGame game;
	private Turn turn;

	


	
	public ButtonLeftListener(MainGame game) {
		this.game = game;
		this.turn = game.turn;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (turn.phase.equals("placeArmies")) {
			if (turn.player.armiesToPlace != 0) {
				JOptionPane.showMessageDialog(null, "You still have " + game.turn.player.armiesToPlace + " armies to place.");
			} else {
				synchronized (turn.lock) {
					turn.phase = "attackFrom";
					turn.lock.notifyAll();
				}
			}
		} else if (turn.phase.equals("attackFrom")) {
			if(game.turn.instructionPanel.buttonLeft.getText().length() < 10 && turn.player.territoryAttackFrom != null) { //continue vs continue without attacking
				synchronized (turn.lock) {
					turn.phase = "attackTo";
					turn.lock.notifyAll();
				}
			} else {
				synchronized (turn.lock) {
					turn.phase = "attack";
					turn.lock.notifyAll();
				}
			}
		} else if (turn.phase.equals("attackTo")) {
			synchronized (turn.lock) {
				turn.phase = "attack";
				turn.lock.notifyAll();
			}
		}
	}
}
