package risk;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * This class provides the tools to find a player's "true borders" around a
 * continent given a BoardState. A player's "true borders" are all the
 * territories that must be secured in order for a player to defend a continent
 * AND that are adjacent to an enemy territory.
 */

public class BorderFinder {

    /**
     * Given a Player, BoardState, and Continent, findTrueBorders returns that
     * player's "true borders" of the continent.
     */
    public Set<Territory> findTrueBorders(BoardState boardState, Continent continent, Player player) {
        Set<Territory> trueBorders = Sets.newHashSet();
        Set<Territory> potentialBorders = Sets.newHashSet();
        Set<Territory> newPotentialBorders = Sets.newHashSet();
        Set<Territory> notBorders = Sets.newHashSet();
        for (Territory border : continent.getBorders()) {
            if (boardState.getPlayer(border) == player) {
                potentialBorders.add(border);
            }
        }
        while (!potentialBorders.isEmpty()) {
            for (Territory territory : potentialBorders) {
                if (isBorder(boardState, territory)) {
                    trueBorders.add(territory);
                } else {
                    notBorders.add(territory);
                    newPotentialBorders.add(territory);
                }
            }
            potentialBorders.clear();
            for (Territory territory : newPotentialBorders) {
                potentialBorders.addAll(newPotentialBorders(territory, notBorders));
            }
            newPotentialBorders.clear();
        }
        return trueBorders;
    }

    private boolean isBorder(BoardState boardState, Territory territory) {
        Player player = boardState.getPlayer(territory);
        for (Territory adjacent : territory.getAdjacents()) {
            if (boardState.getPlayer(adjacent) != player) {
                return true;
            }
        }
        return false;
    }

    private Set<Territory> newPotentialBorders(Territory territory, Set<Territory> notBorders) {
        Set<Territory> potentialBorders = Sets.newHashSet();
        for (Territory adjacent : territory.getAdjacents()) {
            if (!notBorders.contains(adjacent)) {
                potentialBorders.add(adjacent);
            }
        }
        return potentialBorders;
    }

}
