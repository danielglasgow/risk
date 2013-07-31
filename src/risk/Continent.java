package risk;

import java.util.ArrayList;
import java.util.List;

public class Continent implements Comparable<Continent> {

	// TODO(dani): Type should be List<Territory> the abstract type.  You can
	// assign and ArrayList<Territory> to a List<Territory> ...but the leads to lots of casting..
	public final List<Territory> territories = new ArrayList<Territory>();
	public final List<Territory> borders = new ArrayList<Territory>();
	private final List<TerritoryCluster> clusters = new ArrayList<TerritoryCluster>();
	public final String name;
	// TODO(dani): Can bonusArmies be final?  Prefer to make as many variables final as possible.
	public final int bonusArmies;
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
	
	public void addBorders() {
		for (Territory t : territories) {
			if (isBorder(t)) {
				borders.add(t);
			}
		}
	}
	
	private boolean isBorder(Territory territory) {
		for (Territory t : territory.adjacents) {
			if (!territories.contains(t)) {
				return true;
			}
		}
		return false;
	}
	
	public void setTerritoryCluster(ArrayList<TerritoryCluster> clusters) {
		this.clusters.clear();
		this.clusters.addAll(clusters);
	}
	
	public List<TerritoryCluster> getClusters() {
		return clusters;
	}
}
