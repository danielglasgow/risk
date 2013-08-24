package risk;

/**
 * This class provides a framework for handling the MainPhases of a human
 * player's turn: PLACE_ARMIES, ATTACK, and FORTIFICATION.
 */
public abstract class MainPhaseHandler {

    private final BoardState boardState;
    private final MainPhase nextMainPhase;

    private SubPhase subPhase;

    public MainPhaseHandler(BoardState boardState, SubPhase firstSubPhase, MainPhase nextPhase) {
        this.boardState = boardState;
        this.subPhase = firstSubPhase;
        this.nextMainPhase = nextPhase;
    }

    /**
     * This method is called on every MainPhaseHandler object, starting an
     * internal loop within the MainPhaseHanlder that handles that MainPhase's
     * SubPhases.
     */
    public MainPhase runPhase() {
        while (subPhase != SubPhase.END_SUB_PHASE) {
            runSubPhase(subPhase);
        }
        return nextMainPhase;
    }

    /**
     * Each MainPhaseHandler must override this method with a method that checks
     * the given subPhase in order to determine which subPhase it must handle
     * (by instantiating a SubPhaseHanlder and passing it to handleSubPhase).
     */
    protected abstract void runSubPhase(SubPhase subPhase);

    /**
     * Displays the interactive interface (InstructionPanel) of a given
     * subPhaseHandler, and pauses the current thread, while the human player
     * interacts with the subPhaseHandler's GUI thread.
     */
    protected void handleSubPhase(SubPhaseHandler subPhaseHandler) {
        boardState.getBoard().getMouse().setPhaseHandler(subPhaseHandler);
        subPhaseHandler.displayInterface();
        subPhase = subPhaseHandler.await();
    }

}
