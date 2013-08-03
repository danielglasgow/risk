package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class AttackTo extends PhaseHandler {

	private final Player player;
	private final InstructionPanel instructionPanel;

	public AttackTo(Player player, InstructionPanel instructionPanel) {
		this.player = player;
		this.instructionPanel = instructionPanel;
	}

	public void setInterface() {
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				nextPhase = Phase.ATTACK_FROM;
				latch.countDown();
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
		} else if (territory.player.equals(player)) {
			JOptionPane.showMessageDialog(null,
					"You cannot attack a territory you control");
		} else {
			player.territoryAttackTo = territory;
			nextPhase = Phase.ATTACK;
			latch.countDown();
		}
	}

	@Override
	public void action(Territory territory) {
		setAttackToTerritory(territory);
	}

}
