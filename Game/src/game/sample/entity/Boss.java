package game.sample.entity;

import game.sample.logic.GameFrameOld;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Iterator;

public class Boss {
    private int x, y;
    private int width, height;
    private int health;
    private int maxHealth;
    private long lastAttackTime;
    private int attackInterval; // in milliseconds
    private Random random;

    private BufferedImage bossImage;

    private ArrayList<Missile> missiles;

    private int speedX;

    private int speedY;


    public Boss(int x, int y, int width, int height, int maxHealth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.lastAttackTime = System.currentTimeMillis();
        this.attackInterval = 2000; // default attack every 2 seconds
        this.random = new Random();
        loadBossImage();
        missiles = new ArrayList<>();

        this.speedX = 5;  // Replace with the desired speed in the x direction
        this.speedY = 5;  // Replace with the desired speed in the x direction

    }
    private void loadBossImage() {
        try {
            bossImage = ImageIO.read(new File("/Users/frederick_zou/Desktop/GameJam2023/Game/resource/image/monster-removebg-preview.png"));
        } catch (IOException e) {
            e.printStackTrace();
            bossImage = null;
        }
    }

    public void attack() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime > attackInterval) {
            lastAttackTime = currentTime;
            launchMissile();
        }
    }

    private void launchMissile() {
        int missileWidth = 10;
        int missileHeight = 20;
        int missileSpeed = 5;
//        Missile missile = new Missile(x + width / 2 - missileWidth / 2, y + height, missileWidth, missileHeight, missileSpeed);
//        missiles.add(missile);
        // Generate a random angle between 0 and 360 degrees
        double angle = Math.toRadians(random.nextInt(360));

        // Calculate the missile's speed in the X and Y directions
        int speedX = (int) (missileSpeed * Math.cos(angle));
        int speedY = (int) (missileSpeed * Math.sin(angle));

        Missile missile = new Missile(x + width / 2 - missileWidth / 2, y + height, missileWidth, missileHeight, speedX, speedY);
        missiles.add(missile);

    }

    public void render(Graphics2D g2d) {
        if (bossImage != null) {
            g2d.drawImage(bossImage, x, y, width, height, null);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillRect(x, y, width, height);
        }

        // Draw the health bar
        g2d.setColor(Color.GREEN);
        int healthBarWidth = (int) (((double) health / maxHealth) * width);
        g2d.fillRect(x, y - 20, healthBarWidth, 10);

        // Draw the health bar border
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y - 20, width, 10);

        for (Missile missile : missiles) {
            missile.render(g2d);
        }
    }

    public void update() {
        x += speedX;
        y += speedY;

        // Random movement
//        x += random.nextInt(21) - 10; // Move randomly between -10 and 10
//        y += random.nextInt(21) - 10;

        // Boundary checking (adjust as needed)
        if (x < 0 || x > GameFrameOld.GAME_WIDTH - width) {
            speedX = -speedX; // Reverse direction when hitting the sides
        }
        if (y < 0 || y > GameFrameOld.GAME_HEIGHT - height) {
            speedY = -speedY; // Reverse direction when hitting top/bottom
        }

        // Update attack speed based on health
        if (health < maxHealth / 4) {
            attackInterval = 666; // Triple speed
        } else if (health < maxHealth / 2) {
            attackInterval = 1000; // Double speed
        }

        // Handle attacking
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime > attackInterval) {
            lastAttackTime = currentTime;
            launchMissile();

        }
        Iterator<Missile> missileIterator = missiles.iterator();
        while (missileIterator.hasNext()) {
            Missile missile = missileIterator.next();
            missile.update();

            // Check if the missile is off-screen
            if (missile.getX() + missile.getWidth() < 0 ||
                    missile.getX() > GameFrameOld.GAME_WIDTH ||
                    missile.getY() + missile.getHeight() < 0 ||
                    missile.getY() > GameFrameOld.GAME_HEIGHT) {
                missileIterator.remove();
            }
        }
    }

    public void takeDamage(int damage) {
        if (random.nextInt(10) != 0) { // 90% chance to take damage
            health -= damage;
            if (health < 0) {
                health = 0;
            }
        }
    }

    public boolean isAlive() {
        return health > 0;
    }
}
