package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ComputerStrategy implements Strategy {
	public final MainGame game;
	public Player player;
	private Continent goalContinent;
	private int armiesToPlace;
	private InstructionPanel instructionPanel;
	private CountDownLatch latch;

	private final BoardState boardState;

	public ComputerStrategy(MainGame game) {
		this.game = game;
		this.boardState = game.boardState;
		this.instructionPanel = boardState.getBoard().getInstructionPanel();
	}

	@Override
	public void takeTurn(Player player) {
		latch = new CountDownLatch(1);
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
		if (goalContinent.ratio != 1) {
			AttackRoute chosenRoute = chooseAttackRoute();
			placeArmies(chosenRoute.get(0), armiesToPlace);
			System.out.println("Chosen ROute:" + chosenRoute);
			attack(chosenRoute);
		}
		/*
		 * ComputerFortifier fortifier = new ComputerFortifier(player,
		 * goalContinent); List<Territory> fortification =
		 * fortifier.getFortification(); System.out.println(fortification); int
		 * armiesToMove = fortification.get(0).armies - 1;
		 * fortification.get(0).armies = 1; fortification.get(1).armies +=
		 * armiesToMove; game.board.updateBackground();
		 */
	}

	private void attack(AttackRoute attackRoute) {
		int next = 0;
		boolean capturedTerritory = false;
		Territory attackFrom = attackRoute.get(next);
		next++;
		Territory attackTo = attackRoute.get(next);
		next++;
		while (boardState.getArmies(attackFrom) > 4 || !capturedTerritory) {
			simulateAttack(attackFrom, attackTo);
			if (boardState.getArmies(attackTo) < 1) {
				capturedTerritory = true;
				boardState.setPlayer(attackTo, player);
				boardState.increaseArmies(attackTo,
						boardState.getArmies(attackFrom) - 1);
				boardState.setArmies(attackFrom, 1);
				attackFrom = attackTo;
				boardState.updateBackground();
				if (attackRoute.size() > next) {
					attackTo = attackRoute.get(next);
					next++;
				} else {
					break;
				}

			} else if (boardState.getArmies(attackFrom) < 2) {
				break;
			}
			boardState.updateBackground();
		}
	}

	private void simulateAttack(Territory attackFrom, Territory attackTo) {
		Random random = new Random();
		int attackArmies = boardState.getArmies(attackFrom);
		int defenseArmies = boardState.getArmies(attackTo);
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
		boardState.decreaseArmies(attackFrom, attackLosses);
		boardState.decreaseArmies(attackTo, defenseLosses);
		boardState.updateBackground();
	}

	private void placeArmies(Territory territory, int numArmies) {
		boardState.increaseArmies(territory, numArmies);
		boardState.updateBackground();
	}

	private void setArmiesToPlace() {
		armiesToPlace = player.getArmiesToPlace(false);
	}

	private List<AttackRoute> buildAttackRoutes() {
		List<AttackRoute> attackRoutes = Lists.newArrayList();
		goalContinent.setClusters(TerritoryCluster.generateTerritoryClusters(
				player, goalContinent, boardState));
		for (TerritoryCluster territoryCluster : goalContinent.getClusters()) {
			territoryCluster.makeRoutes();
			attackRoutes.addAll(territoryCluster.getAttackRoutes());
		}
		return attackRoutes;
	}

	private Map<BoardState, AttackRoute> buildBoardStates(
			List<AttackRoute> attackRoutes) {
		Map<BoardState, AttackRoute> boardStates = Maps.newHashMap();
		for (AttackRoute attackRoute : attackRoutes) {
			BoardState boardState = attackRoute
					.getExpectedBoardState(game.boardState.getTerritories());
			if (boardState != null) {
				boardStates.put(boardState, attackRoute);
			}
		}
		return boardStates;
	}

	private AttackRoute chooseAttackRoute() {
		Map<BoardState, Integer> boardStateValues = Maps.newHashMap();
		BasicBoardEvaluator boardEvaluator = new BasicBoardEvaluator();
		Map<BoardState, AttackRoute> boardStates = buildBoardStates(buildAttackRoutes());
		for (BoardState boardState : boardStates.keySet()) {
			boardStateValues.put(boardState, boardEvaluator.getBoardValue(
					boardState, player, goalContinent));
		}
		int highestBoardValue = 0;
		BoardState bestBoardState = null;
		for (BoardState boardState : boardStateValues.keySet()) {
			if (boardStateValues.get(boardState) > highestBoardValue) {
				highestBoardValue = boardStateValues.get(boardState);
				bestBoardState = boardState;
			}
		}
		return boardStates.get(bestBoardState);
	}

	private void setGoalContinent() {
		List<Continent> continentRatios = new ArrayList<Continent>();
		for (int i = 0; i < 6; i++) {
			double numTerritories = 0;
			double numTerritoriesControlled = 0;
			double armiesControlled = 0;
			double enemyArmies = 0;
			for (Territory territory : game.continents.get(i).getTerritories()) {
				numTerritories++;
				if (boardState.getPlayer(territory) == player) {
					numTerritoriesControlled++;
					armiesControlled += boardState.getArmies(territory);
				} else {
					enemyArmies += boardState.getArmies(territory);
				}
			}
			double ratio = (numTerritoriesControlled + armiesControlled)
					/ (numTerritories + enemyArmies + armiesControlled);
			game.continents.get(i).ratio = ratio;
			continentRatios.add(game.continents.get(i));
			Collections.sort(continentRatios);
		}
		goalContinent = continentRatios.get(0);
	}

}
