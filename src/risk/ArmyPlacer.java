package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.google.common.collect.Maps;

public class ArmyPlacer {
	private final MainGame game;
	private final Player player;
	private final InstructionPanel instructionPanel;
	private final int armiesToPlace;
	private final CountDownLatch latch = new CountDownLatch(1);
	private final Map<Territory, Integer> initialArmiesPerTerritory = Maps
			.newHashMap();

	private int armiesPlaced = 0;

	public ArmyPlacer(MainGame game, Player player,
			InstructionPanel instructionPanel, int armiesToPlace) {
		this.game = game;
		this.player = player;
		for (Territory territory : game.territories) {
			initialArmiesPerTerritory.put(territory, territory.armies);
		}
		this.instructionPanel = instructionPanel;
		this.armiesToPlace = armiesToPlace;
	}

	public void placeArmies(Territory territory) {
		if (armiesPlaced < armiesToPlace) {
			if (territory.player == player) {
				territory.armies++;
				armiesPlaced++;
				if (armiesPlaced == armiesToPlace) {
					confirmPlacement();
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"You must place armies on territories you control");
			}
		}
	}

	/**
	 * Restart place armies from initial state when user presses RESTART button.
	 */
	private void restart() {
		for (Territory territory : game.territories) {
			territory.armies = initialArmiesPerTerritory.get(territory);
		}
		game.board.updateBackground();
		armiesPlaced = 0;
		run();
	}

	public void run() {
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
						InstructionPanel.newVisible,
						player.color.toUpperCase()
								+ "'s turn! Distribute "
								+ armiesToPlace
								+ " armies between your territories by clicking on the territory's army indicator.",
						button);
	}

	/**
	 * Prompt user to confirm completion of army placement.
	 */
	private void confirmPlacement() {
		JButton leftButton = new JButton();
		leftButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				instructionPanel.removeButtons();
				latch.countDown();
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
						InstructionPanel.newVisible,
						"You have placed all of your armies. If you would like to replace armies again click Place Again, otherwise click Continue",
						leftButton, rightButton);
	}

	/**
	 * Wait until all armies have been placed and user presses continue.
	 */
	public void await() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
