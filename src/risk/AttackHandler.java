package risk;

public class AttackHandler extends MainPhaseHandler {

    // should I make these all protected?
    private final BoardState boardState;
    private final InstructionPanel instructionPanel;
    private final Player player;

    private Territory attackingTerritory = null;
    private Territory defendingTerritory = null;

    public AttackHandler(BoardState boardState, InstructionPanel instructionPanel, Player player) {
        super(boardState, SubPhase.SELECT_ATTACKING_TERRITORY, MainPhase.FORTIFY);
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
        this.player = player;
    }

    public void setAttackFrom(Territory territory) {
        attackingTerritory = territory;
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

    @Override
    protected void runSubPhase(SubPhase subPhase) {
        if (subPhase == SubPhase.SELECT_ATTACKING_TERRITORY) {
            handlePhase(new AttackTerritorySelector(boardState, player, instructionPanel, this));
        } else if (subPhase == SubPhase.SELECT_DEFENDING_TERRITORY) {
            handlePhase(new DefenseTerritorySelector(boardState, player, instructionPanel, this));
        } else if (subPhase == SubPhase.BATTLE) {
            handlePhase(new BattleHandler(boardState, player, instructionPanel, this));
        } else if (subPhase == SubPhase.WON_TERRITORY) {
            handlePhase(new WonTerritoryHandler(boardState, instructionPanel, this));
        }
    }
}
