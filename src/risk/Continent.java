package risk;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

public class Continent implements Comparable<Continent> {
    private final ImmutableList<Territory> territories;
    private final ImmutableList<Territory> borders;
    private final Set<TerritoryCluster> clusters = Sets.newHashSet();
    private final String name;
    private final int bonusArmies;

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
