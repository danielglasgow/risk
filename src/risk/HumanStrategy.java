package risk;

public class HumanStrategy implements Strategy {

    private final BoardState boardState;
    private final InstructionPanel instructionPanel;

    private MainPhase phase;

    public HumanStrategy(BoardState boardState, InstructionPanel instructionPanel) {
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
    }

    @Override
    public void takeTurn(Player player) {
        phase = MainPhase.PLACE_ARMIES;
        while (true) {
            if (phase == MainPhase.PLACE_ARMIES) {
                phase = new PlaceArmiesHandler(boardState, instructionPanel, player).runPhase();
            } else if (phase == MainPhase.ATTACK) {
                phase = new AttackHandler(boardState, instructionPanel, player).runPhase();
            } else if (phase == MainPhase.FORTIFY) {
                phase = new FortifyHandler(boardState, instructionPanel, player).runPhase();
            } else if (phase == MainPhase.END_TURN) {
                break;
            }
        }
    }

}
