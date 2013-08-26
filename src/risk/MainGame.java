package risk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Provides the implementation to start a risk game. The MainGame, handles the
 * play loop, calls upon other classes to build the game data, and is home to
 * the main method.
 */
public class MainGame {
    private static final String TERRITORY_FILENAME = "TerritoryInfo/territories.txt";
    private static final String ADJACENCY_FILENAME = "TerritoryInfo/Adjacentterritories.txt";
    private static final String Continents_FILENAME = "TerritoryInfo/Continents.txt";

    private final List<Player> activePlayers = Lists.newArrayList();
    private final BoardState boardState;
    private final ImmutableList<Territory> territories;
    private final ImmutableList<Continent> continents;
    private final ImmutableList<Player> immutablePlayers;
    private final ImmutableMap<String, Strategy> strategies;

    private boolean editMode = false;

    public MainGame() {
        TerritoriesBuilder territoriesBuilder = new TerritoriesBuilder(TERRITORY_FILENAME,
                ADJACENCY_FILENAME, Continents_FILENAME);
        BoardModel boardModel = territoriesBuilder.build();
        this.territories = boardModel.getTerritories();
        this.continents = boardModel.getContinents();
        this.boardState = new BoardState(territories, new Board(), this);
        this.immutablePlayers = ImmutableList.copyOf(buildImmutablePlayers());
        this.strategies = ImmutableMap.copyOf(buildStrategies());
        boardState.getBoard().addMouse(new Mouse(boardState));
    }

    public void startGame() throws InterruptedException {
        if (!loadBoard()) {
            StartMenu startMenu = new StartMenu();
            int numPlayers = startMenu.await();
            addPlayers(numPlayers);
            divideTerritories(numPlayers);
        }
        boardState.updateBackground();
    }

    private void addPlayers(int numPlayers) {
        for (int i = 0; i < numPlayers; i++) {
            if (i == -1) {
                immutablePlayers.get(i).setStrategy(strategies.get("HumanStrategy"));
            } else {
                immutablePlayers.get(i).setStrategy(strategies.get("ComputerStrategy"));
            }
        }
    }

    private List<Player> buildImmutablePlayers() {
        List<Player> players = Lists.newArrayList();
        String[] colors = { "red", "blue", "green", "black", "purple", "orange" };
        for (int i = 1; i <= 6; i++) {
            players.add(new Player("Player" + i, colors[i - 1], boardState, continents));
        }
        return players;
    }

    private Map<String, Strategy> buildStrategies() {
        Map<String, Strategy> strategies = Maps.newHashMap();
        strategies.put("EditMode", new EditMode(boardState, boardState.getBoard()
                .getInstructionPanel()));
        strategies.put("ComputerStrategy", new ComputerStrategy(boardState, continents));
        strategies.put("HumanStrategy", new HumanStrategy(boardState, boardState.getBoard()
                .getInstructionPanel()));
        return strategies;

    }

    private void divideTerritories(int numPlayers) {
        List<Territory> mutableTerritories = new ArrayList<Territory>(territories);
        Collections.shuffle(mutableTerritories);
        int counter = 0;
        for (Territory territory : mutableTerritories) {
            counter = (counter + 1) % numPlayers;
            Player currentPlayer = immutablePlayers.get(counter);
            boardState.setPlayer(territory, currentPlayer);
        }
    }

    private boolean loadBoard() {
        BoardStateAsserter.assertBoardValues(territories, this, new BoardEvaluator2(),
                immutablePlayers.get(0), continents);
        return BoardStateSaver.loadBoard(boardState);
    }

    private void setActivePlayers() {
        activePlayers.clear();
        for (Player player : immutablePlayers) {
            if (player.getStrategy() != null) {
                activePlayers.add(player);
            }
        }

    }

    public MainGame play() {
        Player editor = new Player("editor", null, boardState, continents);
        editor.setStrategy(strategies.get("EditMode"));
        while (true) {
            setActivePlayers();
            for (Player player : activePlayers) {
                if (editMode) {
                    editor.takeTurn();
                } else {
                    if (player.hasTerritories()) {
                        player.takeTurn();
                    }
                }
            }
            int count = 0;
            for (Player player : activePlayers) {
                if (player.hasTerritories()) {
                    count++;
                }
            }
            if (count == 1) {
                break;
            }
        }
        return null; // return a new game if the user loads a new board;
    }

    public static void main(String[] args) throws InterruptedException {
        MainGame game = new MainGame();
        while (game != null) {
            game.startGame();
            game = game.play();
        }
    }

    public List<Player> getPlayers() {
        return activePlayers;
    }

    public List<Player> getImmutablePlayers() {
        return immutablePlayers;
    }

    public List<Continent> getContinents() {
        return continents;
    }

    public void setEditMode(boolean b) {
        editMode = b;
    }

    public Map<String, Strategy> getStrategies() {
        return strategies;
    }

}
