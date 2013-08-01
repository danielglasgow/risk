package risk;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO(dani): add Java doc.  Document the format of the files!
public class TerritoriesBuilder { 
	private final String territroyFileName;
	private final String adjacencyFileName;
	private final String continentsFileName;
	
	public TerritoriesBuilder(String territoryFileName, String adjacencyFileName, String continentsFileName) {
		this.territroyFileName = territoryFileName;
		this.adjacencyFileName = adjacencyFileName;
		this.continentsFileName = continentsFileName;
	}
	
	public BoardModel build() {
		Map<String, Continent> continents = readContinents(new File(continentsFileName));
		Map<String, Territory> territories = readTerritories(new File(territroyFileName), continents);
		buildBorders(continents);
		return new BoardModel(new ArrayList<Territory>(territories.values()), 
								new ArrayList<Continent>(continents.values()));
	}
	
	private void buildBorders(Map<String, Continent> continents) {
		for (Continent c : continents.values()) {
			c.addBorders();
		}
	}
	
	private Map<String, Continent> readContinents(File file) {
		Map<String, Continent> continents = new HashMap<String, Continent>();
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
		return continents;
	}
	
	private Map<String, Territory> readTerritories(File file, Map<String, Continent> continents) {
		Map<String, Territory> territories = new HashMap<String, Territory>();
		try {
			java.util.Scanner scanner = new java.util.Scanner(file);
			String continentName = "";
			while (scanner.hasNext()) {
				String name = scanner.next();
				if (name.contains("[")) {
					continentName = name.substring(1, name.length() - 1);
					continue;
				}
				int x = scanner.nextInt();
				int y = scanner.nextInt();
				Territory t = new Territory(name, x, y);
				territories.put(name, t);
				continents.get(continentName).territories.add(t);
				
				// TODO(dani): If all you continents were in a Map, you could just
				// to continentMap.get(continent).add(t), and avoid all these if statements,
				// it would also be more flexible for boards that have greater or fewer continents.
				// Your game would be more 'data driven'.
				
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		readAdjacentTerritories(new File(adjacencyFileName), territories);	
		return territories;
	}
	
	private void readAdjacentTerritories(File file, Map<String, Territory> territories) {
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
	
}
