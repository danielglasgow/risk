package risk;

/**
 * This class handles all the different actions a player must complete during a
 * turn (army placement, attack, fortification). It does this by calling the
 * PlaceArmyHandler, AttackHandler, and FortifyHandler, to handle a player's
 * decisions.
 */
public class HumanStrategy implements Strategy {

    private final BoardState boardState;
    private final InstructionPanel instructionPanel;

    public HumanStrategy(BoardState boardState, InstructionPanel instructionPanel) {
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
    }

    @Override
    public void takeTurn(Player player) {
        MainPhase phase = MainPhase.PLACE_ARMIES;
        while (true) {
            if (phase == MainPhase.PLACE_ARMIES) {
                phase = new PlaceArmiesHandler(boardState, instructionPanel, player).runPhase();
            } else if (phase == MainPhase.ATTACK) {
                phase = new AttackHandler(boardState, instructionPanel, player).runPhase();
            } else if (phase == MainPhase.FORTIFICATION) {
                phase = new FortifyHandler(boardState, instructionPanel, player).runPhase();
            } else if (phase == MainPhase.END_TURN) {
                break;
            }
        }
    }

}
