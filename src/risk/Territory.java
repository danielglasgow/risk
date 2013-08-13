package risk;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class Territory {

	public final String name;
	public final int locX;
	public final int locY;
	private final List<Territory> adjacents = Lists.newArrayList();

	public Territory(String name, int x, int y) {
		this.name = name;
		this.locX = x;
		this.locY = y;
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
