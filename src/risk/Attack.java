package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JButton;

public class Attack extends PhaseHandler {

	private final Player player;
	private final InstructionPanel instructionPanel;
	private final MainGame game;

	private int[] attackRolls = new int[3];
	private int[] defenseRolls = new int[2];
	private int attackLosses = 0;
	private int defenseLosses = 0;

	public Attack(MainGame game, Player player,
			InstructionPanel instructionPanel) {
		this.game = game;
		this.player = player;
		this.instructionPanel = instructionPanel;
	}

	public void setInterface() {
		JButton buttonRight = new JButton();
		JButton buttonLeft = new JButton();
		buttonRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				nextPhase = Phase.ATTACK_FROM;
				latch.countDown();
			}
		});
		buttonLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				nextPhase = Phase.ATTACK;
				latch.countDown();
			}
		});

		simulateAttack();

		if (player.territoryAttackTo.armies < 1) {
			playerWinsInterface(buttonRight);
		} else if (player.territoryAttackFrom.armies < 2) {
			playerLosesInterface(buttonRight);
		} else {
			buttonLeft.setText("Continue Attacking");
			buttonRight.setText("Stop Attacking");
			instructionPanel.addCustomButtons(
					InstructionPanel.newVisible,
					"Attack Rolls: " + printRolls(3, attackRolls)
							+ "    Defense rolls: "
							+ printRolls(2, defenseRolls)
							+ "     Attack Loses: " + attackLosses
							+ "    Defense Loses: " + defenseLosses + "    ",
					buttonLeft, buttonRight);
		}
	}

	private void simulateAttack() {
		Random random = new Random();
		int attackArmies = player.territoryAttackFrom.armies;
		int defenseArmies = player.territoryAttackTo.armies;
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
		player.territoryAttackFrom.armies -= attackLosses;
		player.territoryAttackTo.armies -= defenseLosses;
		game.board.updateBackground();
	}

	private void playerWinsInterface(JButton button) {
		button.setText("Continue");
		player.territoryAttackFrom.armies--;
		player.territoryAttackTo.player = player;
		player.territoryAttackTo.armies++;
		game.board.updateBackground();
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

	private String printRolls(int num, int[] array) {
		String rolls = "" + array[num - 1];
		for (int i = num - 2; i >= 0; i--) {
			rolls = rolls + ", " + array[i];
		}
		return rolls;
	}

}
