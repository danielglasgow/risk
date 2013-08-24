package risk;

public abstract class MainPhaseHandler {

    private final BoardState boardState;
    private final MainPhase nextMainPhase;

    private SubPhase subPhase;

    public MainPhaseHandler(BoardState boardState, SubPhase firstSubPhase, MainPhase nextPhase) {
        this.boardState = boardState;
        this.subPhase = firstSubPhase;
        this.nextMainPhase = nextPhase;
    }

    public MainPhase runPhase() {
        while (subPhase != SubPhase.END_SUB_PHASE) {
            runSubPhase(subPhase);
        }
        return nextMainPhase;
    }

    protected abstract void runSubPhase(SubPhase subPhase);

    protected void handlePhase(SubPhaseHandler phaseHandler) {
        boardState.getBoard().getMouse().setPhaseHandler(phaseHandler);
        phaseHandler.displayInterface();
        subPhase = phaseHandler.await();
    }

}
