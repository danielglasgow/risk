package risk;

import java.util.ArrayList;

public class Continent implements Comparable<Continent> {

	public ArrayList<Territory> territories = new ArrayList<Territory>();
	public ArrayList<Territory> boarders = new ArrayList<Territory>();
	public String name;
	public int bonusArmies;
	public double ratio;
	
	public Continent(String name, int bonusArmies) {
		this.name = name;
		this.bonusArmies = bonusArmies;
	}
	
	public ArrayList<Territory> getTerritories(Player player, Boolean controlledByPlayer) {
		ArrayList<Territory> playerTerritories = new ArrayList<Territory>();
		ArrayList<Territory> otherTerritories = new ArrayList<Territory>();
		for (Territory t : territories) {
			if (t.player.equals(player)) {
				playerTerritories.add(t);
			} else {
				otherTerritories.add(t);
			}
		}
		if (controlledByPlayer) {
			return playerTerritories;
		} else {
			return otherTerritories;
		}
	}
	
	public String toString() {
		String continent = name + ": ";
		//for (Territory t : territories) {
			//continent = continent + " " + t.name;
		//}
		return continent;
	}

	@Override
	public int compareTo(Continent continent) {
		if (continent.ratio > ratio) {
			return 1;
		} else if (continent.ratio < ratio) {
			return -1;
		}
		return 0;
	}
	
	public void addBoarders() {
		for (Territory t : territories) {
			if (isBoarder(t)) {
				boarders.add(t);
			}
		}
	}
	
	private boolean isBoarder(Territory territory) {
		for (Territory t : territory.adjacents) {
			if (!territories.contains(t)) {
				return true;
			}
		}
		return false;
	}

	

	
}
