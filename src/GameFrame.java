import javax.swing.*;

public class GameFrame implements Runnable{

    private GameScreen p;

    public GameFrame() {
        JFrame f = new JFrame("meh");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(800, 500);
        p = new GameScreen();
        f.add(p);
        f.setVisible(true);
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while (true) {
            p.repaint();  // we don't ever call "paintComponent" directly, but call this to refresh the panel
        }
    }
}
