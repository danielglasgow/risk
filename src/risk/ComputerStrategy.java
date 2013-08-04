package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;

public class ComputerStrategy implements Strategy {
	public final MainGame game;
	public Player player;
	private List<Continent> continentRatios = new ArrayList<Continent>();
	private Continent goalContinent;
	private int armiesToPlace;
	private final List<AttackRoute> attackRoutes = new ArrayList<AttackRoute>();
	private InstructionPanel instructionPanel;
	private CountDownLatch latch = new CountDownLatch(1);

	public ComputerStrategy(MainGame game) {
		this.game = game;
		this.instructionPanel = game.instructionPanel;
		System.out.println(instructionPanel);
	}

	@Override
	public void takeTurn(Player player) {
		this.player = player;
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				latch.countDown();
			}
		});
		button.setText("Continue");
		setArmiesToPlace();
		setGoalContinent();
		instructionPanel.addCustomButtons(InstructionPanel.newVisible,
				"player: " + player.color + "Goal Continent: "
						+ goalContinent.name, button);

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		buildAttackRoutes();
		if (captureGoalContinent()) {
			// lots of code
		} else {
			placeArmies(attackRoutes.get(0).get(0), armiesToPlace);
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("attacking");
			attack(attackRoutes.get(0));
		}
	}

	private void attack(AttackRoute attackRoute) {
		int next = 0;
		boolean capturedTerritory = false;
		Territory attackFrom = attackRoute.get(next);
		next++;
		Territory attackTo = attackRoute.get(next);
		next++;
		while (attackFrom.armies > 4 || !capturedTerritory) {
			simulateAttack(attackFrom, attackTo);
			if (attackTo.armies < 1) {
				System.out.println("Won!");
				capturedTerritory = true;
				attackTo.player = player;
				attackTo.armies = attackFrom.armies - 1;
				attackFrom.armies = 1;
				attackFrom = attackTo;
				game.board.updateBackground();
				if (attackRoute.size() > next) {
					attackTo = attackRoute.get(next);
					next++;
				} else {
					break;
				}

			} else if (attackFrom.armies < 2) {
				System.out.println("lost");
				break;
			}
			game.board.updateBackground();
		}
	}

	private void simulateAttack(Territory attackFrom, Territory attackTo) {
		Random random = new Random();
		int attackArmies = attackFrom.armies;
		int defenseArmies = attackTo.armies;
		int attackDice = 1;
		int defenseDice = 1;
		int[] attackRolls = new int[3];
		int[] defenseRolls = new int[2];
		int attackLosses = 0;
		int defenseLosses = 0;

		if (attackArmies == 3) {
			attackDice = 2;
		} else if (attackArmies > 3) {
			attackDice = 3;
		}
		for (int i = 0; i < attackDice; i++) {
			attackRolls[i] = (random.nextInt(6) + 1);
		}

		if (defenseArmies > 1) {
			defenseDice = 2;
		}
		for (int i = 0; i < defenseDice; i++) {
			defenseRolls[i] = (random.nextInt(6) + 1);
		}
		Arrays.sort(attackRolls);
		Arrays.sort(defenseRolls);
		if (attackRolls[2] > defenseRolls[1]) { // for best of 1 die
			defenseLosses++;
		} else {
			attackLosses++;
		}

		if (attackArmies > 3 && defenseArmies > 1) { // for best of 2 dice
			if (attackRolls[1] > defenseRolls[0]) {
				defenseLosses++;
			} else {
				attackLosses++;
			}

		}
		attackFrom.armies -= attackLosses;
		attackTo.armies -= defenseLosses;
		game.board.updateBackground();
	}

	private void placeArmies(Territory territory, int numArmies) {
		territory.armies += numArmies;
		game.board.updateBackground();
	}

	private void setArmiesToPlace() {
		armiesToPlace = player.getArmiesToPlace();
	}

	private void buildAttackRoutes() {
		goalContinent.generateTerritoryClusters(player);
		for (TerritoryCluster territoryCluster : goalContinent.getClusters()) {
			territoryCluster.makeRoutes();
			attackRoutes.addAll(territoryCluster.getAttackRoutes());
		}
		Collections.sort(attackRoutes);
	}

	private void setGoalContinent() {
		for (int i = 0; i < 6; i++) {
			double numTerritories = 0;
			double numTerritoriesControlled = 0;
			for (Territory t : game.continents.get(i).territories) {
				numTerritories++;
				if (t.player.equals(player)) {
					numTerritoriesControlled++;
				}
			}
			double ratio = numTerritoriesControlled / numTerritories;
			game.continents.get(i).ratio = ratio;
			continentRatios.add(game.continents.get(i));
			Collections.sort(continentRatios);
		}
		if (!continentRatios.get(0).name.equals("Asia")) {
			goalContinent = continentRatios.get(0);
		} else {
			goalContinent = continentRatios.get(1);
		}
	}

	private boolean captureGoalContinent() {
		ArrayList<Territory> territoriesControlled = new ArrayList<Territory>();
		int controlledArmies = 0;
		int enemyArmies = 0;
		int enemyTerritories = 0;
		for (Territory t : goalContinent.territories) {
			if (t.player.equals(player)) {
				territoriesControlled.add(t);
				controlledArmies += t.armies;
			} else {
				enemyArmies += t.armies;
				enemyTerritories++;
			}

		}
		if (controlledArmies + armiesToPlace > 2 * (enemyArmies + enemyTerritories)) {
			return true;
		}
		return false;
	}

}
