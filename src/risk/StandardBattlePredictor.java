package risk;

import java.util.Map;

import com.google.common.base.Objects;
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

    private final Map<DiscreteBattle, Double> outcomes = Maps.newHashMap();

    public StandardBattlePredictor() {
        buildStartingValues();
    }

    private void buildStartingValues() {
        outcomes.put(new DiscreteBattle(2, 1), WIN_PERCENTAGE_ONE_DIE_VS_ONE * 2
                + LOSE_PERCENTAGE_ONE_DIE_VS_ONE * 1);
        outcomes.put(new DiscreteBattle(3, 1), WIN_PERCENTAGE_TWO_DICE_VS_ONE * 3
                + LOSE_PERCENTAGE_TWO_DICE_VS_ONE * outcomes.get(new DiscreteBattle(2, 1)));
    }

    @Override
    public double predict(Battle battle) {
        int attackArmies = (int) battle.getAttackArmies();
        int defenseArmies = (int) battle.getDefenseArmies();
        double outcome = predictDiscreteBattle(new DiscreteBattle(attackArmies, defenseArmies));
        return battle.getAttackArmies() * outcome / attackArmies;
    }

    private double predictDiscreteBattle(DiscreteBattle discreteBattle) {
        if (outcomes.containsKey(discreteBattle)) {
            return outcomes.get(discreteBattle);
        }
        double outcome = predictOutcome(discreteBattle);
        outcomes.put(discreteBattle, outcome);
        return outcome;
    }

    private double predictOutcome(DiscreteBattle discreteBattle) {
        if (discreteBattle.attackArmies == 1) {
            return 1;
        }
        if (discreteBattle.defenseArmies == 0) {
            return discreteBattle.attackArmies;
        }
        if (discreteBattle.attackArmies == 2) {
            assert discreteBattle.defenseArmies >= 2;
            return WIN_PERCENTAGE_ONE_DIE_VS_TWO
                    * predictDiscreteBattle(discreteBattle.defenseLosesOne())
                    + LOSE_PERCENTAGE_ONE_DIE_VS_TWO
                    * predictDiscreteBattle(discreteBattle.attackLosesOne());
        }
        if (discreteBattle.attackArmies == 3) {
            assert discreteBattle.defenseArmies >= 2;
            return WIN_PERCENTAGE_TWO_DICE_VS_TWO
                    * predictDiscreteBattle(discreteBattle.defenseLosesTwo())
                    + LOSE_PERCENTAGE_TWO_DICE_VS_TWO
                    * predictDiscreteBattle(discreteBattle.attackLosesTwo())
                    + SPLIT_PERCENTAGE_TWO_DICE_VS_TWO
                    * predictDiscreteBattle(discreteBattle.splits());
        }
        if (discreteBattle.attackArmies > 3 && discreteBattle.defenseArmies == 1) {
            return WIN_PERCENTAGE_THREE_DICE_VS_ONE
                    * predictDiscreteBattle(discreteBattle.defenseLosesOne())
                    + LOSE_PERCENTAGE_THREE_DICE_VS_ONE
                    * predictDiscreteBattle(discreteBattle.attackLosesOne());
        }
        if (discreteBattle.attackArmies > 3 && discreteBattle.defenseArmies > 1) {
            return WIN_PERCENTAGE_THREE_DICE_VS_TWO
                    * predictDiscreteBattle(discreteBattle.defenseLosesTwo())
                    + LOSE_PERCENTAGE_THREE_DICE_VS_TWO
                    * predictDiscreteBattle(discreteBattle.attackLosesTwo())
                    + SPLIT_PERCENTAGE_THREE_DICE_VS_TWO
                    * predictDiscreteBattle(discreteBattle.splits());
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

    /**
     * A battle between integer values, whose results are added to the outcomes
     * table.
     */
    private class DiscreteBattle {
        public final int attackArmies;
        public final int defenseArmies;

        public DiscreteBattle(int attackArmies, int defenseArmies) {
            this.attackArmies = attackArmies;
            this.defenseArmies = defenseArmies;
        }

        @Override
        public boolean equals(Object object) {
            DiscreteBattle discreteBattle = (DiscreteBattle) object;
            return (discreteBattle.attackArmies == attackArmies && discreteBattle.defenseArmies == defenseArmies);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(attackArmies, defenseArmies);
        }

        public DiscreteBattle attackLosesOne() {
            return new DiscreteBattle(attackArmies - 1, defenseArmies);
        }

        public DiscreteBattle defenseLosesOne() {
            return new DiscreteBattle(attackArmies, defenseArmies - 1);
        }

        public DiscreteBattle attackLosesTwo() {
            return new DiscreteBattle(attackArmies - 2, defenseArmies);
        }

        public DiscreteBattle defenseLosesTwo() {
            return new DiscreteBattle(attackArmies, defenseArmies - 2);
        }

        public DiscreteBattle splits() {
            return new DiscreteBattle(attackArmies - 1, defenseArmies - 1);
        }
    }

    public void print() {
        System.out.println(this);
    }

    public Map<DiscreteBattle, Double> getOutcomes() {
        return outcomes;
    }

}
