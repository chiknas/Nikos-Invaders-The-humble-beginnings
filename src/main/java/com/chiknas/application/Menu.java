package com.chiknas.application;

import com.chiknas.application.game.GameMedia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Menu implements Initializable {

  @FXML
  private ImageView chiknasLogo;

  @FXML
  private Button start;

  @FXML
  private Button leaderboard;

  @FXML
  private Button settings;

  @FXML
  private Button quit;

  public void buttonGame(ActionEvent event) {
    Main.navigateGame();
  }

  public void buttonLeaderBoard(ActionEvent event) {
    Main.navigateLeaderboard();
  }

  public void buttonSettings(ActionEvent event) {
    Main.navigateSettings();
  }

  public void buttonQuit(ActionEvent event) {
    System.exit(0);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    chiknasLogo.setImage(new Image(getClass().getResource("/images/chiknasLogo.png").toExternalForm()));
    initilizeHoverEffect();
  }

  private void initilizeHoverEffect() {
    start.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
      GameMedia.playNavigateSound();
    });

    leaderboard.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
      GameMedia.playNavigateSound();
    });

    settings.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
      GameMedia.playNavigateSound();
    });

    quit.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
      GameMedia.playNavigateSound();
    });
  }
}
