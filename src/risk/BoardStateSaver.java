package risk;

import java.util.Map;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVReadProc;
import au.com.bytecode.opencsv.CSVWriteProc;
import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.collect.Maps;

public class BoardStateSaver {

	public void saveBoard(final BoardState boardState, String fileName) {
		CSV csv = CSV.separator(',').quote('"').create();
		String directory = "/Users/danielglasgow/Documents/Source/workspace/risk/SavedBoardStates/";
		csv.write(directory + fileName, new CSVWriteProc() {
			public void process(CSVWriter out) {
				for (Territory territory : boardState.getTerritories()) {
					String armies = "" + boardState.getArmies(territory);
					String player = "" + boardState.getPlayer(territory).name;
					out.writeNext(territory.name, armies, player);
				}
			}
		});
	}

	public void loadFile(final BoardState boardState, String fileName) {
		Map<String, Territory> territoryMap = Maps.newHashMap();
		for (Territory territory : boardState.getTerritories()) {
			territoryMap.put(territory.name, territory);
		}
		Map<String, Player> playerMap = Maps.newHashMap();
		for (Territory territory : boardState.getTerritories()) {
			playerMap.put(boardState.getPlayer(territory).name,
					boardState.getPlayer(territory));
		}
		final Map<String, Territory> finalTerritoryMap = Maps
				.newHashMap(territoryMap);
		final Map<String, Player> finalPlayerMap = Maps.newHashMap(playerMap);
		CSV csv = CSV.separator(',').quote('"').create();
		csv.read(fileName, new CSVReadProc() {
			public void procRow(int rowIndex, String... values) {
				Territory territory = finalTerritoryMap.get(values[0]);
				int armies = Integer.parseInt(values[1]);
				Player player = finalPlayerMap.get(values[2]);
				boardState.setArmies(territory, armies);
				boardState.setPlayer(territory, player);
			}
		});

	}

}
