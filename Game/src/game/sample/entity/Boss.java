package game.sample.entity;

import game.sample.logic.BossFrame;

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

    private BufferedImage healthBarImage;


    private ArrayList<Missile> missiles;

    private int speedX;

    private int speedY;


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Boss(int x, int y, int width, int height, int maxHealth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.lastAttackTime = System.currentTimeMillis();
        this.attackInterval = 1000;
        this.random = new Random();
        loadBossImage();
        missiles = new ArrayList<>();

        this.speedX = 5;  // Replace with the desired speed in the x direction
        this.speedY = 5;  // Replace with the desired speed in the x direction

    }
    private void loadBossImage() {
        try {
            bossImage = ImageIO.read(new File("Game/resource/image/monster-removebg-preview.png"));
            healthBarImage = ImageIO.read(new File("Game/resource/image/Healthbar.png"));
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
        int missileWidth = 50;
        int missileHeight = 70;
        int missileSpeed = 50;
//        Missile missile = new Missile(x + width / 2 - missileWidth / 2, y + height, missileWidth, missileHeight, missileSpeed);
//        missiles.add(missile);

        // Calculate the missile's starting X and Y coordinates
        int startX = x - missileWidth / 2;
        int startY = y + height / 2 - missileHeight / 2;

        // Generate a random angle between 0 and 360 degrees
        double angle = Math.toRadians(random.nextInt(360));

        // Calculate the missile's speed in the X and Y directions
        int speedX = (int) (missileSpeed * Math.cos(angle));
        int speedY = (int) (missileSpeed * Math.sin(angle));

        Missile missile = new Missile(startX, startY, missileWidth, missileHeight, speedX, speedY);
//        Missile missile = new Missile(x + width / 2 - missileWidth / 2, y + height, missileWidth, missileHeight, speedX, speedY);
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
        if (healthBarImage != null) {
            int healthBarHeight = 100; // Adjust as needed
            int offsetY = -50; // Adjust as needed to position the health bar above the boss
            int healthBarWidth = (int) (((double) health / maxHealth) * healthBarImage.getWidth());
            g2d.drawImage(healthBarImage,
                    (x-90), y + offsetY,
                    (x-600) + healthBarWidth, y + offsetY + healthBarHeight,
                    0, 0,
                    healthBarWidth, healthBarImage.getHeight(),
                    null);
        } else {
            // Fallback if health bar image could not be loaded
            g2d.setColor(Color.RED);
            int healthBarWidth = (int) (((double) health / maxHealth) * width);
            int healthBarHeight = 20; // Adjust as needed
            int offsetY = -30; // Adjust as needed to position the health bar above the boss
            g2d.fillRect(x, y + offsetY, healthBarWidth, healthBarHeight);
        }

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
//
//        // Boundary checking (adjust as needed)
//        if (x < 0 || x > BossFrame.GAME_WIDTH - width) {
//            speedX = -speedX; // Reverse direction when hitting the sides
//        }
//        if (y < 0 || y > BossFrame.GAME_HEIGHT - height) {
//            speedY = -speedY; // Reverse direction when hitting top/bottom
//        }

        // Ensure the boss stays in the right half of the window
        int halfWidth = BossFrame.GAME_WIDTH / 2;
        if (x < halfWidth) {
            x = halfWidth; // Set to the left edge of the right half
            speedX = -speedX; // Reverse direction
        } else if (x > BossFrame.GAME_WIDTH - width) {
            x = BossFrame.GAME_WIDTH - width; // Set to the right edge of the window
            speedX = -speedX; // Reverse direction
        }

        if (y < 0) {
            y = 0; // Set to the top edge of the window
            speedY = -speedY; // Reverse direction
        } else if (y > BossFrame.GAME_HEIGHT - height) {
            y = BossFrame.GAME_HEIGHT - height; // Set to the bottom edge of the window
            speedY = -speedY; // Reverse direction
        }

//        // Update attack speed based on health
//        if (health < (600) / 4) {
//            attackInterval = 100; // Triple speed
//        } else if (health < (750) / 2) {
//            attackInterval = 200; // Double speed
//        }else {
//            attackInterval = 600; // Normal speed
//        }

        if (health < 600) {
            attackInterval = 200; // Triple speed
        } else if (health < 750) {
            attackInterval = 500; // Double speed
        } else {
            attackInterval = 1000; // Normal speed
        }
//        System.out.println("health current:"+health);
//        System.out.println("attackIntervalh:"+attackInterval);



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
                    missile.getX() > BossFrame.GAME_WIDTH ||
                    missile.getY() + missile.getHeight() < 0 ||
                    missile.getY() > BossFrame.GAME_HEIGHT) {
                missileIterator.remove();
            }
        }
    }

    public void takeDamage(int damage) {
        if (random.nextInt(10) != 0) { // 90% chance to take damage
            health -= damage;
//            System.out.println("health:"+health);
            if (health < 510) { //should set to 510
                health = 0;
            }
        }
    }

    public boolean isAlive() {
        return health > 0;
    }
}
