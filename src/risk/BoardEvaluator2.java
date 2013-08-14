package risk;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class BoardEvaluator2 {

	private static final double ARMY_DISPERSION_PENALTY = 2;
	private static final double BORDER_ARMIES_MULTIPLIER = 2;
	private static final double CLUSTER_PENALTY = 5;
	private static final int ARMY_BONUS_MULTIPLIER = 1;
	private static final double HIGHEST_TERRITORY_MULTIPLIER = 1;
	private static final double CLUSTER_BONUS = 1;

	private final BorderFinder borderFinder = new BorderFinder();

	public double getBoardValue(BoardState boardState, Player player,
			List<Continent> continents) {
		State state = new State(boardState, player, continents);
		double continentBonuses = getContinentBonuses(state);
		double territoryBonus = getTerritoryBonus(state);
		double armyBonus = getArmyBonus(state);
		System.out.println("Army Bonus: " + armyBonus);
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
		double clusterBonus = 0;
		double highestTerritoryArmies = 0;
		for (Territory territory : state.boardState.getTerritories()) {
			if (state.boardState.getPlayer(territory) == state.player) {
				armyBonus += state.boardState.getArmies(territory)
						* ARMY_BONUS_MULTIPLIER;
				if (state.boardState.getArmies(territory) > 1) {
					armyConcentration -= ARMY_DISPERSION_PENALTY;
				}
				if (state.boardState.getArmies(territory) > highestTerritoryArmies) {
					highestTerritoryArmies = state.boardState
							.getArmies(territory);
				}
			}
		}
		Continent goalContinent = getGoalContinent(state);
		goalContinent.setClusters(TerritoryCluster.generateTerritoryClusters(
				state.player, goalContinent, state.boardState));
		Set<TerritoryCluster> clusters = goalContinent.getClusters();
		System.out.println("Clusters: " + clusters);
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
			System.out.println("Biggest Cluster: " + biggestClusters);
			for (Territory territory : goalContinent.getTerritories()) {
				if (state.boardState.getPlayer(territory) == state.player
						&& state.boardState.getArmies(territory) == highestTerritoryArmies
						&& bordersCluster(territory, biggestClusters)) {
					clusterBonus += CLUSTER_BONUS;
				}
			}
			clusterBonus -= (clusters.size() * CLUSTER_PENALTY);
		}

		return armyBonus + armyConcentration + clusterBonus
				+ highestTerritoryArmies * HIGHEST_TERRITORY_MULTIPLIER;
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
		double armiesOnBorder = 0;
		for (Territory border : trueBorders) {
			armiesOnBorder += state.boardState.getArmies(border);
		}
		double averageBorderArmies = armiesOnBorder / trueBorders.size();
		double dispersionPenalty = 0;
		for (Territory border : trueBorders) {
			if (1 < state.boardState.getArmies(border) - averageBorderArmies) {
				dispersionPenalty++;
			}
		}
		double maxBorderArmies = averageBorderArmies;
		double minBorderArmies = averageBorderArmies;
		for (Territory border : trueBorders) {
			if (state.boardState.getArmies(border) > maxBorderArmies) {
				maxBorderArmies = state.boardState.getArmies(border);
			} else if (state.boardState.getArmies(border) < minBorderArmies) {
				minBorderArmies = state.boardState.getArmies(border);
			}
		}
		dispersionPenalty += maxBorderArmies - minBorderArmies;
		double continentSecurity = armiesOnBorder * BORDER_ARMIES_MULTIPLIER
				- dispersionPenalty;
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
