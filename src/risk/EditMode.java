package risk;

import java.util.List;

/**
 * Allows a person to set the armies on a specific territory, set the player,
 * and get a boardValue for a given setup.
 */
public class EditMode implements Strategy {

    private final BoardState boardState;
    private final InstructionPanel instructionPanel;

    private HumanTurnPhases phase;

    public EditMode(BoardState boardState, InstructionPanel instructionPanel) {
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
    }

    @Override
    public void takeTurn(Player player) {
        BoardStateSaver.loadBoard(boardState);
        phase = HumanTurnPhases.EDIT;
        while (true) {
            if (phase == HumanTurnPhases.EDIT) {
                handlePhase(new BoardEditor(boardState, getPlayers(),
                        instructionPanel));
            } else if (phase == HumanTurnPhases.END_TURN) {
                boardState.getGame().setEditMode(false);
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
        return boardState.getPlayers();
    }

}
