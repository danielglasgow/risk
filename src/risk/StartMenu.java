package risk;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * The StartMenu presents a dialog box and asks for the number of players. It
 * provides a wait() function which will wait until the number of players has
 * been selected. Clients can then ask the object for the number of players
 * using getNumPlayers().
 */
public class StartMenu {
    private final JFrame startMenuFrame;
    private int numPlayers;
    private final CountDownLatch latch = new CountDownLatch(1);

    public StartMenu() {
        startMenuFrame = new JFrame();
        startMenu(startMenuFrame);
    }

    private void startMenu(JFrame startMenu) {
        startMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startMenu.setLayout(new FlowLayout());
        JLabel prompt = new JLabel();
        prompt.setText("Choose number of players:");
        startMenu.add(prompt);
        for (int i = 2; i <= 6; i++) {
            JButton button = new JButton();
            final int finalPlayer = i;
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    startMenuFrame.dispose();
                    numPlayers = finalPlayer;
                    latch.countDown();
                }
            });
            button.setText("" + i);
            startMenu.add(button);
        }
        startMenu.pack();
        startMenu.setVisible(true);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void await() throws InterruptedException {
        latch.await();
    }
}
