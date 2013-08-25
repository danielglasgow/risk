package risk;

import java.util.concurrent.CountDownLatch;

/**
 * Provides a framework for user input during a SubPhase of a player's turn.
 */
public abstract class SubPhaseHandler<E> {

    private E nextSubPhase = null;
    private final CountDownLatch latch = new CountDownLatch(1);

    /**
     * Displays the interactive interface (InstructionPanel) of a given
     * subPhaseHandler, and pauses the current thread, while the human player
     * interacts with the subPhaseHandler's GUI thread.
     */
    protected E run(Mouse mouse) {
        mouse.setPhaseHandler(this);
        displayInterface();
        return await();
    }

    /**
     * Waits until the subPhase interaction is complete, at which time it
     * returns the nextSubPhase (which is determined during the SubPhase while
     * the main thread is waiting).
     */
    public E await() {
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
    protected void finishPhase(E nextSubPhase) {
        this.nextSubPhase = nextSubPhase;
        latch.countDown();
    }

    public abstract void mouseClicked(Territory territory);

    public abstract void displayInterface();

}
