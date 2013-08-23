package risk;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * This class represents a list of territories which represent a legal attack
 * path for the computer to traverse. Most importantly, the AttackRoute has
 * public methods to return the expected boardState if the computer were to
 * follow the attack path represented by the AttackRoute.
 * 
 */
// TODO(dani): Maybe implement something more than Iterable? Collection?
public class AttackRoute implements Iterable<Territory> {

	private final BoardState boardState;
	private final List<Territory> territories = Lists.newArrayList();

	public AttackRoute(BoardState boardState, AttackRoute attackRoute) {
		this.boardState = boardState;
		this.territories.addAll(attackRoute.territories);
	}

	/**
	 * This method returns the expected boardState if the computer were to
	 * attack, following the attack path represented by the AttackRoute.
	 */
	public BoardState getExpectedBoardState(List<Territory> territories) {
		BoardState expectedBoardState = boardState.copy();
		// TODO(dani): Do not pass 'this'.
		// Reconfigure Ecslipse to use SPACES not TABS.
		double expectedArmies = attackRouteExpectedArmies(this);
		Player player = boardState.getPlayer(territories.get(0));
		if (expectedArmies < 1) {
			return null;
		} else {
			int territoryCount = 0;
			for (Territory territory : territories) {
				territoryCount++;
				if (territoryCount != territories.size()) {
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

	// TODO (dani):
	// This method should invoke a method that takes a boardState and an
	// attackFrom territory and attackTo territory and returns the boardState
	// after a battle. This should be pieced together to figure out the
	// boardState after the entire attackRoute has been traversed.
	private double attackRouteExpectedArmies(AttackRoute attackRoute) {
		BattlePredictor battlePredictor = new StandardBattlePredictor(); // battle
																			// predictor
																			// should
																			// be
																			// passed
																			// in.
		Iterator<Territory> attackRouteIterator = attackRoute.iterator();
		int attackArmies = boardState.getArmies(attackRouteIterator.next())
				+ boardState.getPlayer(attackRoute.get(0)).getArmiesToPlace(
						false);
		int defenseArmies = boardState.getArmies(attackRouteIterator.next());
		double expectedArmies = battlePredictor.predict(new Battle(
				attackArmies, defenseArmies)) - 1;
		double percentage = expectedArmies / ((double) attackArmies - 1);
		while (attackRouteIterator.hasNext()) {
			attackArmies = (int) expectedArmies + 1;
			defenseArmies = boardState.getArmies(attackRouteIterator.next());
			expectedArmies = battlePredictor.predict(new Battle(attackArmies,
					defenseArmies)) - 1;
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
		territories.add(territory);
	}

	public void remove(Territory t) {
		territories.remove(t);
	}

	public String toString() {
		return territories.toString();
	}

	public Territory get(int index) {
		return territories.get(index);
	}

	public int size() {
		return territories.size();
	}

	public boolean contains(Territory territory) {
		return territories.contains(territory);
	}

	public boolean isEmpty() {
		return territories.isEmpty();
	}

	public boolean addAll(AttackRoute route) {
		return this.territories.addAll(route.getRoute());
	}

	// TODO(dani): Can you remove this method?
	public List<Territory> getRoute() {
		return territories;
	}

	@Override
	public Iterator<Territory> iterator() {
		return territories.iterator();
	}

}
