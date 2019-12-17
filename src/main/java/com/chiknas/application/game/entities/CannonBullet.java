package com.chiknas.application.game.entities;

/**
 * com.chiknas.application.maingame.CannonBullet, created on 26/07/2019 19:18 <p>
 * @author NikolaosK
 */
public class CannonBullet extends Bullet {
  public CannonBullet(String bulletIcon, int positionX) {
    super(bulletIcon, positionX);
    size = 80;
    velocity = 3;
  }
}
