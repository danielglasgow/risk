package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * TODO(dani): Needs Java doc. What is an "AttackTo"? TODO(dani): Object names
 * must be nouns.
 */
public class AttackToHandler extends HumanPhaseHandler {

	private final BoardState boardState;
	private final Player player;
	private final InstructionPanel instructionPanel;

	public AttackToHandler(BoardState boardState, Player player,
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
				finishPhase(HumanTurnPhases.ATTACK_FROM);
			}
		});
		button.setText("Choose a Different Territory to Attack From");
		instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE,
				"Attacking from " + boardState.getAttackFrom().name
						+ ".  Select the territory you would like to attack.",
				button);
	}

	public void setAttackToTerritory(Territory territory) {
		if (!boardState.getAttackFrom().getAdjacents().contains(territory)) {
			JOptionPane.showMessageDialog(
					null,
					"You must attack a territory adjacent to "
							+ boardState.getAttackFrom().name);
		} else if (boardState.getPlayer(territory).equals(player)) {
			JOptionPane.showMessageDialog(null,
					"You cannot attack a territory you control");
		} else {
			boardState.setAttackTo(territory);
			finishPhase(HumanTurnPhases.ATTACK);
		}
	}

	@Override
	public void mouseClicked(Territory territory) {
		setAttackToTerritory(territory);
	}

}
