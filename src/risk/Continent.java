package risk;

import java.util.ArrayList;
import java.util.List;

public class Continent implements Comparable<Continent> {
	private final List<Territory> territories = new ArrayList<Territory>();
	private final List<Territory> borders = new ArrayList<Territory>();
	private final List<TerritoryCluster> clusters = new ArrayList<TerritoryCluster>();
	public final String name;
	public final int bonusArmies;
	public double ratio;

	public Continent(String name, int bonusArmies) {
		this.name = name;
		this.bonusArmies = bonusArmies;
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

	public List<Territory> getTerritories() {
		return territories;
	}

	public List<TerritoryCluster> getClusters() {
		return clusters;
	}

	public void setClusters(List<TerritoryCluster> newClusters) {
		clusters.clear();
		clusters.addAll(newClusters);
	}

	public List<Territory> getBorders() {
		return borders;
	}
}
