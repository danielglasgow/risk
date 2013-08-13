package risk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

// TODO(dani): add Java doc.  Document the format of the files!
public class TerritoriesBuilder {
	private final String territroyFileName;
	private final String adjacencyFileName;
	private final String continentsFileName;

	public TerritoriesBuilder(String territoryFileName,
			String adjacencyFileName, String continentsFileName) {
		this.territroyFileName = territoryFileName;
		this.adjacencyFileName = adjacencyFileName;
		this.continentsFileName = continentsFileName;
	}

	public BoardModel build() {
		Map<String, ContinentModel> modelContinents = readContinents(new File(
				continentsFileName));
		Map<String, Territory> territories = readTerritories(new File(
				territroyFileName), modelContinents);
		buildBorders(modelContinents);
		return new BoardModel(new ArrayList<Territory>(territories.values()),
				buildContinents(modelContinents));
	}

	private List<Continent> buildContinents(
			Map<String, ContinentModel> modelContinents) {
		List<Continent> continents = Lists.newArrayList();
		for (ContinentModel modelContinent : modelContinents.values()) {
			continents.add(new Continent(modelContinent.name,
					modelContinent.bonusArmies, modelContinent.territories,
					modelContinent.borders));
		}
		return continents;
	}

	private void buildBorders(Map<String, ContinentModel> continents) {
		for (ContinentModel continent : continents.values()) {
			continent.addBorders();
		}
	}

	private Map<String, ContinentModel> readContinents(File file) {
		Map<String, ContinentModel> modelContinents = Maps.newHashMap();
		try {
			java.util.Scanner scanner = new java.util.Scanner(file);
			while (scanner.hasNext()) {
				String name = scanner.next();
				int bonusArmies = scanner.nextInt();
				modelContinents
						.put(name, new ContinentModel(name, bonusArmies));
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		return modelContinents;
	}

	private Map<String, Territory> readTerritories(File file,
			Map<String, ContinentModel> modelContinents) {
		Map<String, Territory> territories = Maps.newHashMap();
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
				Territory territory = new Territory(name, x, y);
				territories.put(name, territory);
				modelContinents.get(continentName).territories.add(territory);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		readAdjacentTerritories(new File(adjacencyFileName), territories);
		return territories;
	}

	private void readAdjacentTerritories(File file,
			Map<String, Territory> territories) {
		try {
			java.util.Scanner scanner = new java.util.Scanner(file);
			String currentTerritory = "";
			while (scanner.hasNext()) {
				String name = scanner.next();
				if (name.contains(":")) {
					currentTerritory = name.substring(0, name.length() - 1);
				} else {
					territories.get(currentTerritory).addAdjacentTerritory(
							territories.get(name));
				}
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}

	private class ContinentModel {
		public final List<Territory> territories = Lists.newArrayList();
		public final List<Territory> borders = Lists.newArrayList();
		public final String name;
		public final int bonusArmies;

		public ContinentModel(String name, int bonusArmies) {
			this.name = name;
			this.bonusArmies = bonusArmies;
		}

		public void addBorders() {
			for (Territory territory : territories) {
				if (isBorder(territory)) {
					borders.add(territory);
				}
			}
		}

		private boolean isBorder(Territory territory) {
			for (Territory adjacent : territory.getAdjacents()) {
				if (!territories.contains(adjacent)) {
					return true;
				}
			}
			return false;
		}
	}
}
