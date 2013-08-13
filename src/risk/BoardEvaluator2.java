package risk;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class BoardEvaluator2 {

	private static final double ARMY_DISPERSION_PENALTY = 2;
	private static final double BORDER_CONTROL_BONUS = 1;

	private final BorderFinder borderFinder = new BorderFinder();

	public double getBoardValue(BoardState boardState, Player player,
			List<Continent> continents) {
		State state = new State(boardState, player, continents);
		double continentBonuses = getContinentBonuses(state);
		double territoryBonus = getTerritoryBonus(state);
		double armyBonus = getArmyBonus(state);
		return continentBonuses + territoryBonus + armyBonus;
	}

	private double getTerritoryBonus(State state) {
		double territoryBonus = 0;
		for (Territory territory : state.boardState.getTerritories()) {
			if (state.boardState.getPlayer(territory) == state.player) {
				territoryBonus++;
			}
		}
		return territoryBonus;
	}

	private double getArmyBonus(State state) {
		double armyBonus = 0;
		double armyConcentration = 0;
		double borderControl = 0;
		for (Territory territory : state.boardState.getTerritories()) {
			if (state.boardState.getPlayer(territory) == state.player) {
				armyBonus += state.boardState.getArmies(territory);
				if (state.boardState.getArmies(territory) > 1) {
					armyConcentration -= ARMY_DISPERSION_PENALTY;
					boolean hasAttackPath = false;
					for (Territory adjacent : territory.getAdjacents()) {
						if (state.boardState.getPlayer(adjacent) != state.player) {
							hasAttackPath = true;
						}
					}
					if (!hasAttackPath) {
						armyConcentration -= state.boardState
								.getArmies(territory);
					}
				}
			}
		}
		for (Territory territory : borderFinder.findTrueBorders(
				state.boardState, goalContinent(state), state.player)) {
			if (state.boardState.getPlayer(territory) == state.player) {
				borderControl += BORDER_CONTROL_BONUS;
			}
		}
		return armyBonus + armyConcentration + borderControl;
	}

	private Continent goalContinent(State state) {
		List<Continent> continentRatios = Lists.newArrayList();
		for (int i = 0; i < 6; i++) {
			double numTerritories = 0;
			double numTerritoriesControlled = 0;
			double armiesControlled = 0;
			double enemyArmies = 0;
			for (Territory territory : state.continents.get(i).getTerritories()) {
				numTerritories++;
				if (state.boardState.getPlayer(territory) == state.player) {
					numTerritoriesControlled++;
					armiesControlled += state.boardState.getArmies(territory);
				} else {
					enemyArmies += state.boardState.getArmies(territory);
				}
			}
			double ratio = (numTerritoriesControlled + armiesControlled)
					/ (numTerritories + enemyArmies + armiesControlled);
			state.continents.get(i).ratio = ratio;
			continentRatios.add(state.continents.get(i));
			Collections.sort(continentRatios);
		}
		return continentRatios.get(0);
	}

	private double getContinentBonuses(State state) {
		double continentBonuses = 0;
		for (Continent continent : state.continents) {
			if (isCaptured(state, continent)
					&& state.boardState.getPlayer(continent.getTerritories()
							.get(0)) == state.player) {
				continentBonuses += getContinentBonus(state, continent);
			}
		}
		return continentBonuses;

	}

	private double getContinentBonus(State state, Continent continent) {
		List<Territory> trueBorders = borderFinder.findTrueBorders(
				state.boardState, continent, state.player);
		double borderArmies = 0;
		for (Territory territory : trueBorders) {
			borderArmies += state.boardState.getArmies(territory);
		}
		double continentSecurity = borderArmies / (double) trueBorders.size();
		double continentBonusArmies = continent.getBonusArmies();
		return continentSecurity * continentBonusArmies;
	}

	private boolean isCaptured(State state, Continent continent) {
		Set<Player> players = Sets.newHashSet();
		for (Territory territory : continent.getTerritories()) {
			players.add(state.boardState.getPlayer(territory));
		}
		return players.size() == 1;
	}

	private class State {
		public final BoardState boardState;
		public final Player player;
		public final List<Continent> continents;

		public State(BoardState boardState, Player player,
				List<Continent> continents) {
			this.boardState = boardState;
			this.player = player;
			this.continents = continents;
		}
	}

}
