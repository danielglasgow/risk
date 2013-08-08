package risk;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

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

	private List<Territory> getTerritories() {
		ArrayList<Territory> territories = new ArrayList<Territory>();
		for (Territory t : game.territories) {
			if (t.player.equals(this)) {
				territories.add(t);
			}
		}
		return territories;
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
		for (Territory t : continent.territories) {
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

	public List<Territory> getTerritories(Continent continent) {

		return null;
	}
}
