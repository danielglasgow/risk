package risk;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Player {

	public String color;
	public String name;
	private final MainGame game;
	//private final boolean isComputer;
	public int armiesToPlace = 0;
	public Territory territoryAttackTo;
	public Territory territoryAttackFrom;
	public Territory fortify1;
	public Territory fortify2;
	
	public Player(String name, String color, MainGame game) {
		this.game = game;
		this.name = name;
		this.color = color;
	}

	private ArrayList<Territory> getTerritories() {
		ArrayList<Territory> territories = new ArrayList<Territory>();
		for (Territory t : game.territories) {
			if (t.player.equals(this)) {
				territories.add(t);
			}
		}
		return territories;
	}
	
	public void calculateArmiesToPlace() {
		armiesToPlace = getTerritories().size() / 3;
		if (armiesToPlace < 3) {
			armiesToPlace = 3;
		}
		armiesToPlace += checkContinents();
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
	
	private int checkContinents() {
		int extraArmies = 0;
		for (Continent c : game.continents) {
			if (hasContinent(c)) {
				extraArmies += c.bonusArmies;
				JOptionPane.showMessageDialog(null, "You have been awarded " + c.bonusArmies + " extra armies for controling " + c.name);
			}
		}
		return extraArmies;
	}
}
