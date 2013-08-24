package risk;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

/**
 * This class represents a list of fixed territories that make up a continent as
 * well as a list of the continent's 'natural borders', territories that are
 * adjacent to at least one territory outside the continent. This class also
 * holds the number of bonus armies a player is awarding for controlling every
 * territory in the continent.
 * 
 * Right now this class also stores territory clusters (groups of contiguous
 * territories not controlled by specified player), and ratio, a value
 * representing a given player's control of a continent. The ratio is used to
 * compare continents (thus comparable implementation), in order to determine
 * which continent a player is best suited to capture. Both these values,
 * especially ratio, do not belong inside of continent.
 */

public class Continent implements Comparable<Continent> {
    private final ImmutableList<Territory> territories;
    private final ImmutableList<Territory> borders;
    private final Set<TerritoryCluster> clusters = Sets.newHashSet();
    private final String name;
    private final int bonusArmies;

    // NEEDS TO BE FIXED
    public double ratio;

    public Continent(String name, int bonusArmies, List<Territory> territories,
            List<Territory> borders) {
        this.name = name;
        this.bonusArmies = bonusArmies;
        this.territories = ImmutableList.copyOf(territories);
        this.borders = ImmutableList.copyOf(borders);
    }

    public String toString() {
        String continent = name + ": " + ratio;
        return continent;
    }

    @Override
    public int compareTo(Continent continent) {
        if (continent.ratio > ratio) {
            return 1;
        } else if (continent.ratio < ratio) {
            return -1;
        }
        return 0;
    }

    public List<Territory> getTerritories() {
        return territories;
    }

    public Set<TerritoryCluster> getClusters() {
        return clusters;
    }

    public void setClusters(Set<TerritoryCluster> newClusters) {
        clusters.clear();
        clusters.addAll(newClusters);
    }

    public List<Territory> getBorders() {
        return borders;
    }

    public int getBonusArmies() {
        return bonusArmies;
    }

    public String getName() {
        return name;
    }
}
