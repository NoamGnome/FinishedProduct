import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Time;

public class GameScreen extends JPanel implements KeyListener, MouseListener, ActionListener {
    public Worm worm;
    private Timer timer = new Timer(20, this);
    private Time t = new Time(1);
    private BufferedImage background;
    private BufferedImage rightImage;
    private BufferedImage leftImage;
    private BufferedImage button1;
    private BufferedImage firstImage;
    private BufferedImage secondImage;
    private BufferedImage thirdImage;
    private Image jumpRight;
    private Image jumpLeft;
    private Image attack;
    private Rectangle rect;
    private float fadeSpeed = 2f;
    private boolean yesAttack;
    private boolean[] keys;
    private boolean lastLeft;
    private boolean start = false;
    private boolean lastRight = true;
    private boolean jumpKeyPressed = false;
    private boolean animation = false;
    private boolean idle = true;
    private boolean hasWeapon;
    public GameScreen() {
        timer.start();
        try {
            background = ImageIO.read(new File("src/background.png"));
            rightImage = ImageIO.read(new File("src/wormR.png"));
            leftImage = ImageIO.read(new File("src/wormL.png"));
            button1 = ImageIO.read(new File("src/startButton.png"));
            firstImage = ImageIO.read(new File("src/firstImage.png"));
            secondImage = ImageIO.read(new File("src/secondImage.png"));
            thirdImage = ImageIO.read(new File("src/thirdImage.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        jumpRight = new ImageIcon("src/wormLongJumpRGIF.gif").getImage();
        jumpLeft = new ImageIcon("src/wormLongJumpLGIF.gif").getImage();
        attack = new ImageIcon("src/wormSword.gif").getImage();
        worm = new Worm("src/wormWalkRGIF (1).gif", "src/wormWalkLGIF (1).gif");
        keys = new boolean[128];
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        if (!start) {
            g.drawImage(rightImage, 400, 290, null);
            g.drawImage(button1, 300, 160, null);
            rect = new Rectangle(320, 208, (button1.getWidth() - 34), (button1.getHeight()) - 112);
        }
        if (start) {
            idle = !keys[65] && !keys[68] && !keys[32] && !worm.isJumping;
            if (idle) {
                if (lastRight) {
                    g.drawImage(rightImage, worm.getX(), worm.getY(), null);
                }
                if (lastLeft) {
                    g.drawImage(leftImage, worm.getX(), worm.getY(), null);
                }
            } else {
                if (lastRight && !keys[65] && !keys[68] && !worm.isJumping) {
                    g.drawImage(rightImage, worm.getX(), worm.getY(), null);
                }
                if (lastLeft && !keys[65] && !keys[68] && !worm.isJumping) {
                    g.drawImage(leftImage, worm.getX(), worm.getY(), null);
                }
                if (keys[65]) {
                    worm.fLeft();
                    worm.moveLeft();
                    lastLeft = true;
                    lastRight = false;
                }
                if (keys[68]) {
                    ;
                    worm.fRight();
                    worm.moveRight();
                    lastLeft = false;
                    lastRight = true;
                }
                if (keys[32] && !jumpKeyPressed) { // Space key for jump
                    jumpKeyPressed = true;
                    if (lastRight) {
                        worm.jumpRight();
                    }
                    if (lastLeft) {
                        worm.jumpLeft();
                    }
                }
                if (!keys[32]) {
                    jumpKeyPressed = false;
                }
                worm.update();
                if (worm.isJumping) {
                    if (lastRight) {
                        g.drawImage(jumpRight, worm.getX(), worm.getY(), null);
                    } else {
                        g.drawImage(jumpLeft, worm.getX(), worm.getY(), null);
                    }
                } else {
                    g.drawImage(worm.getWorm(), worm.getX(), worm.getY(), null);
                }
            }
        }
    }

    public void gameLoop() {
        while (true) {
            handleInput();
            repaint();
            try {
                Thread.sleep(16); // Simulate ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void handleInput() {
        // All input handling is done in the paintComponent method
    }
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        System.out.println(key);
        keys[key] = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        keys[key] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (hasWeapon) {
            if (rect.contains(e.getPoint())) {
                start = true;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            yesAttack = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fadeSpeed -= 0.01f;
        if (fadeSpeed < 0) {
            fadeSpeed = 0;
            timer.stop();
        }
        repaint();
    }
}
