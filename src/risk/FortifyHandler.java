package risk;

/**
 * This class handles the "FORTIFICATION" MainPhase by subdividing
 * responsibility among SubPhaseHandlers, FortifySelector and Fortifier.
 */

public class FortifyHandler extends MainPhaseHandler {
    private final BoardState boardState;
    private final InstructionPanel instructionPanel;
    private final Player player;

    private Territory fortifyTo;
    private Territory fortifyFrom;

    public FortifyHandler(BoardState boardState, InstructionPanel instructionPanel, Player player) {
        super(boardState, SubPhase.FORTIFY_SELECTION, MainPhase.END_TURN);
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
        this.player = player;
    }

    @Override
    protected void runSubPhase(SubPhase subPhase) {
        if (subPhase == SubPhase.FORTIFY_SELECTION) {
            FortifySelector fortifySelector = new FortifySelector(boardState, player,
                    instructionPanel, fortifyTo, fortifyFrom);
            handleSubPhase(fortifySelector);
            fortifyTo = fortifySelector.getFortifyTo();
            fortifyFrom = fortifySelector.getFortifyFrom();
        } else if (subPhase == SubPhase.FORTIFY) {
            handleSubPhase(new Fortifier(boardState, instructionPanel, fortifyTo, fortifyFrom));
        }
    }
}
