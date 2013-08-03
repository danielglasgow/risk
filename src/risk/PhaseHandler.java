package risk;

import java.util.concurrent.CountDownLatch;

public class PhaseHandler {

	protected Phase nextPhase = null;
	protected final CountDownLatch latch = new CountDownLatch(1);

	public Phase await() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return nextPhase;
	}

	// To be overriden.. should I make an interface?
	public void action(Territory territory) {

	}

	// To be overriden.. should I make an interface?
	public void setInterface() {

	}

}
