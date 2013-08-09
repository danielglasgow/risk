package risk;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * BoardState represents a potential state of the risk board determined by how
 * many armies on each territory and which player controls each territory.
 * 
 */
public class BoardState {

	private List<Territory> boardState;

	public BoardState(List<Territory> territories) {
		buildBoardState(territories);
	}

	private void buildBoardState(List<Territory> territories) {
		boardState = Lists.newArrayList();
		for (Territory territory : territories) {
			Territory territoryCopy = new Territory(territory.name, 0, 0);
			territoryCopy.armies = territory.armies;
			territoryCopy.player = territory.player;
			boardState.add(territoryCopy);
		}
	}

	private Territory getTerritory(Territory territory) {
		Territory matchTerritory = null;
		for (Territory potentialMatch : boardState) {
			if (potentialMatch.name.equals(territory.name)) {
				matchTerritory = potentialMatch;
			}
		}
		return matchTerritory;
	}

	public void setTerritoryArmies(Territory territory, int armies) {
		getTerritory(territory).armies = armies;
	}

	public void setTerritoryPlayer(Territory territory, Player player) {
		getTerritory(territory).player = player;
	}

	public List<Territory> getTerritories() {
		return boardState;
	}

}
