package risk;

import java.util.concurrent.CountDownLatch;

public class PhaseHandler {

	// TODO(dani): Both of these class members should be private.
	// TODO(dani): Write a function setNextPhase(....);
	protected Phase nextPhase = null;
	// TODO(dani): Write a function countDown(), or better yet, write a function
	// void finishPhase(Phase nextPhase) { .... }; Then everything can be
	// private,
	// and you'll only have one function. The classes which "extend"
	// PhaseHandler will
	// not know the details of the implementation -- and they shouldn't.
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
	// TODO(dani): Make this abstract.
	// http://docs.oracle.com/javase/tutorial/java/IandI/abstract.html
	// TODO(dani): Rename this to be: mouseClicked(Territory territory). It will
	// make more sense.
	public void action(Territory territory) {

	}

	// To be overridden.. should I make an interface?
	// TODO(dani): Make this abstract.
	// http://docs.oracle.com/javase/tutorial/java/IandI/abstract.html
	// TODO(dani): Do you really want to call this "setInterface"?
	// displayInterface()?
	public void setInterface() {

	}

}
