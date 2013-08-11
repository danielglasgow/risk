package risk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttackRoute implements Iterable<Territory> {

	private final BoardState boardState;
	private List<Territory> route = new ArrayList<Territory>();

	public AttackRoute(BoardState boardState, AttackRoute attackRoute) {
		this.boardState = boardState;
		this.route.addAll(attackRoute.route);
	}

	public AttackRoute(BoardState boardState) {
		this.boardState = boardState;
	}

	public BoardState getExpectedBoardState(List<Territory> territories) {
		BoardState expectedBoardState = boardState.copy();
		double expectedArmies = attackRouteExpectedArmies(this);
		Player player = boardState.getPlayer(route.get(0));
		if (expectedArmies < 1) {
			return null;
		} else {
			int territoryCount = 0;
			for (Territory territory : route) {
				territoryCount++;
				if (territoryCount != route.size()) {
					expectedBoardState.setArmies(territory, 1);
				} else {
					expectedBoardState.setArmies(territory,
							(int) expectedArmies);
				}
				expectedBoardState.setPlayer(territory, player);
			}
		}
		return expectedBoardState;
	}

	private double attackRouteExpectedArmies(AttackRoute attackRoute) {
		StandardBattlePredictor sbp = new StandardBattlePredictor();
		Iterator<Territory> attackRouteIterator = attackRoute.iterator();
		int attackArmies = boardState.getArmies(attackRouteIterator.next())
				+ boardState.getPlayer(attackRoute.get(0)).getArmiesToPlace(
						false);
		int defenseArmies = boardState.getArmies(attackRouteIterator.next());
		double expectedArmies = sbp.predict(new Battle(attackArmies,
				defenseArmies)) - 1;
		double percentage = expectedArmies / ((double) attackArmies - 1);
		while (attackRouteIterator.hasNext()) {
			attackArmies = (int) expectedArmies + 1;
			defenseArmies = boardState.getArmies(attackRouteIterator.next());
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
