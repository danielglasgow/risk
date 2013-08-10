package risk;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Provides a map from a battle (a given number of attack armies and defense
 * armies) to the expected outcome of that battle (how many attack armies, on
 * average, are left).
 */
public class StandardBattlePredictor implements BattlePredictor {
	// 1 Attack Die 1 Defense Die
	static final double WIN_PERCENTAGE_ONE_DIE_VS_ONE = 0.417;// 66666666667;
	static final double LOSE_PERCENTAGE_ONE_DIE_VS_ONE = (1 - WIN_PERCENTAGE_ONE_DIE_VS_ONE);

	// 2 Attack Dice 1 Defense Die
	static final double WIN_PERCENTAGE_TWO_DICE_VS_ONE = 0.579;// 7037037037;
	static final double LOSE_PERCENTAGE_TWO_DICE_VS_ONE = (1 - WIN_PERCENTAGE_TWO_DICE_VS_ONE);

	// 3 Attack Dice 1 Defense Die
	static final double WIN_PERCENTAGE_THREE_DICE_VS_ONE = 0.660;// 0.6597222222222;
	static final double LOSE_PERCENTAGE_THREE_DICE_VS_ONE = (1 - WIN_PERCENTAGE_THREE_DICE_VS_ONE);

	// 1 Attack Die 2 Defense Dice
	static final double WIN_PERCENTAGE_ONE_DIE_VS_TWO = 0.255;// 0.25462962962963;
	static final double LOSE_PERCENTAGE_ONE_DIE_VS_TWO = (1 - WIN_PERCENTAGE_ONE_DIE_VS_TWO);

	// 2 Attack Dice 2 Defense Dice
	static final double WIN_PERCENTAGE_TWO_DICE_VS_TWO = 0.228;
	static final double SPLIT_PERCENTAGE_TWO_DICE_VS_TWO = 0.324;
	static final double LOSE_PERCENTAGE_TWO_DICE_VS_TWO = (1 - WIN_PERCENTAGE_TWO_DICE_VS_TWO - SPLIT_PERCENTAGE_TWO_DICE_VS_TWO);

	// 3 Attack Dice 2 Defense Dice
	static final double WIN_PERCENTAGE_THREE_DICE_VS_TWO = 0.372;
	static final double SPLIT_PERCENTAGE_THREE_DICE_VS_TWO = 0.336;
	static final double LOSE_PERCENTAGE_THREE_DICE_VS_TWO = (1 - WIN_PERCENTAGE_THREE_DICE_VS_TWO - SPLIT_PERCENTAGE_THREE_DICE_VS_TWO);

	private final Map<Battle, Double> outcomes = Maps.newHashMap();

	public StandardBattlePredictor() {
		buildStartingValues();
	}

	private void buildStartingValues() {
		outcomes.put(new Battle(2, 1), WIN_PERCENTAGE_ONE_DIE_VS_ONE * 2
				+ LOSE_PERCENTAGE_ONE_DIE_VS_ONE * 1);
		outcomes.put(
				new Battle(3, 1),
				WIN_PERCENTAGE_TWO_DICE_VS_ONE * 3
						+ LOSE_PERCENTAGE_TWO_DICE_VS_ONE
						* outcomes.get(new Battle(2, 1)));
	}

	public double predict(Battle battle) {
		if (outcomes.containsKey(battle)) {
			return outcomes.get(battle);
		}
		double outcome = predictOutcome(battle);
		outcomes.put(battle, outcome);
		return outcome;
	}

	private double predictOutcome(Battle battle) {
		if (battle.getAttackArmies() == 1) {
			return 1;
		}
		if (battle.getDefenseArmies() == 0) {
			return battle.getAttackArmies();
		}
		if (battle.getAttackArmies() == 2) {
			assert battle.getDefenseArmies() >= 2;
			return WIN_PERCENTAGE_ONE_DIE_VS_TWO
					* predict(battle.defenseLosesOne())
					+ LOSE_PERCENTAGE_ONE_DIE_VS_TWO
					* predict(battle.attackLosesOne());
		}
		if (battle.getAttackArmies() == 3) {
			assert battle.getDefenseArmies() >= 2;
			return WIN_PERCENTAGE_TWO_DICE_VS_TWO
					* predict(battle.defenseLosesTwo())
					+ LOSE_PERCENTAGE_TWO_DICE_VS_TWO
					* predict(battle.attackLosesTwo())
					+ SPLIT_PERCENTAGE_TWO_DICE_VS_TWO
					* predict(battle.splits());
		}
		if (battle.getAttackArmies() > 3 && battle.getDefenseArmies() == 1) {
			return WIN_PERCENTAGE_THREE_DICE_VS_ONE
					* predict(battle.defenseLosesOne())
					+ LOSE_PERCENTAGE_THREE_DICE_VS_ONE
					* predict(battle.attackLosesOne());
		}
		if (battle.getAttackArmies() > 3 && battle.getDefenseArmies() > 1) {
			return WIN_PERCENTAGE_THREE_DICE_VS_TWO
					* predict(battle.defenseLosesTwo())
					+ LOSE_PERCENTAGE_THREE_DICE_VS_TWO
					* predict(battle.attackLosesTwo())
					+ SPLIT_PERCENTAGE_THREE_DICE_VS_TWO
					* predict(battle.splits());
		}

		return 0;
	}

	public String toString() {
		String string = "";
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (i == 0) {
					if (j != 0) {
						if (j == 9) {
							System.out.println("      " + j);
						} else {
							System.out.print("      " + j);
						}
					}
				} else {
					if (j == 0) {
						System.out.println("");
						System.out.print(i);
					} else {
						String value = "";
						value += outcomes.get(new Battle(i, j));
						String newValue = value + " ";
						if (value.length() > 3) {
							newValue = value.substring(0, 4);
						}
						if (j == 9) {
							System.out.println("   " + newValue);
						} else {
							System.out.print("   " + newValue);
						}
					}
				}
			}
		}
		return string;
	}

	public void print() {
		System.out.println(this);
	}

	public Map<Battle, Double> getOutcomes() {
		return outcomes;
	}

}
