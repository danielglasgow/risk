package risk;

import java.util.concurrent.CountDownLatch;

/**
 * Provides a framework for user input during a SubPhase of a player's turn.
 */
public abstract class SubPhaseHandler {

    private SubPhase nextSubPhase = null;
    private final CountDownLatch latch = new CountDownLatch(1);

    /**
     * Waits until the subPhase interaction is complete, at which time it
     * returns the nextSubPhase (which is determined during the SubPhase while
     * the main thread is waiting).
     */
    public SubPhase await() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return nextSubPhase;
    }

    /**
     * Sets the next SubPhase and triggers the main thread to stop waiting.
     */
    protected void finishPhase(SubPhase nextSubPhase) {
        this.nextSubPhase = nextSubPhase;
        latch.countDown();
    }

    public abstract void mouseClicked(Territory territory);

    public abstract void displayInterface();

}
