package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class BoardEditor extends HumanPhaseHandler {

	private final List<Player> players;
	private final InstructionPanel instructionPanel;
	private final BoardState boardState;

	private int currentPlayerIndex = 0;

	public BoardEditor(BoardState boardState, List<Player> players,
			InstructionPanel instructionPanel) {
		this.boardState = boardState;
		this.players = players;
		this.instructionPanel = instructionPanel;
	}

	@Override
	public void mouseClicked(Territory territory) {
		boardState.setEditTerritory(territory);
		finishPhase(Phase.EDIT);
	}

	@Override
	public void displayInterface() {
		JButton button5 = new JButton();
		JButton button4 = new JButton();
		JButton button3 = new JButton();
		JButton button2 = new JButton();
		JButton button1 = new JButton();
		button5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveGame();
			}
		});
		button4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				finishPhase(Phase.END_TURN);
			}
		});
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changePlayer();

			}
		});
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				increaseArmies();
			}
		});
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				decreaseArmies();

			}
		});

		button1.setText("Decrease Armies");
		button2.setText("Increase Armies");
		button3.setText("Change Player");
		button4.setText("EndTurn");
		button5.setText("Save Board");

		String territoryName = "(Choose Territory)";

		if (boardState.getEditTerritory() != null) {
			territoryName = boardState.getEditTerritory().name;
		}

		instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE, "Edit "
				+ territoryName, button1, button2, button3, button4, button5);

	}

	private void changePlayer() {
		if (currentPlayerIndex == players.size() - 1) {
			currentPlayerIndex = 0;
		} else {
			currentPlayerIndex++;
		}
		Player nextPlayer = players.get(currentPlayerIndex);
		boardState.setPlayer(boardState.getEditTerritory(), nextPlayer);
		boardState.updateBackground();
	}

	private void increaseArmies() {
		boardState.increaseArmies(boardState.getEditTerritory(), 1);
		boardState.updateBackground();
	}

	private void decreaseArmies() {
		boardState.decreaseArmies(boardState.getEditTerritory(), 1);
		boardState.updateBackground();
	}

	private void saveGame() {
		BoardStateSaver saver = new BoardStateSaver();
		String fileName = JOptionPane.showInputDialog("Save As: ");
		saver.saveBoard(boardState, fileName + ".csv");
	}

}
