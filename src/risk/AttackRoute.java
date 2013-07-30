package risk;

import java.util.ArrayList;


public class AttackRoute {

	private ArrayList<Territory> route = new ArrayList<Territory>();
	
	public AttackRoute() {
		
	}
	
	public AttackRoute(AttackRoute attackRoute) {
		this.route.addAll(attackRoute.route);
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

}
