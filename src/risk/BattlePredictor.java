package risk;

/**
 * Predicts that outcome of a battle, estimating the expected number of armies
 * the attacker has left after conquering the defender. If the expected number
 * of armies is less than 2, then the attacker will not have captured the
 * defenders territory, because he will usually not have enough armies to move
 * into it.
 */
public interface BattlePredictor {

    double predict(Battle battle);

}
