package risk;

/**
 * Represents a number of attack armies and a number of defense armies.
 */
public class Battle {
    private final int attackArmies;
    private final int defenseArmies;

    public Battle(int attackArmies, int defenseArmies) {
        this.attackArmies = attackArmies;
        this.defenseArmies = defenseArmies;
    }

    @Override
    public boolean equals(Object object) {
        Battle battle = (Battle) object;
        return (battle.getAttackArmies() == attackArmies && battle
                .getDefenseArmies() == defenseArmies);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + attackArmies;
        result = 31 * result + defenseArmies;
        return result;
    }

    public int getAttackArmies() {
        return attackArmies;
    }

    public int getDefenseArmies() {
        return defenseArmies;
    }

    public Battle attackLosesOne() {
        return new Battle(attackArmies - 1, defenseArmies);
    }

    public Battle defenseLosesOne() {
        return new Battle(attackArmies, defenseArmies - 1);
    }

    public Battle attackLosesTwo() {
        return new Battle(attackArmies - 2, defenseArmies);
    }

    public Battle defenseLosesTwo() {
        return new Battle(attackArmies, defenseArmies - 2);
    }

    public Battle splits() {
        return new Battle(attackArmies - 1, defenseArmies - 1);
    }

}
