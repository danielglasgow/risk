package risk;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


// TODO(dani): add Java doc.  Document the format of the files!

public class TerritoriesBuilder {

	private final HashMap<String, Territory> territories = new HashMap<String, Territory>(); 
	private final HashMap<String, Continent> continents = new HashMap<String, Continent>();
	private String territroyFileName;
	private String adjacencyFileName;
	private String continentsFileName;
	
	// TODO(dani): constructor should probably take two filenames
	// filenames should be private static final TERRITORY_FILENAME = ".....";
	// and private static final ADJACENCY_FILENAME = "....."; declared inside main,
	// and passed to the TerritoriesBuilder();
	public TerritoriesBuilder(String territoryFileName, String adjacencyFileName, String continentsFileName) {
		this.territroyFileName = territoryFileName;
		this.adjacencyFileName = adjacencyFileName;
		this.continentsFileName = continentsFileName;
		// TODO(dani): Anything below this line should happen in the build() function.
	}
	
	// TODO(dani): rename this function build().  It should return a List<Continents>
	// There is no reason that the Territory builder needs to know about the game object.
	// The game should call build, get a List<Continent> back, and then game can call
	// game.contitnents.addAll(continentsBuilder.build());
	
	public void build() {
		readContinents(new File(continentsFileName));
		readTerritories(new File(territroyFileName));
		buildBorders();
		divideTerritories(MainGame.players.size());
	}
	
	// You probably want to return a Map<Continent>.
	
	private void buildBorders() {
		for (Continent c : getContinents()) {
			c.addBorders();
		}
	}
	
	private void readContinents(File file) {
		try {
			java.util.Scanner scanner = new java.util.Scanner(file);
			while (scanner.hasNext()) {
				String name = scanner.next();
				int value = scanner.nextInt();
				continents.put(name, new Continent(name, value));
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}

		for (String name : continents.keySet()) {
			continents.get(name).addBorders();
		}
	}
	
	private void readTerritories(File file) {
		try {
			java.util.Scanner scanner = new java.util.Scanner(file);
			String continent = "";
			while (scanner.hasNext()) {
				// TODO(dani): if name.contains("[") { continent = name.substring(1,..); continue; }
				String name = scanner.next();
				if (name.contains("[")) {
					// TODO(dani): What if the format was [continent, bonusPoints], then the file
					// would have ALL the data!
					continent = name.substring(1, name.length()-1);
					name = scanner.next();
				}
				int x = scanner.nextInt();
				int y = scanner.nextInt();
				Territory t = new Territory(name, x, y);
				territories.put(name, t);
				continents.get(continent).territories.add(t);
				
				// TODO(dani): If all you continents were in a Map, you could just
				// to continentMap.get(continent).add(t), and avoid all these if statements,
				// it would also be more flexible for boards that have greater or fewer continents.
				// Your game would be more 'data driven'.
				
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		readAdjacentTerritories(new File(adjacencyFileName));	
	}
	
	private void readAdjacentTerritories(File file) {
		try {
			java.util.Scanner scanner = new java.util.Scanner(file);
			String currentTerritory = "";
			while (scanner.hasNext()) {
				String name = scanner.next();
				if (name.contains(":")) {
					currentTerritory = name.substring(0, name.length() - 1);
				} else {
					territories.get(currentTerritory).adjacents.add(territories.get(name));
				}
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}
	
	// TODO(dani): add java doc.  
	private void divideTerritories(int numPlayers) {
		ArrayList<Territory> territories = getTerritories();
		Collections.shuffle(territories);
		int counter = 0;
		for (Territory t : territories) {
			// TODO(dani), use modular arithmetic.  i.e.
			// counter = (counter + 1) % MainGame.players.size()
			// BUT for that matter, MainGame.players.size() should be an argument passed to
			// this function, i.e,  divideTerritories(int numPlayers) { ... }
			counter = (counter + 1) % numPlayers;
			Player currentPlayer = MainGame.players.get(counter);
			t.player = currentPlayer;
		}
	}
	
	public ArrayList<Territory> getTerritories() {
		ArrayList<Territory> territoryList = new ArrayList<Territory>();
		for (String name : territories.keySet()) {
			territoryList.add(territories.get(name));
		}
		return territoryList;
	}
	
	public ArrayList<Continent> getContinents() {
		ArrayList<Continent> continentsList = new ArrayList<Continent>();
		for (String name : continents.keySet()) {
			continentsList.add(continents.get(name));
		}
		return continentsList;
	}
	
}
