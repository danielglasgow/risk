package risk;

public class HumanStrategy implements Strategy {

	private final BoardState boardState;
	private final InstructionPanel instructionPanel;

	private HumanTurnPhases phase;

	public HumanStrategy(BoardState boardState,
			InstructionPanel instructionPanel) {
		this.boardState = boardState;
		this.instructionPanel = instructionPanel;
	}

	@Override
	public void takeTurn(Player player) {
		phase = HumanTurnPhases.PLACE_ARMIES;
		while (true) {
			if (phase == HumanTurnPhases.PLACE_ARMIES) {
				handlePhase(new PlaceArmiesHandler(boardState, player,
						instructionPanel, player.getArmiesToPlace(false)));
			} else if (phase == HumanTurnPhases.ATTACK_TO) {
				handlePhase(new AttackToHandler(boardState, player,
						instructionPanel));
			} else if (phase == HumanTurnPhases.ATTACK_FROM) {
				handlePhase(new AttackFromHandler(boardState, player,
						instructionPanel));
			} else if (phase == HumanTurnPhases.ATTACK) {
				handlePhase(new AttackHandler(boardState, player,
						instructionPanel));
			} else if (phase == HumanTurnPhases.WON_TERRITORY) {
				handlePhase(new WonTerritoryHandler(boardState,
						instructionPanel));
			} else if (phase == HumanTurnPhases.FORTIFY_SELECTION) {
				handlePhase(new FortifySelectionHandler(boardState, player,
						instructionPanel));
			} else if (phase == HumanTurnPhases.FORTIFY) {
				handlePhase(new FortifyHandler(boardState, instructionPanel));
			} else if (phase == HumanTurnPhases.END_TURN) {
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
