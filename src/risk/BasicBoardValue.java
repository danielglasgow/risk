package risk;

import java.util.List;

import com.google.common.collect.Lists;

public class BasicBoardValue implements BoardValue {

	private final List<Territory> playerTerritories;
	private final List<Territory> enemyTerritories;

	// private final MainGame game;

	public BasicBoardValue() {
		// this.game = game;
		this.playerTerritories = Lists.newArrayList();
		this.enemyTerritories = Lists.newArrayList();
	}

	@Override
	public int getBoardValue(Player player, List<Territory> territories,
			Continent goalContinent) {
		reset();
		sortTerritoriesByPlayer(player, territories);
		double armyScore = getArmyScore();
		double continentScore = getContinentScore(goalContinent, player);
		// Continent goalContinent = setGoalContinent();
		int score = (int) Math.round(armyScore + continentScore);
		return score;
	}

	private double getContinentScore(Continent goalContinent, Player player) {
		double score = 0;
		int territoriesControlled = 0;
		boolean controlsContinent = false;
		for (Territory territory : goalContinent.territories) {
			if (territory.player == player) {
				score += 0.25;
				territoriesControlled++;
			}
		}
		if (territoriesControlled == goalContinent.territories.size()) {
			score += goalContinent.bonusArmies;
			controlsContinent = true;
		}
		if (controlsContinent) {
			for (Territory territory : goalContinent.borders) {
				int armies = territory.armies;
				for (int i = 0; i < armies; i++) {
					score += Math.pow(0.95, i);
				}
			}
		} else {
			score -= goalContinent.getClusters().size() * 2;
		}
		return score;
	}

	private double getArmyScore() {
		double score = 0;
		for (Territory territory : playerTerritories) {
			score += territory.armies * 1.25;
		}
		return score;
	}

	/*
	 * Builds playerTerritories and enemyTerritories by sorting through the a
	 * list of territories and adding all territories that belong to player to
	 * playerTerritories and all other territories to enemyTerritories.
	 */
	private void sortTerritoriesByPlayer(Player player,
			List<Territory> territories) {
		for (Territory territory : territories) {
			if (territory.player == player) {
				playerTerritories.add(territory);
			} else {
				enemyTerritories.add(territory);
			}
		}

	}

	/*
	 * Reset's instance field states so a BoardValue can be calculated for a new
	 * player based off a new board setup
	 */
	private void reset() {
		playerTerritories.clear();
		enemyTerritories.clear();

	}

}
