package risk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainGame {
	public List<Territory> territories;
	public List<Continent> continents;
	public final ArrayList<Player> players = new ArrayList<Player>();
	public Board board;
	public Object startMenuLock = new Object();
	public InstructionPanel instructionPanel;
	public PlayerTurn playerTurn;
	public ComputerTurn compTurn;
	public boolean wait = true;

	private static final String TERRITORY_FILENAME = "TerritoryInfo/territories.txt";
	private static final String ADJACENCY_FILENAME = "TerritoryInfo/Adjacentterritories.txt";
	private static final String Continents_FILENAME = "TerritoryInfo/Continents.txt";

	public MainGame() {
		this.playerTurn = new PlayerTurn(this);
		this.compTurn = new ComputerTurn(this);
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
		String[] colors = { "red", "blue", "green", "black", "yellow", "orange" };
		for (int i = 1; i <= numPlayers; i++) {
			Player player = new Player("Player" + i, colors[i - 1], this);
			players.add(player);
		}
	}

	private void divideTerritories(int numPlayers) {
		Collections.shuffle(territories);
		int counter = 0;
		for (Territory t : territories) {
			counter = (counter + 1) % numPlayers;
			Player currentPlayer = players.get(counter);
			t.player = currentPlayer;
		}
	}

	public void play() {
		int count = 0;
		while (true) {
			playerTurn.takeTurn(players.get(count % players.size()));
			count++;
		}
	}

	public Territory getTerritory(String name) {
		Territory territory = new Territory("Blank", 0, 0);
		for (Territory t : territories) {
			if (t.name.equals(name)) {
				territory = t;
			}
		}
		return territory;
	}

	public static void main(String[] args) throws InterruptedException {
		MainGame game = new MainGame();
		game.startGame();
		game.play();
	}

}
