package com.chiknas.application.game.entities;

import com.chiknas.application.Main;
import com.chiknas.application.game.Game;
import javafx.scene.image.Image;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * com.chiknas.application.maingame.Enemy, created on 28/06/2019 23:02 <p>
 * @author NikolaosK
 */
public class Enemy extends Explosion {
  private Image enemy;
  public static final int size = 120;
  private int velocity;
  private int points;
  private int positionX;
  private int positionY;
  private int health;
  private boolean hasHitSpaceShip = false;
  private Difficulty difficulty;
  private boolean hasPowerUp;

  public Enemy() {
    try {
      Resource[] availableEnemies = new PathMatchingResourcePatternResolver().getResources("/gameIcons/enemies/*.png");
      enemy = new Image(availableEnemies[ThreadLocalRandom.current().nextInt(0, availableEnemies.length)].getURL().toExternalForm());
      positionX = ThreadLocalRandom.current().nextInt(0, Game.canvasMaxWidth);
      positionY = -size;
      this.difficulty = Difficulty.getDifficulty(Main.settingsMap.get("difficulty"));
      health = difficulty.getHealthValue();
      velocity = difficulty.getVelocityValue();
      points = health * velocity * difficulty.getMultiplier();
      int powerUpChance = ThreadLocalRandom.current().nextInt(1, 10);
      hasPowerUp = powerUpChance == 9;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Image getEnemy() {
    return this.enemy;
  }

  public int getPositionY() {
    return positionY;
  }

  public void move() {
    positionY += velocity;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public void damage(int damageTaken) {
    health -= damageTaken;
  }

  public int getVelocity() {
    return velocity;
  }

  public int getPoints() {
    return points;
  }

  public int getPositionX() {
    return positionX;
  }

  public boolean hasHitSpaceShip() {
    return hasHitSpaceShip;
  }

  public void hitSpaceShip() {
    this.hasHitSpaceShip = true;
  }

  public boolean hasPowerUp() {
    return hasPowerUp;
  }

  public PowerUp dropPowerUp() {
    if (hasPowerUp) {
      hasPowerUp = false;
      return new PowerUp(positionX, positionY, velocity);
    }
    return null;
  }

}
