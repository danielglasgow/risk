package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class AttackFrom extends PhaseHandler {

	private final Player player;
	private final InstructionPanel instructionPanel;

	public AttackFrom(Player player, InstructionPanel instructionPanel) {
		this.player = player;
		this.instructionPanel = instructionPanel;

	}

	public void setInterface() {
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				nextPhase = Phase.FORTIFY_SELECTION;
			}
		});
		button.setText("End Attack Phase");
		player.territoryAttackFrom = null;
		instructionPanel.addCustomButtons(InstructionPanel.newVisible,
				"Select the territory you would like to attack from", button);
	}

	private void setAttackFromTerritory(Territory territory) {
		boolean canAttackFrom = true;
		String failMsg = "";
		if (territory.armies < 2) {
			canAttackFrom = false;
			failMsg = "You cannot attack from a territory with less than two armies";
		}
		if (territory.player != player) {
			canAttackFrom = false;
			failMsg = "You cannot attack from a territory you do not control";
		}
		if (canAttackFrom) {
			player.territoryAttackFrom = territory;
			nextPhase = Phase.ATTACK_TO;
			latch.countDown();
		} else {
			JOptionPane.showMessageDialog(null, failMsg);
		}
	}

	@Override
	public void action(Territory territory) {
		setAttackFromTerritory(territory);
	}

}
