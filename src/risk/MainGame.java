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
	public InstructionPanel instructionPanel;
	public ArrayList<Integer> boardArmies = new ArrayList<Integer>();
	public boolean restartPlaceArmies = false;
	
	
	
	public MainGame()  {
		new StartMenu(this);
		this.instructionPanel = new InstructionPanel(this);
		this.territories = new InitTerritories(this).territories;
		this.board = new Board(this);
		board.updateBackground();
	}
	
	

	public void takeTurn(Player player) {
		for (Territory t : territories) {
			boardArmies.add(t.armies);
		}
		placeArmies(false);
		attack(player);
	}
	
	private void placeArmies(Boolean redo) {
		if(redo) {
			restartPlaceArmies = false;
			int count = 0;
			for (Territory t : territories) {
				t.armies = boardArmies.get(count);
				count++;
			}
			board.updateBackground();
		}
		int armies = activePlayer.getTerritories().size()/ 3;
		if (armies < 3) {
			armies = 3;
		}
		activePlayer.armiesToPlace = armies;
		phase = "placeArmies";
		instructionPanel.setText(instructionPanel.newVisible,
				"Distribute " + armies + " armies between your territories by clicking on the territory's army indicator. To start over, click RESTART",
				"Continue",
				"Restart");
		synchronized (lock) {
			while(activePlayer.armiesToPlace > 0 && !restartPlaceArmies) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if(restartPlaceArmies) {
			placeArmies(true); 
		} else {
			instructionPanel.setText(instructionPanel.newVisible,
					"You have placed all of your armies. If you would like to replace armies again click PLACE AGAIN, otherwise click CONTINUE",
					"Continue",
					"Place Again");
		}
			
		
		
	}
	
	private void attack(Player player) {
		phase = "attackTo";
		int choice = JOptionPane.showConfirmDialog(null, "If you would like to attack, click yes. Then click the territory you would like to attack");
		if (choice != JOptionPane.YES_OPTION) {
		} else {
			synchronized (lock) {
				while(phase.equals("attackTo")) {
					try {	
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			int choice2 = JOptionPane.showConfirmDialog(null, "If you would like to attack " + activePlayer.territroyToAttack.name + " click yes. Then click the territory you would like to attack from");
			if (choice2 != JOptionPane.YES_OPTION) {
				attack(player);
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		MainGame game = new MainGame();
		game.activePlayer = game.players.get(0);
		game.takeTurn(game.players.get(0));
	}

}
