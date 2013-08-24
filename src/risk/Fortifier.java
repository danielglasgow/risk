package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * This class handles user input and interface while a human player chooses how
 * many armies to move from his fortifyFrom to his fortifyTo territory. This
 * class is exclusively instantiated from within the FortifyHandler class.
 * 
 * The human player clicks on the fortifyFrom territory to move armies from the
 * fortifyTo territory and clicks on the fortifyTo territory to move armies from
 * the fortifyFrom territory. Once a player is satisfied with his army movement,
 * he clicks "End Turn", which prompts advancement to the MainPhase END_TURN.
 */
public class Fortifier extends SubPhaseHandler {

    private final BoardState boardState;
    private final InstructionPanel instructionPanel;
    private final Territory fortifyTo;
    private final Territory fortifyFrom;

    public Fortifier(BoardState boardState, InstructionPanel instructionPanel, Territory fortifyTo,
            Territory fortifyFrom) {
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
        this.fortifyTo = fortifyTo;
        this.fortifyFrom = fortifyFrom;
    }

    @Override
    public void mouseClicked(Territory territory) {
        if (fortifyFrom.equals(territory)) {
            if (boardState.getArmies(fortifyTo) > 1) {
                boardState.increaseArmies(fortifyFrom, 1);
                boardState.decreaseArmies(fortifyTo, 1);
            } else {
                JOptionPane.showMessageDialog(null, "Territories must have at least one army");
            }
        } else if (fortifyTo.equals(territory)) {
            if (boardState.getArmies(fortifyFrom) > 1) {
                boardState.increaseArmies(fortifyTo, 1);
                boardState.decreaseArmies(fortifyFrom, 1);
            } else {
                JOptionPane.showMessageDialog(null, "Territories must have at least one army");
            }
        } else {
            JOptionPane.showMessageDialog(null, "You must click on " + fortifyFrom.name + " or "
                    + fortifyTo.name);
        }

    }

    @Override
    public void displayInterface() {
        JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                finishPhase(SubPhase.END_SUB_PHASE);
            }
        });
        button.setText("End Turn");
        instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE, "Click on "
                + fortifyFrom.name + " to move armies from " + fortifyTo.name + ". Click on "
                + fortifyTo.name + " to move armies from " + fortifyFrom.name, button);
    }

}
