package risk;

import java.util.List;

/**
 * Determines a given player's BoardValue, an integer score of how well that
 * player is doing according to a board set up (how many armies on each
 * territory and who controls each territory). The higher a player's BoardValue,
 * the better that player is doing. The player with the highest BoardValue of
 * all the players in a given risk game is considered to be winning.
 */
public interface BoardEvaluator {

	double getBoardValue(BoardState boardState, Player player,
			List<Continent> continents);

}
