package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JButton;

/**
 * This class handles user input and interface while a player attacks from one
 * territory to another. This class is exclusively instantiated from within the
 * AttackHandler class.
 * 
 * The computer simulates a round of battle (attack rolls dice and defense rolls
 * dice) and displays the result. The player must then choose to press the
 * "continue attacking" or "stop attacking" button. If a player has defeated a
 * territory or can no longer legally attack, the player is notified, and
 * prompted to click the "continue" button. This changes the SubPhase to
 * WON_TERRITORY or SELECT_ATTACK_TERRITORY respectively.
 */
public class BattleHandler extends SubPhaseHandler<AttackHandler.SubPhase> {

    private final Player player;
    private final InstructionPanel instructionPanel;
    private final BoardState boardState;
    private final Territory attackTerritory;
    private final Territory defenseTerritory;

    private int[] attackRolls = new int[3];
    private int[] defenseRolls = new int[2];
    private int attackLosses = 0;
    private int defenseLosses = 0;

    public BattleHandler(BoardState boardState, Player player, InstructionPanel instructionPanel,
            Territory attackTerritory, Territory defenseTerritory) {
        this.boardState = boardState;
        this.player = player;
        this.instructionPanel = instructionPanel;
        this.attackTerritory = attackTerritory;
        this.defenseTerritory = defenseTerritory;

    }

    @Override
    public void displayInterface() {
        JButton buttonRight = new JButton();
        JButton buttonLeft = new JButton();
        buttonRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                finishPhase(AttackHandler.SubPhase.SELECT_ATTACKING_TERRITORY);
            }
        });
        buttonLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                finishPhase(AttackHandler.SubPhase.BATTLE);
            }
        });

        int[] dice = simulateAttack();

        if (boardState.getArmies(defenseTerritory) < 1) {
            playerWinsInterface(dice);
        } else if (boardState.getArmies(attackTerritory) < 2) {
            playerLosesInterface(buttonRight, dice);
        } else {
            continueAttackInterface(buttonRight, buttonLeft, dice);
        }
    }

    private int[] simulateAttack() {
        int attackArmies = boardState.getArmies(attackTerritory);
        int defenseArmies = boardState.getArmies(defenseTerritory);
        int attackDice = Math.min(attackArmies - 1, 3);
        int defenseDice = Math.min(defenseArmies, 2);

        for (int i = 0; i < attackDice; i++) {
            attackRolls[i] = (rollDie());
        }

        for (int i = 0; i < defenseDice; i++) {
            defenseRolls[i] = (rollDie());
        }

        Arrays.sort(attackRolls);
        Arrays.sort(defenseRolls);
        if (attackRolls[2] > defenseRolls[1]) { // for best of 1 die
            defenseLosses++;
        } else {
            attackLosses++;
        }

        if (attackArmies > 3 && defenseArmies > 1) { // for best of 2 dice
            if (attackRolls[1] > defenseRolls[0]) {
                defenseLosses++;
            } else {
                attackLosses++;
            }

        }
        boardState.decreaseArmies(attackTerritory, attackLosses);
        boardState.decreaseArmies(defenseTerritory, defenseLosses);
        boardState.updateBackground();

        int[] dice = { attackDice, defenseDice };
        return dice;
    }

    private int rollDie() {
        return new Random().nextInt(6) + 1; // is it bad to create so many
                                            // Random Objects?
    }

    private void playerWinsInterface(int[] dice) {
        JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                finishPhase(AttackHandler.SubPhase.WON_TERRITORY);
            }
        });
        button.setText("Continue");
        boardState.decreaseArmies(attackTerritory, 1);
        boardState.setPlayer(defenseTerritory, player);
        boardState.increaseArmies(defenseTerritory, 1);
        boardState.updateBackground();
        instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE,
                "Attack Rolls: " + rollsToString(dice[0], attackRolls) + "    Defense rolls: "
                        + rollsToString(dice[1], defenseRolls) + "     Attack Loses: "
                        + attackLosses + "    Defense Loses: " + defenseLosses + "    "
                        + "You have defeated " + defenseTerritory.name + "!", button);
    }

    private void playerLosesInterface(JButton button, int[] dice) {
        button.setText("Continue");
        instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE,
                "Attack Rolls: " + rollsToString(dice[0], attackRolls) + "    Defense rolls: "
                        + rollsToString(dice[1], defenseRolls) + "     Attack Loses: "
                        + attackLosses + "    Defense Loses: " + defenseLosses + "    "
                        + "You can no longer attack from " + attackTerritory.name
                        + " because it only has one army", button);
    }

    private void continueAttackInterface(JButton buttonRight, JButton buttonLeft, int[] dice) {
        buttonLeft.setText("Continue Attacking");
        buttonRight.setText("Stop Attacking");
        instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE,
                "Attack Rolls: " + rollsToString(dice[0], attackRolls) + "    Defense rolls: "
                        + rollsToString(dice[1], defenseRolls) + "     Attack Loses: "
                        + attackLosses + "    Defense Loses: " + defenseLosses + "    ",
                buttonLeft, buttonRight);
    }

    private String rollsToString(int num, int[] rollsArray) {
        String rolls = "" + rollsArray[num - 1];
        for (int i = num - 2; i >= 0; i--) {
            rolls = rolls + ", " + rollsArray[i];
        }
        return rolls;
    }

    @Override
    public void mouseClicked(Territory territory) {
        // There is no response to mouse clicks on the board during this phase.
    }

}
