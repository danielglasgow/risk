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
				synchronized (game.lock) {
					turn.phase = "attackFrom";
					game.lock.notifyAll();
				}
			}
		} else if (turn.phase.equals("attackFrom") && !turn.firstPulse) {
			if(game.turn.instructionPanel.buttonLeft.getText().length() < 10 && turn.player.territoryAttackFrom != null) { //continue vs continue without attacking
				synchronized (game.lock) {
					turn.phase = "attackTo";
					game.lock.notifyAll();
				}
			} else {
				synchronized (game.lock) {
					turn.phase = "attack";
					game.lock.notifyAll();
				}
			}
		} else if (turn.phase.equals("attackTo") && !turn.firstPulse) {
			synchronized (game.lock) {
				turn.phase = "attack";
				game.lock.notifyAll();
			}
		}
	turn.firstPulse = false;
	System.out.println("First pulse has been switched to: " + turn.firstPulse);
	}
}
