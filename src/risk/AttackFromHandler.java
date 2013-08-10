package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class AttackFromHandler extends HumanPhaseHandler {

	private final BoardState boardState;
	private final Player player;
	private final InstructionPanel instructionPanel;

	public AttackFromHandler(BoardState boardState, Player player,
			InstructionPanel instructionPanel) {
		this.boardState = boardState;
		this.player = player;
		this.instructionPanel = instructionPanel;

	}

	public void displayInterface() {
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				finishPhase(Phase.FORTIFY_SELECTION);
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
		if (boardState.getArmies(territory) < 2) {
			canAttackFrom = false;
			failMsg = "You cannot attack from a territory with less than two armies";
		}
		if (boardState.getPlayer(territory) != player) {
			canAttackFrom = false;
			failMsg = "You cannot attack from a territory you do not control";
		}
		if (canAttackFrom) {
			player.territoryAttackFrom = territory;
			finishPhase(Phase.ATTACK_TO);
		} else {
			JOptionPane.showMessageDialog(null, failMsg);
		}
	}

	@Override
	public void mouseClicked(Territory territory) {
		setAttackFromTerritory(territory);
	}

}
