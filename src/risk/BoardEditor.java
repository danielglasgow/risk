package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

/**
 * This class handles user input and interface during the edit SubPhase. It
 * allows a developer set the armies and player on a specific territory. It is
 * solely for developers, and is instantiated exclusively from within the
 * EditMode strategy.
 * 
 */
public class BoardEditor extends SubPhaseHandler {

    private final List<Player> players;
    private final InstructionPanel instructionPanel;
    private final BoardState boardState;

    private Territory editTerritory;
    private int currentPlayerIndex = 0;

    public BoardEditor(BoardState boardState, List<Player> players,
            InstructionPanel instructionPanel, Territory editTerritory) {
        this.boardState = boardState;
        this.players = players;
        this.instructionPanel = instructionPanel;
        this.editTerritory = editTerritory;
    }

    @Override
    public void mouseClicked(Territory territory) {
        editTerritory = territory;
        finishPhase(SubPhase.EDIT);
    }

    @Override
    public void displayInterface() {
        JButton button5 = new JButton();
        JButton button4 = new JButton();
        JButton button3 = new JButton();
        JButton button2 = new JButton();
        JButton button1 = new JButton();
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                BoardStateSaver.saveBoard(boardState);
            }
        });
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                finishPhase(SubPhase.END_SUB_PHASE);
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                changePlayer();

            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                increaseArmies();
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                decreaseArmies();

            }
        });

        button1.setText("Decrease Armies");
        button2.setText("Increase Armies");
        button3.setText("Change Player");
        button4.setText("EndTurn");
        button5.setText("Save Board");

        String territoryName = "(Choose Territory)";

        if (editTerritory != null) {
            territoryName = editTerritory.name;
        }

        instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE, "Edit " + territoryName,
                button1, button2, button3, button4, button5);

    }

    /**
     * Changes a territory's owner in the board state by cycling through the
     * CURRENT list of active players.
     */
    private void changePlayer() {
        if (currentPlayerIndex == players.size() - 1) {
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }
        Player nextPlayer = players.get(currentPlayerIndex);
        boardState.setPlayer(editTerritory, nextPlayer);
        boardState.updateBackground();
    }

    private void increaseArmies() {
        boardState.increaseArmies(editTerritory, 1);
        boardState.updateBackground();
    }

    private void decreaseArmies() {
        boardState.decreaseArmies(editTerritory, 1);
        boardState.updateBackground();
    }

    public Territory getEditTerritory() {
        return editTerritory;
    }

}
