package risk;

import java.util.List;

public class BoardModel {
	
	//Both these should be immutable
	private final List<Territory> territories; 
	private final List<Continent> continents;
	
	public BoardModel(List<Territory> territories, List<Continent> continents) {
		this.territories = territories;
		this.continents = continents;
	}
	
	public List<Territory> getTerritories() {
		return territories;
	}
	
	public List<Continent> getContinents() {
		return continents;
	}

}
