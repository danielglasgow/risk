package risk;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class Tester {

	private static int[] diceOdds(int attackDice, int defenseDice) {
		Random random = new Random();
		int[] attackRolls = new int[3];
		int[] defenseRolls = new int[2];
		int defenseLosses = 0;
		int attackLosses = 0;
		for (int i = 0; i < attackDice; i++) {
			attackRolls[i] = (random.nextInt(6) + 1);
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
		if (attackDice > 1 && defenseDice == 2) {
			if (attackRolls[1] > defenseRolls[0]) { // for best of 2 dice
				defenseLosses++;
			} else {
				attackLosses++;
			}
		}
		System.out.println("attackLosses: " + attackLosses);
		System.out.println("defenseLosses: " + defenseLosses);
		int[] results = { attackLosses, defenseLosses };
		return results;
	}

	private static void diceOddsResults(int numRuns, int attackDice,
			int defenseDice) {
		double runs = 0;
		double attackWins = 0;
		double defenseWins = 0;
		double splits = 0;
		while (runs < numRuns) {
			runs++;
			int[] results = diceOdds(attackDice, defenseDice);
			if (results[1] == 0) {
				defenseWins++;
			} else if (results[0] == 0) {
				attackWins++;
			} else {
				splits++;
			}
		}
		System.out.println("Runs: " + runs);
		System.out.println("Attack Wins: " + attackWins + " Percentage: "
				+ attackWins / runs * 100);
		System.out.println("Defense Wins: " + defenseWins + " Percentage: "
				+ defenseWins / runs * 100);
		System.out.println("Splits: " + splits + " Percentage: " + splits
				/ runs * 100);
	}

	private static int[] simulateAttack(int attackArmies, int defenseArmies) {
		Random random = new Random();
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
		attackArmies -= attackLosses;
		defenseArmies -= defenseLosses;
		int[] results = { attackArmies, defenseArmies };
		return results;
	}

	private static int[] simulateBattle(int attackArmies, int defenseArmies) {
		int[] results = { attackArmies, defenseArmies };
		while (results[0] != 1 && results[1] != 0) {
			results = simulateAttack(results[0], results[1]);
		}
		if (results[0] == 0) {
			return results;
		} else {
			return results;
		}
	}

	private static void showBattleResults(int numRuns, int attackArmies,
			int defenseArmies) {
		double attackWins = 0;
		double defenseWins = 0;
		Map<Integer, Integer> attackLosses = new HashMap<Integer, Integer>();
		for (int i = 0; i < attackArmies; i++) {
			attackLosses.put(i, 0);
		}
		double runs = 0;
		while (runs < numRuns) {
			runs++;
			int[] results = simulateBattle(attackArmies, defenseArmies);
			if (results[1] == 0) {
				attackWins++;
			} else {
				defenseWins++;
			}
			int value = attackLosses.get(attackArmies - results[0]) + 1;
			attackLosses.put(attackArmies - results[0], value);

		}
		System.out.println("Runs: " + runs);
		System.out.println("Attack Wins: " + attackWins);
		System.out.println("Defense Wins: " + defenseWins);
		System.out.println("Attack Win Percentage: " + attackWins / runs * 100);
		double expectedArmies = 0;
		for (Integer losses : attackLosses.keySet()) {
			expectedArmies += (attackLosses.get(losses) / runs)
					* (attackArmies - losses);
			System.out.println("Attack Lost " + losses + ": "
					+ attackLosses.get(losses) + " Percentage: "
					+ attackLosses.get(losses) / runs * 100);
		}
		System.out.println("Expected Armies: " + expectedArmies);
	}

	private static void probableOutCome(AttackRoute attackRoute) {
		System.out.println("attackRoute; " + attackRoute);
		double runs = 100000;
		Map<Integer, Integer> armiesLeft = new HashMap<Integer, Integer>();
		for (int i = 0; i < attackRoute.get(0).armies + 2 - attackRoute.size(); i++) {
			armiesLeft.put(i, 0);
		}
		for (int i = 0; i < runs; i++) {
			Iterator<Territory> attackRouteIterator = attackRoute.iterator();
			int[] results = { attackRouteIterator.next().armies,
					attackRoute.get(1).armies };
			while (attackRouteIterator.hasNext()) {
				System.out.println("attackArmies: " + results[0]);
				System.out.println("defense Armies: " + results[1]);
				results = simulateBattle(results[0], results[1]);
				if (results[0] == 1) {
					results[0] = 0;
					break;
				}
				if (results[1] == 0) {
					System.out.println("won!");
					results[0] = results[0] - 1;
					results[1] = attackRouteIterator.next().armies;
				}
			}
			System.out.println(results[0]);
			int value = armiesLeft.get(results[0]) + 1;
			armiesLeft.put(results[0], value);
		}
		for (Integer armies : armiesLeft.keySet()) {
			System.out.println("Attack Finishes With " + armies + ": "
					+ armiesLeft.get(armies) + " Percentage: "
					+ armiesLeft.get(armies) / runs * 100);
		}
	}

	private static AttackRoute prepareAttackRoute() {
		AttackRoute attackRoute = new AttackRoute(null);
		Territory t1 = new Territory("t1", 0, 0);
		Territory t2 = new Territory("t2", 0, 0);
		Territory t3 = new Territory("t3", 0, 0);
		Territory t4 = new Territory("t4", 0, 0);
		t1.armies = 9;
		t2.armies = 1;
		t3.armies = 1;
		t4.armies = 1;
		attackRoute.add(t1);
		attackRoute.add(t2);
		attackRoute.add(t3);
		attackRoute.add(t4);
		return attackRoute;
	}

	private static void ABCombos(int num0s, int num1s) {
		ArrayList<Integer> nums = new ArrayList<Integer>();// why can't I make
															// the decleration a
															// list
		for (int i = 0; i < num0s; i++) {
			nums.add(0);
		}
		for (int i = 0; i < num1s; i++) {
			nums.add(1);
		}

	}

	private static void expectedValueNv1(double n) {
		Map<Double, Double> outcomePercentages = new HashMap<Double, Double>();
		for (double i = 0; i < n; i++) {
			if (n - i > 3) {
				if (i == 0) {
					outcomePercentages.put(i, .659);
				} else {
					outcomePercentages.put(i,
							outcomePercentages.get(i - 1) * .341);
				}
			} else if (n - i == 3) {
				outcomePercentages.put(i, Math.pow(.341, i) * .579);
			} else if (n - i == 2) {
				outcomePercentages
						.put(i, Math.pow(.341, (i - 1)) * .421 * .414);
			} else if (n - i == 1) {
				outcomePercentages
						.put(i, Math.pow(.341, (i - 2)) * .421 * .586);
			}
		}
		double expectedValue = 0;
		for (Double losses : outcomePercentages.keySet()) {
			double outcomePercentage = outcomePercentages.get(losses);
			System.out.println("Losses: " + losses + " percentage: "
					+ outcomePercentage);
			expectedValue += outcomePercentage * (n - losses);
		}
		System.out.println("Expected Value: " + expectedValue);
	}

	public static void attackRouteExpectedValue(AttackRoute attackRoute) {
		ExpectedValue expectedValue = new ExpectedValue();
		Map<Point, Double> expectedValueTable = expectedValue
				.getExpectedValueTable();
		Iterator<Territory> attackRouteIterator = attackRoute.iterator();
		int attackArmies = attackRouteIterator.next().armies;
		int defenseArmies = attackRouteIterator.next().armies;
		// double expectedArmies =
	}

	public static void main(String[] args) {
		// expectedValueNv1(4);
		showBattleResults(100000, 5, 4);
		// diceOddsResults(1000000, 2, 2);
		// probableOutCome(prepareAttackRoute());
		// System.out.println(ABCombos(1, , 0));
		new ExpectedValue();

	}

}