package risk;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * This class represents a list of territories which represent a legal attack
 * path for the computer to traverse. Most importantly, the AttackRoute has
 * public methods to return the expected boardState if the computer were to
 * follow the attack path represented by the AttackRoute.
 * 
 */
// TODO(dani): Maybe implement something more than Iterable? Collection?
public class AttackRoute implements Iterable<Territory> {

    private final BoardState boardState;
    private final List<Territory> territories = Lists.newArrayList();

    public AttackRoute(BoardState boardState, AttackRoute attackRoute) {
        this.boardState = boardState;
        if (attackRoute != null) {
            this.territories.addAll(attackRoute.territories);
        }
    }

    /**
     * This method returns the expected boardState if the computer were to
     * attack, following the attack path represented by the AttackRoute.
     */
    public BoardState getExpectedBoardState(int armiesToPlace) {
        BoardState expectedBoardState = boardState.copy();
        BattlePredictor battlePredictor = new StandardBattlePredictor();
        Iterator<Territory> territoriesIterator = territories.iterator();
        Territory attackingTerritory = territoriesIterator.next();
        double attackingArmies = boardState.getArmies(attackingTerritory) + armiesToPlace;
        Player attackingPlayer = boardState.getPlayer(attackingTerritory);
        while (territoriesIterator.hasNext()) {
            Territory defendingTerritory = territoriesIterator.next();
            double defendingArmies = boardState.getArmies(defendingTerritory);
            double outcome = battlePredictor.predict(new Battle(attackingArmies, defendingArmies)) - 1;
            // assuming we move in all but one army.
            expectedBoardState.setArmies(attackingTerritory, 1);
            if (outcome <= 1) {
                break;
            }
            expectedBoardState.setArmies(defendingTerritory, (int) outcome);
            expectedBoardState.setPlayer(defendingTerritory, attackingPlayer);
            attackingArmies = outcome;
            attackingTerritory = defendingTerritory;
        }
        return expectedBoardState;
    }

    public void add(Territory territory) {
        territories.add(territory);
    }

    public void remove(Territory t) {
        territories.remove(t);
    }

    public String toString() {
        return territories.toString();
    }

    public Territory get(int index) {
        return territories.get(index);
    }

    public int size() {
        return territories.size();
    }

    public boolean contains(Territory territory) {
        return territories.contains(territory);
    }

    public boolean isEmpty() {
        return territories.isEmpty();
    }

    public boolean addAll(AttackRoute route) {
        return this.territories.addAll(route.getRoute());
    }

    // TODO(dani): Can you remove this method?
    // ..for now no.. I'll get back to this though
    public List<Territory> getRoute() {
        return territories;
    }

    @Override
    public Iterator<Territory> iterator() {
        return territories.iterator();
    }

}
