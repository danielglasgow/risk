package risk;

import java.util.ArrayList;
import java.util.List;

public class Continent implements Comparable<Continent> {
	public final List<Territory> territories = new ArrayList<Territory>();
	public final List<Territory> borders = new ArrayList<Territory>();
	private final List<TerritoryCluster> clusters = new ArrayList<TerritoryCluster>();
	public final String name;
	public final int bonusArmies;
	public double ratio;

	public Continent(String name, int bonusArmies) {
		this.name = name;
		this.bonusArmies = bonusArmies;
	}

	public ArrayList<Territory> getTerritories(Player player,
			Boolean controlledByPlayer) {
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
		String continent = name + ": " + ratio;
		// for (Territory t : territories) {
		// continent = continent + " " + t.name;
		// }
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

	public void generateTerritoryClusters(Player player) {
		List<TerritoryCluster> territoryClusters = new ArrayList<TerritoryCluster>();
		for (Territory t : getTerritories(player, false)) {
			territoryClusters.add(new TerritoryCluster(t, this, player));
		}
		List<TerritoryCluster> uniqueClusters = new ArrayList<TerritoryCluster>();
		uniqueClusters.add(territoryClusters.get(0));
		for (TerritoryCluster cluster1 : territoryClusters) {
			boolean newCluster = true;
			for (TerritoryCluster cluster2 : uniqueClusters) {
				if (TerritoryCluster.compareClusters(cluster1, cluster2)) {
					newCluster = false;
				}
			}
			if (newCluster) {
				uniqueClusters.add(cluster1);
			}
		}
		clusters.clear();
		clusters.addAll(uniqueClusters);
	}

	public List<TerritoryCluster> getClusters() {
		return clusters;
	}
}
