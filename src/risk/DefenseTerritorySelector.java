package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * TODO(dani): Needs Java doc. What is an "AttackTo"? TODO(dani): Object names
 * must be nouns.
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
