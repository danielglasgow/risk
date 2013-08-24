package risk;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * This class represents a risk territory and stores its coordinates on the game
 * board, its name, and a list of its adjacent territories.
 */
public class Territory {

    public final String name;
    public final int coordinateX;
    public final int coordinateY;
    private final List<Territory> adjacents = Lists.newArrayList();

    public Territory(String name, int x, int y) {
        this.name = name;
        this.coordinateX = x;
        this.coordinateY = y;
    }

    public String toString() {
        return name;
    }

    public void addAdjacentTerritory(Territory territory) {
        adjacents.add(territory);
    }

    public ImmutableList<Territory> getAdjacents() {
        return ImmutableList.copyOf(adjacents);
    }

}
