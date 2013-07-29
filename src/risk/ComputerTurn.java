package risk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class ComputerTurn {
	public MainGame game;
	public Player player; 
	private ArrayList<Continent> continentRatios = new ArrayList<Continent>();
	private Continent goalContinent;
	private ArrayList<Territory> path = new ArrayList<Territory>();
	private int armiesToPlace;
	
	
	
	public ComputerTurn(MainGame game) {
		this.game = game;
	}
	
	public void takeTurn(Player player) {
		this.player = player;
		setArmiesToPlace();
		setGoalContinent();
		System.out.println(goalContinent.name);
		for (Territory t : goalContinent.getTerritories(player, false)) {
		//	System.out.println(t.name + ": " + hasPath)
		}
		
	}
	
	private void printTerritories(ArrayList<Territory> territories) {
		ArrayList<String> territoryNames = new ArrayList<String>();
		for (Territory t : territories) {
			territoryNames.add(t.name);
		}
		System.out.println(territoryNames);
	}
	
	private void setArmiesToPlace() {
		armiesToPlace = player.getTerritories().size() / 3;
		if (armiesToPlace < 3) {
			armiesToPlace = 3;
		}
		armiesToPlace += checkContinents();
	}
	
	private void setGoalContinent() {
		for (int i = 0; i < 6; i++){
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
	
	
	
	private void hasPath(ArrayList<Territory> territories) {
		for (Territory t : territories) {
			hasPath(t.adjacents);
		}
		
		
	}
	
	private void buildPath(ArrayList<Territory> used) {
		
	}
	
	
	private Set<Territory> qualifyingAdjacentTerritories(Territory territory, Set<Territory> territories) {
		Set<Territory> adjacentControlled = new HashSet<Territory>();
		for (Territory t : territory.adjacents) {
			if(!t.player.equals(player) && territories.contains(t)) {
				adjacentControlled.add(t);
			}
		}
		return adjacentControlled;
	}
	
	private void placeArmies() {
		if (captureGoalContinent()) {
			//lots of code
		} else {
			
		}
	}
	
	private boolean hasContinent(Continent continent) {
		boolean hasContinent = true;
		for (Territory t : continent.territories) {
			if (!player.getTerritories().contains(t)) {
				hasContinent = false;
			}
		}
		return hasContinent;
	}
	
	private int checkContinents() {
		int extraArmies = 0;
		for (Continent c : game.continents) {
			if (hasContinent(c)) {
				extraArmies += c.bonusArmies;
			}
		}
		return extraArmies;
	}
	
}
