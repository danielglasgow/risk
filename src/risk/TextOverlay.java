package risk;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class TextOverlay extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private String text;
	private int locX;
	private int locY;
	private String color;
	private Map<String, Color> colors;

	public TextOverlay(BufferedImage inputImage) {
		this.image = inputImage;
	}

	public TextOverlay(BufferedImage inputImage, Territory territory,
			int armies, Player player) {
		this.image = inputImage;
		this.text = "" + armies;
		this.locX = territory.locX;
		this.locY = territory.locY;
		this.color = player.color;
		this.colors = new HashMap<String, Color>();

		initializeColors();

		this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		image = process(image);
	}

	private BufferedImage process(BufferedImage old) {
		int w = old.getWidth();
		int h = old.getHeight();
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.drawImage(old, 0, 0, null);
		g2d.setPaint(colors.get(color));
		g2d.setFont(new Font("Serif", Font.BOLD, 20));
		String s = text;
		// FontMetrics fm = g2d.getFontMetrics();
		// int x = img.getWidth() - fm.stringWidth(s) - 100; make a checker that
		// x and y are in range
		// int y = fm.getHeight();
		g2d.drawString(s, locX, locY);
		g2d.dispose();
		return img;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

	private void initializeColors() {
		colors.put("red", Color.red);
		colors.put("blue", Color.blue);
		colors.put("gray", Color.gray);
		colors.put("green", Color.green);
		colors.put("black", Color.black);
		colors.put("yellow", Color.yellow);
		colors.put("orange", Color.orange);
	}

	public BufferedImage getImage() {
		return this.image;
	}

}