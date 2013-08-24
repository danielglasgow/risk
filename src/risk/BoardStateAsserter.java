package risk;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * This class compares the BoardValues of different BoardStates using a given
 * BoardEvaluator. Each comparison has been previously reviewed such that one
 * BoardState must ALWAYS return a higher value than the other. If it doesn't
 * the assert warns the developer, who then knows the new BoardEvaluator is
 * broken.
 */

public abstract class BoardStateAsserter {

    public static void assertBoardValues(List<Territory> territories, MainGame game,
            BoardEvaluator evaluator, Player player, List<Continent> continents) {
        String directory = "/Users/danielglasgow/Documents/Source/workspace/risk/SavedBoardStates/asserts/";
        BoardState boardStateWorse = new BoardState(territories, null, game);
        BoardState boardStateBetter = new BoardState(territories, null, game);
        Map<String, String> comparisons = Maps.newHashMap();
        comparisons.put("BadRoute.csv", "LotR.csv");
        for (String worseBoard : comparisons.keySet()) {
            String betterBoard = comparisons.get(worseBoard);
            BoardStateSaver.loadBoard(boardStateWorse, directory + worseBoard);
            BoardStateSaver.loadBoard(boardStateBetter, directory + betterBoard);
            double betterBoardScore = evaluator.getBoardValue(boardStateBetter, player, continents);
            double worseBoardScore = evaluator.getBoardValue(boardStateWorse, player, continents);
            assert betterBoardScore > worseBoardScore;
            System.out.println("Successful Assert");
        }
    }
}
