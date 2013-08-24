package risk;

import java.util.List;

/**
 * Allows a developer to set the armies on a specific territory, set the player,
 * and get a BoardValue for a given setup.
 */
public class EditMode implements Strategy {

    private final BoardState boardState;
    private final InstructionPanel instructionPanel;

    private Territory editTerritory;
    private SubPhase phase;

    public EditMode(BoardState boardState, InstructionPanel instructionPanel) {
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
    }

    @Override
    public void takeTurn(Player player) {
        BoardStateSaver.loadBoard(boardState);
        phase = SubPhase.EDIT;
        while (true) {
            if (phase == SubPhase.EDIT) {
                BoardEditor boardEditor = new BoardEditor(boardState, getPlayers(),
                        instructionPanel, editTerritory);
                handlePhase(boardEditor);
                editTerritory = boardEditor.getEditTerritory();
            } else if (phase == SubPhase.END_SUB_PHASE) {
                System.out.println("Enterd");
                boardState.getGame().setEditMode(false);
                break;
            }
        }

    }

    private void handlePhase(SubPhaseHandler phaseHandler) {
        boardState.getBoard().getMouse().setPhaseHandler(phaseHandler);
        phaseHandler.displayInterface();
        phase = phaseHandler.await();
    }

    private List<Player> getPlayers() {
        return boardState.getPlayers();
    }

}
