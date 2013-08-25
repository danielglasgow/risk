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

    public EditMode(BoardState boardState, InstructionPanel instructionPanel) {
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
    }

    @Override
    public void takeTurn(Player player) {
        BoardStateSaver.loadBoard(boardState);
        SubPhase phase = SubPhase.EDIT;
        while (true) {
            if (phase == SubPhase.EDIT) {
                BoardEditor boardEditor = new BoardEditor(boardState, getPlayers(),
                        instructionPanel, editTerritory);
                phase = boardEditor.run(boardState.getBoard().getMouse());
                editTerritory = boardEditor.getEditTerritory();
            } else if (phase == SubPhase.END_SUB_PHASE)
                ;
            boardState.getGame().setEditMode(false);
            break;
        }
    }

    private List<Player> getPlayers() {
        return boardState.getPlayers();
    }

    /**
     * The phases a human player completes during EditMode.
     */
    public enum SubPhase {
        EDIT, //
        END_SUB_PHASE,
    }

}
