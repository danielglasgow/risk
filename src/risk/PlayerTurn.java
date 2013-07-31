package risk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JOptionPane;

public class PlayerTurn {
	
	private final MainGame game;
	private final ArrayList<Integer> boardArmies = new ArrayList<Integer>();
	public boolean restartPlaceArmies = false;
	public Player player;
	public Phase phase;
	public InstructionPanel instructionPanel; 
	public Object phaseLock = new Object();
	public Object lock = new Object();
	public boolean attackWon;
		
	public PlayerTurn(MainGame game) {
		this.game = game;
	}
	
	public void takeTurn(Player player) {
		for (Territory t : game.territories) {
			boardArmies.add(t.armies);
		}
		this.instructionPanel = game.instructionPanel;
		this.player = player;
		
		phase = Phase.PLACE_ARMIES;
		
		boolean endTurn = false;
		while (!endTurn) {
				if (phase == Phase.PLACE_ARMIES) {
					placeArmies();
				} else if (phase == Phase.ATTACK_TO) {
					attackTo();
				} else if (phase == Phase.ATTACK_FROM) {
					attackFrom();
				} else if (phase == Phase.ATTACK) {
					attack();
				} else if (phase == Phase.WON_TERRITORY) {
					wonTerritory();
				} else if (phase == Phase.FORTIFY_SELECTION) {
					fortifySelection();
				} else if (phase == Phase.FORTIFY) {
					fortify();
				} else if (phase == Phase.END_TURN) {
					endTurn = true;
				}
			}
		}
	
	private void placeArmies() {
		if(!restartPlaceArmies) {
			boardArmies.clear();
			for (Territory t : game.territories) {
				boardArmies.add(t.armies);
			}
		} else if(restartPlaceArmies) {
			restartPlaceArmies = false;
			int count = 0;
			for (Territory t : game.territories) {
				t.armies = boardArmies.get(count);
				count++;
			}
			game.board.updateBackground();
		}
		/*int armies = player.getTerritories().size() / 3;
		if (armies < 3) {
			armies = 3;
		}
		armies += checkContinents();
		
		player.armiesToPlace = armies; */
		player.calculateArmiesToPlace();
		
		instructionPanel.setText(InstructionPanel.newVisible,
				player.color.toUpperCase() +  "'s turn! Distribute " + player.armiesToPlace + " armies between your territories by clicking on the territory's army indicator.",
				"Continue",
				"Restart Army Placement");
		synchronized (lock) {
			while(player.armiesToPlace > 0 && !restartPlaceArmies) {
				try {
					lock.wait();
					instructionPanel.setText(InstructionPanel.newVisible,
							player.color.toUpperCase() +  "'s turn! Distribute " + player.armiesToPlace + " armies between your territories by clicking on the territory's army indicator.",
							"Continue",
							"Restart Army Placement");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (!restartPlaceArmies) {
			instructionPanel.setText(InstructionPanel.newVisible,
					"You have placed all of your armies. If you would like to replace armies again click Place Again, otherwise click Continue",
					"Continue",
					"Place Again");
		}
		synchronized (lock) {
			while (phase == Phase.PLACE_ARMIES && !restartPlaceArmies) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void attackTo() {
		instructionPanel.setText(InstructionPanel.newVisible,
				"Attacking from " + player.territoryAttackFrom.name + ".  Select the territory you would like to attack.",
				"---",
				"Choose a Different Territory to Attack From");
		synchronized (lock) {
			while (phase == Phase.ATTACK_TO) {
				try {	
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void attackFrom() {
		player.territoryAttackFrom = null;
		instructionPanel.setText(InstructionPanel.newVisible,
				"Select the territory you would like to attack from",
				"End Attack Phase",
				"---");
		synchronized (lock) {
			while (phase == Phase.ATTACK_FROM) {
				try {	
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void attack() {
		attackWon = false;
		if (player.territoryAttackFrom != null && player.territoryAttackTo != null) {
			synchronized (lock) {
				while (phase == Phase.ATTACK) {
					attackSimulator();
					boolean attackLost = false;
					if (player.territoryAttackFrom.armies < 2 && !attackWon) {
						phase = Phase.ATTACK_FROM;
						attackLost = true;
						JOptionPane.showMessageDialog(null, "You can no longer attack from " + player.territoryAttackFrom.name + " because it only has one army");
					} 
					try {
						if(!attackLost) {
							lock.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void fortifySelection() {
		instructionPanel.setText(InstructionPanel.newVisible,
				"Click on two territories to fortify",
				"Continue",
				"End Turn");
		synchronized (lock) {
			while(phase == Phase.FORTIFY_SELECTION) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String fort2 = "...";
				if (player.fortify2 != null) {
					fort2 = player.fortify2.name + " (click continue or select territories again)";
				} 
				if (player.fortify1 == null) {
					instructionPanel.setText(InstructionPanel.newInvisible, 
							"Click on two territories to fortify",
							"Continue",
							"End Turn");
				} else {
					instructionPanel.setText(InstructionPanel.newInvisible, 
							"Fortify from " + player.fortify1.name +" to " + fort2,
							"Continue",
							"End Turn");
				}
			}
		}
	}
	
	private void attackSimulator() {
		Random random = new Random();
		int attackArmies = player.territoryAttackFrom.armies;
		int defenseArmies = player.territoryAttackTo.armies;
		int[] attackRolls = new int[3];
		int[] defenseRolls = new int[2];
		int attackDice = 1;
		int defenseDice = 1;
		int attackLosses = 0;
		int defenseLosses = 0;
		
		if (attackArmies == 3) {
			attackDice = 2;
		} else if (attackArmies > 3){
			attackDice = 3;
		}
		for (int i = 0; i < attackDice; i++) {
			attackRolls[i] = (random.nextInt(6) + 1);
		}
		
		if (defenseArmies > 1) {
			defenseDice = 2;
		}
		for (int i = 0; i < defenseDice; i++) {
			defenseRolls[i] = (random.nextInt(6) + 1);
		}
		
		Arrays.sort(attackRolls);
		Arrays.sort(defenseRolls);
		
		if(attackRolls[2] > defenseRolls[1]) { //for best of 1 die
			defenseLosses++;
		} else {
			attackLosses++;
			
		}
		
		if (attackArmies > 3 && defenseArmies > 1) { //for best of 2 dice
			if(attackRolls[1] > defenseRolls[0]) {
				defenseLosses++;
			} else {
				attackLosses++;
			}
			
		}
		player.territoryAttackFrom.armies -= attackLosses;
		player.territoryAttackTo.armies -= defenseLosses;
		
		String winMsg = "";
		String newVisibility = InstructionPanel.newInvisible;
		String buttonLeft = "Continue Attacking";
		String buttonRight = "Stop Attacking";
		if (player.territoryAttackTo.armies < 1) {
			winMsg = "You have defeated " + player.territoryAttackTo.name + "!";
			newVisibility = InstructionPanel.newVisible;
			buttonLeft = "Continue";
			buttonRight = "---";
			player.territoryAttackFrom.armies--;
			player.territoryAttackTo.player = player;
			player.territoryAttackTo.armies++;
			attackWon = true;
		}
		game.board.updateBackground();
		instructionPanel.setText(newVisibility,
				"Attack Rolls: " + printRolls(3, attackRolls) + "    Defense rolls: " + printRolls(2, defenseRolls) + "     Attack Loses: " + attackLosses + "    Defense Loses: " + defenseLosses + "    " + winMsg,
				buttonLeft,					
				buttonRight);
	}
	
	private String printRolls(int num, int[] array) {
		String rolls = "" + array[num - 1];
		for (int i = num - 2; i >= 0; i--) {
			rolls = rolls + ", " + array[i];
		}
		return rolls;
	}
	
	private void wonTerritory() {
		instructionPanel.setText(InstructionPanel.newVisible,
				"Click on " + player.territoryAttackTo.name + " to move armies from " + player.territoryAttackFrom.name + ". Click on " + player.territoryAttackFrom.name + " to move armies from " + player.territoryAttackTo.name ,
				"Move All",
				"Continue");
		
		synchronized (lock) {
			while (phase == Phase.WON_TERRITORY) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void fortify() {
		instructionPanel.setText(InstructionPanel.newVisible,
				"Click on " + player.fortify1.name + " to move armies from " + player.fortify2.name + ". Click on " + player.fortify2.name + " to move armies from " + player.fortify1.name ,
				"---",
				"End Turn");
		synchronized (lock) {
			while (phase == Phase.WON_TERRITORY) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
