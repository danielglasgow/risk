package risk;

/**
 * Represents the number of armies on each territory and which player controls
 * those armies.
 */
public class TerritoryState {

	private final int armies;
	private final Player player;

	public TerritoryState(int armies, Player player) {
		this.armies = armies;
		this.player = player;
	}

	public int getArmies() {
		return armies;
	}

	public Player getPlayer() {
		return player;
	}

	public TerritoryState setArmies(int armies) {
		return new TerritoryState(armies, player);
	}

	public TerritoryState setPlayer(Player player) {
		return new TerritoryState(armies, player);
	}

}
