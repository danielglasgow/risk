package risk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A TerritoryCluster is an object which represents a group of contiguous
 * territories not controlled by a given player within a continent.
 * 
 * @author danielglasgow
 */
public class TerritoryCluster implements Iterable<Territory> {

	private final BoardState boardState;
	private final List<Territory> clusterTerritories;
	private final Continent continent;
	private final Player player;
	private final Set<Territory> playerTerritories;

	// This is computed later if needed.
	private final List<AttackRoute> attackRoutes = new ArrayList<AttackRoute>();

	public TerritoryCluster(BoardState boardState, Territory territory,
			Continent continent, Player player) {
		this.boardState = boardState;
		this.continent = continent;
		this.player = player;
		this.clusterTerritories = buildCluster(territory);
		this.playerTerritories = getAdjacentPlayerTerritories();
	}

	public static List<TerritoryCluster> generateTerritoryClusters(
			Player player, Continent continent, BoardState boardState) {
		List<TerritoryCluster> territoryClusters = new ArrayList<TerritoryCluster>();
		for (Territory territory : continent.getTerritories()) {
			if (boardState.getPlayer(territory) == player) {
				territoryClusters.add(new TerritoryCluster(boardState,
						territory, continent, player));
			}
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
		return uniqueClusters;
	}

	/**
	 * Creates every possible attack route in the cluster and adds them to the
	 * list attackRoutes.
	 */
	public void makeRoutes() {
		for (Territory baseTerritory : playerTerritories) {
			List<AttackRoute> attackRoutes = new ArrayList<AttackRoute>();
			List<AttackRoute> newRoutes = new ArrayList<AttackRoute>();
			List<AttackRoute> oldRoutes = new ArrayList<AttackRoute>();
			AttackRoute baseRoute = new AttackRoute(boardState);
			baseRoute.add(baseTerritory);
			attackRoutes.add(baseRoute);
			boolean stillWorking = true;
			while (stillWorking) {
				stillWorking = false;
				for (AttackRoute route : attackRoutes) {
					oldRoutes.add(route);
					Territory extensionTerritory = route.get(route.size() - 1);
					for (Territory territory : extensionTerritory.adjacents) {
						if (!route.contains(territory)
								&& clusterTerritories.contains(territory)
								&& boardState.getPlayer(territory) != player) {
							stillWorking = true;
							AttackRoute newRoute = new AttackRoute(boardState,
									route);
							newRoute.add(territory);
							newRoutes.add(newRoute);
						}
					}
				}
				this.attackRoutes.addAll(newRoutes);
				attackRoutes.clear();
				attackRoutes.addAll(newRoutes);
				newRoutes.clear();
				attackRoutes.removeAll(oldRoutes);
				oldRoutes.clear();
			}
		}
	}

	/**
	 * Returns a list of all the territories in the continent (which by
	 * definition are the player's territories) that border the cluster's
	 * territories.
	 */
	private Set<Territory> getAdjacentPlayerTerritories() {
		Set<Territory> playerTerritories = new HashSet<Territory>();
		for (Territory clusterTerritory : clusterTerritories) {
			for (Territory borderTerritory : clusterTerritory.adjacents) {
				if (continent.getTerritories().contains(borderTerritory)
						&& boardState.getPlayer(borderTerritory) == player) {
					playerTerritories.add(borderTerritory);
				}
			}
		}
		return playerTerritories;
	}

	/**
	 * Starting from a given enemy territory, builds the list of contiguous
	 * enemy territories within this continent.
	 */
	private List<Territory> buildCluster(Territory territory) {
		// TODO(Dani + Abba) use sets and better algorithm.
		List<Territory> cluster = new ArrayList<Territory>();
		List<Territory> newNeighbors = new ArrayList<Territory>();
		List<Territory> neighbors = new ArrayList<Territory>();
		newNeighbors.add(territory);
		while (!newNeighbors.isEmpty()) {
			cluster.addAll(newNeighbors);
			neighbors.addAll(newNeighbors);
			newNeighbors.clear();
			for (Territory t : neighbors) {
				newNeighbors.addAll(qualifyingNeighbors(t, cluster));
			}
		}
		return cluster;
	}

	/**
	 * A helper function to buildCluster.
	 */
	private ArrayList<Territory> qualifyingNeighbors(Territory territory,
			List<Territory> cluster) {
		ArrayList<Territory> adjacents = new ArrayList<Territory>();
		for (Territory adjacent : territory.adjacents) {
			if (boardState.getPlayer(adjacent) != player
					&& !cluster.contains(adjacent)
					&& continent.getTerritories().contains(adjacent)) {
				adjacents.add(adjacent);
			}
		}
		return adjacents;
	}

	public static boolean compareClusters(TerritoryCluster territoryCluster1,
			TerritoryCluster territoryCluster2) {
		for (Territory t : territoryCluster1.clusterTerritories) {
			if (!territoryCluster2.clusterTerritories.contains(t)) {
				return false;
			}
		}
		return true;
	}

	public List<AttackRoute> getAttackRoutes() {
		return attackRoutes;
	}

	public String toString() {
		return clusterTerritories.toString();
	}

	@Override
	public Iterator<Territory> iterator() {
		return clusterTerritories.iterator();
	}

	public List<Territory> getTerritories() {
		return clusterTerritories;
	}

}
