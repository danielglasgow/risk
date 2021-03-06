package risk;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

/**
 * BoardState represents a potential state of the risk board determined by how
 * many armies on each territory and which player controls each territory.
 */
public class BoardState {

    private final Map<Territory, TerritoryState> boardState;
    private final ImmutableList<Territory> territories;
    private final Board board;
    private final MainGame game;

    public BoardState(List<Territory> territories, Board board, MainGame game) {
        this.territories = ImmutableList.copyOf(territories);
        this.boardState = Maps.newHashMap();
        this.board = board;
        this.game = game;
        buildBoardState();
    }

    private BoardState(BoardState boardState) {
        this.boardState = Maps.newHashMap();
        this.territories = boardState.territories;
        this.board = boardState.board;
        this.game = boardState.game;
        this.update(boardState);
    }

    private void buildBoardState() {
        for (Territory territory : territories) {
            boardState.put(territory, new TerritoryState(1, null));
        }
    }

    public int getArmies(Territory territory) {
        return boardState.get(territory).getArmies();
    }

    public Player getPlayer(Territory territory) {
        return boardState.get(territory).getPlayer();
    }

    public void setArmies(Territory territory, int armies) {
        TerritoryState newTerritoryState = boardState.get(territory).setArmies(armies);
        boardState.put(territory, newTerritoryState);
    }

    public void setPlayer(Territory territory, Player player) {
        TerritoryState newTerritoryState = boardState.get(territory).setPlayer(player);
        boardState.put(territory, newTerritoryState);
    }

    public void decreaseArmies(Territory territory, int n) {
        int currentArmies = boardState.get(territory).getArmies();
        setArmies(territory, currentArmies - n);
    }

    public void increaseArmies(Territory territory, int n) {
        int currentArmies = boardState.get(territory).getArmies();
        setArmies(territory, currentArmies + n);
    }

    public List<Territory> getTerritories() {
        return territories;
    }

    public void updateBackground() {
        board.updateBackground(this);
    }

    public BoardState copy() {
        return new BoardState(this);
    }

    public TerritoryState getTerritoryState(Territory territory) {
        return boardState.get(territory);
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Updates boardState so that it is identical to the boardState being passed
     * in.
     */
    public void update(BoardState boardState) {
        for (Territory territory : territories) {
            this.boardState.put(territory, boardState.getTerritoryState(territory));
        }
    }

    public List<Continent> getContinents() {
        return game.getContinents();
    }

    public List<Player> getPlayers() {
        return game.getPlayers();
    }

    public MainGame getGame() {
        return game;
    }

}
