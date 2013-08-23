package risk;

import java.util.Set;

import com.google.common.collect.Sets;

public class HumanAttackPhase extends MetaPhaseHandler {

    private Territory attackingTerritory = null;
    private Territory defendingTerritory = null;

    public HumanAttackPhase(BoardState boardState, InstructionPanel instructionPanel, Player player) {
        super(boardState, instructionPanel, player, HumanTurnPhases.SELECT_ATTACKING_TERRITORY,
                HumanTurnPhases.FORTIFY_SELECTION);
        setSubPhaseHanlders(makeSubPhaseHandlers(boardState, player, instructionPanel));
    }

    private Set<HumanPhaseHandler> makeSubPhaseHandlers(BoardState boardState, Player player,
            InstructionPanel instructionPanel) {
        Set<HumanPhaseHandler> subPhaseHanlders = Sets.newHashSet();
        subPhaseHanlders
                .add(new AttackTerritorySelector(boardState, player, instructionPanel, this));
        subPhaseHanlders.add(new DefenseTerritorySelector(boardState, player, instructionPanel,
                this));
        subPhaseHanlders.add(new BattleHandler(boardState, player, instructionPanel, this));
        subPhaseHanlders.add(new WonTerritoryHandler(boardState, instructionPanel, this));
        return subPhaseHanlders;
    }

    public void setAttackFrom(Territory territory) {
        attackingTerritory = territory;
        System.out.println("Set attackteriitory: " + territory);
    }

    public Territory getAttackFrom() {
        return attackingTerritory;
    }

    public void setAttackTo(Territory territory) {
        defendingTerritory = territory;
    }

    public Territory getAttackTo() {
        return defendingTerritory;
    }

}
