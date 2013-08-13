package risk;

import java.util.List;

import com.google.common.collect.Lists;

public class BorderFinder {

	public List<Territory> findTrueBorders(BoardState boardState,
			Continent continent, Player player) {
		List<Territory> trueBorders = Lists.newArrayList();
		List<Territory> potentialBorders = Lists.newArrayList();
		List<Territory> foundBorders = Lists.newArrayList();
		List<Territory> notBorders = Lists.newArrayList();
		for (Territory border : continent.getBorders()) {
			if (boardState.getPlayer(border) == player) {
				potentialBorders.add(border);
			}
		}
		while (!potentialBorders.isEmpty()) {
			for (Territory territory : potentialBorders) {
				if (isBorder(boardState, territory)
						&& !foundBorders.contains(territory)) {
					trueBorders.add(territory);
					foundBorders.add(territory);
				} else {
					notBorders.add(territory);
				}
			}
			potentialBorders.clear();
			for (Territory territory : notBorders) {
				potentialBorders.addAll(newPotentialBorders(territory,
						notBorders));
			}
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

	private List<Territory> newPotentialBorders(Territory territory,
			List<Territory> notBorders) {
		List<Territory> potentialBorders = Lists.newArrayList();
		for (Territory adjacent : territory.getAdjacents()) {
			if (!notBorders.contains(adjacent)) {
				potentialBorders.add(adjacent);
			}
		}
		return potentialBorders;
	}

}
