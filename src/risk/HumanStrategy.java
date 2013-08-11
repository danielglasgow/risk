package risk;

public class HumanStrategy implements Strategy {

	private final BoardState boardState;
	private final InstructionPanel instructionPanel;

	public Phase phase;

	public HumanStrategy(BoardState boardState,
			InstructionPanel instructionPanel) {
		this.boardState = boardState;
		this.instructionPanel = instructionPanel;
	}

	@Override
	public void takeTurn(Player player) {
		phase = Phase.PLACE_ARMIES;
		while (true) {
			if (phase == Phase.PLACE_ARMIES) {
				handlePhase(new PlaceArmiesHandler(boardState, player,
						instructionPanel, player.getArmiesToPlace(false)));
			} else if (phase == Phase.ATTACK_TO) {
				handlePhase(new AttackToHandler(boardState, player,
						instructionPanel));
			} else if (phase == Phase.ATTACK_FROM) {
				handlePhase(new AttackFromHandler(boardState, player,
						instructionPanel));
			} else if (phase == Phase.ATTACK) {
				handlePhase(new AttackHandler(boardState, player,
						instructionPanel));
			} else if (phase == Phase.WON_TERRITORY) {
				handlePhase(new WonTerritoryHandler(boardState, player,
						instructionPanel));
			} else if (phase == Phase.FORTIFY_SELECTION) {
				handlePhase(new FortifySelectionHandler(boardState, player,
						instructionPanel));
			} else if (phase == Phase.FORTIFY) {
				handlePhase(new FortifyHandler(boardState, player,
						instructionPanel));
			} else if (phase == Phase.END_TURN) {
				break;
			}
		}
	}

	private void handlePhase(HumanPhaseHandler phaseHandler) {
		boardState.getBoard().getMouse().setPhaseHandler(phaseHandler);
		phaseHandler.displayInterface();
		phase = phaseHandler.await();
	}

}
