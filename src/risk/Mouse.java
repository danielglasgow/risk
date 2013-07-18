package risk;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Mouse implements MouseListener {
	
	private Board board;
	private MainGame game;
	private Turn turn;
	private int x;
	private int y;
	private Territory territory;
	
	public Mouse(Board board) {
		this.board = board;
		this.game = board.game;
		this.turn = game.turn;
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
			}
		} else {
			JOptionPane.showMessageDialog(null,"Make sure to click on the army indicator in order select a territory");
		}
		
		board.updateBackground();
	}

	private void placeArmies() {
		Player player = turn.player;
		if (player.armiesToPlace > 0) {
			if(territory.player.name.equals(player.name)) {
				synchronized (game.lock) {
					territory.armies++;
					player.armiesToPlace--;
					game.lock.notifyAll();
				}
			} else {
				JOptionPane.showMessageDialog(null, "You must place armies on territories you control");
			}
		}
	}
	
	private void attackTo() {
		System.out.println("msue");
		if (!game.turn.player.territoryAttackFrom.adjacents.contains(territory)) {
			JOptionPane.showMessageDialog(null, "You must attack a territory adjacent to " + game.turn.player.territoryAttackFrom.name);
		} else if (territory.player.equals(game.turn.player)) {
			JOptionPane.showMessageDialog(null, "You cannot attack a territory you control");
		} else {
			game.turn.player.territoryToAttack = territory;
			game.turn.phase = "attack";
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
			synchronized (game.lock) {
				game.lock.notifyAll();
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
