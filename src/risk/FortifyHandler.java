package risk;

public class FortifyHandler extends MainPhaseHandler {
    private final BoardState boardState;
    private final InstructionPanel instructionPanel;
    private final Player player;

    public FortifyHandler(BoardState boardState, InstructionPanel instructionPanel, Player player) {
        super(boardState, SubPhase.FORTIFY_SELECTION, MainPhase.END_TURN);
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
        this.player = player;
    }

    @Override
    protected void runSubPhase(SubPhase subPhase) {
        if (subPhase == SubPhase.FORTIFY_SELECTION) {
            handlePhase(new FortifySelector(boardState, player, instructionPanel));
        } else if (subPhase == SubPhase.FORTIFY) {
            handlePhase(new Fortifier(boardState, instructionPanel));
        }
    }
}
