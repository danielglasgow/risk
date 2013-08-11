package risk;

import java.util.List;

import javax.swing.JOptionPane;

import com.google.common.collect.Lists;

public class Player {

	public final String color;
	public final String name;
	private final MainGame game;
	private final Strategy strategy;
	public Territory territoryAttackTo;
	public Territory territoryAttackFrom;
	public Territory fortify1;
	public Territory fortify2;

	public Player(String name, String color, MainGame game, Strategy strategy) {
		this.game = game;
		this.name = name;
		this.color = color;
		this.strategy = strategy;
	}

	public boolean hasTerritories() {
		return (getTerritories().size() > 1);
	}

	public int getArmiesToPlace(boolean hasPlayer) {
		int armiesToPlace = getTerritories().size() / 3;
		if (armiesToPlace < 3) {
			armiesToPlace = 3;
		}
		armiesToPlace += checkContinents(hasPlayer);
		return armiesToPlace;
	}

	private boolean hasContinent(Continent continent) {
		boolean hasContinent = true;
		for (Territory t : continent.getTerritories()) {
			if (!getTerritories().contains(t)) {
				hasContinent = false;
			}
		}
		return hasContinent;
	}

	private int checkContinents(boolean hasPlayer) {
		int extraArmies = 0;
		for (Continent c : game.continents) {
			if (hasContinent(c) && hasPlayer) {
				extraArmies += c.bonusArmies;
				JOptionPane.showMessageDialog(null, "You have been awarded "
						+ c.bonusArmies + " extra armies for controling "
						+ c.name);
			}
		}
		return extraArmies;
	}

	public void takeTurn() {
		strategy.takeTurn(this);
	}

	public List<Territory> getTerritories() {
		List<Territory> playerTerritories = Lists.newArrayList();
		for (Territory territory : game.boardState.getTerritories()) {
			if (game.boardState.getPlayer(territory) == this) {
				playerTerritories.add(territory);
			}
		}
		return playerTerritories;
	}
}
