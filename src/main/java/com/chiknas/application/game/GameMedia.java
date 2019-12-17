package com.chiknas.application.game;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * com.chiknas.application.GameMedia, created on 03/07/2019 16:51 <p>
 * @author NikolaosK
 */
public class GameMedia {

  public static Optional<MediaPlayer> getMediaPlayer(String resource) {
    Media sound = new Media(GameMedia.class.getResource(resource).toExternalForm());
    return Optional.of(new MediaPlayer(sound));
  }

  public static Optional<MediaPlayer> getLoseMediaPlayer(String spaceshipName) {
    URL playerLostSound = GameMedia.class.getResource("/sounds/lose/lose-" + spaceshipName + ".mp3");
    Media sound = playerLostSound != null
        ? new Media(playerLostSound.toExternalForm())
        : new Media(GameMedia.class.getResource("/sounds/lose/lose-spaceship.mp3").toExternalForm());

    return Optional.of(new MediaPlayer(sound));
  }

  public static Optional<MediaPlayer> getFireMediaPlayer(String spaceshipName) {
    URL playerFireSound = GameMedia.class.getResource("/sounds/fire/" + spaceshipName + ThreadLocalRandom.current().nextInt(1, 4) + ".mp3");
    URL defaultFireSound = GameMedia.class.getResource("/sounds/fire/spaceship" + ThreadLocalRandom.current().nextInt(1, 4) + ".mp3");
    Media sound = playerFireSound != null
        ? new Media(playerFireSound.toExternalForm())
        : new Media(defaultFireSound.toExternalForm());

    return Optional.of(new MediaPlayer(sound));
  }

  public static void playKillEnemySound() {
    play("/sounds/kill.mp3");
  }

  public static void playNavigateSound() {
    play("/sounds/navigate.mp3");
  }

  public static void playSelectionSound() { play("/sounds/selection.mp3"); }

  public static void playRewardSound() {play("/sounds/reward.mp3");}

  private static void play(String sound) {
    URL resource = GameMedia.class.getResource(sound);
    Media media = new Media(resource.toExternalForm());
    Optional<MediaPlayer> mediaPlayer = Optional.of(new MediaPlayer(media));
    mediaPlayer.ifPresent(MediaPlayer::play);
  }
}
