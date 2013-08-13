package risk;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Board { // make board a JPanel not frame...
	private final JFrame mainFrame;
	private BufferedImage background;
	private TextOverlay currentBackground;
	private final InstructionPanel instructionPanel;

	// YUCK
	private Mouse mouse = null;

	public Board() {
		mainFrame = new JFrame("risk");
		try {
			background = ImageIO.read(new File(
					"/Users/danielglasgow/Desktop/riskboard.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		currentBackground = new TextOverlay(background);
		instructionPanel = new InstructionPanel();
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(currentBackground, BorderLayout.CENTER);
		mainFrame.add(instructionPanel.getMainPanel(), BorderLayout.SOUTH);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);
		mainFrame.setResizable(false);

	}

	public void addMouse(Mouse mouse) {
		this.mouse = mouse;
		mainFrame.addMouseListener(mouse);
	}

	public void updateBackground(BoardState boardState) {
		TextOverlay newBackGround = new TextOverlay(background);
		for (Territory territory : boardState.getTerritories()) {
			newBackGround = new TextOverlay(newBackGround.getImage(),
					territory, boardState.getArmies(territory),
					boardState.getPlayer(territory));
		}
		mainFrame.remove(currentBackground);
		currentBackground = newBackGround;
		mainFrame.add(currentBackground);
		mainFrame.pack();
	}

	public InstructionPanel getInstructionPanel() {
		return instructionPanel;
	}

	public Mouse getMouse() {
		return mouse;
	}

}
