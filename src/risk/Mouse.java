package risk;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Mouse implements MouseListener {

	private final Board board;
	private final MainGame game;

	// YUCK... FIX THIS LATER!
	private PhaseHandler phaseHandler = null;

	public Mouse(Board board) {
		this.board = board;
		this.game = board.game;
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
			phaseHandler.mouseClicked(territory);
		} else {
			JOptionPane
					.showMessageDialog(null,
							"Make sure to click on the army indicator in order select a territory");
		}
		board.updateBackground();
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
