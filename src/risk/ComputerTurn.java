package risk;

import java.util.ArrayList;
import java.util.Collections;

public class ComputerTurn {
	public MainGame game;
	public Player player;
	private ArrayList<Continent> continentRatios = new ArrayList<Continent>();
	private Continent goalContinent;
	private int armiesToPlace;

	public ComputerTurn(MainGame game) {
		this.game = game;
	}

	public void takeTurn(Player player) {
		this.player = player;
		setArmiesToPlace();
		setGoalContinent();
		System.out.println(goalContinent.name);
		goalContinent.setTerritoryCluster(UniqueClusters());
		for (TerritoryCluster tc : goalContinent.getClusters()) {
			System.out.println("Making routes");
			tc.makeRoutes();
		}

	}

	private void setArmiesToPlace() {
		player.calculateArmiesToPlace();
		armiesToPlace = player.armiesToPlace;
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
		for (Territory t : goalContinent.territories) {
			if (t.player.equals(player)) {
				territoriesControlled.add(t);
				controlledArmies += t.armies;
			} else {
				enemyArmies += t.armies;
			}

		}
		if (controlledArmies + armiesToPlace > 2 * enemyArmies) {
			return true;
		}
		return false;
	}

	/*
	 * private void placeArmies() { if (captureGoalContinent()) { //lots of code
	 * } else { ArrayList<TerritoryCluster> territoryClusters =
	 * UniqueClusters(); Collections.sort(territoryClusters); } }
	 */

	private ArrayList<TerritoryCluster> UniqueClusters() {
		ArrayList<TerritoryCluster> territoryClusters = new ArrayList<TerritoryCluster>();
		for (Territory t : goalContinent.getTerritories(player, false)) {
			territoryClusters
					.add(new TerritoryCluster(t, goalContinent, player));
		}
		ArrayList<TerritoryCluster> uniqueClusters = new ArrayList<TerritoryCluster>();
		uniqueClusters.add(territoryClusters.get(0));
		for (TerritoryCluster cluster1 : territoryClusters) {
			boolean newCluster = true;
			for (TerritoryCluster cluster2 : uniqueClusters) {
				if (TerritoryCluster.compareClusters(cluster1, cluster2)) {
					newCluster = false;
				}
			}
			if (newCluster) {
				uniqueClusters.add(cluster1);
			}
		}
		return uniqueClusters;
	}

}
