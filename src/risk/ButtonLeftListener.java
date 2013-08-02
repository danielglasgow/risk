package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ButtonLeftListener implements ActionListener {

	public MainGame game;
	private HumanStrategy turn;

	public ButtonLeftListener(MainGame game) {
		this.game = game;
		this.turn = null;// game.playerTurn;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (turn.phase == Phase.ATTACK_FROM) {
			synchronized (turn.lock) {
				if (turn.player.territoryAttackFrom != null) {
					turn.phase = Phase.ATTACK_TO;
				} else {
					turn.phase = Phase.FORTIFY_SELECTION;
				}
				turn.lock.notifyAll();
			}
		} else if (turn.phase == Phase.ATTACK) {
			synchronized (turn.lock) {
				if (turn.attackWon) {
					if (turn.player.territoryAttackFrom.armies > 1) {
						turn.phase = Phase.WON_TERRITORY;
					} else {
						turn.phase = Phase.ATTACK_FROM;
					}
				}
				turn.lock.notifyAll();
			}
		} else if (turn.phase == Phase.WON_TERRITORY) {
			synchronized (turn.lock) {
				int armies = --turn.player.territoryAttackFrom.armies;
				turn.player.territoryAttackTo.armies += armies;
				turn.player.territoryAttackFrom.armies = 1;
				turn.phase = Phase.ATTACK_FROM;
				game.board.updateBackground();
				turn.lock.notifyAll();
			}
		} else if (turn.phase == Phase.FORTIFY_SELECTION) {
			synchronized (turn.lock) {
				if (turn.player.fortify1 != null
						&& turn.player.fortify2 != null) {
					turn.phase = Phase.FORTIFY;
					turn.lock.notifyAll();
				} else {
					JOptionPane
							.showMessageDialog(null,
									"You must select two territories to fortify from first");
				}
			}
		}
	}
}
