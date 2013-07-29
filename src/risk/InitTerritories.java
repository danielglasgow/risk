package risk;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

// TODO(dani): add Java doc.  Document the format of the files!
// TODO(dani): rename this class TerritoriesBuilder.
public class InitTerritories {

	public final ArrayList<Territory> territories = new ArrayList<Territory>(); 
	public final MainGame game;
	private final Continent NorthAmerica = new Continent("NorthAmerica", 5);
	private final Continent SouthAmerica = new Continent("SouthAmerica", 2);
	private final Continent Africa = new Continent("Africa", 3);
	private final Continent Europe = new Continent("Europe", 5);
	private final Continent Asia = new Continent("Asia", 7);
	private final Continent Australia = new Continent("Australia", 2);
	
	// TODO(dani): constructor should probably take two filenames
	// filenames should be private static final TERRITORY_FILENAME = ".....";
	// and private static final ADJACENCY_FILENAME = "....."; declared inside main,
	// and passed to the TerritoriesBuilder();
	public InitTerritories(MainGame game) {
		this.game = game;
		
		// TODO(dani): Anything below this line should happen in the build() function.
		readTerritories(new File("TerritoryInfo/territories.txt"));
		divideTerritories();
		initContinents();
	}
	
	// TODO(dani): rename this function build().  It should return a List<Continents>
	// There is no reason that the Territory builder needs to know about the game object.
	// The game should call build, get a List<Continent> back, and then game can call
	// game.contitnents.addAll(continentsBuilder.build());
	private void initContinents() {
		game.continents.add(NorthAmerica);
		game.continents.add(SouthAmerica);
		game.continents.add(Europe);
		game.continents.add(Africa);
		game.continents.add(Asia);
		game.continents.add(Australia);
		for (Continent c : game.continents) {
			c.addBorders();
		}
	}

	private void readTerritories(File file) {
		try {
			java.util.Scanner scanner = new java.util.Scanner(file);
			for(int i = 0; i< 42; i++) {
				// TODO(dani): if name.contains("[") { continent = name.substring(1,..); continue; }
				String name = scanner.next();
				int x = scanner.nextInt();
				int y = scanner.nextInt();
				Territory t = new Territory(name, x, y);
				territories.add(t);
				if (i < 9) {
					NorthAmerica.territories.add(t);
				} else if (i < 12 ) {  // TODO(dani): These numbers are totally bogus.  Put a marker in the file.
					// Either use lines like:
					// North America: Alberta 201 203
					// North America: Alaska 78 92
					// or
					// Separate the continents with lines like
					// [North America]
					// Alberta 201 203
					// Alaska 78 92
					// [South America]
					// ...
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
		try {
			java.util.Scanner scanner = new java.util.Scanner(file);
			scanner.next(); //necessary to move the scanner to the right starting place
			for (Territory t : territories) {
				String lastChar = "";
				while (!lastChar.equals(":") && scanner.hasNext()) {
					String name = scanner.next();
					lastChar = "" + name.charAt(name.length() -1);
					if (!lastChar.equals(":")) {
						t.adjacents.add(getTerritory(name));
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}
	
	// TODO(dani): add java doc.  
	private Territory getTerritory(String name) {
		// TODO(dani): Use a map.
		// If you are going to do this, then you might consider using a Map<String, Territory>
		// instead of a List<Territory>.
		for (Territory t : territories ) {
			if (t.name.equals(name)) {
				return t;
			}
		}
		System.out.println(name);
		return null;
	}
	
	// TODO(dani): add java doc.  
	private void divideTerritories() {
		Collections.shuffle(territories);
		int counter = 0;
		for (Territory t : territories) {
			// TODO(dani), use modular arithmetic.  i.e.
			// counter = (counter + 1) % MainGame.players.size()
			// BUT for that matter, MainGame.players.size() should be an argument passed to
			// this function, i.e,  divideTerritories(int numPlayers) { ... }
			if (counter == MainGame.players.size()) {
				counter = 0;
			}
			Player currentPlayer = MainGame.players.get(counter);
			t.player = currentPlayer;
			counter++;
		}
	}
	
}
