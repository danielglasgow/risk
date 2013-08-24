package risk;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * This class listens to mouse clicks, and handles the data collected from the
 * mouse clicks to the appropriate subPhaseHandler.
 */

public class Mouse implements MouseListener {

    private final BoardState boardState;

    // YUCK... FIX THIS LATER!
    private SubPhaseHandler phaseHandler = null;

    public Mouse(BoardState boardState) {
        this.boardState = boardState;
    }

    public void setPhaseHandler(SubPhaseHandler phaseHandler) {
        this.phaseHandler = phaseHandler;
    }

    public void mouseClicked(MouseEvent e) {
        boardState.getBoard().getInstructionPanel().getNewIndicator()
                .setText(InstructionPanel.NEW_INVISIBLE);
        PointerInfo a = MouseInfo.getPointerInfo();
        Point point = new Point(a.getLocation());
        SwingUtilities.convertPointFromScreen(point, e.getComponent());
        int x = (int) point.getX();
        int y = (int) point.getY();
        Territory territory = findMatch(x, y);
        if (!territory.name.equals("NoMatch")) {
            phaseHandler.mouseClicked(territory);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Make sure to click on the army indicator in order select a territory");
        }
        boardState.updateBackground();
    }

    private Territory findMatch(int x, int y) {
        for (Territory territory : boardState.getTerritories()) {
            if ((Math.abs(territory.coordinateX - x) < 30) && (Math.abs(territory.coordinateY - y) < 30)) {
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
