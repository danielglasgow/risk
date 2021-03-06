package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * This class handles all the different actions a computer must complete during
 * a turn. It does this by calling on other classes to decide the comptuer's
 * best army placement, attack, and fortification decisions.
 */
public class ComputerStrategy implements Strategy {
    private Player player;
    private Continent goalContinent;
    private int armiesToPlace;
    private CountDownLatch latch;

    private final BoardState boardState;
    private final InstructionPanel instructionPanel;
    private final ImmutableList<Continent> continents;

    public ComputerStrategy(BoardState boardState, List<Continent> continents) {
        this.boardState = boardState;
        this.continents = ImmutableList.copyOf(continents);
        this.instructionPanel = boardState.getBoard().getInstructionPanel();
    }

    @Override
    public void takeTurn(Player player) {
        System.out.println("Turn: " + player.name);
        BoardEvaluator2 boardEvaluator = new BoardEvaluator2();
        System.out.println("Board State At beggining of Turn: "
                + boardEvaluator.getBoardValue(boardState, player, continents));
        latch = new CountDownLatch(1);
        this.player = player;
        JButton button1 = new JButton();
        JButton button2 = new JButton();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                latch.countDown();
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                boardState.getGame().setEditMode(true);
                latch.countDown();
            }
        });
        button1.setText("Continue");
        button2.setText("Edit Mode");
        setArmiesToPlace();
        setGoalContinent();
        instructionPanel.addCustomButtons(InstructionPanel.NEW_VISIBLE, "player: " + player.color
                + "Goal Continent: " + goalContinent.getName(), button1, button2);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (goalContinent.score != 1) {
            AttackRoute chosenRoute = chooseAttackRoute();
            placeArmies(chosenRoute.get(0), armiesToPlace);
            // System.out.println("Chosen ROute:" + chosenRoute);
            attack(chosenRoute);
        }
        chooseFortification();
    }

    private void chooseFortification() {
        BoardEvaluator2 boardEvaluator = new BoardEvaluator2();
        ComputerFortifier fortifier = new ComputerFortifier(boardState);
        Map<BoardState, Double> boardStateValues = Maps.newHashMap();
        for (BoardState boardState : fortifier.getFortificationOptions(player)) {
            boardStateValues.put(boardState,
                    boardEvaluator.getBoardValue(boardState, player, continents));
        }
        double highestBoardValue = 0;
        BoardState bestBoardState = null;
        for (BoardState boardState : boardStateValues.keySet()) {
            if (boardStateValues.get(boardState) > highestBoardValue) {
                highestBoardValue = boardStateValues.get(boardState);
                bestBoardState = boardState;
            }
        }
        boardState.update(bestBoardState);
        boardState.updateBackground();
    }

    private void attack(AttackRoute attackRoute) {
        int next = 0;
        boolean capturedTerritory = false;
        Territory attackFrom = attackRoute.get(next);
        next++;
        Territory attackTo = attackRoute.get(next);
        next++;
        while (boardState.getArmies(attackFrom) > 3 || !capturedTerritory) {
            simulateAttack(attackFrom, attackTo);
            if (boardState.getArmies(attackTo) < 1) {
                capturedTerritory = true;
                boardState.setPlayer(attackTo, player);
                boardState.increaseArmies(attackTo, boardState.getArmies(attackFrom) - 1);
                boardState.setArmies(attackFrom, 1);
                attackFrom = attackTo;
                boardState.updateBackground();
                if (attackRoute.size() > next) {
                    attackTo = attackRoute.get(next);
                    next++;
                } else {
                    break;
                }

            } else if (boardState.getArmies(attackFrom) < 2) {
                break;
            }
            boardState.updateBackground();
        }
    }

    private void simulateAttack(Territory attackFrom, Territory attackTo) {
        Random random = new Random();
        int attackArmies = boardState.getArmies(attackFrom);
        int defenseArmies = boardState.getArmies(attackTo);
        int attackDice = 1;
        int defenseDice = 1;
        int[] attackRolls = new int[3];
        int[] defenseRolls = new int[2];
        int attackLosses = 0;
        int defenseLosses = 0;

        if (attackArmies == 3) {
            attackDice = 2;
        } else if (attackArmies > 3) {
            attackDice = 3;
        }
        for (int i = 0; i < attackDice; i++) {
            attackRolls[i] = (random.nextInt(6) + 1);
        }

        if (defenseArmies > 1) {
            defenseDice = 2;
        }
        for (int i = 0; i < defenseDice; i++) {
            defenseRolls[i] = (random.nextInt(6) + 1);
        }
        Arrays.sort(attackRolls);
        Arrays.sort(defenseRolls);
        if (attackRolls[2] > defenseRolls[1]) { // for best of 1 die
            defenseLosses++;
        } else {
            attackLosses++;
        }

        if (attackArmies > 3 && defenseArmies > 1) { // for best of 2 dice
            if (attackRolls[1] > defenseRolls[0]) {
                defenseLosses++;
            } else {
                attackLosses++;
            }

        }
        boardState.decreaseArmies(attackFrom, attackLosses);
        boardState.decreaseArmies(attackTo, defenseLosses);
        boardState.updateBackground();
    }

    private void placeArmies(Territory territory, int numArmies) {
        boardState.increaseArmies(territory, numArmies);
        boardState.updateBackground();
    }

    private void setArmiesToPlace() {
        armiesToPlace = player.getArmiesToPlace(false);
    }

    private List<AttackRoute> buildAttackRoutesOld() {
        List<AttackRoute> attackRoutes = Lists.newArrayList();
        // Might need to set clusters later...
        goalContinent.setClusters(TerritoryCluster.generateTerritoryClusters(player, goalContinent,
                boardState));
        for (TerritoryCluster territoryCluster : goalContinent.getClusters()) {
            territoryCluster.makeRoutes();
            attackRoutes.addAll(territoryCluster.getAttackRoutes());
        }
        return attackRoutes;
    }

    private List<AttackRoute> buildAttackRoutes() {
        List<AttackRoute> finalRoutes = Lists.newArrayList();
        for (Territory baseTerritory : boardState.getTerritories()) {
            int baseArmies = boardState.getArmies(baseTerritory);
            List<AttackRoute> attackRoutes = new ArrayList<AttackRoute>();
            List<AttackRoute> newRoutes = new ArrayList<AttackRoute>();
            List<AttackRoute> oldRoutes = new ArrayList<AttackRoute>();
            AttackRoute baseRoute = new AttackRoute(boardState, null);
            baseRoute.add(baseTerritory);
            attackRoutes.add(baseRoute);
            boolean stillWorking = true;
            while (stillWorking) {
                stillWorking = false;
                for (AttackRoute route : attackRoutes) {
                    oldRoutes.add(route);
                    Territory extensionTerritory = route.get(route.size() - 1);
                    for (Territory territory : extensionTerritory.getAdjacents()) {
                        if (!route.contains(territory)
                                && route.size() < (armiesToPlace + baseArmies) / 2
                                && boardState.getPlayer(territory) != player) {
                            stillWorking = true;
                            AttackRoute newRoute = new AttackRoute(boardState, route);
                            newRoute.add(territory);
                            newRoutes.add(newRoute);
                        }
                    }
                }
                finalRoutes.addAll(newRoutes);
                attackRoutes.clear();
                attackRoutes.addAll(newRoutes);
                newRoutes.clear();
                attackRoutes.removeAll(oldRoutes);
                oldRoutes.clear();
            }
        }
        return finalRoutes;
    }

    private Map<BoardState, AttackRoute> buildBoardStates(List<AttackRoute> attackRoutes) {
        Map<BoardState, AttackRoute> boardStates = Maps.newHashMap();
        for (AttackRoute attackRoute : attackRoutes) {
            BoardState boardState = attackRoute.getExpectedBoardState(armiesToPlace);
            if (boardState != null) {
                boolean armiesToMove = false;
                for (Territory territory : attackRoute) {
                    if (boardState.getArmies(territory) > 1) {
                        armiesToMove = true;
                    }
                }
                ComputerFortifier fortifier = new ComputerFortifier(boardState);
                List<BoardState> fortifyOptions = fortifier.getFortificationOptions(player); // should
                                                                                             // take
                                                                                             // boardstate
                if (armiesToMove != (fortifyOptions.size() > 1)) {
                    System.out.println("fail");
                }
                for (BoardState fortifyState : fortifyOptions) {
                    boardStates.put(fortifyState, attackRoute);
                }
            }
        }
        return boardStates;
    }

    private AttackRoute chooseAttackRoute() {
        Map<BoardState, Double> boardStateValues = Maps.newHashMap();
        BoardEvaluator2 boardEvaluator = new BoardEvaluator2();
        Map<BoardState, AttackRoute> boardStates = buildBoardStates(buildAttackRoutes());
        for (BoardState boardState : boardStates.keySet()) {
            System.out.print("AttackROute: " + boardStates.get(boardState) + " ");
            boardStateValues.put(boardState,
                    boardEvaluator.getBoardValue(boardState, player, continents));
        }
        double highestBoardValue = 0;
        BoardState bestBoardState = null;
        for (BoardState boardState : boardStateValues.keySet()) {
            if (boardStateValues.get(boardState) > highestBoardValue) {
                highestBoardValue = boardStateValues.get(boardState);
                bestBoardState = boardState;
            }
        }
        System.out.println("Chosen Attack Route: " + boardStates.get(bestBoardState)
                + "BoardState Value: " + boardStateValues.get(bestBoardState));
        return boardStates.get(bestBoardState);
    }

    private void setGoalContinent() {
        List<Continent> continentRatios = new ArrayList<Continent>();
        for (int i = 0; i < 6; i++) {
            double numTerritories = 0;
            double numTerritoriesControlled = 0;
            double armiesControlled = 0;
            double enemyArmies = 0;
            for (Territory territory : continents.get(i).getTerritories()) {
                numTerritories++;
                if (boardState.getPlayer(territory) == player) {
                    numTerritoriesControlled++;
                    armiesControlled += boardState.getArmies(territory);
                } else {
                    enemyArmies += boardState.getArmies(territory);
                }
            }
            double ratio = (numTerritoriesControlled + armiesControlled)
                    / (numTerritories + enemyArmies + armiesControlled);
            continents.get(i).score = ratio * (10 - continents.get(i).getBonusArmies());
            continentRatios.add(continents.get(i));
            Collections.sort(continentRatios);
        }
        goalContinent = continentRatios.get(0);
    }

}
