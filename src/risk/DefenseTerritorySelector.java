package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * TODO(dani): Needs Java doc. What is an "AttackTo"? TODO(dani): Object names
 * must be nouns.
 */
public class DefenseTerritorySelector extends HumanPhaseHandler {

    private final BoardState boardState;
    private final Player player;
    private final InstructionPanel instructionPanel;
    private final HumanAttackPhase attackPhase;

    public DefenseTerritorySelector(BoardState boardState, Player player,
            InstructionPanel instructionPanel, HumanAttackPhase attackPhase) {
        super(HumanTurnPhases.SELECT_DEFENDING_TERRITORY);
        this.boardState = boardState;
        this.player = player;
        this.instructionPanel = instructionPanel;
        this.attackPhase = attackPhase;
    }

    public void displayInterface() {
        JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                finishPhase(HumanTurnPhases.SELECT_ATTACKING_TERRITORY);
            }
        });
        button.setText("Choose a Different Territory to Attack From");
        instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE, "Attacking from "
                + attackPhase.getAttackFrom().name
                + ".  Select the territory you would like to attack.", button);
    }

    public void setAttackToTerritory(Territory territory) {
        if (!attackPhase.getAttackFrom().getAdjacents().contains(territory)) {
            JOptionPane.showMessageDialog(null, "You must attack a territory adjacent to "
                    + attackPhase.getAttackFrom().name);
        } else if (boardState.getPlayer(territory).equals(player)) {
            JOptionPane.showMessageDialog(null, "You cannot attack a territory you control");
        } else {
            attackPhase.setAttackTo(territory);
            finishPhase(HumanTurnPhases.BATTLE);
        }
    }

    @Override
    public void mouseClicked(Territory territory) {
        setAttackToTerritory(territory);
    }

}
