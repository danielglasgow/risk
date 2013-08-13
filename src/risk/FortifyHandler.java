package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class FortifyHandler extends HumanPhaseHandler {

	private final BoardState boardState;
	private final Player player;
	private final InstructionPanel instructionPanel;

	public FortifyHandler(BoardState boardState, Player player,
			InstructionPanel instructionPanel) {
		this.boardState = boardState;
		this.player = player;
		this.instructionPanel = instructionPanel;
	}

	@Override
	public void mouseClicked(Territory territory) {
		if (player.fortify1.equals(territory)) {
			if (boardState.getArmies(player.fortify2) > 1) {
				boardState.increaseArmies(player.fortify1, 1);
				boardState.decreaseArmies(player.fortify2, 1);
			} else {
				JOptionPane.showMessageDialog(null,
						"Territories must have at least one army");
			}
		} else if (player.fortify2.equals(territory)) {
			if (boardState.getArmies(player.fortify1) > 1) {
				boardState.increaseArmies(player.fortify2, 1);
				boardState.decreaseArmies(player.fortify1, 1);
			} else {
				JOptionPane.showMessageDialog(null,
						"Territories must have at least one army");
			}
		} else {
			JOptionPane.showMessageDialog(null, "You must click on "
					+ player.fortify1.name + " or " + player.fortify2.name);
		}

	}

	@Override
	public void displayInterface() {
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				finishPhase(Phase.END_TURN);
			}
		});
		button.setText("End Turn");
		instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE,
				"Click on " + player.fortify1.name + " to move armies from "
						+ player.fortify2.name + ". Click on "
						+ player.fortify2.name + " to move armies from "
						+ player.fortify1.name, button);
	}

}
