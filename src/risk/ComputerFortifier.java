package risk;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

public class ComputerFortifier {
	private final BoardState boardState;

	public ComputerFortifier(BoardState boardState) {
		this.boardState = boardState;
	}

	private List<Territory> makeFortifyFromOptions(Player player) {
		List<Territory> fortifyFromOptions = Lists.newArrayList();
		for (Territory territory : player.getTerritories()) {
			if (boardState.getArmies(territory) > 1) {
				fortifyFromOptions.add(territory);
			}
		}
		return fortifyFromOptions;

	}

	private List<Territory> makefortifyToOptions(Player player,
			Territory territory) {
		Set<Territory> fortifyToOptions = new HashSet<Territory>();
		List<Territory> newOptions = Lists.newArrayList();
		for (Territory adjacent : territory.getAdjacents()) {
			if (boardState.getPlayer(adjacent) == player) {
				newOptions.add(adjacent);
			}
		}
		while (!newOptions.isEmpty()) {
			fortifyToOptions.addAll(newOptions);
			newOptions.clear();
			for (Territory fortifyOption : fortifyToOptions) {
				for (Territory adjacent : fortifyOption.getAdjacents()) {
					if (adjacent != territory
							&& !fortifyToOptions.contains(adjacent)
							&& boardState.getPlayer(adjacent) == player) {
						newOptions.add(adjacent);
					}
				}
			}
		}
		List<Territory> options = Lists.newArrayList();
		options.addAll(fortifyToOptions);
		return options;

	}

	public List<BoardState> getFortificationOptions(Player player) {
		List<BoardState> fortificationOptions = Lists.newArrayList();
		List<Territory> fortifyFromOptions = makeFortifyFromOptions(player);
		for (Territory fortifyFrom : fortifyFromOptions) {
			System.out.print("FOrtifyFrom: " + fortifyFrom);
			List<Territory> fortifyToOptions = makefortifyToOptions(player,
					fortifyFrom);
			System.out.println(" FortufyTOOptions: " + fortifyToOptions);
			for (Territory fortifyTo : fortifyToOptions) {
				BoardState newBoardState1 = boardState.copy();
				BoardState newBoardState2 = boardState.copy();
				int armiesToMove = boardState.getArmies(fortifyFrom) - 1;
				newBoardState1.decreaseArmies(fortifyFrom, armiesToMove);
				newBoardState1.increaseArmies(fortifyTo, armiesToMove);
				armiesToMove = boardState.getArmies(fortifyFrom) / 2;
				newBoardState2.decreaseArmies(fortifyFrom, armiesToMove);
				newBoardState2.increaseArmies(fortifyTo, armiesToMove);
				fortificationOptions.add(newBoardState1);
				fortificationOptions.add(newBoardState2);
			}
		}
		fortificationOptions.add(boardState);
		return fortificationOptions;
	}

}
