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
        super(boardState, SubPhase.SELECT_ATTACKING_TERRITORY, MainPhase.FORTIFICATION);
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
        this.player = player;
    }

    @Override
    protected void runSubPhase(SubPhase subPhase) {
        if (subPhase == SubPhase.SELECT_ATTACKING_TERRITORY) {
            AttackTerritorySelector attackSelector = new AttackTerritorySelector(boardState,
                    player, instructionPanel);
            handleSubPhase(attackSelector);
            attackTerritory = attackSelector.getAttackTerritory();
        } else if (subPhase == SubPhase.SELECT_DEFENDING_TERRITORY) {
            DefenseTerritorySelector defenseSelector = new DefenseTerritorySelector(boardState,
                    player, instructionPanel, attackTerritory);
            handleSubPhase(defenseSelector);
            defenseTerritory = defenseSelector.getDefenseTerritory();
        } else if (subPhase == SubPhase.BATTLE) {
            handleSubPhase(new BattleHandler(boardState, player, instructionPanel, attackTerritory,
                    defenseTerritory));
        } else if (subPhase == SubPhase.WON_TERRITORY) {
            handleSubPhase(new WonTerritoryHandler(boardState, instructionPanel, attackTerritory,
                    defenseTerritory));
        }
    }
}
