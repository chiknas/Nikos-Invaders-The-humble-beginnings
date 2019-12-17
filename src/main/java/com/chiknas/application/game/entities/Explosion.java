package com.chiknas.application.game.entities;

import javafx.scene.image.Image;

/**
 * com.chiknas.application.maingame.Explosion, created on 26/07/2019 22:50 <p>
 * @author NikolaosK
 */
public class Explosion {

  public static final int explosionTime = 10;
  private int currentExplosionTime = explosionTime;
  private int explosionStage = 1;

  public int getExplosionTime() {
    currentExplosionTime -= 1;
    //calculate the explosion stage of the killed enemy
    switch (currentExplosionTime) {
      case 3 * (explosionTime / 4):
        explosionStage = 2;
        break;
      case 2 * (explosionTime / 4):
        explosionStage = 3;
        break;
      case explosionTime / 4:
        explosionStage = 4;
        break;
    }
    return currentExplosionTime;
  }

  public Image getExplosion() {
    return new Image(getClass().getResource("/gameIcons/enemies/explosion/explosion" + explosionStage + ".png").toExternalForm());
  }
}
