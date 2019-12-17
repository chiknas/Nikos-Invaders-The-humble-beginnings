package com.chiknas.application.game.entities;

import com.chiknas.application.game.Game;
import javafx.scene.image.Image;

import java.util.Arrays;

/**
 * Class responsible for bullet attributes.
 *
 * com.chiknas.application.maingame.Bullet, created on 28/06/2019 23:25 <p>
 * @author NikolaosK
 */
public class Bullet extends Explosion {
  private Image bullet;
  public static final int damage = 25;
  public int size = 20;
  public int velocity = 10;
  private int positionX;
  private int positionY = Game.canvasMaxHeight;
  private boolean hasHit = false;

  public Bullet(String bulletIcon, int positionX) {
    bullet = new Image(getClass().getResource("/gameIcons/bullets/bullet-" + bulletIcon + ".png").toExternalForm());
    this.positionX = positionX + Spaceship.size / 2 - 10;
  }

  public Image getBullet() {
    return bullet;
  }

  public int getPositionY() {
    positionY -= velocity;
    return this.positionY;
  }

  public int getPositionX() {
    return this.positionX;
  }

  public boolean hasHit() {
    return hasHit;
  }

  public void hit() {
    this.hasHit = true;
  }

  //-------------------------------------------------------------- AVAILABLE BULLETS ----------------------------------------------------
  public enum Bullets {
    laser, apple, coffee, beer, burger, kame, dart, swatter, donut;

    public static String[] getNames() {
      return Arrays.stream(Bullets.class.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
  }
}
