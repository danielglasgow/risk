package risk;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class BoardModel {
	private final ImmutableList<Territory> territories;
	private final ImmutableList<Continent> continents;

	public BoardModel(List<Territory> territories, List<Continent> continents) {
		this.territories = ImmutableList.copyOf(territories);
		this.continents = ImmutableList.copyOf(continents);
	}

	public ImmutableList<Territory> getTerritories() {
		return territories;
	}

	public ImmutableList<Continent> getContinents() {
		return continents;
	}

}
