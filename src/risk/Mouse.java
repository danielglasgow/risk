package risk;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Mouse implements MouseListener {

	private final Board board;
	private final MainGame game;
	private final HumanStrategy turn;

	// YUCK... FIX THIS LATER!
	private PhaseHandler phaseHandler = null;

	public Mouse(Board board) {
		this.board = board;
		this.game = board.game;
		this.turn = null; // game.playerTurn;
	}

	public void setPhaseHandler(PhaseHandler phaseHandler) {
		this.phaseHandler = phaseHandler;
	}

	public void mouseClicked(MouseEvent e) {
		game.instructionPanel.newIndicator
				.setText(InstructionPanel.newInvisible);
		PointerInfo a = MouseInfo.getPointerInfo();
		Point point = new Point(a.getLocation());
		SwingUtilities.convertPointFromScreen(point, e.getComponent());
		int x = (int) point.getX();
		int y = (int) point.getY();
		Territory territory = findMatch(x, y);
		if (!territory.name.equals("NoMatch")) {
			if (turn == null || turn.phase == Phase.PLACE_ARMIES) {
				placeArmies(territory);
			} else if (turn.phase == Phase.ATTACK_FROM) {
				attackFrom(territory);
			} else if (turn.phase == Phase.ATTACK_TO) {
				attackTo(territory);
			} else if (turn.phase == Phase.WON_TERRITORY) {
				wonTerritory(territory);
			} else if (turn.phase == Phase.FORTIFY_SELECTION) {
				fortifySelection(territory);
			} else if (turn.phase == Phase.FORTIFY) {
				fortify(territory);
			}
		} else {
			JOptionPane
					.showMessageDialog(null,
							"Make sure to click on the army indicator in order select a territory");
		}

		board.updateBackground();
	}

	private void fortifySelection(Territory territory) {
		if (!territory.player.equals(turn.player)) {
			JOptionPane.showMessageDialog(null,
					"You must fortify between to territories you control");
		} else {
			synchronized (turn.lock) {
				if (turn.player.fortify2 == null
						&& turn.player.fortify1 != null) {
					if (hasPath(turn.player.fortify1, territory)) {
						turn.player.fortify2 = territory;
					} else {
						turn.player.fortify1 = null;
						JOptionPane
								.showMessageDialog(
										null,
										"There must be a contiguous path of territories you control in order to fortify between two territories");
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

		Set<Territory> nextSet = adjacentControlledTerritories(startTerritory,
				territories);
		Set<Territory> currentSet = new HashSet<Territory>();
		territories.remove(startTerritory);
		while (!(currentSet.contains(endTerritory))) {
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

	private Set<Territory> adjacentControlledTerritories(Territory territory,
			Set<Territory> territories) {
		Set<Territory> adjacentControlled = new HashSet<Territory>();
		for (Territory t : territory.adjacents) {
			if (t.player.equals(turn.player) && territories.contains(t)) {
				adjacentControlled.add(t);
			}
		}
		return adjacentControlled;
	}

	private void placeArmies(Territory territory) {
		phaseHandler.action(territory);
	}

	private void attackTo(Territory territory) {
		phaseHandler.action(territory);
	}

	private void wonTerritory(Territory territory) {
		if (turn.player.territoryAttackFrom.equals(territory)) {
			if (turn.player.territoryAttackTo.armies > 1) {
				turn.player.territoryAttackFrom.armies++;
				turn.player.territoryAttackTo.armies--;
			} else {
				JOptionPane.showMessageDialog(null,
						"Territories must have at least one army");
			}
		} else if (turn.player.territoryAttackTo.equals(territory)) {
			if (turn.player.territoryAttackFrom.armies > 1) {
				turn.player.territoryAttackFrom.armies--;
				turn.player.territoryAttackTo.armies++;
			} else {
				JOptionPane.showMessageDialog(null,
						"Territories must have at least one army");
			}
		} else {
			JOptionPane.showMessageDialog(null, "You must click on "
					+ turn.player.territoryAttackFrom.name + " or "
					+ turn.player.territoryAttackTo.name);
		}
	}

	private void fortify(Territory territory) {
		if (turn.player.fortify1.equals(territory)) {
			if (turn.player.fortify2.armies > 1) {
				turn.player.fortify1.armies++;
				turn.player.fortify2.armies--;
			} else {
				JOptionPane.showMessageDialog(null,
						"Territories must have at least one army");
			}
		} else if (turn.player.fortify2.equals(territory)) {
			if (turn.player.fortify1.armies > 1) {
				turn.player.fortify1.armies--;
				turn.player.fortify2.armies++;
			} else {
				JOptionPane.showMessageDialog(null,
						"Territories must have at least one army");
			}
		} else {
			JOptionPane.showMessageDialog(null, "You must click on "
					+ turn.player.fortify1.name + " or "
					+ turn.player.fortify2.name);
		}
	}

	private void attackFrom(Territory territory) {

	}

	private Territory findMatch(int x, int y) {
		for (Territory territory : board.getTerritories()) {
			if ((Math.abs(territory.locX - x) < 30)
					&& (Math.abs(territory.locY - y) < 30)) {
				return territory;
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
