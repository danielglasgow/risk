package risk;

/**
 * This class handles the "PLACE_ARMIES" MainPhase by handing responsibility to
 * the ArmyPlacer subPhaseHandler.
 */
public class PlaceArmiesHandler extends MainPhaseHandler {
    private final BoardState boardState;
    private final InstructionPanel instructionPanel;
    private final Player player;

    public PlaceArmiesHandler(BoardState boardState, InstructionPanel instructionPanel,
            Player player) {
        super(MainPhase.ATTACK);
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
        this.player = player;
    }

    @Override
    public MainPhase runPhase() {
        new ArmyPlacer(boardState, player, instructionPanel, player.getArmiesToPlace(false))
                .run(boardState.getBoard().getMouse());
        return nextMainPhase;
    }

    /**
     * The phases a human player completes during different Attack Phase.
     */
    public enum SubPhase {
        END_SUB_PHASE
    }
}
