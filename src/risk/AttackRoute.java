package risk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttackRoute implements Comparable<AttackRoute>,
		Iterable<Territory> {

	private List<Territory> route = new ArrayList<Territory>();
	private Continent continent;
	private double routeDifficulty = 0;

	public AttackRoute(Continent continent) {
		this.continent = continent;
	}

	public AttackRoute(Continent continent, AttackRoute attackRoute) {
		this.continent = continent;
		this.route.addAll(attackRoute.route);
	}

	public void calculateRouteDifficulty() {
		for (Territory territory : route) {
			if (!territory.equals(route.get(0))) {
				routeDifficulty += territory.armies;
			}
		}
	}

	public double routeEfficiency() {
		double expectedArmies = attackRouteExpectedArmies(this);
		// System.out.print(expectedArmies);
		double efficiency = 0;
		if (expectedArmies - 5 < 0) {
			efficiency -= (expectedArmies - 5);
		}
		if (completesCluster()) {
			efficiency -= 1;
		}
		if (continent.borders.contains(route.get(route.size() - 1))) {
			efficiency -= 0.5;
		}
		// efficiency -= ((double) baseTerritory.armies) * 0.25;
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

	private double attackRouteExpectedArmies(AttackRoute attackRoute) {
		StandardBattlePredictor sbp = new StandardBattlePredictor();
		Iterator<Territory> attackRouteIterator = attackRoute.iterator();
		int attackArmies = attackRouteIterator.next().armies
				+ attackRoute.get(0).player.getArmiesToPlace(false);
		int defenseArmies = attackRouteIterator.next().armies;
		double expectedArmies = sbp.predict(new Battle(attackArmies,
				defenseArmies)) - 1;
		double percentage = expectedArmies / ((double) attackArmies - 1);
		while (attackRouteIterator.hasNext()) {
			attackArmies = (int) expectedArmies + 1;
			defenseArmies = attackRouteIterator.next().armies;
			expectedArmies = sbp
					.predict(new Battle(attackArmies, defenseArmies)) - 1;
			if (attackArmies == 1) {
				return 0;
			}
			double newPercentage = expectedArmies / ((double) attackArmies - 1);
			expectedArmies *= percentage;
			percentage = newPercentage;
		}
		return expectedArmies;
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

	public double getRouteDifficulty() {
		return routeDifficulty;
	}

	@Override
	public Iterator<Territory> iterator() {
		return route.iterator();
	}

}
