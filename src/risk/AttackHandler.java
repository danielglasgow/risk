package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JButton;

public class AttackHandler extends HumanPhaseHandler {

	private final Player player;
	private final InstructionPanel instructionPanel;
	private final BoardState boardState;

	private int[] attackRolls = new int[3];
	private int[] defenseRolls = new int[2];
	private int attackLosses = 0;
	private int defenseLosses = 0;

	public AttackHandler(BoardState boardState, Player player,
			InstructionPanel instructionPanel) {
		this.boardState = boardState;
		this.player = player;
		this.instructionPanel = instructionPanel;
	}

	public void displayInterface() {
		JButton buttonRight = new JButton();
		JButton buttonLeft = new JButton();
		buttonRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				finishPhase(Phase.ATTACK_FROM);
			}
		});
		buttonLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				finishPhase(Phase.ATTACK);

			}
		});

		simulateAttack();

		if (boardState.getArmies(player.territoryAttackTo) < 1) {
			playerWinsInterface();
		} else if (boardState.getArmies(player.territoryAttackFrom) < 2) {
			playerLosesInterface(buttonRight);
		} else {
			continueAttackInterface(buttonRight, buttonLeft);
		}
	}

	private void simulateAttack() {
		Random random = new Random();
		int attackArmies = boardState.getArmies(player.territoryAttackFrom);
		int defenseArmies = boardState.getArmies(player.territoryAttackTo);
		int attackDice = 1;
		int defenseDice = 1;

		if (attackArmies == 3) {
			attackDice = 2;
		} else if (attackArmies > 3) {
			attackDice = 3;
		}
		for (int i = 0; i < attackDice; i++) {
			attackRolls[i] = (random.nextInt(6) + 1);
		}

		if (defenseArmies > 1) {
			defenseDice = 2;
		}
		for (int i = 0; i < defenseDice; i++) {
			defenseRolls[i] = (random.nextInt(6) + 1);
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
		boardState.decreaseArmies(player.territoryAttackFrom, attackLosses);
		boardState.decreaseArmies(player.territoryAttackTo, defenseLosses);
		boardState.updateBackground();
	}

	private void playerWinsInterface() {
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				finishPhase(Phase.WON_TERRITORY);
			}
		});
		button.setText("Continue");
		boardState.decreaseArmies(player.territoryAttackFrom, 1);
		boardState.setPlayer(player.territoryAttackTo, player);
		boardState.increaseArmies(player.territoryAttackTo, 1);
		boardState.updateBackground();
		instructionPanel.addCustomButtons(InstructionPanel.newVisible,
				"Attack Rolls: " + printRolls(3, attackRolls)
						+ "    Defense rolls: " + printRolls(2, defenseRolls)
						+ "     Attack Loses: " + attackLosses
						+ "    Defense Loses: " + defenseLosses + "    "
						+ "You have defeated " + player.territoryAttackTo.name
						+ "!", button);
	}

	private void playerLosesInterface(JButton button) {
		button.setText("Continue");
		instructionPanel.addCustomButtons(InstructionPanel.newVisible,
				"Attack Rolls: " + printRolls(3, attackRolls)
						+ "    Defense rolls: " + printRolls(2, defenseRolls)
						+ "     Attack Loses: " + attackLosses
						+ "    Defense Loses: " + defenseLosses + "    "
						+ "You can no longer attack from "
						+ player.territoryAttackFrom.name
						+ " because it only has one army", button);
	}

	private void continueAttackInterface(JButton buttonRight, JButton buttonLeft) {
		buttonLeft.setText("Continue Attacking");
		buttonRight.setText("Stop Attacking");
		instructionPanel.addCustomButtons(InstructionPanel.newVisible,
				"Attack Rolls: " + printRolls(3, attackRolls)
						+ "    Defense rolls: " + printRolls(2, defenseRolls)
						+ "     Attack Loses: " + attackLosses
						+ "    Defense Loses: " + defenseLosses + "    ",
				buttonLeft, buttonRight);
	}

	private String printRolls(int num, int[] array) {
		String rolls = "" + array[num - 1];
		for (int i = num - 2; i >= 0; i--) {
			rolls = rolls + ", " + array[i];
		}
		return rolls;
	}

	@Override
	public void mouseClicked(Territory territory) {
		// TODO Auto-generated method stub

	}

}
