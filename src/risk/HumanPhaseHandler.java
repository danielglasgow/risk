package risk;

import java.util.concurrent.CountDownLatch;

/**
 * Provides a framework for user input during different phases of a player's
 * turn.
 */
public abstract class HumanPhaseHandler {

    private HumanTurnPhases nextPhase = null;
    private final CountDownLatch latch = new CountDownLatch(1);

    private final HumanTurnPhases phaseType;

    public HumanPhaseHandler(HumanTurnPhases phaseType) {
        this.phaseType = phaseType;
    }

    public HumanTurnPhases await() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return nextPhase;
    }

    protected void finishPhase(HumanTurnPhases nextPhase) {
        this.nextPhase = nextPhase;
        latch.countDown();
    }

    public HumanTurnPhases getPhaseType() {
        return phaseType;
    }

    public abstract void mouseClicked(Territory territory);

    public abstract void displayInterface();

}
