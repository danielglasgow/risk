package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class WonTerritoryHandler extends HumanPhaseHandler {

    private final BoardState boardState;
    private final InstructionPanel instructionPanel;
    private final HumanAttackPhase attackPhase;

    public WonTerritoryHandler(BoardState boardState, InstructionPanel instructionPanel,
            HumanAttackPhase attackPhase) {
        super(HumanTurnPhases.WON_TERRITORY);
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
        this.attackPhase = attackPhase;
    }

    @Override
    public void mouseClicked(Territory territory) {
        if (attackPhase.getAttackFrom().equals(territory)) {
            if (boardState.getArmies(attackPhase.getAttackTo()) > 1) {
                boardState.increaseArmies(attackPhase.getAttackFrom(), 1);
                boardState.decreaseArmies(attackPhase.getAttackTo(), 1);
            } else {
                JOptionPane.showMessageDialog(null, "Territories must have at least one army");
            }
        } else if (attackPhase.getAttackTo().equals(territory)) {
            if (boardState.getArmies(attackPhase.getAttackFrom()) > 1) {
                boardState.increaseArmies(attackPhase.getAttackTo(), 1);
                boardState.decreaseArmies(attackPhase.getAttackFrom(), 1);
            } else {
                JOptionPane.showMessageDialog(null, "Territories must have at least one army");
            }
        } else {
            JOptionPane.showMessageDialog(null, "You must click on "
                    + attackPhase.getAttackFrom().name + " or " + attackPhase.getAttackTo().name);
        }

    }

    @Override
    public void displayInterface() {
        JButton buttonLeft = new JButton();
        buttonLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                moveAll();
                finishPhase(HumanTurnPhases.FORTIFY_SELECTION);
            }
        });
        buttonLeft.setText("Move All");
        JButton buttonRight = new JButton();
        buttonRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                finishPhase(HumanTurnPhases.FORTIFY_SELECTION);
            }
        });
        buttonRight.setText("Continue");
        instructionPanel.addCustomButtons(
                InstructionPanel.NEW_VISIBLE,
                "Click on " + attackPhase.getAttackTo().name + " to move armies from "
                        + attackPhase.getAttackFrom().name + ". Click on "
                        + attackPhase.getAttackFrom().name + " to move armies from "
                        + attackPhase.getAttackTo().name + ".", buttonLeft, buttonRight);

    }

    private void moveAll() {
        int armiesToMove = boardState.getArmies(attackPhase.getAttackFrom()) - 1;
        boardState.increaseArmies(attackPhase.getAttackTo(), armiesToMove);
        boardState.setArmies(attackPhase.getAttackFrom(), 1);
        boardState.updateBackground();
    }

}
