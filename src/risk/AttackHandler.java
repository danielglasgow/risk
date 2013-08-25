package risk;

/**
 * This class handles the "ATTACK" MainPhase by subdividing responsibility among
 * SubPhaseHandlers, AttackTerritorySelector, DefenseTerritory, BattleHandler
 * and WonTerritoryHandler.
 */
public class AttackHandler extends MainPhaseHandler {

    private final BoardState boardState;
    private final InstructionPanel instructionPanel;
    private final Player player;

    private Territory attackTerritory = null;
    private Territory defenseTerritory = null;

    public AttackHandler(BoardState boardState, InstructionPanel instructionPanel, Player player) {
        super(MainPhase.FORTIFICATION);
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
        this.player = player;
    }

    @Override
    public MainPhase runPhase() {
        SubPhase subPhase = SubPhase.SELECT_ATTACKING_TERRITORY;
        while (subPhase != null) {
            subPhase = runSubPhase(subPhase);
        }
        return nextMainPhase;
    }

    /**
     * Each MainPhaseHandler must override this method with a method that checks
     * the given subPhase in order to determine which subPhase it must handle
     * (by instantiating a SubPhaseHanlder and passing it to handleSubPhase).
     */
    protected SubPhase runSubPhase(SubPhase subPhase) {
        Mouse mouse = boardState.getBoard().getMouse();
        if (subPhase == SubPhase.SELECT_ATTACKING_TERRITORY) {
            AttackTerritorySelector attackSelector = new AttackTerritorySelector(boardState,
                    player, instructionPanel);
            subPhase = attackSelector.run(mouse);
            attackTerritory = attackSelector.getAttackTerritory();
        } else if (subPhase == SubPhase.SELECT_DEFENDING_TERRITORY) {
            DefenseTerritorySelector defenseSelector = new DefenseTerritorySelector(boardState,
                    player, instructionPanel, attackTerritory);
            subPhase = defenseSelector.run(mouse);
            defenseTerritory = defenseSelector.getDefenseTerritory();
        } else if (subPhase == SubPhase.BATTLE) {
            subPhase = new BattleHandler(boardState, player, instructionPanel, attackTerritory,
                    defenseTerritory).run(mouse);
        } else if (subPhase == SubPhase.WON_TERRITORY) {
            subPhase = new WonTerritoryHandler(boardState, instructionPanel, attackTerritory,
                    defenseTerritory).run(mouse);
        }
        return subPhase;
    }

    /**
     * The phases a human player completes during different Attack Phase.
     */
    public enum SubPhase {
        SELECT_DEFENDING_TERRITORY, SELECT_ATTACKING_TERRITORY, BATTLE, WON_TERRITORY, //
        END_SUB_PHASE,
    }

}
