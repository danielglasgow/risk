package risk;

import java.util.ArrayList;

public class HumanStrategy implements Strategy {

	private final MainGame game;
	private final ArrayList<Integer> boardArmies = new ArrayList<Integer>();
	public boolean restartPlaceArmies = false;
	public Player player;
	public Phase phase;
	public InstructionPanel instructionPanel;
	public Object phaseLock = new Object();
	public Object lock = new Object();

	public HumanStrategy(MainGame game) {
		this.game = game;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see risk.Strategy#takeTurn(risk.Player)
	 */
	@Override
	public void takeTurn(Player player) {
		for (Territory t : game.territories) {
			boardArmies.add(t.armies);
		}
		this.instructionPanel = game.instructionPanel;
		this.player = player;

		phase = Phase.PLACE_ARMIES;

		boolean endTurn = false;
		while (!endTurn) {
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
				wonTerritory();
			} else if (phase == Phase.FORTIFY_SELECTION) {
				fortifySelection();
			} else if (phase == Phase.FORTIFY) {
				fortify();
			} else if (phase == Phase.END_TURN) {
				endTurn = true;
			}
		}
	}

	private void handlePhase(PhaseHandler phaseHandler) {
		game.board.mouse.setPhaseHandler(phaseHandler);
		phaseHandler.setInterface();
		phase = phaseHandler.await();
	}

	private void fortifySelection() {
		instructionPanel.setText(InstructionPanel.newVisible,
				"Click on two territories to fortify", "Continue", "End Turn");
		synchronized (lock) {
			while (phase == Phase.FORTIFY_SELECTION) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String fort2 = "...";
				if (player.fortify2 != null) {
					fort2 = player.fortify2.name
							+ " (click continue or select territories again)";
				}
				if (player.fortify1 == null) {
					instructionPanel.setText(InstructionPanel.newInvisible,
							"Click on two territories to fortify", "Continue",
							"End Turn");
				} else {
					instructionPanel.setText(InstructionPanel.newInvisible,
							"Fortify from " + player.fortify1.name + " to "
									+ fort2, "Continue", "End Turn");
				}
			}
		}
	}

	private void wonTerritory() {
		instructionPanel.setText(InstructionPanel.newVisible, "Click on "
				+ player.territoryAttackTo.name + " to move armies from "
				+ player.territoryAttackFrom.name + ". Click on "
				+ player.territoryAttackFrom.name + " to move armies from "
				+ player.territoryAttackTo.name, "Move All", "Continue");

		synchronized (lock) {
			while (phase == Phase.WON_TERRITORY) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void fortify() {
		instructionPanel.setText(InstructionPanel.newVisible, "Click on "
				+ player.fortify1.name + " to move armies from "
				+ player.fortify2.name + ". Click on " + player.fortify2.name
				+ " to move armies from " + player.fortify1.name, "---",
				"End Turn");
		synchronized (lock) {
			while (phase == Phase.WON_TERRITORY) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
