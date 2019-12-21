package com.chiknas.application;

import com.chiknas.application.components.CustomScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Main application class responsible for setting up the stage, application attributes and navigation of the application.
 */
@SpringBootApplication
@EnableTransactionManagement
public class Main extends Application {

  public static ConfigurableApplicationContext run;

  private static Stage stage;
  public static Map<String, String> settingsMap = new HashMap<String, String>() {{
    put("difficulty", "Easy");
    put("character", "spaceship");
    put("bullet", "laser");
  }};

  @Override
  public void start(Stage primaryStage) {
    stage = primaryStage;
    stage.getIcons().add(new Image(getClass().getResource("/gameIcons/logo.jpg").toExternalForm()));
    stage.setTitle("Nikos Invaders");
    stage.setResizable(false);
    stage.setFullScreen(true);
    stage.setFullScreenExitHint("Use F12 to enable/disable full screen mode.");
    stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    stage.setWidth(1280d);
    stage.setHeight(1000d);
    navigateMainMenu();
    primaryStage.show();
    run = SpringApplication.run(Main.class);
  }


  public static void main(String[] args) {
    launch(args);
  }

  public static Scene getScene() {
    return stage.getScene();
  }

  /**
   * Adds a key press listener to the passed in scene. Mainly used for main menu navigation screens
   * @param scene - the scene to add the listener to
   */
  public static void initiliseApplicationKeyListener(Scene scene) {
    scene.setOnKeyPressed(event -> {
      if (KeyCode.ESCAPE.equals(event.getCode())) {
        navigateMainMenu();
      }else if(KeyCode.F12.equals(event.getCode())){
        stage.setFullScreen(!stage.isFullScreen());
      }
    });
  }


  //----------------------------------------------------------- NAVIGATION METHODS ---------------------------------------------------------

  public static void navigateGame() {
    try {
      Parent game = FXMLLoader.load(Main.class.getResource("/game.fxml"));
      String image = Main.class.getResource("/gameIcons/background.png").toExternalForm();
      game.setStyle("-fx-background-image: url('" + image + "');" +
          "-fx-background-position: center; " +
          "-fx-background-size: cover; " +
          "-fx-background-repeat: no-repeat;");
      setScene(game);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void navigateMainMenu() {
    try {
      Parent root = FXMLLoader.load(Main.class.getResource("/menu.fxml"));
      String image = Main.class.getResource("/images/background.png").toExternalForm();
      root.setStyle("-fx-background-image: url('" + image + "');" +
          "-fx-background-position: center; " +
          "-fx-background-size: cover; " +
          "-fx-background-repeat: no-repeat;");
      setScene(root);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void navigateLeaderboard() {
    try {
      Parent leaderBoard = FXMLLoader.load(Main.class.getResource("/leaderboard.fxml"));
      String image = Main.class.getResource("/images/Leaderboard.png").toExternalForm();
      leaderBoard.setStyle("-fx-background-image: url('" + image + "');" +
          "-fx-background-position: center; " +
          "-fx-background-size: cover; " +
          "-fx-background-repeat: no-repeat;");
      setScene(leaderBoard);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void navigateSettings() {
    try {
      Parent settings = FXMLLoader.load(Main.class.getResource("/settings.fxml"));
      String image = Main.class.getResource("/images/settings-background.png").toExternalForm();
      settings.setStyle("-fx-background-image: url('" + image + "');" +
          "-fx-background-position: center; " +
          "-fx-background-size: cover; " +
          "-fx-background-repeat: no-repeat;");
      settings.getStylesheets().add(Main.class.getResource("/application.css").toExternalForm());
      setScene(settings);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void setScene(Parent parent) {
    if(stage.getScene() != null){
      stage.getScene().setRoot(parent);
    }else{
      CustomScene scene = new CustomScene(parent);
      scene.getStylesheets().add(Main.class.getResource("/application.css").toExternalForm());
      initiliseApplicationKeyListener(scene);
      stage.setScene(scene);
    }
  }
}
