package risk;

/**
 * This class handles the "FORTIFICATION" MainPhase by subdividing
 * responsibility among SubPhaseHandlers, FortifySelector and Fortifier.
 */

public class FortifyHandler implements MainPhaseHandler {
    private final BoardState boardState;
    private final InstructionPanel instructionPanel;
    private final Player player;

    private Territory fortifyTo;
    private Territory fortifyFrom;

    public FortifyHandler(BoardState boardState, InstructionPanel instructionPanel, Player player) {
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
        this.player = player;
    }

    @Override
    public void runPhase() {
        SubPhase subPhase = SubPhase.FORTIFY_SELECTION;
        while (subPhase != SubPhase.END_SUB_PHASE) {
            subPhase = runSubPhase(subPhase);
        }
    }

    protected SubPhase runSubPhase(SubPhase subPhase) {
        Mouse mouse = boardState.getBoard().getMouse();
        if (subPhase == SubPhase.FORTIFY_SELECTION) {
            FortifySelector fortifySelector = new FortifySelector(boardState, player,
                    instructionPanel, fortifyTo, fortifyFrom);
            subPhase = fortifySelector.run(mouse);
            fortifyTo = fortifySelector.getFortifyTo();
            fortifyFrom = fortifySelector.getFortifyFrom();
        } else if (subPhase == SubPhase.FORTIFY) {
            subPhase = new Fortifier(boardState, instructionPanel, fortifyTo, fortifyFrom)
                    .run(mouse);
        }
        return subPhase;
    }

    /**
     * The sub phases a human player completes during the Fortification Phase.
     */
    public enum SubPhase {
        FORTIFY_SELECTION, FORTIFY, //
        END_SUB_PHASE,
    }
}
