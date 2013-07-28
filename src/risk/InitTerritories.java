package risk;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class InitTerritories {

	public ArrayList<Territory> territories = new ArrayList<Territory>(); 
	public MainGame game;
	private Continent NorthAmerica = new Continent("NorthAmerica", 5);
	private Continent SouthAmerica = new Continent("SouthAmerica", 2);
	private Continent Africa = new Continent("Africa", 3);
	private Continent Europe = new Continent("Europe", 5);
	private Continent Asia = new Continent("Asia", 7);
	private Continent Australia = new Continent("Australia", 2);
	
	
	public InitTerritories(MainGame game) {
		this.game = game;
		readTerritories(new File("TerritoryInfo/territories.txt"));
		divideTerritories();
		initContinents();
	}
	
	private void initContinents() {
		game.continents.add(NorthAmerica);
		game.continents.add(SouthAmerica);
		game.continents.add(Europe);
		game.continents.add(Africa);
		game.continents.add(Asia);
		game.continents.add(Australia);
		for (Continent c : game.continents) {
			c.addBoarders();
		}
	}

	private void readTerritories(File file) {
		try{
			java.util.Scanner scanner = new java.util.Scanner(file);
			for(int i = 0; i< 42; i++){
				String name = scanner.next();
				int x = scanner.nextInt();
				int y = scanner.nextInt();
				Territory t = new Territory(name, x, y);
				territories.add(t);
				if (i < 9) {
					NorthAmerica.territories.add(t);
				} else if (i < 12 ) {
					SouthAmerica.territories.add(t);
				} else if (i < 18 ) {
					Africa.territories.add(t);
				} else if (i < 30 ) {
					Asia.territories.add(t);
				} else if (i < 37 ) {
					Europe.territories.add(t);
				} else {
					Australia.territories.add(t);
				}
			}
		} catch (Exception e) {
			System.out.println("Error in Recommender.readMovies: "+e);
		}
		addAdjacentTerritories(new File("TerritoryInfo/Adjacentterritories.txt"));
		
	}
	
	private void addAdjacentTerritories(File file) {
		try{
			java.util.Scanner scanner = new java.util.Scanner(file);
			scanner.next(); //necessary to move the scanner to the right starting place
			for (Territory t : territories) {
				String lastChar = "";
				while(!lastChar.equals(":") && scanner.hasNext()) {
					String name = scanner.next();
					lastChar = "" + name.charAt(name.length() -1);
					if (!lastChar.equals(":")) {
						t.adjacents.add(getTerritory(name));
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error: "+e);
		}
	}
	
	
	private Territory getTerritory(String name) {
		for (Territory t : territories ) {
			if (t.name.equals(name)) {
				return t;
			}
		}
		System.out.println(name);
		return null;
	}
	
	
	private void divideTerritories() {
		Collections.shuffle(territories);
		int counter = 0;
		for (Territory t : territories) {
			if (counter == MainGame.players.size()) {
				counter = 0;
			}
			Player currentPlayer = MainGame.players.get(counter);
			t.player = currentPlayer;
			counter++;
		}
	}
	
}
