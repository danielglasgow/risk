package risk;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Board { // make board a JPanel not frame...
	private final JFrame mainFrame;
	private BufferedImage background;
	private TextOverlay currentBackground;
	private final List<Territory> territories;
	public MainGame game;

	public Board(MainGame game) {
		mainFrame = new JFrame("risk");
		try {
			background = ImageIO.read(new File(
					"/Users/danielglasgow/Desktop/riskboard.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.territories = game.territories;
		this.game = game;

		currentBackground = new TextOverlay(background);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(currentBackground, BorderLayout.CENTER);
		mainFrame.add(game.instructionPanel.getMainPanel(), BorderLayout.SOUTH);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);
		mainFrame.setResizable(false);
		Mouse mouse = new Mouse(this);
		mainFrame.addMouseListener(mouse);
	}

	public void updateBackground() {
		TextOverlay newBG = new TextOverlay(background);
		for (Territory territory : territories) {
			newBG = new TextOverlay(newBG.getImage(), territory);
		}
		mainFrame.remove(currentBackground);
		currentBackground = newBG;
		mainFrame.add(currentBackground);
		mainFrame.pack();
	}

	public List<Territory> getTerritories() {
		return territories;
	}
}
