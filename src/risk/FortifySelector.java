package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class FortifySelector extends SubPhaseHandler {

    private final BoardState boardState;
    private final Player player;
    private final InstructionPanel instructionPanel;

    private Territory fortifyTo;
    private Territory fortifyFrom;

    public FortifySelector(BoardState boardState, Player player, InstructionPanel instructionPanel,
            Territory fortifyTo, Territory fortifyFrom) {
        this.boardState = boardState;
        this.player = player;
        this.instructionPanel = instructionPanel;
        this.fortifyTo = fortifyTo;
        this.fortifyFrom = fortifyFrom;
    }

    @Override
    public void mouseClicked(Territory territory) {
        if (boardState.getPlayer(territory) != player) {
            JOptionPane.showMessageDialog(null,
                    "You must fortify between to territories you control");
        } else {
            if (fortifyTo == null && fortifyFrom != null) {
                if (hasPath(fortifyFrom, territory)) {
                    fortifyTo = territory;
                } else {
                    fortifyFrom = null;
                    JOptionPane
                            .showMessageDialog(
                                    null,
                                    "There must be a contiguous path of territories you control in order to fortify between two territories");
                }
            } else {
                fortifyFrom = territory;
                fortifyTo = null;
            }
        }
        finishPhase(SubPhase.FORTIFY_SELECTION);
    }

    private boolean hasPath(Territory startTerritory, Territory endTerritory) {
        Set<Territory> territories = new HashSet<Territory>();
        territories.addAll(boardState.getTerritories());

        Set<Territory> nextSet = adjacentControlledTerritories(startTerritory, territories);
        Set<Territory> currentSet = new HashSet<Territory>();
        territories.remove(startTerritory);
        while (!(currentSet.contains(endTerritory))) {
            System.out.println(nextSet.size());
            if (nextSet.size() < 1) {
                System.out.println("entered");
                return false;
            }
            territories.removeAll(nextSet);
            currentSet.addAll(nextSet);
            nextSet.clear();
            System.out.println(nextSet);
            for (Territory t : currentSet) {
                nextSet.addAll(adjacentControlledTerritories(t, territories));
            }
        }
        return true;
    }

    private Set<Territory> adjacentControlledTerritories(Territory territory,
            Set<Territory> territories) {
        Set<Territory> adjacentControlled = new HashSet<Territory>();
        for (Territory adjacent : territory.getAdjacents()) {
            if (boardState.getPlayer(adjacent) == player && territories.contains(adjacent)) {
                adjacentControlled.add(adjacent);
            }
        }
        return adjacentControlled;
    }

    @Override
    public void displayInterface() {
        JButton buttonRight = new JButton();
        JButton buttonLeft = new JButton();
        buttonRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                finishPhase(SubPhase.END_SUB_PHASE);
            }
        });
        buttonLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                finishPhase(SubPhase.FORTIFY);

            }
        });

        buttonLeft.setText("Continue");
        buttonRight.setText("Continue Without Fortifying (End Turn)");

        if (fortifyFrom == null) {
            instructionPanel.addCustomButtons(InstructionPanel.NEW_INVISIBLE,
                    "Click on two territories to fortify", buttonRight);
        } else if (fortifyTo != null) {
            instructionPanel.addCustomButtons(InstructionPanel.NEW_INVISIBLE, "Fortify from "
                    + fortifyFrom.name + " to " + fortifyTo.name
                    + " (click continue or select territories again)", buttonLeft);
        } else {
            instructionPanel.addCustomButtons(InstructionPanel.NEW_INVISIBLE, "Fortify from "
                    + fortifyFrom.name + " to ...", buttonRight);
        }

    }

    public Territory getFortifyTo() {
        return fortifyTo;
    }

    public Territory getFortifyFrom() {
        return fortifyFrom;
    }

}
