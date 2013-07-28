package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ButtonLeftListener implements ActionListener {
	
	public MainGame game;
	private PlayerTurn turn;

	


	
	public ButtonLeftListener(MainGame game) {
		this.game = game;
		this.turn = game.playerTurn;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (turn.phase.equals("placeArmies")) {
			if (turn.player.armiesToPlace != 0) {
				JOptionPane.showMessageDialog(null, "You still have " + game.playerTurn.player.armiesToPlace + " armies to place.");
			} else {
				synchronized (turn.lock) {
					turn.phase = "attackFrom";
					turn.lock.notifyAll();
				}
			}
		} else if (turn.phase.equals("attackFrom")) {
				synchronized (turn.lock) {
					if (turn.player.territoryAttackFrom != null) {
						turn.phase = "attackTo";
					} else {
						turn.phase = "fortifySelection";
					}
					turn.lock.notifyAll();
			}
		} else if (turn.phase.equals("attack")) {
			synchronized (turn.lock) {
				if (turn.attackWon) {
					if (turn.player.territoryAttackFrom.armies > 1) {
						turn.phase = "wonTerritory";
					} else {
						turn.phase = "attackFrom";
					}
				}
				turn.lock.notifyAll();
			}
		} else if(turn.phase.equals("wonTerritory")) {
			synchronized (turn.lock) {
				int armies = --turn.player.territoryAttackFrom.armies;
				turn.player.territoryAttackTo.armies += armies;
				turn.player.territoryAttackFrom.armies = 1;
				turn.phase = "attackFrom";
				game.board.updateBackground();
				turn.lock.notifyAll();
			}
		} else if (turn.phase.equals("fortifySelection")) {
			synchronized (turn.lock) {
				if (turn.player.fortify1 != null && turn.player.fortify2 != null) {
					turn.phase = "fortify";
					turn.lock.notifyAll();
				} else {
					JOptionPane.showMessageDialog(null, "You must select two territories to fortify from first");
				}
			}
		}
	}
}
