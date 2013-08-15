package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class WonTerritoryHandler extends HumanPhaseHandler {

    private final BoardState boardState;
    private final Player player;
    private final InstructionPanel instructionPanel;

    public WonTerritoryHandler(BoardState boardState, Player player,
            InstructionPanel instructionPanel) {
        this.boardState = boardState;
        this.player = player;
        this.instructionPanel = instructionPanel;
    }

    @Override
    public void mouseClicked(Territory territory) {
        if (player.territoryAttackFrom.equals(territory)) {
            if (boardState.getArmies(player.territoryAttackTo) > 1) {
                boardState.increaseArmies(player.territoryAttackFrom, 1);
                boardState.decreaseArmies(player.territoryAttackTo, 1);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Territories must have at least one army");
            }
        } else if (player.territoryAttackTo.equals(territory)) {
            if (boardState.getArmies(player.territoryAttackFrom) > 1) {
                boardState.increaseArmies(player.territoryAttackTo, 1);
                boardState.decreaseArmies(player.territoryAttackFrom, 1);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Territories must have at least one army");
            }
        } else {
            JOptionPane.showMessageDialog(null, "You must click on "
                    + player.territoryAttackFrom.name + " or "
                    + player.territoryAttackTo.name);
        }

    }

    @Override
    public void displayInterface() {
        JButton buttonLeft = new JButton();
        buttonLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                moveAll();
                finishPhase(Phase.FORTIFY_SELECTION);
            }
        });
        buttonLeft.setText("Move All");
        JButton buttonRight = new JButton();
        buttonRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                finishPhase(Phase.FORTIFY_SELECTION);
            }
        });
        buttonRight.setText("Continue");
        instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE,
                "Click on " + player.territoryAttackTo.name
                        + " to move armies from "
                        + player.territoryAttackFrom.name + ". Click on "
                        + player.territoryAttackFrom.name
                        + " to move armies from "
                        + player.territoryAttackTo.name + ".", buttonLeft,
                buttonRight);

    }

    private void moveAll() {
        int armiesToMove = boardState.getArmies(player.territoryAttackFrom) - 1;
        boardState.increaseArmies(player.territoryAttackTo, armiesToMove);
        boardState.setArmies(player.territoryAttackFrom, 1);
        boardState.updateBackground();
    }

}
