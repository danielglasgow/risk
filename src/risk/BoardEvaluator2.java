package risk;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class BoardEvaluator2 {

	/**
	 * 1 Point for each territory player controls.
	 */
	private static final double TERRITORY_BONUS = 1;
	/**
	 * 1 Point for each army player controls
	 */
	private static final int ARMY_BONUS = 1;

	/**
	 * -2 Points for every territory with more than 1 army.
	 */
	private static final double ARMY_DISPERSION_PENALTY = 2;

	/**
	 * -2 Points for every cluster in player's goal continent.
	 */
	private static final double CLUSTER_PENALTY = 2;

	/**
	 * 1 Point for each army adjacent to the biggest cluster in player's goal
	 * continent. Those armies must all belong to the same territory. (The
	 * territory with the most armies adjacent to the biggest cluster).
	 */
	private static final double CLUSTER_BONUS = 1;

	private static final double BORDER_ARMIES_MULTIPLIER = 2;
	private static final double BORDER_ARMIES_DEFLATOR = 0.9;
	private static final double MOST_ARMIES_DEFLATOR = 0.5;
	private static final double MOST_ARMIES_MULTIPLIER = 5;
	private static final double MOST_ARMIES_CONSTANT = 1;

	private final BorderFinder borderFinder = new BorderFinder();

	public double getBoardValue(BoardState boardState, Player player,
			List<Continent> continents) {
		State state = new State(boardState, player, continents);
		double continentBonuses = getContinentBonuses(state);
		double dispersionPenalty = getDispersionPenalty(state);
		double territoryBonus = getTerritoryBonus(state);
		double armyBonus = getArmyBonus(state);
		double clusterBonus = getClusterBonus(state);
		return continentBonuses + territoryBonus + armyBonus
				+ dispersionPenalty + clusterBonus;
	}

	private double getDispersionPenalty(State state) {
		double dispersionPenalty = 0;
		for (Territory territory : state.boardState.getTerritories()) {
			if (state.boardState.getArmies(territory) > 1)
				dispersionPenalty -= ARMY_DISPERSION_PENALTY;
		}
		return dispersionPenalty;
	}

	private double getTerritoryBonus(State state) {
		double territoryBonus = 0;
		for (Territory territory : state.boardState.getTerritories()) {
			if (state.boardState.getPlayer(territory) == state.player) {
				territoryBonus += TERRITORY_BONUS;
			}
		}
		return territoryBonus;
	}

	private double getArmyBonus(State state) {
		double armyBonus = 0;
		for (Territory territory : state.boardState.getTerritories()) {
			if (state.boardState.getPlayer(territory) == state.player) {
				armyBonus += state.boardState.getArmies(territory) * ARMY_BONUS;
			}
		}
		return armyBonus;
	}

	private double getClusterBonus(State state) {
		double clusterBonus = 0;
		Continent goalContinent = getGoalContinent(state);
		goalContinent.setClusters(TerritoryCluster.generateTerritoryClusters(
				state.player, goalContinent, state.boardState));
		Set<TerritoryCluster> clusters = goalContinent.getClusters();
		if (!clusters.isEmpty()) {
			Iterator<TerritoryCluster> clusterIterator = clusters.iterator();
			List<TerritoryCluster> biggestClusters = Lists.newArrayList();
			biggestClusters.add(clusterIterator.next());
			for (TerritoryCluster cluster : clusters) {
				if (biggestClusters.get(0).getTerritories().size() < cluster
						.getTerritories().size()) {
					biggestClusters.clear();
					biggestClusters.add(cluster);
				} else if (biggestClusters.get(0).getTerritories().size() == cluster
						.getTerritories().size()) {
					biggestClusters.add(cluster);
				}
			}
			Set<Territory> clusterBorders = Sets.newHashSet();
			for (Territory territory : goalContinent.getTerritories()) {
				if (state.boardState.getPlayer(territory) == state.player
						&& bordersCluster(territory, biggestClusters)) {
					clusterBorders.add(territory);
				}
			}
			clusterBonus = mostArmies(state, clusterBorders) * CLUSTER_BONUS;
			clusterBonus -= (clusters.size() * CLUSTER_PENALTY);
		}
		return clusterBonus;
	}

	private double mostArmies(State state, Set<Territory> territories) {
		int mostArmies = 0;
		for (Territory territory : territories) {
			int armies = state.boardState.getArmies(territory);
			if (armies > mostArmies) {
				mostArmies = armies;
			}
		}
		double mostArmiesBonus = 0;
		for (int i = 1; i < mostArmies; i++) {
			mostArmiesBonus += MOST_ARMIES_MULTIPLIER
					* Math.pow(MOST_ARMIES_DEFLATOR, i) + MOST_ARMIES_CONSTANT;
		}

		return mostArmiesBonus;
	}

	private boolean bordersCluster(Territory territory,
			List<TerritoryCluster> biggestClusters) {
		for (Territory adjacent : territory.getAdjacents()) {
			for (TerritoryCluster biggestCluster : biggestClusters) {
				if (biggestCluster.getTerritories().contains(adjacent)) {
					return true;
				}
			}
		}
		return false;
	}

	private Continent getGoalContinent(State state) {
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
		Set<Territory> trueBorders = borderFinder.findTrueBorders(
				state.boardState, continent, state.player);
		double armiesOnBorderBonus = 0;
		for (Territory border : trueBorders) {
			for (int i = 1; i < state.boardState.getArmies(border); i++) {
				armiesOnBorderBonus += BORDER_ARMIES_MULTIPLIER
						* Math.pow(BORDER_ARMIES_DEFLATOR, i);
			}

		}
		double continentSecurity = armiesOnBorderBonus / trueBorders.size() + 1;
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
