package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * This class handles user input while a human player is placing armies on the
 * board. This class also handles the interface while a human player is placing
 * armies.
 * 
 * The player clicks on a territory to increase its armies by one. If a player
 * wants to restart army placement , he may click on the "restart placement"
 * button which removes all the armies he has placed so far. Once a player has
 * placed all his alloted armies, he must verify he is satisfied with his
 * placement by clicking the "continue" button which changes the turn phase to
 * "ATTACK_FROM". Otherwise, the player must click the "Place Armies Again"
 * button.
 */
public class PlaceArmiesHandler extends HumanPhaseHandler {
	private final Player player;
	private final InstructionPanel instructionPanel;
	private final int armiesToPlace;
	private final BoardState boardState;
	private final BoardState savedBoardState;

	private int armiesPlaced = 0;
	private boolean confirmingPlacement = false;

	public PlaceArmiesHandler(BoardState boardState, Player player,
			InstructionPanel instructionPanel, int armiesToPlace) {
		this.boardState = boardState;
		this.player = player;
		this.savedBoardState = boardState.copy();
		this.instructionPanel = instructionPanel;
		this.armiesToPlace = armiesToPlace;
	}

	private void placeArmies(Territory territory) {
		if (boardState.getPlayer(territory) == player) {
			boardState.increaseArmies(territory, 1);
			armiesPlaced++;
			displayInterface();
			if (armiesPlaced == armiesToPlace) {
				confirmPlacement();
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"You must place armies on territories you control");
		}
	}

	/**
	 * Restart place armies from initial state when user presses RESTART button.
	 */
	private void restart() {
		boardState.update(savedBoardState);
		boardState.updateBackground();
		armiesPlaced = 0;
		confirmingPlacement = false;
		displayInterface();
	}

	/**
	 * Sets instruction panel interface
	 */
	@Override
	public void displayInterface() {
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				restart();
			}
		});
		button.setText("Restart Army Placement");
		instructionPanel
				.addCustomButtons(
						InstructionPanel.NEW_VISIBLE,
						player.color.toUpperCase()
								+ "'s turn! Distribute "
								+ (armiesToPlace - armiesPlaced)
								+ " armies between your territories by clicking on the territory's army indicator.",
						button);
	}

	/**
	 * Prompt user to confirm completion of army placement.
	 */
	private void confirmPlacement() {
		confirmingPlacement = true;
		JButton leftButton = new JButton();
		leftButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				finishPhase(HumanTurnPhases.ATTACK_FROM);

			}
		});
		leftButton.setText("Continue");

		JButton rightButton = new JButton();
		rightButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				restart();
			}
		});
		rightButton.setText("Place Again");

		instructionPanel
				.addCustomButtons(
						InstructionPanel.NEW_VISIBLE,
						"You have placed all of your armies. If you would like to place armies again click Place Again, otherwise click Continue",
						leftButton, rightButton);
	}

	@Override
	public void mouseClicked(Territory territory) {
		if (!confirmingPlacement) {
			placeArmies(territory);
		}
	}

}
