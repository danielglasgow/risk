package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class FortifySelectionHandler extends HumanPhaseHandler {

	private final BoardState boardState;
	private final Player player;
	private final InstructionPanel instructionPanel;

	public FortifySelectionHandler(BoardState boardState, Player player,
			InstructionPanel instructionPanel) {
		this.boardState = boardState;
		this.player = player;
		this.instructionPanel = instructionPanel;
	}

	@Override
	public void mouseClicked(Territory territory) {
		if (boardState.getPlayer(territory) != player) {
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
		territories.addAll(boardState.getTerritories());

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
		for (Territory adjacent : territory.adjacents) {
			if (boardState.getPlayer(adjacent) == player
					&& territories.contains(adjacent)) {
				adjacentControlled.add(adjacent);
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
