package risk;

import java.util.List;

import javax.swing.JOptionPane;

/**
 * Allows a person to set the armies on a specific territory, set the player,
 * and get a boardValue for a given setup.
 */
public class EditMode implements Strategy {

	private final BoardState boardState;
	private final InstructionPanel instructionPanel;
	private final MainGame game;

	private Phase phase;

	public EditMode(BoardState boardState, InstructionPanel instructionPanel,
			MainGame game) {
		this.boardState = boardState;
		this.instructionPanel = instructionPanel;
		this.game = game;
	}

	@Override
	public void takeTurn(Player player) {
		int choice = JOptionPane.showConfirmDialog(null, "Load File",
				"Load File?", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			String fileName = JOptionPane.showInputDialog("File Name: ");
			BoardStateSaver saver = new BoardStateSaver();
			saver.loadFile(boardState,
					"/Users/danielglasgow/Documents/Source/workspace/risk/SavedBoardStates/"
							+ fileName + ".csv");
			boardState.updateBackground();
		}

		phase = Phase.EDIT;
		while (true) {
			if (phase == Phase.EDIT) {
				handlePhase(new BoardEditor(boardState, getPlayers(),
						instructionPanel));
			} else if (phase == Phase.END_TURN) {
				break;
			}
		}

	}

	private void handlePhase(HumanPhaseHandler phaseHandler) {
		boardState.getBoard().getMouse().setPhaseHandler(phaseHandler);
		phaseHandler.displayInterface();
		phase = phaseHandler.await();
	}

	private List<Player> getPlayers() {
		return game.getPlayers();
	}

}
