import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameScreen extends JPanel implements KeyListener, MouseListener, ActionListener {
    public Worm worm;
    private Timer timer1 = new Timer(20, this);
    private Timer timer2 = new Timer(20, this);
    private BufferedImage background;
    private BufferedImage rightImage;
    private BufferedImage leftImage;
    private BufferedImage button1;
    private BufferedImage title;
    private BufferedImage firstImage;
    private BufferedImage secondImage;
    private BufferedImage thirdImage;
    private BufferedImage nextButton1;
    private BufferedImage nextButton2;
    private BufferedImage sword;
    private BufferedImage swordRightImage;
    private BufferedImage swordLeftImage;
    private Image jumpRight;
    private Image jumpLeft;
    private Image attackRight;
    private Image attackLeft;
    private Rectangle startRect;
    private Rectangle next1;
    private Rectangle next2;
    private Rectangle swordRect;
    private float fadeSpeed = 1f;
    private boolean[] keys;
    private boolean lastLeft;
    private boolean lastRight = true;
    private boolean jumpKeyPressed = false;
    private boolean idle = true;
    public static boolean hasWeapon = false;
    private boolean startScreen = false;
    private boolean playScreen;
    private boolean next1Clicked = false;
    private boolean next2Clicked = false;
    private boolean click;
    private boolean isAttacking = false; // New flag for attack state
    private long attackStartTime; // New variable to store the start time of the attack
    private static final long ATTACK_DURATION = 500; // Duration of the attack animation in milliseconds

    public GameScreen() {
        if (next1Clicked) {
            timer1.start();
        }
        if (next2Clicked) {
            timer2.start();
        }
        try {
            background = ImageIO.read(new File("src/background.png"));
            rightImage = ImageIO.read(new File("src/wormR.png"));
            leftImage = ImageIO.read(new File("src/wormL.png"));
            button1 = ImageIO.read(new File("src/startButton.png"));
            firstImage = ImageIO.read(new File("src/firstImage.png"));
            secondImage = ImageIO.read(new File("src/secondImage.png"));
            thirdImage = ImageIO.read(new File("src/thirdImage.png"));
            title = ImageIO.read(new File("src/Title.png"));
            nextButton1 = ImageIO.read(new File("src/nextButton1.png"));
            nextButton2 = ImageIO.read(new File("src/nextButton2.png"));
            sword = ImageIO.read(new File("src/swordImage.png"));
            swordLeftImage = ImageIO.read(new File("src/wormSwordLeft.png"));
            swordRightImage = ImageIO.read(new File("src/wormSwordRight.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        jumpRight = new ImageIcon("src/wormLongJumpRGIF.gif").getImage();
        jumpLeft = new ImageIcon("src/wormLongJumpLGIF.gif").getImage();
        attackRight = new ImageIcon("src/wormSwordAttackRight.gif").getImage();
        attackLeft = new ImageIcon("src/wormSwordAttackLeft.gif").getImage();
        worm = new Worm("src/wormWalkRGIF (1).gif", "src/wormWalkLGIF (1).gif", "src/wormWalkGIFSwordRight.gif", "src/wormWalkGIFSwordLeft.gif");
        keys = new boolean[128];
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeSpeed));
        g.setColor(Color.PINK);
        if (!next1Clicked) {
            g.drawImage(firstImage, 0, 0, null);
            g.drawImage(nextButton1, 600, 10, null);
            next1 = new Rectangle(600, 10, nextButton1.getWidth(), nextButton1.getHeight());
        }
        if (next1Clicked && !next2Clicked) {
            g.drawImage(secondImage, 0, 0, null);
            g.drawImage(nextButton2, 600, 10, null);
            next2 = new Rectangle(600, 10, nextButton1.getWidth(), nextButton1.getHeight());
        }
        if (startScreen && next2Clicked) {
            g.drawImage(thirdImage, 0, 0, null);
            g.drawImage(title, 0, 190, null);
            g.drawImage(button1, 470, 230, null);
            startRect = new Rectangle(470, 230, (button1.getWidth() - 34), (button1.getHeight()) - 112);
        }
        if (playScreen) {
            g.drawImage(background, 0, 0, null);
            idle = !keys[65] && !keys[68] && !keys[32] && !worm.isJumping && !keys[81] && !isAttacking;
            if (!hasWeapon) {
                g.drawImage(sword, 30, 270, null);
                swordRect = new Rectangle(30, 270, sword.getWidth(), sword.getHeight());
            }
            if (idle) {
                if (lastRight && !hasWeapon) {
                    g.drawImage(rightImage, worm.getX(), worm.getY(), null);
                }
                if (lastLeft && !hasWeapon) {
                    g.drawImage(leftImage, worm.getX(), worm.getY(), null);
                }
                if (lastRight && hasWeapon) {
                    g.drawImage(swordRightImage, worm.getX(), worm.getY(), null);
                }
                if (lastLeft && hasWeapon) {
                    g.drawImage(swordLeftImage, worm.getX(), worm.getY(), null);
                }
            } else {
                if (lastRight && !keys[65] && !keys[68] && !worm.isJumping && !click && !keys[81] && !isAttacking) {
                    g.drawImage(rightImage, worm.getX(), worm.getY(), null);
                }
                if (lastLeft && !keys[65] && !keys[68] && !worm.isJumping && !click && !keys[81] && !isAttacking) {
                    g.drawImage(leftImage, worm.getX(), worm.getY(), null);
                }
                if (keys[69] && worm.wormRect().intersects(swordRect)) {
                    hasWeapon = true;
                }
                if (keys[65]) {
                    worm.fLeft();
                    worm.moveLeft();
                    lastLeft = true;
                    lastRight = false;
                }
                if (keys[81] && hasWeapon && !worm.isJumping) {
                    isAttacking = true;
                    attackStartTime = System.currentTimeMillis();
                }
                if (keys[68]) {
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
                    if (isAttacking) {
                        if (lastRight) {
                            g.drawImage(attackRight, worm.getX(), worm.getY(), null);
                        } else {
                            g.drawImage(attackLeft, worm.getX(), worm.getY(), null);
                        }
                        // Check if the attack animation has finished
                        long elapsed = System.currentTimeMillis() - attackStartTime;
                        if (elapsed >= ATTACK_DURATION) { // Use a constant for the GIF duration
                            isAttacking = false;
                        }
                    } else {
                        g.drawImage(worm.getWorm(), worm.getX(), worm.getY(), null);
                    }
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
        click = e.getButton() == 1;
        if (next1.contains(e.getPoint())) {
            next1Clicked = true;
        }
        if (next2.contains(e.getPoint())) {
            next2Clicked = true;
            startScreen = true;
        }
        if (startRect.contains(e.getPoint())) {
            playScreen = true;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
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
        if (next1Clicked) {
            fadeSpeed -= 0.01f;
            if (fadeSpeed < 0) {
                fadeSpeed = 0;
                timer1.stop();
            }
            repaint();
        }
    }
}
