package risk;

/**
 * This interface provides a framework for handling the MainPhases of a human
 * player's turn: PLACE_ARMIES, ATTACK, and FORTIFICATION.
 */
public interface MainPhaseHandler {

    /**
     * This method is called on every MainPhaseHandler object, starting an
     * internal loop within the MainPhaseHanlder that handles that MainPhase's
     * SubPhases.
     */
    public void runPhase();

}
