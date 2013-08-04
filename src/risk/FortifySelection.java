package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class FortifySelection extends PhaseHandler {

	private Player player;
	private InstructionPanel instructionPanel;
	private MainGame game;

	public FortifySelection(MainGame game, Player player,
			InstructionPanel instructionPanel) {
		this.game = game;
		this.player = player;
		this.instructionPanel = instructionPanel;
	}

	@Override
	public void mouseClicked(Territory territory) {
		if (!territory.player.equals(player)) {
			JOptionPane.showMessageDialog(null,
					"You must fortify between to territories you control");
		} else {
			if (player.fortify2 == null && player.fortify1 != null) {
				if (hasPath(player.fortify1, territory)) {
					player.fortify2 = territory;
				} else {
					player.fortify1 = null;
					JOptionPane
							.showMessageDialog(
									null,
									"There must be a contiguous path of territories you control in order to fortify between two territories");
				}
			} else {
				player.fortify1 = territory;
				player.fortify2 = null;
			}
		}
		finishPhase(Phase.FORTIFY_SELECTION);
	}

	private boolean hasPath(Territory startTerritory, Territory endTerritory) {
		Set<Territory> territories = new HashSet<Territory>();
		territories.addAll(game.territories);

		Set<Territory> nextSet = adjacentControlledTerritories(startTerritory,
				territories);
		Set<Territory> currentSet = new HashSet<Territory>();
		territories.remove(startTerritory);
		while (!(currentSet.contains(endTerritory))) {
			System.out.println(nextSet.size());
			if (nextSet.size() < 1) {
				System.out.println("entered");
				return false;
			}
			territories.removeAll(nextSet);
			currentSet.addAll(nextSet);
			nextSet.clear();
			System.out.println(nextSet);
			for (Territory t : currentSet) {
				nextSet.addAll(adjacentControlledTerritories(t, territories));
			}
		}
		return true;
	}

	private Set<Territory> adjacentControlledTerritories(Territory territory,
			Set<Territory> territories) {
		Set<Territory> adjacentControlled = new HashSet<Territory>();
		for (Territory t : territory.adjacents) {
			if (t.player.equals(player) && territories.contains(t)) {
				adjacentControlled.add(t);
			}
		}
		return adjacentControlled;
	}

	@Override
	public void displayInterface() {
		JButton buttonRight = new JButton();
		JButton buttonLeft = new JButton();
		buttonRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				finishPhase(Phase.END_TURN);
			}
		});
		buttonLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				finishPhase(Phase.FORTIFY);

			}
		});

		buttonLeft.setText("Continue");
		buttonRight.setText("EndTurn");

		instructionPanel.addCustomButtons(InstructionPanel.newVisible,
				"Click on two territories to fortify", buttonLeft, buttonRight);
		String fort2 = "...";
		if (player.fortify2 != null) {
			fort2 = player.fortify2.name
					+ " (click continue or select territories again)";
		}
		if (player.fortify1 == null) {
			instructionPanel.addCustomButtons(InstructionPanel.newInvisible,
					"Click on two territories to fortify", buttonLeft,
					buttonRight);
		} else {
			instructionPanel.addCustomButtons(InstructionPanel.newInvisible,
					"Fortify from " + player.fortify1.name + " to " + fort2,
					buttonLeft, buttonRight);
		}

	}

}
