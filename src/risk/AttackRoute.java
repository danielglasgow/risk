package risk;

import java.util.ArrayList;
import java.util.List;

public class AttackRoute implements Comparable<AttackRoute> {

	private List<Territory> route = new ArrayList<Territory>();
	private Continent continent;

	public AttackRoute(Continent continent) {
		this.continent = continent;
	}

	public AttackRoute(Continent continent, AttackRoute attackRoute) {
		this.continent = continent;
		this.route.addAll(attackRoute.route);
	}

	public double routeEfficiency() {
		Territory baseTerritory = route.get(0);
		int friendlyArmies = baseTerritory.armies
				+ baseTerritory.player.getArmiesToPlace() - 4;
		int enemyArmies = 0;
		for (Territory t : route.subList(1, route.size() - 1)) {
			enemyArmies += t.armies;
		}
		int efficiency = Math.abs(friendlyArmies - enemyArmies);
		if (continent.borders.contains(route.get(route.size() - 1))) {
			efficiency -= 0.5;
		}
		if (completesCluster()) {
			efficiency--;
		}
		return efficiency;

	}

	private boolean completesCluster() {
		ArrayList<TerritoryCluster> clusters = (ArrayList<TerritoryCluster>) continent
				.getClusters();
		boolean completesCluster = true;
		for (TerritoryCluster cluster : clusters) {
			for (Territory t : cluster) {
				if (!route.contains(t)) {
					completesCluster = false;
				}
			}
			if (completesCluster) {
				return true;
			}
			completesCluster = true;
		}
		return false;
	}

	public void add(Territory territory) {
		route.add(territory);
	}

	public void remove(Territory t) {
		route.remove(t);
	}

	public String toString() {
		return route.toString();
	}

	public Territory get(int index) {
		return route.get(index);
	}

	public int size() {
		return route.size();
	}

	public boolean contains(Territory territory) {
		return route.contains(territory);
	}

	public boolean isEmpty() {
		return route.isEmpty();
	}

	public boolean addAll(AttackRoute route) {
		return this.route.addAll(route.getRoute());
	}

	public List<Territory> getRoute() {
		return route;
	}

	@Override
	public int compareTo(AttackRoute route) {
		return Double.compare(this.routeEfficiency(), route.routeEfficiency());
	}

}
