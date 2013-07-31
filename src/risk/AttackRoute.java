package risk;

import java.util.ArrayList;


public class AttackRoute implements Comparable<AttackRoute> {

	private ArrayList<Territory> route = new ArrayList<Territory>();
	private Continent continent;
	

	public AttackRoute(Continent continent) {
		this.continent = continent;
	}
	
	public AttackRoute(Continent continent, AttackRoute attackRoute) {
		this.continent = continent;
		this.route.addAll(attackRoute.route);
	}
	
	private int routeEfficiency() {
		Territory baseTerritory = route.get(0);
		int friendlyArmies = baseTerritory.armies + baseTerritory.player.armiesToPlace - 4;
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
		ArrayList<TerritoryCluster> clusters = (ArrayList<TerritoryCluster>) continent.getClusters();
		boolean completesCluster = true;
		for (TerritoryCluster tc : clusters) {
			for (Territory t : tc.cluster) {
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
	
	public ArrayList<Territory> getRoute() {
		return route;
	}

	@Override
	public int compareTo(AttackRoute route) {
		if (this.route.size() < route.size()) {
			return 1;
		} else if (this.route.size() > route.size()){
			return -1;
		} else {
			Territory routeLast = route.get(route.size() - 1);
			Territory thisLast = this.route.get(this.route.size() - 1);
			Boolean routeHasBorder = false;
			Boolean thisHasBorder = false;
			for (Territory t : continent.borders) {
				if (routeLast.equals(t)) {
					routeHasBorder = true;
				} 
				if (thisLast.equals(t)) {
					thisHasBorder = true;
				}
			}
			if (routeHasBorder && !thisHasBorder) {
				return 1;
			} else if (!routeHasBorder && thisHasBorder) {
				return -1;
			}
		}
		return 0;
	}

}
