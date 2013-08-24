package risk;

import java.util.List;

import javax.swing.JOptionPane;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * This class represents a player of the risk game, storing information such as
 * that player's name, color, and the player's strategy (human or computer).
 * 
 * This class also determines how many armies a player has to place at the
 * beginning of his turn. This feature probably belongs somewhere else.
 */
public class Player {

    public final String color;
    public final String name;
    private final BoardState boardState;
    private final ImmutableList<Continent> continents;

    private Strategy strategy = null;

    public Player(String name, String color, BoardState boardState,
            ImmutableList<Continent> continents) {
        this.boardState = boardState;
        this.name = name;
        this.color = color;
        this.continents = continents;
    }

    /**
     * Returns true if a player has any territories on the board, otherwise
     * returns false;
     */
    public boolean hasTerritories() {
        return (getTerritories(boardState).size() > 0);
    }

    /**
     * Returns the number of armies a player has to place at the beginning of
     * his turn.
     */
    public int getArmiesToPlace(boolean hasPlayer) {
        int armiesToPlace = getTerritories(boardState).size() / 3;
        if (armiesToPlace < 3) {
            armiesToPlace = 3;
        }
        armiesToPlace += checkContinents(hasPlayer);
        return armiesToPlace;
    }

    private boolean hasContinent(Continent continent) {
        boolean hasContinent = true;
        for (Territory t : continent.getTerritories()) {
            if (!getTerritories(boardState).contains(t)) {
                hasContinent = false;
            }
        }
        return hasContinent;
    }

    private int checkContinents(boolean hasPlayer) {
        int extraArmies = 0;
        for (Continent continent : continents) {
            if (hasContinent(continent) && hasPlayer) {
                extraArmies += continent.getBonusArmies();
                JOptionPane.showMessageDialog(null,
                        "You have been awarded " + continent.getBonusArmies()
                                + " extra armies for controling " + continent.getName());
            }
        }
        return extraArmies;
    }

    public void takeTurn() {
        strategy.takeTurn(this);
    }

    /**
     * Returns a list of territories that the player controles.
     */
    public List<Territory> getTerritories(BoardState boardState) {
        List<Territory> playerTerritories = Lists.newArrayList();
        for (Territory territory : boardState.getTerritories()) {
            if (boardState.getPlayer(territory) == this) {
                playerTerritories.add(territory);
            }
        }
        return playerTerritories;
    }

    public Strategy getStrategy() {
        return this.strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
