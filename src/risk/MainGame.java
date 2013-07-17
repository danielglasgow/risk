package risk;


import java.util.ArrayList;


public class MainGame {
	
	public ArrayList<Territory> territories =  new ArrayList<Territory>();
	public ArrayList<Player> players =  new ArrayList<Player>();
	public Board board;
	public Object lock = new Object();
	public InstructionPanel instructionPanel;
	public Turn turn;

	public MainGame()  {
		new StartMenu(this);
		this.turn = new Turn(this);
		this.instructionPanel = new InstructionPanel(this);
		
		this.territories = new InitTerritories(this).territories;
		this.board = new Board(this);
		board.updateBackground();
	}
	
	public static void main(String[] args) {
		MainGame game = new MainGame();
		game.turn.takeTurn(game.players.get(0));
	}

}
