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

	// YUCK
	public final Mouse mouse;
	public MainGame game;

	public Board(MainGame game) {
		this.game = game;
		mainFrame = new JFrame("risk");
		try {
			background = ImageIO.read(new File(
					"/Users/danielglasgow/Desktop/riskboard.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		currentBackground = new TextOverlay(background);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(currentBackground, BorderLayout.CENTER);
		mainFrame.add(game.instructionPanel.getMainPanel(), BorderLayout.SOUTH);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);
		mainFrame.setResizable(false);
		mouse = new Mouse(this);
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

}
