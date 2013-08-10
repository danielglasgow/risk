package risk;

public class HumanStrategy implements Strategy {

	private final MainGame game;
	private final InstructionPanel instructionPanel;

	public Player player;
	public Phase phase;

	public HumanStrategy(MainGame game) {
		this.game = game;
		this.instructionPanel = game.instructionPanel;
	}

	@Override
	public void takeTurn(Player player) {
		this.player = player;
		phase = Phase.PLACE_ARMIES;
		while (true) {
			if (phase == Phase.PLACE_ARMIES) {
				handlePhase(new PlaceArmiesHandler(game, player, instructionPanel,
						player.getArmiesToPlace(false)));
			} else if (phase == Phase.ATTACK_TO) {
				handlePhase(new AttackToHandler(player, instructionPanel));
			} else if (phase == Phase.ATTACK_FROM) {
				handlePhase(new AttackFromHandler(player, instructionPanel));
			} else if (phase == Phase.ATTACK) {
				handlePhase(new AttackHandler(game, player, instructionPanel));
			} else if (phase == Phase.WON_TERRITORY) {
				handlePhase(new WonTerritoryHandler(game, player, instructionPanel));
			} else if (phase == Phase.FORTIFY_SELECTION) {
				handlePhase(new FortifySelection(game, player, instructionPanel));
			} else if (phase == Phase.FORTIFY) {
				handlePhase(new FortifyHandler(player, instructionPanel));
			} else if (phase == Phase.END_TURN) {
				break;
			}
		}
	}

	private void handlePhase(HumanPhaseHandler phaseHandler) {
		game.board.mouse.setPhaseHandler(phaseHandler);
		phaseHandler.displayInterface();
		phase = phaseHandler.await();
	}

}
