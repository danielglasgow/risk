package risk;

import java.util.List;

import com.google.common.collect.Lists;

public class BasicBoardEvaluator implements BoardValue {

	private static final double ARMY_MULTIPLIER = 1.25;
	private static final double CONTINENT_BONUS = 0.25;
	private static final double BORDER_BONUS_DEFLATOR = 0.95;

	public BasicBoardEvaluator() {

	}

	@Override
	public int getBoardValue(BoardState boardState, Player player,
			Continent goalContinent) {
		State state = new State(boardState, player, goalContinent);
		double armyScore = getArmyScore(state);
		double continentScore = getContinentScore(state);
		int score = (int) Math.round(armyScore + continentScore);
		return score;
	}

	private double getContinentScore(State state) {
		double score = 0;
		int territoriesControlled = 0;
		boolean controlsContinent = false;
		for (Territory territory : state.goalContinent.getTerritories()) {
			if (state.boardState.getPlayer(territory) == state.player) {
				score += CONTINENT_BONUS;
				territoriesControlled++;
			}
		}
		if (territoriesControlled == state.goalContinent.getTerritories()
				.size()) {
			score += state.goalContinent.bonusArmies;
			controlsContinent = true;
		}
		if (controlsContinent) {
			for (Territory territory : state.goalContinent.getBorders()) {
				int armies = state.boardState.getArmies(territory);
				for (int i = 0; i < armies; i++) {
					score += Math.pow(BORDER_BONUS_DEFLATOR, i);
				}
			}
		} else {
			score -= state.goalContinent.getClusters().size() * 2;
		}
		return score;
	}

	private double getArmyScore(State state) {
		double score = 0;
		for (Territory territory : state.playerTerritories) {
			score += state.boardState.getArmies(territory) * ARMY_MULTIPLIER;
		}
		return score;
	}

	private class State {

		public final BoardState boardState;
		public final Player player;
		public final Continent goalContinent;
		public final List<Territory> playerTerritories;
		public final List<Territory> enemyTerritories;

		public State(BoardState boardState, Player player,
				Continent goalContinent) {
			this.boardState = boardState;
			this.player = player;
			this.goalContinent = goalContinent;
			this.playerTerritories = Lists.newArrayList();
			this.enemyTerritories = Lists.newArrayList();
			sortTerritoriesByPlayer(player, boardState);
		}

		/**
		 * Builds playerTerritories and enemyTerritories by sorting through the
		 * a list of territories and adding all territories that belong to
		 * player to playerTerritories and all other territories to
		 * enemyTerritories.
		 */
		private void sortTerritoriesByPlayer(Player player,
				BoardState boardState) {
			for (Territory territory : boardState.getTerritories()) {
				if (boardState.getPlayer(territory) == player) {
					playerTerritories.add(territory);
				} else {
					enemyTerritories.add(territory);
				}
			}
		}
	}

}
