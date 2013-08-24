package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Fortifier extends SubPhaseHandler {

    private final BoardState boardState;
    private final InstructionPanel instructionPanel;

    public Fortifier(BoardState boardState, InstructionPanel instructionPanel) {
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
    }

    @Override
    public void mouseClicked(Territory territory) {
        if (boardState.getFortifyFrom().equals(territory)) {
            if (boardState.getArmies(boardState.getFortifyTo()) > 1) {
                boardState.increaseArmies(boardState.getFortifyFrom(), 1);
                boardState.decreaseArmies(boardState.getFortifyTo(), 1);
            } else {
                JOptionPane.showMessageDialog(null, "Territories must have at least one army");
            }
        } else if (boardState.getFortifyTo().equals(territory)) {
            if (boardState.getArmies(boardState.getFortifyFrom()) > 1) {
                boardState.increaseArmies(boardState.getFortifyTo(), 1);
                boardState.decreaseArmies(boardState.getFortifyFrom(), 1);
            } else {
                JOptionPane.showMessageDialog(null, "Territories must have at least one army");
            }
        } else {
            JOptionPane.showMessageDialog(null, "You must click on "
                    + boardState.getFortifyFrom().name + " or " + boardState.getFortifyTo().name);
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
        instructionPanel.addCustomButtons(
                InstructionPanel.NEW_VISIBLE,
                "Click on " + boardState.getFortifyFrom().name + " to move armies from "
                        + boardState.getFortifyTo().name + ". Click on "
                        + boardState.getFortifyTo().name + " to move armies from "
                        + boardState.getFortifyFrom().name, button);
    }

}
