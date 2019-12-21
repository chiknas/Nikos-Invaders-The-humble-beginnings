package com.chiknas.application.game;

import com.chiknas.application.HighScore;
import com.chiknas.application.Main;
import com.chiknas.application.components.CustomAlert;
import com.chiknas.application.database.HighScoreUCI;
import com.chiknas.application.game.entities.*;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * com.chiknas.application.Game, created on 28/06/2019 21:21 <p>
 * @author NikolaosK
 */
public class Game implements Initializable {

  @FXML
  private Canvas canvas;

  @FXML
  private Label score;

  @FXML
  private Label health1;

  @FXML
  private Label health2;

  @FXML
  private Label health3;

  @FXML
  private Label cannonBullet1;

  @FXML
  private Label cannonBullet2;

  @FXML
  private Label cannonBullet3;

  public static int canvasMaxWidth;
  public static int canvasMaxHeight;

  private AnimationTimer animationTimer;
  private Clip music;
  private GraphicsContext gc;
  private Spaceship spaceship;
  private Image heart = new Image(getClass().getResource("/gameIcons/heart.png").toExternalForm(), 160, 60, true, true);
  private Image brokenHeart = new Image(getClass().getResource("/gameIcons/heart-broken.png").toExternalForm(), 160, 60, true, true);
  private Image cannonBullet = new Image(getClass().getResource("/gameIcons/bullets/bullet-cannon.png").toExternalForm(), 160, 60, true, true);

  private int points = 0;
  private List<Enemy> enemies = new ArrayList<>();
  private List<Enemy> deadEnemies = new ArrayList<>();
  private List<Enemy> explodedEnemies = new ArrayList<>();
  private List<Bullet> bullets = new ArrayList<>();
  private List<CannonBullet> cannonBullets = new ArrayList<>();
  private List<Bullet> usedBullets = new ArrayList<>();
  private List<CannonBullet> usedCannonBullets = new ArrayList<>();
  private List<PowerUp> powerUps = new ArrayList<>();
  private List<PowerUp> usedPowerUps = new ArrayList<>();

  private long createdMillis = System.currentTimeMillis();
  public int secondsToGenerateEnemy;


  final Set<KeyCode> pressedKeys = new HashSet();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    canvasMaxWidth = (int) (Main.stageWidth - Spaceship.size);
    canvasMaxHeight = (int) (Main.stageHeight - Spaceship.size - 50);
    canvas.setHeight(Main.stageHeight);
    canvas.setWidth(Main.stageWidth);

    gc = canvas.getGraphicsContext2D();

    //start the jam!
    try {
      play(getClass().getClassLoader().getResourceAsStream("sounds/music.wav"), true, 1f);
    } catch (Exception e) {
      e.printStackTrace();
    }

    spaceship = new Spaceship(Main.settingsMap.get("character"));

    //initialise spaceship's health
    health1.setGraphic(new ImageView(heart));
    health2.setGraphic(new ImageView(heart));
    health3.setGraphic(new ImageView(heart));

    //initialise spaceship's cannon bullets
    cannonBullet1.setGraphic(new ImageView(cannonBullet));
    cannonBullet2.setGraphic(new ImageView(cannonBullet));
    cannonBullet3.setGraphic(new ImageView(cannonBullet));

    //main game loop
    animationTimer = new AnimationTimer() {
      public void handle(long currentNanoTime) {
        //start the jam if it has ended
        if (!music.isRunning()) {
          music.stop();
          music.setFramePosition(0);
          music.start();
        }

        drawFrame();
      }
    };

    //initial game message
    Alert message = new CustomAlert(Alert.AlertType.INFORMATION);
    message.setGraphic(new ImageView(spaceship.getSpaceship()));
    message.setTitle("Welcome!");
    message.setHeaderText(Main.settingsMap.get("character").toUpperCase() + ", stuff are in danger!!! \n\n " +
        "Fight the incoming enemies. Do not let them go behind you. You are our only hope!\n\n" +
        "Controls:\n" +
        "LEFT and RIGHT arrows to move your face\n" +
        "SPACE BAR to fire at the enemy bugs\n" +
        "CTRL to fire a cannon ball\n" +
        "ESC to go back to the main menu at any time\n\n" +
        "Good luck! Make us proud!");
    message.show();

    //start the game when the message closes
    message.setOnHiding(event -> {
      canvas.requestFocus();
      animationTimer.start();
    });
  }

  public Clip play(InputStream filename, boolean autostart, float gain) throws Exception {
    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(filename));
    music = AudioSystem.getClip();
    music.open(audioInputStream);
    music.setFramePosition(0);

    // values have min/max values, for now don't check for outOfBounds values
    FloatControl gainControl = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(gain);

    if (autostart) music.start();
    return music;
  }

  private void calculateDamage() {
    enemies.forEach(enemy -> bullets.forEach(bullet -> {
      if (bullet.getPositionY() <= enemy.getPositionY() + Enemy.size
          && bullet.getPositionX() > enemy.getPositionX() && bullet.getPositionX() < enemy.getPositionX() + Enemy.size) {
        bullet.hit();
        enemy.damage(Bullet.damage);
      }
    }));
  }

  private void loseHealth() {
    if (spaceship.getHP() == 3) {
      health3.setGraphic(new ImageView(brokenHeart));
    } else if (spaceship.getHP() == 2) {
      health2.setGraphic(new ImageView(brokenHeart));
    } else if (spaceship.getHP() == 1) {
      //you just lost. stop the game and save score.
      health1.setGraphic(new ImageView(brokenHeart));
      stopGame();
      saveScore();
    }
    spaceship.hitSound();
    spaceship.damage();
  }

  private void loseCannonBullet() {
    if (spaceship.availableCannonBullets() == 2) {
      cannonBullet3.setGraphic(null);
    } else if (spaceship.availableCannonBullets() == 1) {
      cannonBullet2.setGraphic(null);
    } else if (spaceship.availableCannonBullets() == 0) {
      cannonBullet1.setGraphic(null);
    }
  }

  private void stopGame() {
    animationTimer.stop();
    music.stop();
    spaceship.lostSound();
  }

  private void saveScore() {
    TextInputDialog save = new TextInputDialog();
    save.setTitle("Highscore");
    save.setHeaderText(Main.settingsMap.get("character").toUpperCase() + "\nScore: " + points);
    final Button saveButton = (Button) save.getDialogPane().lookupButton(ButtonType.OK);
    final Button cancelButton = (Button) save.getDialogPane().lookupButton(ButtonType.CANCEL);
    saveButton.addEventFilter(ActionEvent.ACTION, event -> {
          //savehighscore
          String playerName = save.getEditor().getText();
          HighScore score = new HighScore(playerName.isEmpty() ? Main.settingsMap.get("character").toUpperCase() : playerName, points);
          Main.run.getBean(HighScoreUCI.class).saveHighScore(score);
          backToMainMenu();
        }
    );
    cancelButton.addEventFilter(ActionEvent.ACTION, event -> backToMainMenu());
    save.show();

    //style the dialog
    save.setWidth(310d);
    save.setHeight(310d);
    save.getDialogPane().setGraphic(new ImageView(new Image(getClass().getResource("/gameIcons/prize.png").toExternalForm(), 160, 160, true, true)));
    save.getDialogPane().getStylesheets().add(
        getClass().getResource("/application.css").toExternalForm());
    save.getDialogPane().getStyleClass().add("alertDialog");
  }

  private void backToMainMenu() {
    Main.navigateMainMenu();
  }

  //--------------------------------------------------------------- CANVAS DRAWINGS --------------------------------------------------------
  private void drawFrame() {
    clearCanvas();
    spaceship.move();
    gc.drawImage(spaceship.getSpaceship(), spaceship.getPosition(), getRevertedY(0), Spaceship.size, Spaceship.size);
    drawEnemies();
    drawBullets();
    drawCannonBullets();
    drawPowerUps();
    calculateDamage();
    score.setText(Integer.toString(points));
  }

  private void clearCanvas() {
    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  private void drawEnemies() {
    //generate an enemy if necessary
    long nowMillis = System.currentTimeMillis();
    int secondsSinceLastEnemy = (int) ((nowMillis - this.createdMillis) / 1000);
    if (secondsSinceLastEnemy >= secondsToGenerateEnemy) {
      secondsToGenerateEnemy = ThreadLocalRandom.current().nextInt(0, 3);
      createdMillis = System.currentTimeMillis();
      enemies.add(new Enemy());
    }

    //draw all enemies that still have HP. if they are killed get the points and remove them. if the go by lose a heart.
    enemies.forEach(enemy -> {
      enemy.move();
      if (enemy.getHealth() > 0) {
        if (enemy.getPositionY() < canvasMaxHeight) {
          gc.drawImage(enemy.getEnemy(), enemy.getPositionX(), enemy.getPositionY(), Enemy.size, Enemy.size);
        } else {
          if (!enemy.hasHitSpaceShip()) {
            enemy.hitSpaceShip();
            loseHealth();
          }
        }
      } else {
        GameMedia.playKillEnemySound();
        points += enemy.getPoints();
        deadEnemies.add(enemy);
      }
    });

    //explode deadEnemies before removing them from the game
    //and drop the power up if they have one
    deadEnemies.forEach(enemy -> {
      if (enemy.hasPowerUp()) {
        powerUps.add(enemy.dropPowerUp());
      }
      explode(enemy, explodedEnemies, enemy.getPositionX(), enemy.getPositionY(), Enemy.size);
    });

    //clear enemies that are dead
    deadEnemies.removeAll(explodedEnemies);
    enemies.removeAll(deadEnemies);
    explodedEnemies.clear();
  }

  private void drawBullets() {
    for (Bullet bullet : bullets) {
      if (bullet.getPositionY() < 0 || bullet.hasHit()) {
        usedBullets.add(bullet);
      } else {
        gc.drawImage(bullet.getBullet(), bullet.getPositionX(), bullet.getPositionY(), bullet.size, bullet.size);
      }
    }

    bullets.removeAll(usedBullets);
    usedBullets.clear();
  }

  private void drawCannonBullets() {
    for (CannonBullet bullet : cannonBullets) {
      if (bullet.getPositionY() < canvasMaxHeight / 2) {
        explode(bullet, usedCannonBullets, bullet.getPositionX() - 150, bullet.getPositionY() - 150, 300);
        //kill all enemies
        enemies.forEach(enemy -> enemy.setHealth(0));
      } else {
        gc.drawImage(bullet.getBullet(), bullet.getPositionX(), bullet.getPositionY(), bullet.size, bullet.size);
      }
    }
    cannonBullets.removeAll(usedCannonBullets);
    usedCannonBullets.clear();
  }

  private void drawPowerUps() {
    powerUps.forEach(powerUp -> {
      powerUp.move();

      //check if the power up was picked up by the spaceship
      if ((powerUp.getPositionX() > spaceship.getPosition() && powerUp.getPositionX() < spaceship.getPosition() + Spaceship.size
               || powerUp.getPositionX() + PowerUp.size > spaceship.getPosition() && powerUp.getPositionX() + PowerUp.size < spaceship.getPosition() + Spaceship.size)
          && powerUp.getPositionY() > canvasMaxHeight - Spaceship.size) {
        powerUp.hitSpaceShip();
        GameMedia.playRewardSound();
      }

      //draw the power up or put it in the power up garbage can
      if (!powerUp.hasHitSpaceShip() && powerUp.getPositionY() < canvasMaxHeight + Spaceship.size) {
        gc.drawImage(powerUp.getPowerUp(), powerUp.getPositionX(), powerUp.getPositionY(), PowerUp.size, PowerUp.size);
      } else {
        usedPowerUps.add(powerUp);
      }
    });

    //get the rewards from the powerups
    usedPowerUps.forEach(powerUp -> {
      if (powerUp.hasHitSpaceShip()) {
        getPowerUpReward(powerUp.getType());
      }
    });

    powerUps.removeAll(usedPowerUps);
    usedPowerUps.clear();
  }

  /**
   * Depending on the power up type the player gets different advantages in game
   *
   * @param type - the type of the power up we are interested in
   */
  private void getPowerUpReward(PowerUp.PowerUpTypes type) {
    switch (type) {
      case hp:
        if (spaceship.getHP() == 2) {
          spaceship.heal();
          health3.setGraphic(new ImageView(heart));
        } else if (spaceship.getHP() == 1) {
          spaceship.heal();
          health2.setGraphic(new ImageView(heart));
        }
        break;
      case cannon:
        if (spaceship.availableCannonBullets() == 2) {
          spaceship.reloadCannon();
          cannonBullet3.setGraphic(new ImageView(cannonBullet));
        } else if (spaceship.availableCannonBullets() == 1) {
          spaceship.reloadCannon();
          cannonBullet2.setGraphic(new ImageView(cannonBullet));
        } else if (spaceship.availableCannonBullets() == 0) {
          spaceship.reloadCannon();
          cannonBullet1.setGraphic(new ImageView(cannonBullet));
        }
        break;
      case points:
        this.points += 1000;
        break;

    }

  }

  private <T extends Explosion> void explode(T object, List<T> tracker, double X, double Y, int size) {
    int explosionTime = object.getExplosionTime();
    if (explosionTime <= 0) {
      tracker.add(object);
    } else {
      gc.drawImage(object.getExplosion(), X, Y, size, size);
    }
  }

  public static int getRevertedY(int y) {
    return canvasMaxHeight - (y);
  }

  //-------------------------------------------------------- BUTTON HANDLERS ---------------------------------------------------------------
  public void onButtonPress(KeyEvent event) {
    if (!pressedKeys.contains(event.getCode())) {
      if (KeyCode.LEFT.equals(event.getCode())) {
        spaceship.setVelocity(false);
        pressedKeys.add(event.getCode());
      } else if (KeyCode.RIGHT.equals(event.getCode())) {
        spaceship.setVelocity(true);
        pressedKeys.add(event.getCode());
      } else if (KeyCode.SPACE.equals(event.getCode())) {
        bullets.add(new Bullet(Main.settingsMap.get("bullet"), spaceship.getPosition()));
        spaceship.fireSound();
        pressedKeys.add(event.getCode());
      } else if (KeyCode.CONTROL.equals(event.getCode())) {
        if (cannonBullets.isEmpty() && spaceship.availableCannonBullets() > 0) {
          cannonBullets.add(spaceship.fireCannonBullet());
          loseCannonBullet();
        }
        pressedKeys.add(event.getCode());
      } else if (KeyCode.ESCAPE.equals(event.getCode())) {
        stopGame();
      }
    }
  }

  public void onButtonRelease(KeyEvent event) {
    if (KeyCode.LEFT.equals(event.getCode())) {
      spaceship.setVelocity(null);
      pressedKeys.remove(event.getCode());
    } else if (KeyCode.RIGHT.equals(event.getCode())) {
      spaceship.setVelocity(null);
      pressedKeys.remove(event.getCode());
    } else if (KeyCode.SPACE.equals(event.getCode())) {
      pressedKeys.remove(event.getCode());
    } else if (KeyCode.CONTROL.equals(event.getCode())) {
      pressedKeys.remove(event.getCode());
    }
  }
}
