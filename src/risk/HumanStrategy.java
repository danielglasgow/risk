package risk;

public class HumanStrategy implements Strategy {

	private final MainGame game;
	private final InstructionPanel instructionPanel;

	public Player player;
	public Phase phase;

	public HumanStrategy(MainGame game) {
		this.game = game;
		this.instructionPanel = game.instructionPanel;
		System.out.println(instructionPanel);
	}

	@Override
	public void takeTurn(Player player) {
		this.player = player;
		phase = Phase.PLACE_ARMIES;
		while (true) {
			if (phase == Phase.PLACE_ARMIES) {
				handlePhase(new ArmyPlacer(game, player, instructionPanel,
						player.getArmiesToPlace()));
			} else if (phase == Phase.ATTACK_TO) {
				handlePhase(new AttackTo(player, instructionPanel));
			} else if (phase == Phase.ATTACK_FROM) {
				handlePhase(new AttackFrom(player, instructionPanel));
			} else if (phase == Phase.ATTACK) {
				handlePhase(new Attack(game, player, instructionPanel));
			} else if (phase == Phase.WON_TERRITORY) {
				handlePhase(new WonTerritory(game, player, instructionPanel));
			} else if (phase == Phase.FORTIFY_SELECTION) {
				handlePhase(new FortifySelection(game, player, instructionPanel));
			} else if (phase == Phase.FORTIFY) {
				handlePhase(new Fortify(player, instructionPanel));
			} else if (phase == Phase.END_TURN) {
				break;
			}
		}
	}

	private void handlePhase(PhaseHandler phaseHandler) {
		game.board.mouse.setPhaseHandler(phaseHandler);
		phaseHandler.displayInterface();
		phase = phaseHandler.await();
	}

}
