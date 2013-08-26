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
     * This class displays the number of armies on each territory, and which
     * player controls each territory by overlaying text on top of the game's
     * background image.
     */
    private static final long serialVersionUID = 1L;
    private BufferedImage image;
    private String text;
    private int coordinateX;
    private int coordinateY;
    private String color;
    private Map<String, Color> colors;

    public TextOverlay(BufferedImage inputImage) {
        this.image = inputImage;
    }

    public TextOverlay(BufferedImage inputImage, Territory territory, int armies, Player player) {
        this.image = inputImage;
        this.text = "" + armies;
        this.coordinateX = territory.coordinateX;
        this.coordinateY = territory.coordinateY;
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
        g2d.drawString(s, coordinateX, coordinateY);
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
        colors.put("orange", Color.orange);
        colors.put("purple", Color.magenta);
    }

    public BufferedImage getImage() {
        return this.image;
    }

}