package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * This class handles user input and interface when human player has just
 * conquered a territory and must decide how many armies to move into that
 * territory. This class is exclusively instantiated from within the
 * AttackHandler class.
 * 
 * The human player clicks on the defenseTerritory to move armies from the
 * attackTerritory and clicks on the attackTerritory to move armies from the
 * defenseTerritory. The player may also click the "Move All" button which moves
 * all the armies in the attackTerritory but one to the defenseTerritory. Once a
 * player is satisfied with his army movement, he clicks the "continue" button,
 * which sets the SubPhase to SELECT_ATTACKING_TERRITORY.
 */
public class WonTerritoryHandler extends SubPhaseHandler<AttackHandler.SubPhase> {

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
                finishPhase(AttackHandler.SubPhase.SELECT_ATTACKING_TERRITORY);
            }
        });
        buttonLeft.setText("Move All");
        JButton buttonRight = new JButton();
        buttonRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                finishPhase(AttackHandler.SubPhase.SELECT_ATTACKING_TERRITORY);
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
