package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class WonTerritoryHandler extends HumanPhaseHandler {

	private final BoardState boardState;
	private final InstructionPanel instructionPanel;

	public WonTerritoryHandler(BoardState boardState,
			InstructionPanel instructionPanel) {
		this.boardState = boardState;
		this.instructionPanel = instructionPanel;
	}

	@Override
	public void mouseClicked(Territory territory) {
		if (boardState.getAttackFrom().equals(territory)) {
			if (boardState.getArmies(boardState.getAttackTo()) > 1) {
				boardState.increaseArmies(boardState.getAttackFrom(), 1);
				boardState.decreaseArmies(boardState.getAttackTo(), 1);
			} else {
				JOptionPane.showMessageDialog(null,
						"Territories must have at least one army");
			}
		} else if (boardState.getAttackTo().equals(territory)) {
			if (boardState.getArmies(boardState.getAttackFrom()) > 1) {
				boardState.increaseArmies(boardState.getAttackTo(), 1);
				boardState.decreaseArmies(boardState.getAttackFrom(), 1);
			} else {
				JOptionPane.showMessageDialog(null,
						"Territories must have at least one army");
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"You must click on " + boardState.getAttackFrom().name
							+ " or " + boardState.getAttackTo().name);
		}

	}

	@Override
	public void displayInterface() {
		JButton buttonLeft = new JButton();
		buttonLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				moveAll();
				finishPhase(HumanTurnPhases.FORTIFY_SELECTION);
			}
		});
		buttonLeft.setText("Move All");
		JButton buttonRight = new JButton();
		buttonRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				finishPhase(HumanTurnPhases.FORTIFY_SELECTION);
			}
		});
		buttonRight.setText("Continue");
		instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE,
				"Click on " + boardState.getAttackTo().name
						+ " to move armies from "
						+ boardState.getAttackFrom().name + ". Click on "
						+ boardState.getAttackFrom().name
						+ " to move armies from "
						+ boardState.getAttackTo().name + ".", buttonLeft,
				buttonRight);

	}

	private void moveAll() {
		int armiesToMove = boardState.getArmies(boardState.getAttackFrom()) - 1;
		boardState.increaseArmies(boardState.getAttackTo(), armiesToMove);
		boardState.setArmies(boardState.getAttackFrom(), 1);
		boardState.updateBackground();
	}

}
