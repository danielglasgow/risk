package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * This class handles user input and interface while a human player is choosing
 * which territory he wants to attack (this territory must be adjacent to the
 * attack territory he just selected during the SELECT_ATTACKING_TERRITORY
 * SubPhase). This class is exclusively instantiated from within the
 * AttackHandler class.
 * 
 * The human player either selects a territory to attack (by clicking) which
 * advances the SubPhase to "BATTLE", or clicks
 * "choose new territory to attack from", changing the SubPhase to
 * SELECT_ATTACKING_TERRITORY (going 'back' a phase).
 */
public class DefenseTerritorySelector extends SubPhaseHandler {

    private final BoardState boardState;
    private final Player player;
    private final InstructionPanel instructionPanel;
    private final Territory attackTerritory;

    private Territory defenseTerritory;

    public DefenseTerritorySelector(BoardState boardState, Player player,
            InstructionPanel instructionPanel, Territory attackTerritory) {
        this.boardState = boardState;
        this.player = player;
        this.instructionPanel = instructionPanel;
        this.attackTerritory = attackTerritory;
    }

    public void displayInterface() {
        JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                finishPhase(SubPhase.SELECT_ATTACKING_TERRITORY);
            }
        });
        button.setText("Choose a Different Territory to Attack From");
        instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE, "Attacking from "
                + attackTerritory.name + ".  Select the territory you would like to attack.",
                button);
    }

    public void setAttackToTerritory(Territory territory) {
        if (!attackTerritory.getAdjacents().contains(territory)) {
            JOptionPane.showMessageDialog(null, "You must attack a territory adjacent to "
                    + attackTerritory.name);
        } else if (boardState.getPlayer(territory).equals(player)) {
            JOptionPane.showMessageDialog(null, "You cannot attack a territory you control");
        } else {
            defenseTerritory = territory;
            finishPhase(SubPhase.BATTLE);
        }
    }

    @Override
    public void mouseClicked(Territory territory) {
        setAttackToTerritory(territory);
    }

    public Territory getDefenseTerritory() {
        return defenseTerritory;
    }

}
