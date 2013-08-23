package risk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class MainGame {
	private static final String TERRITORY_FILENAME = "TerritoryInfo/territories.txt";
	private static final String ADJACENCY_FILENAME = "TerritoryInfo/Adjacentterritories.txt";
	private static final String Continents_FILENAME = "TerritoryInfo/Continents.txt";

	private final List<Player> players = Lists.newArrayList();
	private final BoardState boardState;
	private final ImmutableList<Territory> territories;
	private final ImmutableList<Continent> continents;
	private final ImmutableList<Player> immutablePlayers;

	private boolean editMode = false;

	public MainGame() {
		TerritoriesBuilder territoriesBuilder = new TerritoriesBuilder(
				TERRITORY_FILENAME, ADJACENCY_FILENAME, Continents_FILENAME);
		BoardModel boardModel = territoriesBuilder.build();
		this.territories = boardModel.getTerritories();
		this.continents = boardModel.getContinents();
		this.boardState = new BoardState(territories, new Board(), this);
		this.immutablePlayers = ImmutableList.copyOf(buildImmutablePlayers());
		boardState.getBoard().addMouse(new Mouse(boardState));
	}

	public void startGame() throws InterruptedException {
		if (!loadBoard()) {
			StartMenu startMenu = new StartMenu();
			startMenu.await();
			addPlayers(startMenu.getNumPlayers());
			System.out.println("Players: " + players.size());
			divideTerritories(players.size());
		}
		EditMode editMode = new EditMode(boardState, boardState.getBoard()
				.getInstructionPanel());
		players.add(new Player("editor", null, boardState, editMode, continents));
		boardState.updateBackground();
	}

	private void addPlayers(int numPlayers) {
		HumanStrategy humanStrategy = new HumanStrategy(boardState, boardState
				.getBoard().getInstructionPanel());
		ComputerStrategy computerStrategy = new ComputerStrategy(boardState,
				continents);
		String[] colors = { "red", "blue", "green", "black", "yellow", "orange" };
		for (int i = 1; i <= numPlayers; i++) {
			Player player = new Player("Player" + i, colors[i - 1], boardState,
					i == 1 ? humanStrategy : computerStrategy, continents);
			players.add(player);
		}
	}

	private List<Player> buildImmutablePlayers() {
		List<Player> players = Lists.newArrayList();
		ComputerStrategy computerStrategy = new ComputerStrategy(boardState,
				continents);
		String[] colors = { "red", "blue", "green", "black", "yellow", "orange" };
		for (int i = 1; i <= 6; i++) {
			players.add(new Player("Player" + i, colors[i - 1], boardState,
					computerStrategy, continents));
		}
		return players;
	}

	private void divideTerritories(int numPlayers) {
		List<Territory> mutableTerritories = new ArrayList<Territory>(
				territories);
		Collections.shuffle(mutableTerritories);
		int counter = 0;
		for (Territory territory : mutableTerritories) {
			counter = (counter + 1) % numPlayers;
			Player currentPlayer = players.get(counter);
			boardState.setPlayer(territory, currentPlayer);
		}
	}

	private boolean loadBoard() {
		BoardStateAsserter.assertBoardValues(territories, this,
				new BoardEvaluator2(), immutablePlayers.get(0), continents);
		return BoardStateSaver.loadBoard(boardState);
	}

	public void play() {
		while (true) {
			for (Player player : players) {
				if (editMode) {
					if (player.name.equals("editor")) {
						player.takeTurn();
					}
				} else {
					if (player.hasTerritories()) {
						player.takeTurn();
					}
				}
			}
			int count = 0;
			for (Player player : players) {
				if (player.hasTerritories()) {
					count++;
				}
			}
			if (count == 1) {
				break;
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		MainGame game = new MainGame();
		game.startGame();
		game.play();
	}

	public List<Player> getPlayers() {
		return players;
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

}
