package risk;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

/**
 * This class calculates a computer player's potential fortification options and
 * returns the BoardStates of all these options.
 */

public class ComputerFortifier {
    private final BoardState boardState;

    public ComputerFortifier(BoardState boardState) {
        this.boardState = boardState;
    }

    private List<Territory> makeFortifyFromOptions(Player player) {
        List<Territory> fortifyFromOptions = Lists.newArrayList();
        for (Territory territory : player.getTerritories(boardState)) {
            if (boardState.getArmies(territory) > 1) {
                fortifyFromOptions.add(territory);
            }
        }
        return fortifyFromOptions;

    }

    private List<Territory> makefortifyToOptions(Player player, Territory territory) {
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
                    if (adjacent != territory && !fortifyToOptions.contains(adjacent)
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

    /**
     * Returns EVERY fortification option belonging to a given player as a
     * BoardState. Every fortification option means every legal fortification a
     * player can make from each of his territories.
     */
    public List<BoardState> getFortificationOptions(Player player) {
        List<BoardState> fortificationOptions = Lists.newArrayList();
        List<Territory> fortifyFromOptions = makeFortifyFromOptions(player);
        for (Territory fortifyFrom : fortifyFromOptions) {
            // System.out.print("FOrtifyFrom: " + fortifyFrom);
            List<Territory> fortifyToOptions = makefortifyToOptions(player, fortifyFrom);
            // System.out.println(" FortufyTOOptions: " + fortifyToOptions);
            for (Territory fortifyTo : fortifyToOptions) {
                for (int i = 1; i <= boardState.getArmies(fortifyFrom); i++) {
                    int armiesToMove = boardState.getArmies(fortifyFrom) - i;
                    BoardState newBoardState = boardState.copy();
                    newBoardState.decreaseArmies(fortifyFrom, armiesToMove);
                    newBoardState.increaseArmies(fortifyTo, armiesToMove);
                    fortificationOptions.add(newBoardState);
                }
            }
        }
        fortificationOptions.add(boardState);
        return fortificationOptions;
    }

}
