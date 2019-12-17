package com.chiknas.application.game.entities;

import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * com.chiknas.application.game.entities.PowerUp, created on 06/08/2019 11:01 <p>
 * @author NikolaosK
 */
public class PowerUp {
  private Image powerUp;
  public static final int size = 60;
  private int velocity;
  private int positionX;
  private int positionY;
  private boolean hasHitSpaceShip = false;
  private PowerUpTypes type;

  public PowerUp(int startingPositionX, int startingPositionY, int velocity) {
    type = PowerUpTypes.getType();
    powerUp = new Image(getClass().getResource("/gameIcons/powerUps/" + type.getPowerUpImage() + ".png").toExternalForm());
    positionX = startingPositionX;
    positionY = startingPositionY;
    this.velocity = velocity;
  }

  public int getPositionX() {return this.positionX;}

  public int getPositionY() {
    return positionY;
  }

  public void move() {
    positionY += velocity;
  }

  public Image getPowerUp() { return this.powerUp; }

  public boolean hasHitSpaceShip() {
    return hasHitSpaceShip;
  }

  public void hitSpaceShip() {
    this.hasHitSpaceShip = true;
  }

  public PowerUpTypes getType() {return this.type;}

  public enum PowerUpTypes {
    hp("heart"), cannon("cannon"), points("prize");

    String powerUpImage;

    PowerUpTypes(String powerUpImage) {
      this.powerUpImage = powerUpImage;
    }

    public String getPowerUpImage() {return this.powerUpImage;}

    public static PowerUpTypes getType() {
      PowerUpTypes[] powerUpTypes = Arrays.stream(PowerUpTypes.class.getEnumConstants()).toArray(PowerUpTypes[]::new);
      return powerUpTypes[ThreadLocalRandom.current().nextInt(0, powerUpTypes.length)];
    }
  }
}
