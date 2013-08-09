package risk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttackRoute implements Iterable<Territory> {

	private List<Territory> route = new ArrayList<Territory>();

	public AttackRoute(AttackRoute attackRoute) {
		this.route.addAll(attackRoute.route);
	}

	public AttackRoute() {
		// TODO Auto-generated constructor stub
	}

	public BoardState getBoardState(List<Territory> territories) {
		BoardState boardState = new BoardState(territories);
		double expectedArmies = attackRouteExpectedArmies(this);
		Player player = route.get(0).player;
		if (expectedArmies < 1) {
			return null;
		} else {
			int territoryCount = 0;
			for (Territory territory : route) {
				territoryCount++;
				if (territoryCount != route.size()) {
					boardState.setTerritoryArmies(territory, 1);
				} else {
					boardState.setTerritoryArmies(territory,
							(int) expectedArmies);
				}
				boardState.setTerritoryPlayer(territory, player);
			}
		}
		return boardState;
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
	public Iterator<Territory> iterator() {
		return route.iterator();
	}

}
