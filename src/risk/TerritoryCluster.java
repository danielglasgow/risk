package risk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * A TerritoryCluster is an object which represents a group of contiguous
 * territories not controlled by a given player within a continent.
 * 
 * @author danielglasgow
 */
public class TerritoryCluster implements Iterable<Territory> {

    private final BoardState boardState;
    private final Set<Territory> territories;
    private final Continent continent;
    private final Player player;
    private final Set<Territory> playerTerritories;

    // This is computed later if needed.
    private final List<AttackRoute> attackRoutes = new ArrayList<AttackRoute>();

    public TerritoryCluster(BoardState boardState, Territory territory,
            Continent continent, Player player) {
        this.boardState = boardState;
        this.continent = continent;
        this.player = player;
        this.territories = buildCluster(territory);
        this.playerTerritories = getAdjacentPlayerTerritories();
    }

    public static Set<TerritoryCluster> generateTerritoryClusters(
            Player player, Continent continent, BoardState boardState) {
        Set<TerritoryCluster> territoryClusters = Sets.newHashSet();
        for (Territory territory : continent.getTerritories()) {
            if (boardState.getPlayer(territory) != player) {
                territoryClusters.add(new TerritoryCluster(boardState,
                        territory, continent, player));
            }
        }
        return territoryClusters;
    }

    /**
     * Creates every possible attack route in the cluster and adds them to the
     * list attackRoutes.
     */
    public void makeRoutes() {
        for (Territory baseTerritory : playerTerritories) {
            List<AttackRoute> attackRoutes = new ArrayList<AttackRoute>();
            List<AttackRoute> newRoutes = new ArrayList<AttackRoute>();
            List<AttackRoute> oldRoutes = new ArrayList<AttackRoute>();
            AttackRoute baseRoute = new AttackRoute(boardState);
            baseRoute.add(baseTerritory);
            attackRoutes.add(baseRoute);
            boolean stillWorking = true;
            while (stillWorking) {
                stillWorking = false;
                for (AttackRoute route : attackRoutes) {
                    oldRoutes.add(route);
                    Territory extensionTerritory = route.get(route.size() - 1);
                    for (Territory territory : extensionTerritory
                            .getAdjacents()) {
                        if (!route.contains(territory)
                                && territories.contains(territory)
                                && boardState.getPlayer(territory) != player) {
                            stillWorking = true;
                            AttackRoute newRoute = new AttackRoute(boardState,
                                    route);
                            newRoute.add(territory);
                            newRoutes.add(newRoute);
                        }
                    }
                }
                this.attackRoutes.addAll(newRoutes);
                attackRoutes.clear();
                attackRoutes.addAll(newRoutes);
                newRoutes.clear();
                attackRoutes.removeAll(oldRoutes);
                oldRoutes.clear();
            }
        }
    }

    /**
     * Returns a list of all the territories in the continent (which by
     * definition are the player's territories) that border the cluster's
     * territories.
     */
    private Set<Territory> getAdjacentPlayerTerritories() {
        Set<Territory> playerTerritories = new HashSet<Territory>();
        for (Territory clusterTerritory : territories) {
            for (Territory borderTerritory : clusterTerritory.getAdjacents()) {
                if (continent.getTerritories().contains(borderTerritory)
                        && boardState.getPlayer(borderTerritory) == player) {
                    playerTerritories.add(borderTerritory);
                }
            }
        }
        return playerTerritories;
    }

    /**
     * Starting from a given enemy territory, builds the list of contiguous
     * enemy territories within this continent.
     */
    private Set<Territory> buildCluster(Territory territory) {
        Set<Territory> cluster = Sets.newHashSet();
        Set<Territory> newAdjacents = Sets.newHashSet();
        Set<Territory> adjacents = Sets.newHashSet();
        newAdjacents.add(territory);
        cluster.addAll(newAdjacents);
        int clusterSize = 0;
        while (clusterSize != cluster.size()) {
            clusterSize = cluster.size();
            adjacents.addAll(newAdjacents);
            newAdjacents.clear();
            for (Territory adjacent : adjacents) {
                newAdjacents.addAll(qualifyingNeighbors(adjacent));
            }
            cluster.addAll(newAdjacents);
        }
        return cluster;
    }

    /**
     * A helper function to buildCluster.
     */
    private ArrayList<Territory> qualifyingNeighbors(Territory territory) {
        ArrayList<Territory> adjacents = new ArrayList<Territory>();
        for (Territory adjacent : territory.getAdjacents()) {
            if (boardState.getPlayer(adjacent) != player
                    && continent.getTerritories().contains(adjacent)) {
                adjacents.add(adjacent);
            }
        }
        return adjacents;
    }

    public static boolean compareClusters(TerritoryCluster territoryCluster1,
            TerritoryCluster territoryCluster2) {
        for (Territory t : territoryCluster1.territories) {
            if (!territoryCluster2.territories.contains(t)) {
                return false;
            }
        }
        return true;
    }

    public List<AttackRoute> getAttackRoutes() {
        return attackRoutes;
    }

    public String toString() {
        return territories.toString();
    }

    @Override
    public Iterator<Territory> iterator() {
        return territories.iterator();
    }

    public Set<Territory> getTerritories() {
        return territories;
    }

    @Override
    public boolean equals(Object o) {
        TerritoryCluster territoryCluster = (TerritoryCluster) o;
        return territories.equals(territoryCluster.getTerritories());

    }

    @Override
    public int hashCode() {
        return territories.hashCode();
    }

}
