package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class WonTerritoryHandler extends SubPhaseHandler {

    private final BoardState boardState;
    private final InstructionPanel instructionPanel;
    private final Territory attackTerritory;
    private final Territory defenseTerritory;

    public WonTerritoryHandler(BoardState boardState, InstructionPanel instructionPanel,
            Territory attackTerritory, Territory defenseTerritory) {
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
        this.attackTerritory = attackTerritory;
        this.defenseTerritory = defenseTerritory;
    }

    @Override
    public void mouseClicked(Territory territory) {
        if (attackTerritory.equals(territory)) {
            if (boardState.getArmies(defenseTerritory) > 1) {
                boardState.increaseArmies(attackTerritory, 1);
                boardState.decreaseArmies(defenseTerritory, 1);
            } else {
                JOptionPane.showMessageDialog(null, "Territories must have at least one army");
            }
        } else if (defenseTerritory.equals(territory)) {
            if (boardState.getArmies(attackTerritory) > 1) {
                boardState.increaseArmies(defenseTerritory, 1);
                boardState.decreaseArmies(attackTerritory, 1);
            } else {
                JOptionPane.showMessageDialog(null, "Territories must have at least one army");
            }
        } else {
            JOptionPane.showMessageDialog(null, "You must click on " + attackTerritory.name
                    + " or " + defenseTerritory.name);
        }

    }

    @Override
    public void displayInterface() {
        JButton buttonLeft = new JButton();
        buttonLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                moveAll();
                finishPhase(SubPhase.SELECT_ATTACKING_TERRITORY);
            }
        });
        buttonLeft.setText("Move All");
        JButton buttonRight = new JButton();
        buttonRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                finishPhase(SubPhase.SELECT_ATTACKING_TERRITORY);
            }
        });
        buttonRight.setText("Continue");
        instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE, "Click on "
                + defenseTerritory.name + " to move armies from " + attackTerritory.name
                + ". Click on " + attackTerritory.name + " to move armies from "
                + defenseTerritory.name + ".", buttonLeft, buttonRight);

    }

    private void moveAll() {
        int armiesToMove = boardState.getArmies(attackTerritory) - 1;
        boardState.increaseArmies(defenseTerritory, armiesToMove);
        boardState.setArmies(attackTerritory, 1);
        boardState.updateBackground();
    }

}
