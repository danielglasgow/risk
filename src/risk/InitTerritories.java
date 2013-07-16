package risk;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class InitTerritories {

	public ArrayList<Territory> territories = new ArrayList<Territory>(); 
	public MainGame game;
	
	public InitTerritories(MainGame game) {
		this.game = game;
		readTerritories(new File("/Users/danielglasgow/Desktop/territories.txt"));
		divideTerritories();
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
			}
		} catch (Exception e) {
			System.out.println("Error in Recommender.readMovies: "+e);
		}
		addAdjacentTerritories(new File("/Users/danielglasgow/Desktop/Adjacentterritories.txt"));
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
			if (counter == game.players.size()) {
				counter = 0;
			}
			Player currentPlayer = game.players.get(counter);
			t.player = currentPlayer;
			counter++;
		}
	}
	
}
