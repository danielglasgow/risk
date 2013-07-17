package risk;

import java.util.ArrayList;

public class Turn {
	
	public MainGame game;
	private ArrayList<Integer> boardArmies = new ArrayList<Integer>();
	public boolean restartPlaceArmies = false;
	public Player player;
	public String phase;
	public InstructionPanel instructionPanel; 
	public boolean nextPhase = false;
	
	
	public Turn(MainGame game) {
		this.game = game;
	}
	
	
	public void takeTurn(Player player) {
		for (Territory t : game.territories) {
			boardArmies.add(t.armies);
		}
		this.instructionPanel = game.instructionPanel;
		this.player = player;
		
		phase = "placeArmies";
		
		while(true) {
			if(phase.equals("placeArmies")) {
				placeArmies();
			}
			System.out.println("moving to attack");
			if(phase.equals("attackTo")) {
				attack();
			}
		}
	}
	
	private void placeArmies() {
		if(restartPlaceArmies) {
			restartPlaceArmies = false;
			int count = 0;
			for (Territory t : game.territories) {
				t.armies = boardArmies.get(count);
				count++;
			}
			game.board.updateBackground();
		}
		int armies = player.getTerritories().size() / 3;
		if (armies < 3) {
			armies = 3;
		}
		player.armiesToPlace = armies;
		instructionPanel.setText(instructionPanel.newVisible,
				"Distribute " + armies + " armies between your territories by clicking on the territory's army indicator. To start over, click Restart",
				"Continue",
				"Restart");
		synchronized (game.lock) {
			while(player.armiesToPlace > 0 && !restartPlaceArmies) {
				try {
					game.lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if(!restartPlaceArmies) {
			instructionPanel.setText(instructionPanel.newVisible,
					"You have placed all of your armies. If you would like to replace armies again click Place Again, otherwise click Continue",
					"Continue",
					"Place Again");
		}
		synchronized (game.lock) {
			while(phase.equals("placeArmies") && !restartPlaceArmies) {
				try {
					System.out.println("Frozen");
					game.lock.wait();
					System.out.println("Unfrozen");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("out of loop");
	}
	
	private void attack() {
		nextPhase = false;
		phase = "attackTo";
		instructionPanel.setText(instructionPanel.newVisible,
				"Select the territory you would like to attack.",
				"Continue Without Attacking",
				"Back");
		synchronized (game.lock) {
			while(phase.equals("attackTo")) {
				try {	
					game.lock.wait();
					if(player.territroyToAttack != null) {
						instructionPanel.setText(instructionPanel.newInvisible,
								"If you would like to attack " + player.territroyToAttack.name.toUpperCase() + " click Continue, otherwise select a different territory.",
								"Continue",
								"Back");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
