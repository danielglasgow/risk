package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * This class handles user input while a human player is choosing which
 * territory (if any) he or she wants to attack from. This class also handles
 * the interface while a human player is choosing a territory to attack from.
 * 
 * The human player selects a territory to attack from (by clicking) which
 * advances the turn phase to ATTACK_TO.
 */
public class AttackTerritorySelector extends SubPhaseHandler {

    private final BoardState boardState;
    private final Player player;
    private final InstructionPanel instructionPanel;

    private Territory attackTerritory;

    public AttackTerritorySelector(BoardState boardState, Player player,
            InstructionPanel instructionPanel) {
        this.boardState = boardState;
        this.player = player;
        this.instructionPanel = instructionPanel;
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
        button.setText("End Attack Phase");
        attackTerritory = null;
        instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE,
                "Select the territory you would like to attack from", button);
    }

    private void setAttackFromTerritory(Territory territory) {
        String failMessage = null;
        if (boardState.getPlayer(territory) != player) {
            failMessage = "You cannot attack from a territory you do not control";
        } else if (boardState.getArmies(territory) < 2) {
            failMessage = "You cannot attack from a territory with less than two armies";
        }
        if (failMessage == null) {
            attackTerritory = territory;
            finishPhase(SubPhase.SELECT_DEFENDING_TERRITORY);
        } else {
            JOptionPane.showMessageDialog(null, failMessage);
        }
    }

    @Override
    public void mouseClicked(Territory territory) {
        setAttackFromTerritory(territory);
    }

    public Territory getAttackTerritory() {
        return attackTerritory;
    }

}
