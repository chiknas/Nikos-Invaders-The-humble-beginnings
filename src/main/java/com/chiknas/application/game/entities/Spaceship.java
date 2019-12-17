package com.chiknas.application.game.entities;

import com.chiknas.application.game.Game;
import com.chiknas.application.game.GameMedia;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

/**
 * Class responsible for the control and attributes of the spaceship/player.
 *
 * com.chiknas.application.maingame.Spaceship, created on 28/06/2019 22:09 <p>
 * @author NikolaosK
 */
public class Spaceship {
  private Image spaceship;
  private String spaceshipName;
  Optional<MediaPlayer> mediaPlayer;
  public static final int size = 80;
  private int hp = 3;
  private int cannonBullets = 3;
  private int position = Game.canvasMaxWidth / 2;
  private int velocity = 0;
  private int maxVelocity = 20;

  private URL spaceshipIcon = getClass().getResource("/gameIcons/players/player-spaceship.png");

  public Spaceship(String spaceshipName) {
    this.spaceshipName = spaceshipName;
    URL playerIcon = getClass().getResource("/gameIcons/players/player-" + spaceshipName + ".png");

    spaceship = playerIcon != null
        ? new Image(playerIcon.toExternalForm())
        : new Image(spaceshipIcon.toExternalForm());
  }

  public Image getSpaceship() {
    return spaceship;
  }

  public void fireSound() {
    mediaPlayer = GameMedia.getFireMediaPlayer(spaceshipName);
    mediaPlayer.ifPresent(MediaPlayer::play);
  }

  public void lostSound() {
    mediaPlayer = GameMedia.getLoseMediaPlayer(spaceshipName);
    mediaPlayer.ifPresent(MediaPlayer::play);
  }

  public void hitSound() {
    mediaPlayer = GameMedia.getMediaPlayer("/sounds/hit.mp3");
    mediaPlayer.ifPresent(player -> {
      player.setVolume(player.getVolume() / 6);
      player.play();
    });
  }

  public int getPosition() {
    return position;
  }

  public void move(){
    if (position < 0) {
      this.position = 0;
    } else if (position > Game.canvasMaxWidth) {
      this.position = Game.canvasMaxWidth;
    } else {
      this.position += velocity;
    }
  }

  public void setVelocity(Boolean right) {
    if (right == null) {
      velocity = 0;
    } else if (right) {
      velocity = maxVelocity;
    } else {
      velocity = -maxVelocity;
    }
  }

  public int getHP() {
    return this.hp;
  }

  public void damage() {
    this.hp -= 1;
  }

  public void heal(){this.hp += 1;}

  public CannonBullet fireCannonBullet(){
    this.cannonBullets -= 1;
    return new CannonBullet("cannon", Game.canvasMaxWidth / 2);
  }

  public void reloadCannon(){this.cannonBullets += 1;}

  public int availableCannonBullets(){
    return this.cannonBullets;
  }

  //-------------------------------------------------------------- AVAILABLE SPACESHIPS ----------------------------------------------------
  public enum Spaceships {
    goku, dragoon, superman, spaceship, nikos, toystory, mud, ghost, storm;

    public static String[] getNames() {
      return Arrays.stream(Spaceships.class.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
  }
}
