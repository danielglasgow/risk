package risk;

import java.util.Map;

import javax.swing.JOptionPane;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVReadProc;
import au.com.bytecode.opencsv.CSVWriteProc;
import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.collect.Maps;

public class BoardStateSaver {

	public static void loadFile(BoardState boardState) {
		int choice = JOptionPane.showConfirmDialog(null, "Load File",
				"Load File?", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			String fileName = JOptionPane.showInputDialog("File Name: ");
			BoardStateSaver saver = new BoardStateSaver();
			saver.loadFile(boardState,
					"/Users/danielglasgow/Documents/Source/workspace/risk/SavedBoardStates/"
							+ fileName + ".csv");
			boardState.updateBackground();
		}
	}

	public static void saveBoard(BoardState boardState) {
		BoardStateSaver saver = new BoardStateSaver();
		String fileName = JOptionPane.showInputDialog("Save As: ");
		saver.saveBoard(boardState, fileName + ".csv");
	}

	public static void saveFile(BoardState boardState, String fileName) {
		BoardStateSaver saver = new BoardStateSaver();
		saver.saveBoard(boardState, fileName);
	}

	private void saveBoard(final BoardState boardState, String fileName) {
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

	private void loadFile(final BoardState boardState, String fileName) {
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
