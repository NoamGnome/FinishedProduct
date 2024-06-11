import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Worm {
    private Image right;
    private Image left;
    private double x;
    private double y;
    private double velocityY = 0;
    private final double gravity = 0.002; // Reduced gravity for slower fall
    private double jumpSpeed = 0.2; // Reduced horizontal speed during jump
    private final double jumpStrength = -.7; // Reduced initial jump strength
    private double speed = .05;
    private boolean fRight;
    public boolean isJumping;
    private final int base = 290;
    private final int maxHeight = 330;
    public Worm(String right, String left) {
        fRight = true;
        x = 400;
        y = 290;
        this.right = new ImageIcon(right).getImage();
        this.left = new ImageIcon(left).getImage();
    }

    public int getX() {
        return (int) x;
    }
    public int getY() {
        return (int) y;
    }
    public void fRight() {
        fRight = true;
    }
    public void fLeft() {
        fRight = false;
    }
    public void jumpRight() {
        if (!isJumping) {
            velocityY = jumpStrength;
            isJumping = true;
            fRight = true;
        }
    }

    public void jumpLeft() {
        if (!isJumping) {
            velocityY = jumpStrength;
            isJumping = true;
            fRight = false;
        }
    }
    public void update() {
        // Apply gravity
        if (isJumping) {
            velocityY += gravity;
            y += velocityY;

            // Apply horizontal movement during jump
            if (fRight && x + jumpSpeed <= 750) {
                x += jumpSpeed;
            } else if (!fRight && x - jumpSpeed >= 0) {
                x -= jumpSpeed;
            }

            // Check for ground collision
            if (y >= base) {
                y = base;
                isJumping = false;
                velocityY = 0;
            }
        }
    }
    public void moveRight() {
        if (x + speed <= 750) {
            x += speed;
        }
    }
    public void moveLeft() {
        if (x - speed >= 0) {
            x-= speed;
        }
    }
    public Image getWorm() {
        if (fRight) {
            return right;
        } else {
            return left;
        }
    }
    public Rectangle wormRect() {
        int wormW = getWorm().getWidth(null);
        int wormH = getWorm().getHeight(null);
        Rectangle r = new Rectangle((int) x, (int) y, wormW, wormH);
        return r;
    }
}
