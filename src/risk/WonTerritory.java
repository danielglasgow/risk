package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class WonTerritory extends PhaseHandler {

    private Player player;
    private InstructionPanel instructionPanel;
    private MainGame game;

    public WonTerritory(MainGame game, Player player,
            InstructionPanel instructionPanel) {
        this.player = player;
        this.instructionPanel = instructionPanel;
        this.game = game;
    }

    @Override
    public void mouseClicked(Territory territory) {
        if (player.territoryAttackFrom.equals(territory)) {
            if (player.territoryAttackTo.armies > 1) {
                player.territoryAttackFrom.armies++;
                player.territoryAttackTo.armies--;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Territories must have at least one army");
            }
        } else if (player.territoryAttackTo.equals(territory)) {
            if (player.territoryAttackFrom.armies > 1) {
                player.territoryAttackFrom.armies--;
                player.territoryAttackTo.armies++;
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
        instructionPanel.addCustomButtons(InstructionPanel.newVisible,
                "Click on " + player.territoryAttackTo.name
                        + " to move armies from "
                        + player.territoryAttackFrom.name + ". Click on "
                        + player.territoryAttackFrom.name
                        + " to move armies from "
                        + player.territoryAttackTo.name + ".", buttonLeft,
                buttonRight);

    }

    private void moveAll() {
        int armies = --player.territoryAttackFrom.armies;
        player.territoryAttackTo.armies += armies;
        player.territoryAttackFrom.armies = 1;
        game.board.updateBackground();
    }

}
