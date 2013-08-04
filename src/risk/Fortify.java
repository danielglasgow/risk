package risk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Fortify extends PhaseHandler {

	private Player player;
	private InstructionPanel instructionPanel;

	public Fortify(Player player, InstructionPanel instructionPanel) {
		this.player = player;
		this.instructionPanel = instructionPanel;
	}

	@Override
	public void mouseClicked(Territory territory) {
		if (player.fortify1.equals(territory)) {
			if (player.fortify2.armies > 1) {
				player.fortify1.armies++;
				player.fortify2.armies--;
			} else {
				JOptionPane.showMessageDialog(null,
						"Territories must have at least one army");
			}
		} else if (player.fortify2.equals(territory)) {
			if (player.fortify1.armies > 1) {
				player.fortify1.armies--;
				player.fortify2.armies++;
			} else {
				JOptionPane.showMessageDialog(null,
						"Territories must have at least one army");
			}
		} else {
			JOptionPane.showMessageDialog(null, "You must click on "
					+ player.fortify1.name + " or " + player.fortify2.name);
		}

	}

	@Override
	public void displayInterface() {
		JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				finishPhase(Phase.END_TURN);
			}
		});
		instructionPanel.addCustomButtons(InstructionPanel.newVisible,
				"Click on " + player.fortify1.name + " to move armies from "
						+ player.fortify2.name + ". Click on "
						+ player.fortify2.name + " to move armies from "
						+ player.fortify1.name, button);
	}

}
