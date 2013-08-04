package risk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainGame {
	private static final String TERRITORY_FILENAME = "TerritoryInfo/territories.txt";
	private static final String ADJACENCY_FILENAME = "TerritoryInfo/Adjacentterritories.txt";
	private static final String Continents_FILENAME = "TerritoryInfo/Continents.txt";

	public final List<Territory> territories;
	public final List<Continent> continents;
	public final ArrayList<Player> players = new ArrayList<Player>();
	public final Object startMenuLock = new Object();

	public Board board;
	public InstructionPanel instructionPanel;

	public MainGame() {
		TerritoriesBuilder territoriesBuilder = new TerritoriesBuilder(
				TERRITORY_FILENAME, ADJACENCY_FILENAME, Continents_FILENAME);
		BoardModel boardModel = territoriesBuilder.build();
		this.territories = boardModel.getTerritories();
		this.continents = boardModel.getContinents();
	}

	public void startGame() throws InterruptedException {
		StartMenu startMenu = new StartMenu();
		startMenu.await();
		addPlayers(startMenu.getNumPlayers());
		divideTerritories(players.size());
		instructionPanel = new InstructionPanel(this);
		board = new Board(this);
		board.updateBackground();
	}

	private void addPlayers(int numPlayers) {
		HumanStrategy humanStrategy = new HumanStrategy(this);
		ComputerStrategy computerStrategy = new ComputerStrategy(this);
		String[] colors = { "red", "blue", "green", "black", "yellow", "orange" };
		for (int i = 1; i <= numPlayers; i++) {
			Player player = new Player("Player" + i, colors[i - 1], this,
					i == 1 ? humanStrategy : computerStrategy);
			players.add(player);
		}
	}

	private void divideTerritories(int numPlayers) {
		List<Territory> mutableTerritories = new ArrayList<Territory>(
				territories);
		Collections.shuffle(mutableTerritories);
		int counter = 0;
		for (Territory t : mutableTerritories) {
			counter = (counter + 1) % numPlayers;
			Player currentPlayer = players.get(counter);
			t.player = currentPlayer;
		}
	}

	public void play() {
		boolean testBool = true;
		while (testBool) {
			for (Player player : players) {
				if (player.hasTerritories()) {
					player.takeTurn();
				}
				// for testing ComputerStrategy
				testBool = false;
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

}
