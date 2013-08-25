package risk;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * This class is a second attempt to build an accurate BoardEvaluator, a class
 * that can take a BoardState and convert how well a player is doing to a number
 * value.
 */

public class BoardEvaluator2 implements BoardEvaluator {

    /**
     * 1 Point for each territory player controls.
     */
    private static final double TERRITORY_BONUS = 1.5;
    /**
     * 1 Point for each army player controls
     */
    private static final int ARMY_BONUS = 1;

    /**
     * -2 Points for every territory with more than 1 army.
     */
    private static final double ARMY_DISPERSION_PENALTY = 0.5;

    /**
     * -2 Points for every cluster in player's goal continent.
     */
    private static final double CLUSTER_PENALTY = 3;

    /**
     * 1 Point for each army adjacent to the biggest cluster in player's goal
     * continent. Those armies must all belong to the same territory. (The
     * territory with the most armies adjacent to the biggest cluster).
     */
    private static final double CLUSTER_BONUS_MULTIPLIER = 2;

    private static final double BORDER_ARMIES_MULTIPLIER = 2;
    private static final double BORDER_ARMIES_DEFLATOR = 0.9;
    private static final double MOST_ARMIES_DEFLATOR = 0.25;
    private static final double MOST_ARMIES_MULTIPLIER = 2.5;
    private static final double ARMY_DISPERSION_INFLATOR = 1.5;
    private static final double ARMY_TERRITORY_RATIO_MULTIPLIER = -100;
    private static final double TARGET_ARMY_FRACTION_MULTIPLIER = 0.0;

    public double getBoardValue(BoardState boardState, Player player, List<Continent> continents) {
        State state = new State(boardState, player, continents);
        double continentBonuses = getContinentBonuses(state);
        double dispersionPenalty = getDispersionPenalty(state);
        double territoryBonus = getTerritoryBonus(state);
        double armyBonus = getArmyBonus(state);
        double clusterBonus = getClusterBonus(state);
        double armyTerritoryRatioBonus = getArmyTerritoryRatioBonus(state);
        double total = continentBonuses + territoryBonus + armyBonus + dispersionPenalty
                + clusterBonus + armyTerritoryRatioBonus;
        System.out.println("Conts: " + continentBonuses + " / Territoires: " + territoryBonus
                + " / Armies: " + armyBonus + " / Dispersion: " + dispersionPenalty
                + " / Cluster: " + round(clusterBonus) + " / Ratio: "
                + round(armyTerritoryRatioBonus) + " Total : " + round(total));
        return total;
    }

    private double round(double number) {
        return ((double) Math.round(number * 10) / 10);
    }

    private double getArmyTerritoryRatioBonus(State state) {
        double territoriesFraction = ((double) state.playerTerritories.size())
                / state.boardState.getTerritories().size();
        double targetArmyFraction = (territoriesFraction * TARGET_ARMY_FRACTION_MULTIPLIER)
                + territoriesFraction;
        int boardArmies = 0;
        int playerArmies = 0;
        for (Territory territory : state.boardState.getTerritories()) {
            boardArmies += state.boardState.getArmies(territory);
            if (state.playerTerritories.contains(territory)) {
                playerArmies += state.boardState.getArmies(territory);
            }
        }
        double armyFraction = ((double) playerArmies) / boardArmies;
        return Math.abs(armyFraction - targetArmyFraction) * ARMY_TERRITORY_RATIO_MULTIPLIER;
    }

    private double getDispersionPenalty(State state) {
        double dispersionPenalty = 0;
        double penaltyCount = 0;
        for (Territory territory : state.playerTerritories) {
            if (state.boardState.getArmies(territory) > 1 && !state.trueBorders.contains(territory)) {
                dispersionPenalty -= ARMY_DISPERSION_PENALTY
                        * Math.pow(ARMY_DISPERSION_INFLATOR, penaltyCount);
                penaltyCount++;
            }
        }
        return dispersionPenalty;
    }

    private double getTerritoryBonus(State state) {
        return state.playerTerritories.size() * TERRITORY_BONUS;
    }

    private double getArmyBonus(State state) {
        double armyBonus = 0;
        for (Territory territory : state.playerTerritories) {
            armyBonus += state.boardState.getArmies(territory) * ARMY_BONUS;
        }
        return armyBonus;
    }

    private double getClusterBonus(State state) {
        double clusterBonus = 0;
        state.goalContinent.setClusters(TerritoryCluster.generateTerritoryClusters(state.player,
                state.goalContinent, state.boardState));
        Set<TerritoryCluster> clusters = state.goalContinent.getClusters();
        if (!clusters.isEmpty()) {
            Iterator<TerritoryCluster> clusterIterator = clusters.iterator();
            List<TerritoryCluster> biggestClusters = Lists.newArrayList();
            biggestClusters.add(clusterIterator.next());
            for (TerritoryCluster cluster : clusters) {
                if (biggestClusters.get(0).getTerritories().size() < cluster.getTerritories()
                        .size()) {
                    biggestClusters.clear();
                    biggestClusters.add(cluster);
                } else if (biggestClusters.get(0).getTerritories().size() == cluster
                        .getTerritories().size()) {
                    biggestClusters.add(cluster);
                }
            }
            Set<Territory> clusterBorders = Sets.newHashSet();
            for (Territory territory : state.playerTerritories) {
                if (bordersCluster(territory, biggestClusters)) {
                    clusterBorders.add(territory);
                }
            }
            clusterBonus = mostArmies(state, clusterBorders) * CLUSTER_BONUS_MULTIPLIER;
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
        for (int i = 1; i < mostArmies; i++) { // This is weird.. look over
            mostArmiesBonus += MOST_ARMIES_MULTIPLIER * Math.pow(MOST_ARMIES_DEFLATOR, i - 1);
        }
        return mostArmiesBonus;
    }

    private boolean bordersCluster(Territory territory, List<TerritoryCluster> biggestClusters) {
        for (Territory adjacent : territory.getAdjacents()) {
            for (TerritoryCluster biggestCluster : biggestClusters) {
                if (biggestCluster.getTerritories().contains(adjacent)) {
                    return true;
                }
            }
        }
        return false;
    }

    private double getContinentBonuses(State state) {
        double continentBonuses = 0;
        for (Continent continent : state.continents) {
            if (isCaptured(state, continent)
                    && state.boardState.getPlayer(continent.getTerritories().get(0)) == state.player) {
                continentBonuses += getContinentBonus(state, continent);
            }
        }
        return continentBonuses;

    }

    private double getContinentBonus(State state, Continent continent) {
        BorderFinder borderFinder = new BorderFinder();
        Set<Territory> trueBorders = borderFinder.findTrueBorders(state.boardState, continent,
                state.player);
        double armiesOnBorderBonus = 0;
        for (Territory border : trueBorders) {
            for (int i = 1; i < state.boardState.getArmies(border); i++) {
                armiesOnBorderBonus += BORDER_ARMIES_MULTIPLIER
                        * Math.pow(BORDER_ARMIES_DEFLATOR, i);
            }
        }
        double armiesInContinent = 0; // to negate cluster bonus which can't be
                                      // earned when a continent is controlled.
        for (Territory territory : continent.getTerritories()) {
            armiesInContinent += state.boardState.getArmies(territory);
        }
        double continentSecurity = armiesOnBorderBonus / trueBorders.size() + 1;
        double continentBonusArmies = continent.getBonusArmies();
        return continentSecurity * continentBonusArmies + armiesInContinent;
    }

    private boolean isCaptured(State state, Continent continent) {
        Set<Player> players = Sets.newHashSet();
        for (Territory territory : continent.getTerritories()) {
            players.add(state.boardState.getPlayer(territory));
        }
        return players.size() == 1;
    }

    /**
     * A class to hold several values, so that the state of the risk board can
     * be passed around easily while calculating a player's boardValue.
     */
    private class State {
        public final BoardState boardState;
        public final Player player;
        public final Continent goalContinent;
        public final List<Continent> continents;
        public final Set<Territory> trueBorders;
        public final Set<Territory> playerTerritories;

        public State(BoardState boardState, Player player, List<Continent> continents) {
            this.boardState = boardState;
            this.player = player;
            this.continents = continents;
            this.goalContinent = getGoalContinent();
            BorderFinder borderFinder = new BorderFinder();
            this.trueBorders = borderFinder.findTrueBorders(boardState, goalContinent, player);
            this.playerTerritories = getPlayerTerritories();
        }

        private Set<Territory> getPlayerTerritories() {
            Set<Territory> playerTerritories = Sets.newHashSet();
            for (Territory territory : boardState.getTerritories()) {
                if (boardState.getPlayer(territory) == player) {
                    playerTerritories.add(territory);
                }
            }
            return playerTerritories;
        }

        private Continent getGoalContinent() {
            List<Continent> continentRatios = Lists.newArrayList();
            for (int i = 0; i < 6; i++) {
                double numTerritories = 0;
                double numTerritoriesControlled = 0;
                double armiesControlled = 0;
                double enemyArmies = 0;
                for (Territory territory : continents.get(i).getTerritories()) {
                    numTerritories++;
                    if (boardState.getPlayer(territory) == player) {
                        numTerritoriesControlled++;
                        armiesControlled += boardState.getArmies(territory);
                    } else {
                        enemyArmies += boardState.getArmies(territory);
                    }
                }
                double ratio = (numTerritoriesControlled + armiesControlled)
                        / (numTerritories + enemyArmies + armiesControlled);
                continents.get(i).ratio = ratio;
                continentRatios.add(continents.get(i));
                Collections.sort(continentRatios);
            }
            return continentRatios.get(0);
        }
    }

}
