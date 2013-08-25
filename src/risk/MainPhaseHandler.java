package risk;

/**
 * This class provides a framework for handling the MainPhases of a human
 * player's turn: PLACE_ARMIES, ATTACK, and FORTIFICATION.
 */
public abstract class MainPhaseHandler {

    protected final MainPhase nextMainPhase;

    public MainPhaseHandler(MainPhase nextPhase) {
        this.nextMainPhase = nextPhase;
    }

    /**
     * This method is called on every MainPhaseHandler object, starting an
     * internal loop within the MainPhaseHanlder that handles that MainPhase's
     * SubPhases.
     */
    public abstract MainPhase runPhase();

}
