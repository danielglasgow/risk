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
		armiesToPlace = player.getTerritories().size() / 3;
		if (armiesToPlace < 3) {
			armiesToPlace = 3;
		}
		armiesToPlace += checkContinents();
		setGoalContinent();
		System.out.println(goalContinent.name);
		printTerritories(goalContinent.boarders);
		
	}
	
	private void printTerritories(ArrayList<Territory> territories) {
		ArrayList<String> territoryNames = new ArrayList<String>();
		for (Territory t : territories) {
			territoryNames.add(t.name);
		}
		System.out.println(territoryNames);
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
		//controlledArmies + 
	return false;
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
