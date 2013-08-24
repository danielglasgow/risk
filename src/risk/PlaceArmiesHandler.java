package risk;

public class PlaceArmiesHandler extends MainPhaseHandler {
    private final BoardState boardState;
    private final InstructionPanel instructionPanel;
    private final Player player;

    public PlaceArmiesHandler(BoardState boardState, InstructionPanel instructionPanel,
            Player player) {
        super(boardState, SubPhase.PLACE_ARMIES, MainPhase.ATTACK);
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
        this.player = player;
    }

    @Override
    protected void runSubPhase(SubPhase subPhase) {
        if (subPhase == SubPhase.PLACE_ARMIES) {
            handlePhase(new ArmyPlacer(boardState, player, instructionPanel,
                    player.getArmiesToPlace(false)));
        }

    }

}
