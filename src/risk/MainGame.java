package risk;

import java.util.ArrayList;

public class MainGame {
	public ArrayList<Territory> territories =  new ArrayList<Territory>();
	public static ArrayList<Player> players =  new ArrayList<Player>();
	public ArrayList<Continent> continents = new ArrayList<Continent>();
	public Board board;
	public Object startMenuLock = new Object();
	public InstructionPanel instructionPanel;
	public PlayerTurn playerTurn;
	public ComputerTurn compTurn;
	public boolean wait = true;

	public MainGame()  {
		this.playerTurn = new PlayerTurn(this);	
	}
	
	public void startGame() {
		new StartMenu(this);
		while (wait) {
			synchronized (startMenuLock) {
				try {
					startMenuLock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		

		/* this.compTurn = new ComputerTurn(this); */
		this.instructionPanel = new InstructionPanel(this);
		
		this.territories = new InitTerritories(this).territories;
		this.board = new Board(this);
		board.updateBackground();
	}

	public void play() {
		int count = 0;
		while (true) {
			playerTurn.takeTurn(MainGame.players.get(count % players.size()));
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
	
	public static void main(String[] args) {
		MainGame game = new MainGame();
		game.startGame();
		game.play();
	}

}
