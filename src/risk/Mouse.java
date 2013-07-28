package risk;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Mouse implements MouseListener {
	
	private Board board;
	private MainGame game;
	private PlayerTurn turn;
	private int x;
	private int y;
	private Territory territory;
	
	
	public Mouse(Board board) {
		this.board = board;
		this.game = board.game;
		this.turn = game.playerTurn;
	}

	

	public void mouseClicked(MouseEvent e) {
		game.instructionPanel.newIndicator.setText(turn.instructionPanel.newInvisible);
		PointerInfo a = MouseInfo.getPointerInfo();
		Point point = new Point(a.getLocation());
		SwingUtilities.convertPointFromScreen(point, e.getComponent());
		x = (int) point.getX();
		y = (int) point.getY();
		territory = findMatch(x, y);
		if (!territory.name.equals("NoMatch")) {
			if (turn.phase.equals("placeArmies")) {
			placeArmies();
			} else if (turn.phase.equals("attackFrom")) {
			attackFrom();
			} else if (turn.phase.equals("attackTo")) {
				attackTo();
			} else if (turn.phase.equals("wonTerritory")) {
				wonTerritory();
			} else if (turn.phase.equals("fortifySelection")) {
				fortifySelection();
			} else if (turn.phase.equals("fortify")) {
				fortify();
			}
		} else {
			JOptionPane.showMessageDialog(null,"Make sure to click on the army indicator in order select a territory");
		}
		
		board.updateBackground();
	}
	
	private void fortifySelection() {
		if (!territory.player.equals(game.playerTurn.player)) {
			JOptionPane.showMessageDialog(null, "You must fortify between to territories you control");
		} else {
			synchronized (turn.lock) {
				if(turn.player.fortify2 == null && turn.player.fortify1 != null) {
					if (hasPath(turn.player.fortify1, territory)) {
						turn.player.fortify2 = territory;
					} else {
						turn.player.fortify1 = null;
						JOptionPane.showMessageDialog(null, "There must be a contiguous path of territories you control in order to fortify between two territories");
					}
				} else {
					turn.player.fortify1 = territory;
					turn.player.fortify2 = null;
				}
				turn.lock.notifyAll();
			}	
		}
	}
	
	private boolean hasPath(Territory startTerritory, Territory endTerritory) {
		Set<Territory> territories = new HashSet<Territory>();
		territories.addAll(game.territories);
		
		Set<Territory> nextSet = adjacentControlledTerritories(startTerritory, territories);
		Set<Territory> currentSet = new HashSet<Territory>();
		territories.remove(startTerritory);
		while(!(currentSet.contains(endTerritory))) {
			System.out.println(nextSet.size());
			if (nextSet.size() < 1) {
				System.out.println("entered");
				return false;
			}
			territories.removeAll(nextSet);
			currentSet.addAll(nextSet);
			nextSet.clear();
			System.out.println(nextSet);
			for (Territory t : currentSet) {
				nextSet.addAll(adjacentControlledTerritories(t, territories));
			}
		}
		return true;
	}
	
	
	private Set<Territory> adjacentControlledTerritories(Territory territory, Set<Territory> territories) {
		Set<Territory> adjacentControlled = new HashSet<Territory>();
		for (Territory t : territory.adjacents) {
			if(t.player.equals(turn.player) && territories.contains(t)) {
				adjacentControlled.add(t);
			}
		}
		return adjacentControlled;
	}



	private void placeArmies() {
		Player player = turn.player;
		if (player.armiesToPlace > 0) {
			if(territory.player.name.equals(player.name)) {
				synchronized (turn.lock) {
					territory.armies++;
					player.armiesToPlace--;
					turn.lock.notifyAll();
				}
			} else {
				JOptionPane.showMessageDialog(null, "You must place armies on territories you control");
			}
		}
	}
	
	private void attackTo() {
		if (!game.playerTurn.player.territoryAttackFrom.adjacents.contains(territory)) {
			JOptionPane.showMessageDialog(null, "You must attack a territory adjacent to " + game.playerTurn.player.territoryAttackFrom.name);
		} else if (territory.player.equals(game.playerTurn.player)) {
			JOptionPane.showMessageDialog(null, "You cannot attack a territory you control");
		} else {
			game.playerTurn.player.territoryAttackTo = territory;
			game.playerTurn.phase = "attack";
			synchronized (turn.lock) {
				turn.lock.notifyAll();
			}
			
		}
	}
	
	private void wonTerritory() {
		if (turn.player.territoryAttackFrom.equals(territory)) {
			if (turn.player.territoryAttackTo.armies > 1) {
				turn.player.territoryAttackFrom.armies++;
				turn.player.territoryAttackTo.armies--;
			} else {
				JOptionPane.showMessageDialog(null, "Territories must have at least one army");
			}
		} else if (turn.player.territoryAttackTo.equals(territory)) {
			if (turn.player.territoryAttackFrom.armies > 1) {
				turn.player.territoryAttackFrom.armies--;
				turn.player.territoryAttackTo.armies++;
			} else {
				JOptionPane.showMessageDialog(null, "Territories must have at least one army");
			}
		} else {
			JOptionPane.showMessageDialog(null, "You must click on " + turn.player.territoryAttackFrom.name + " or " + turn.player.territoryAttackTo.name);
		}
	}
	
	private void fortify() {
		if (turn.player.fortify1.equals(territory)) {
			if (turn.player.fortify2.armies > 1) {
				turn.player.fortify1.armies++;
				turn.player.fortify2.armies--;
			} else {
				JOptionPane.showMessageDialog(null, "Territories must have at least one army");
			}
		} else if (turn.player.fortify2.equals(territory)) {
			if (turn.player.fortify1.armies > 1) {
				turn.player.fortify1.armies--;
				turn.player.fortify2.armies++;
			} else {
				JOptionPane.showMessageDialog(null, "Territories must have at least one army");
			}
		} else {
			JOptionPane.showMessageDialog(null, "You must click on " + turn.player.fortify1.name + " or " + turn.player.fortify2.name);
		}
	}
	
	private void attackFrom() {
		boolean canAttackFrom = true;
		String failMsg = "";
		if (territory.armies < 2) {
			canAttackFrom = false;
			failMsg = "You cannot attack from a territory with less than two armies";
		}
		if (!territory.player.equals(turn.player)) {
			canAttackFrom = false;
			failMsg = "You cannot attack from a territory you do not control";
		}
		if (canAttackFrom) {
			turn.player.territoryAttackFrom = territory;
			turn.phase = "attackTo";
			synchronized (turn.lock) {
				turn.lock.notifyAll();
			}
		} else {
			JOptionPane.showMessageDialog(null, failMsg);
		}	
	}
	
	
	private Territory findMatch(int x, int y) {
		ArrayList<Territory> territories = board.getTerritories();
		for (Territory t : territories) {
			if((Math.abs(t.locX - x) < 30) && (Math.abs(t.locY - y) < 30)) {
				return t;
			}
		}
		return new Territory("NoMatch", 0, 0);
	}
	
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
