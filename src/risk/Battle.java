package risk;

/**
 * Represents a number of attack armies and a number of defense armies.
 */
public class Battle {
	private final double attackArmies;
	private final double defenseArmies;

	public Battle(double attackArmies, double defenseArmies) {
		this.attackArmies = attackArmies;
		this.defenseArmies = defenseArmies;
	}

	public double getAttackArmies() {
		return attackArmies;
	}

	public double getDefenseArmies() {
		return defenseArmies;
	}

}