package risk;


import java.util.ArrayList;
import javax.swing.JOptionPane;




public class MainGame {
	
	public ArrayList<Territory> territories =  new ArrayList<Territory>();
	public boolean condition = true;
	public ArrayList<Player> players =  new ArrayList<Player>();
	public String phase;
	public Player activePlayer;
	public Board board;
	public Object lock = new Object();
	
	
	public MainGame()  {
		new StartMenu(this);
		this.territories = new InitTerritories(this).territories;
		this.board = new Board(this);
		board.updateBackground();
	}
	
	

	public void takeTurn(Player player) {
		ArrayList<Integer> boardArmies = new ArrayList<Integer>();
		for (Territory t : territories) {
			boardArmies.add(t.armies);
		}
		placeArmies(player, boardArmies, false);
	}
	
	private void placeArmies(Player player, ArrayList<Integer> boardArmies, Boolean redo) {
		if(redo) {
			int count = 0;
			for (Territory t: territories) {
				t.armies = boardArmies.get(count);
				count++;
			}
			board.updateBackground();
		}
		int armies = player.getTerritories().size()/ 3;
		if (armies < 3) {
			armies = 3;
		}
		player.armiesToPlace = armies;
		phase = "placeArmies";
		JOptionPane.showMessageDialog(null, "Place " + armies + " armies on your territories by clicking on the territory's army indicator");
		synchronized (lock) {
			while(player.armiesToPlace > 0) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		int choice = JOptionPane.showConfirmDialog(null, 
				"You have placed all your armies. \nYES to continue. NO place your armies again.");
		if (choice != JOptionPane.YES_OPTION) {
			placeArmies(player, boardArmies, true);
		}
		
	}
	
	//private void attack(Player player) {
		//int choice = JOptionPane
	//}
	
	
	
	public static void main(String[] args) {
		MainGame game = new MainGame();
		game.activePlayer = game.players.get(0);
		game.takeTurn(game.players.get(0));
	}

}
