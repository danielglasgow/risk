package risk;

import java.util.ArrayList;

public class Turn {
	
	public MainGame game;
	private ArrayList<Integer> boardArmies = new ArrayList<Integer>();
	public boolean restartPlaceArmies = false;
	public Player player;
	public String phase;
	public InstructionPanel instructionPanel; 
	
	
	
	
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
			synchronized (game.lock) {
			if(phase.equals("placeArmies")) {
				placeArmies();
			} else if(phase.equals("attackTo")) {
				attackTo();
			} else if(phase.equals("attackFrom")) {
				attackFrom();
			} else if(phase.equals("attack")) {
				attack();
			}
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
					game.lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void attackTo() {
		instructionPanel.setText(instructionPanel.newVisible,
				"Select the territory you would like to attack.",
				"Continue Without Attacking",
				"Choose a Different Territory to Attack From");
		synchronized (game.lock) {
			while(phase.equals("attackTo")) {
				try {	
					game.lock.wait();
					if(player.territoryToAttack != null) {
						instructionPanel.setText(instructionPanel.newVisible,
								"If you would like to attack " + player.territoryToAttack.name.toUpperCase() + " click Continue, otherwise select a different territory to attack.",
								"Continue",
								"Choose a Different Territory to Attack From");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void attackFrom() {
		instructionPanel.setText(instructionPanel.newVisible,
				"Select the territory you would like to attack from",
				"Continue Without Attacking",
				"Place Armies Again");
		synchronized (game.lock) {
			while(phase.equals("attackFrom")) {
				try {	
					game.lock.wait();
					if(player.territoryAttackFrom != null) {
						instructionPanel.setText(instructionPanel.newInvisible,
								"If you would like to attack from " + player.territoryAttackFrom.name.toUpperCase() + " click Continue, otherwise select a different territory.",
								"Continue",
								"Place Armies Again");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void attack() {
		instructionPanel.setText(instructionPanel.newVisible,
				"Results From Battle",
				"Continue Attacking",
				"Stop Attacking");
		if (player.territoryAttackFrom != null && player.territoryToAttack != null) {
			System.out.println("Attack from " + player.territoryAttackFrom.name + " to " + player.territoryToAttack);
		} else {
		//System.out.println("Attack Phase skipped");
		}
	}

}
