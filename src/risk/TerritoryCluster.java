package risk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/**
 * A TerritoryCluster is an object which represents a group of contiguous
 * territories not controlled by a given player within a continent.
 * 
 * @author danielglasgow
 */
public class TerritoryCluster implements Comparable<TerritoryCluster>,
		Iterable<Territory> {

	private final List<Territory> territories;
	public final boolean containsBorder;
	private final Continent continent;
	private final Player player;
	private final Set<Territory> playerTerritories;

	// This is computed later if needed.
	private final List<AttackRoute> attackRoutes = new ArrayList<AttackRoute>();

	public TerritoryCluster(Territory territory, Continent continent,
			Player player) {
		this.continent = continent;
		this.player = player;
		this.territories = buildCluster(territory);
		this.containsBorder = containsBorder();
		this.playerTerritories = getAdjacentPlayerTerritories();
	}

	// TODO (Dani) Javadoc.. and get rid of ArrayList declarations
	public void makeRoutes() {
		for (Territory t : playerTerritories) {
			ArrayList<AttackRoute> attackRoutes = new ArrayList<AttackRoute>();
			AttackRoute firstRoute = new AttackRoute(continent);
			firstRoute.add(t);
			attackRoutes.add(firstRoute);
			while (extendRoute(territories, attackRoutes)) {
			}
			this.attackRoutes.addAll(attackRoutes);
		}
		System.out.println("Attack Routes:");
		Collections.sort(attackRoutes);
		for (AttackRoute ar : attackRoutes) {
			System.out.print(" " + ar.routeEfficiency());
			System.out.println(ar);
		}
	}

	private boolean extendRoute(List<Territory> cluster,
			ArrayList<AttackRoute> attackRoutes) {
		boolean stillWorking = false;
		ArrayList<AttackRoute> newRoutes = new ArrayList<AttackRoute>();
		for (AttackRoute route : attackRoutes) {
			Territory territory = route.get(route.size() - 1);
			AttackRoute firstRoute = new AttackRoute(continent);
			for (Territory t : territory.adjacents) {
				if (!route.contains(t) && cluster.contains(t)) {
					stillWorking = true;
					if (firstRoute.isEmpty()) {
						firstRoute.addAll(route);
						if (firstRoute.size() > 1) {
							this.attackRoutes.add(firstRoute);
						}
						route.add(t);
					} else {
						AttackRoute newRoute = new AttackRoute(continent,
								firstRoute);
						newRoute.add(t);
						newRoutes.add(newRoute);
					}
				}
			}
		}
		attackRoutes.addAll(newRoutes);
		return stillWorking;
	}

	/**
	 * Returns a list of all the territories in the continent (which by
	 * definition are the player's territories) that border the cluster's
	 * territories.
	 */
	private Set<Territory> getAdjacentPlayerTerritories() {
		Set<Territory> playerTerritories = new HashSet<Territory>();
		for (Territory clusterTerritory : territories) {
			for (Territory borderTerritory : clusterTerritory.adjacents) {
				if (continent.territories.contains(borderTerritory)
						&& borderTerritory.player.equals(player)) {
					playerTerritories.add(borderTerritory);
				}
			}
		}
		return playerTerritories;
	}

	private boolean containsBorder() {
		boolean containsBorder = false;
		for (Territory t : continent.borders) {
			if (territories.contains(t)) {
				containsBorder = true;
			}
		}
		return containsBorder;
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

	private ArrayList<Territory> qualifyingNeighbors(Territory territory,
			List<Territory> cluster) {
		ArrayList<Territory> adjacents = new ArrayList<Territory>();
		for (Territory t : territory.adjacents) {
			if (!t.player.equals(player) && !cluster.contains(t)
					&& continent.territories.contains(t)) {
				adjacents.add(t);
			}
		}
		return adjacents;
	}

	public static boolean compareClusters(TerritoryCluster territoryCluster1,
			TerritoryCluster territoryCluster2) {
		for (Territory t : territoryCluster1.territories) {
			if (!territoryCluster2.territories.contains(t)) {
				return false;
			}
		}
		return true;
	}

	private Player worstEnemy() {
		Multiset<Player> players = HashMultiset.create();
		for (Territory t : continent.territories) {
			if (!t.player.equals(player)) {
				players.add(t.player);
			}
		}
		int maxAppearance = 0;
		Player maxAppearancePlayer = null;
		for (Player p : players) {
			if (players.count(p) > maxAppearance) {
				maxAppearancePlayer = p;
				maxAppearance = players.count(p);
			}
		}
		return maxAppearancePlayer;
	}

	@Override
	public int compareTo(TerritoryCluster cluster) {
		if (cluster.containsBorder && !containsBorder) {
			return 1;
		} else if (!cluster.containsBorder && containsBorder) {
			return -1;
		} else if (cluster.containsBorder == containsBorder) {
			Player worstEnemy = worstEnemy();
			int threat1 = 0;
			int threat2 = 0;
			for (Territory t : this.territories) {
				if (t.player.equals(worstEnemy)) {
					threat1++;
				}
			}
			for (Territory t : cluster.territories) {
				if (t.player.equals(worstEnemy)) {
					threat2++;
				}
			}
			if (threat2 > threat1) {
				return 1;
			} else {
				return -1;
			}
		}
		return 0;
	}

	public String toString() {
		return territories.toString();
	}

	@Override
	public Iterator<Territory> iterator() {
		return territories.iterator();
	}

}
