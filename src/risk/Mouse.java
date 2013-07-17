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
	
	public Mouse(Board board) {
		this.board = board;
		this.game = board.game;
		this.turn = game.turn;
	}

	

	public void mouseClicked(MouseEvent e) {
		game.instructionPanel.newIndicator.setText(turn.instructionPanel.newInvisible);
		if (turn.phase.equals("placeArmies")) {
			Player player = turn.player;
			if (player.armiesToPlace > 0) {
				PointerInfo a = MouseInfo.getPointerInfo();
				Point point = new Point(a.getLocation());
				SwingUtilities.convertPointFromScreen(point, e.getComponent());
				int x = (int) point.getX();
				int y = (int) point.getY();
				Territory territory = findMatch(x, y);
				if (!territory.name.equals("NoMatch")) {
					if(territory.player.name.equals(player.name)) {
						synchronized (game.lock) {
							territory.armies++;
							player.armiesToPlace--;
							game.lock.notifyAll();
						}
					} else {
						JOptionPane.showMessageDialog(null, "You must place armies on territories you control");
					}
				} else {
					JOptionPane.showMessageDialog(null,"Make sure to click on the army indicator in order to add armies");
				}
				
			}
		} 
		
		if (turn.phase.equals("attackTo")) {
			PointerInfo a = MouseInfo.getPointerInfo();
			Point point = new Point(a.getLocation());
			SwingUtilities.convertPointFromScreen(point, e.getComponent());
			int x = (int) point.getX();
			int y = (int) point.getY();
			Territory territory = findMatch(x, y);
			if (!territory.name.equals("NoMatch")) {
				boolean canAttack = false;
				String failMsg = "You cannot attack " + territory.name + " because you do not control an adjacent territory";
				for (Territory t : territory.adjacents) {
					if (canAttack) {
						break;
					}
					if (t.player.equals(turn.player)) {
						canAttack = true;
						if(t.armies == 1) {
							canAttack = false;
							failMsg = "You cannot attack " + territory.name + " because none of the adjacent territories you control have atleast two armies";
						}
					}
				}
				if (territory.player.equals(turn.player)) {
					canAttack = false;
					failMsg = "You cannot attack a territory you control";
				}
				if (canAttack) {
					//JOptionPane.showMessageDialog(null, "Attacking " + territory.name);
					turn.player.territroyToAttack = territory;
					synchronized (game.lock) {
						//game.phase = "attackFrom";
						game.lock.notifyAll();
					}
				} else {
					JOptionPane.showMessageDialog(null, failMsg);
				}
			} else {
				JOptionPane.showMessageDialog(null,"Make sure to click on the army indicator in order select a territory to attack");
			}
			
		}
				
		
		board.updateBackground();
		
	}

	@Override
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
	
	private Territory findMatch(int x, int y) {
		ArrayList<Territory> territories = board.getTerritories();
		for (Territory t : territories) {
			if((Math.abs(t.locX - x) < 30) && (Math.abs(t.locY - y) < 30)) {
				return t;
			}
		}
		return new Territory("NoMatch", 0, 0);
	}

}
