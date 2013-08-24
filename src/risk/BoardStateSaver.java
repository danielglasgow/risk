package risk;

import java.util.Map;

import javax.swing.JOptionPane;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVReadProc;
import au.com.bytecode.opencsv.CSVWriteProc;
import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.collect.Maps;

/**
 * This class offers to save a BoardState as a csv file in
 * risk/SavedBoardStates/ or a user specified directory.
 */

public class BoardStateSaver {

    public static boolean loadBoard(BoardState boardState) {
        int choice = JOptionPane.showConfirmDialog(null, "Load File", "Load File?",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            String fileName = JOptionPane.showInputDialog("File Name: ");
            BoardStateSaver saver = new BoardStateSaver();
            saver.loadFile(boardState,
                    "/Users/danielglasgow/Documents/Source/workspace/risk/SavedBoardStates/"
                            + fileName + ".csv");
            boardState.updateBackground();
            return true;
        } else {
            return false;
        }
    }

    public static void loadBoard(BoardState boardState, String fileName) {
        BoardStateSaver saver = new BoardStateSaver();
        saver.loadFile(boardState, fileName);
    }

    public static void saveBoard(BoardState boardState) {
        BoardStateSaver saver = new BoardStateSaver();
        String fileName = JOptionPane.showInputDialog("Save As: ");
        saver.saveFile(boardState, fileName + ".csv");
    }

    public static void saveBoard(BoardState boardState, String fileName) {
        BoardStateSaver saver = new BoardStateSaver();
        saver.saveFile(boardState, fileName);
    }

    private void saveFile(final BoardState boardState, String fileName) {
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

    /**
     * Loads a board, by default all players will have the computer strategy.
     */
    private void loadFile(final BoardState boardState, String fileName) {
        Map<String, Territory> territoryMap = Maps.newHashMap();
        for (Territory territory : boardState.getTerritories()) {
            territoryMap.put(territory.name, territory);
        }
        Map<String, Player> playerMap = Maps.newHashMap();
        MainGame game = boardState.getGame();
        for (Player player : game.getImmutablePlayers()) {
            playerMap.put(player.name, player);
        }
        final Map<String, Territory> finalTerritoryMap = Maps.newHashMap(territoryMap);
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
