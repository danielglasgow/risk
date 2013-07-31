package risk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;



public class TerritoryCluster implements Comparable<TerritoryCluster> {

	public ArrayList<Territory> cluster = new ArrayList<Territory>();
	public boolean containsBorder;
	private Continent continent;
	private Player player;
	private final ArrayList<Territory> playerTerritories;
	private final ArrayList<AttackRoute> attackRoutes = new ArrayList<AttackRoute>();
	
	public TerritoryCluster (Territory territory, Continent continent, Player player) {
		this.continent = continent;
		this.player = player;
		this.cluster = findCluster(territory);
		this.containsBorder = containsBorder();
		this.playerTerritories = playerTerritories();
	}
	

	
	public void makeRoutes() {
		for (Territory t : playerTerritories) {
			ArrayList<AttackRoute> attackRoutes = new ArrayList<AttackRoute>();
			AttackRoute firstRoute = new AttackRoute(continent);
			firstRoute.add(t);
			attackRoutes.add(firstRoute);
			int count = 0;
			while (extendRoute(cluster, attackRoutes)) {
				//ArrayList<AttackRoute> placeHolderRoutes = new ArrayList<AttackRoute>(attackRoutes);
				//attackRoutes.clear();
				//attackRoutes.addAll(placeHolderRoutes);
				//System.out.println(count++);
			}
			this.attackRoutes.addAll(attackRoutes);
		}
		System.out.println("Attack Routes:");
		Collections.sort(attackRoutes);
		for (AttackRoute ar : attackRoutes) {
			System.out.println(ar);
		}
		
	}
	
	private boolean extendRoute(ArrayList<Territory> cluster, ArrayList<AttackRoute> attackRoutes) {
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
						route.add(t);
					} else {
						AttackRoute newRoute = new AttackRoute(continent, firstRoute);
						newRoute.add(t);
						newRoutes.add(newRoute);
					}
				}
			}
		}
		attackRoutes.addAll(newRoutes);
		return stillWorking;
	}
	
	
	private ArrayList<Territory> playerTerritories() {
		HashSet<Territory> playerTerritories = new HashSet<Territory>();
		for (Territory t1 : cluster) {
			for (Territory t2 : t1.adjacents) {
				if (continent.territories.contains(t2) && t2.player.equals(player)) {
					playerTerritories.add(t2);
				}
			}
		}
		return new ArrayList<Territory>(playerTerritories);
	}
	
	private boolean containsBorder() {
		boolean containsBorder = false;
		for (Territory t : continent.borders) {
			if (cluster.contains(t)) {
				containsBorder = true;
			}
		}
		return containsBorder;
	}
	
	private ArrayList<Territory> findCluster(Territory territory) {
		ArrayList<Territory> cluster = new ArrayList<Territory>();
		ArrayList<Territory> next = new ArrayList<Territory>();
		ArrayList<Territory> placeHolder = new ArrayList<Territory>();
		next.add(territory);
		while (!next.isEmpty()) {
			cluster.addAll(next);
			placeHolder.addAll(next);
			next.clear();
			for (Territory t : placeHolder) { 
				next.addAll(qualifyingAdjacentTerritories(t, cluster));
			}
		}
		return cluster;
	}
		
	private ArrayList<Territory> qualifyingAdjacentTerritories(Territory territory, ArrayList<Territory> cluster) {
		ArrayList<Territory> adjacents = new ArrayList<Territory>();
		for (Territory t : territory.adjacents) {
			if(!t.player.equals(player) && !cluster.contains(t) && continent.territories.contains(t)) {
				adjacents.add(t);
			}
		}
		return adjacents;
	}
	
	public static boolean compareClusters(TerritoryCluster territoryCluster1, TerritoryCluster territoryCluster2) {
		for (Territory t : territoryCluster1.cluster) {
			if (!territoryCluster2.cluster.contains(t)) {
				return false;
			}
		}
		return true;
	}
	
	private Player worstEnemy() {
		HashMap<Player, Integer> players = new HashMap<Player, Integer>();
		for (Player p : MainGame.players) {
			players.put(p, 0);
		}
		for (Territory t : continent.territories) {
			if (!t.player.equals(player)) {
				int threat = 1 + t.armies;
				players.put(t.player, threat);
			}
		}
		Player worstEnemy = (Player) players.keySet().toArray()[0];
		for (Player p : players.keySet()) {
			if (players.get(worstEnemy) < players.get(p)) {
				worstEnemy = p;
			}
		}
		return worstEnemy;	
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
			for (Territory t : this.cluster) {
				if (t.player.equals(worstEnemy)) {
					threat1++;
				}
			}
			for (Territory t : cluster.cluster) {
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
		return cluster.toString();
		
	}

	
}
