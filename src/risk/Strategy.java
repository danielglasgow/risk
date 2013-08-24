package risk;

/**
 * This interface determines how a given player is going to play when it is his
 * turn.
 */
public interface Strategy {

    /**
     * This method handles all the different actions a player must complete
     * during a turn (army placement, attack, fortification).
     */
    public abstract void takeTurn(Player player);

}