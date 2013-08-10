package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class AttackToHandler extends HumanPhaseHandler {
	
	private final BoardState boardState;
	private final Player player;
	private final InstructionPanel instructionPanel;

	public AttackToHandler(BoardState boardState, Player player, InstructionPanel instructionPanel) {
		this.boardState = boardState;
		this.player = player;
		this.instructionPanel = instructionPanel;
	}

	public void displayInterface() {
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				finishPhase(Phase.ATTACK_FROM);
			}
		});
		button.setText("Choose a Different Territory to Attack From");
		instructionPanel.addCustomButtons(InstructionPanel.newVisible,
				"Attacking from " + player.territoryAttackFrom.name
						+ ".  Select the territory you would like to attack.",
				button);
	}

	public void setAttackToTerritory(Territory territory) {
		if (!player.territoryAttackFrom.adjacents.contains(territory)) {
			JOptionPane.showMessageDialog(null,
					"You must attack a territory adjacent to "
							+ player.territoryAttackFrom.name);
		} else if (boardState.getPlayer(territory).equals(player)) {
			JOptionPane.showMessageDialog(null,
					"You cannot attack a territory you control");
		} else {
			player.territoryAttackTo = territory;
			finishPhase(Phase.ATTACK);
		}
	}

	@Override
	public void mouseClicked(Territory territory) {
		setAttackToTerritory(territory);
	}

}
