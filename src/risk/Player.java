package risk;

import java.util.List;

import javax.swing.JOptionPane;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class Player {

	public final String color;
	public final String name;
	private final BoardState boardState;
	private final Strategy strategy;
	private final ImmutableList<Continent> continents;

	// THIS NEEDS TO BE FIXED
	// public Territory territoryAttackTo;
	// public Territory territoryAttackFrom;
	// public Territory fortify1;
	// public Territory fortify2;

	public Player(String name, String color, BoardState boardState,
			Strategy strategy, ImmutableList<Continent> continents) {
		this.boardState = boardState;
		this.name = name;
		this.color = color;
		this.strategy = strategy;
		this.continents = continents;
	}

	public boolean hasTerritories() {
		return (getTerritories(boardState).size() > 0);
	}

	public int getArmiesToPlace(boolean hasPlayer) {
		int armiesToPlace = getTerritories(boardState).size() / 3;
		if (armiesToPlace < 3) {
			armiesToPlace = 3;
		}
		armiesToPlace += checkContinents(hasPlayer);
		return armiesToPlace;
	}

	private boolean hasContinent(Continent continent) {
		boolean hasContinent = true;
		for (Territory t : continent.getTerritories()) {
			if (!getTerritories(boardState).contains(t)) {
				hasContinent = false;
			}
		}
		return hasContinent;
	}

	private int checkContinents(boolean hasPlayer) {
		int extraArmies = 0;
		for (Continent continent : continents) {
			if (hasContinent(continent) && hasPlayer) {
				extraArmies += continent.getBonusArmies();
				JOptionPane
						.showMessageDialog(
								null,
								"You have been awarded "
										+ continent.getBonusArmies()
										+ " extra armies for controling "
										+ continent.getName());
			}
		}
		return extraArmies;
	}

	public void takeTurn() {
		strategy.takeTurn(this);
	}

	public List<Territory> getTerritories(BoardState boardState) {
		List<Territory> playerTerritories = Lists.newArrayList();
		for (Territory territory : boardState.getTerritories()) {
			if (boardState.getPlayer(territory) == this) {
				playerTerritories.add(territory);
			}
		}
		return playerTerritories;
	}
}
