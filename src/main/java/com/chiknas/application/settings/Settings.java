package com.chiknas.application.settings;

import com.chiknas.application.Main;
import com.chiknas.application.game.GameMedia;
import com.chiknas.application.game.entities.Bullet;
import com.chiknas.application.game.entities.Spaceship;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * com.chiknas.application.Settings, created on 28/06/2019 14:44 <p>
 * @author NikolaosK
 */
public class Settings implements Initializable {

  @FXML
  private Label backButton;
  @FXML
  private RadioButton easy;
  @FXML
  private RadioButton normal;
  @FXML
  private RadioButton hard;
  @FXML
  private GridPane characters;
  @FXML
  private GridPane bullets;
  @FXML
  private Label playerName;
  @FXML
  private Label weaponName;

  public static Map<String, String> settingsMap = Main.settingsMap;
  private BorderPane selectedCharacter;
  private BorderPane selectedBullet;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //backButton
    backButton.setGraphic(new ImageView(new Image(
        getClass().getResource("/gameIcons/back-button.png").toExternalForm(), 100, 100, true, true)));
    backButton.onMouseClickedProperty().setValue(event -> Main.navigateMainMenu());

    //difficulty part
    ToggleGroup group = new ToggleGroup();
    easy.setToggleGroup(group);
    normal.setToggleGroup(group);
    hard.setToggleGroup(group);
    //initialise from the settings
    group.selectToggle(getSelectedDifficulty(settingsMap.get("difficulty")));
    group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
      GameMedia.playSelectionSound();
      settingsMap.put("difficulty", ((RadioButton) newValue.getToggleGroup().getSelectedToggle()).getText());
    });

    //character selection
    ObservableList<String> availableSpaceships = FXCollections.observableArrayList(
        Spaceship.Spaceships.getNames()
    );
    gridPaneFiller(characters, availableSpaceships, "/gameIcons/players/player-", "character", true);

    //bullet selection
    ObservableList<String> availableBullets = FXCollections.observableArrayList(
        Bullet.Bullets.getNames()
    );
    gridPaneFiller(bullets, availableBullets, "/gameIcons/bullets/bullet-", "bullet", false);

  }

  /**
   * Fills the character and bullet panels.
   *
   * @param pane - the pane we want to fill character/bullet
   * @param items - the items that are going into the pane
   * @param itemPath - the main path that the icons can be found for this pane
   * @param settingsKey - the key value that is going to be used in the settingsMap for this setting
   * @param isCharacter - value to distinguish characters and bullets to keep track of the selected items.
   */
  private void gridPaneFiller(GridPane pane, ObservableList<String> items, String itemPath, String settingsKey, boolean isCharacter) {
    Iterator<String> iterator = items.iterator();
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        if (iterator.hasNext()) {
          //get the image
          String next = iterator.next();
          BorderPane imagecontainer = new BorderPane();
          ImageView imageView = new ImageView(new Image(
              getClass().getResource(itemPath + next + ".png").toExternalForm(), 100, 100, true, true));
          //image click listener. change the color of the selected item and put the new value into the settings map.
          imageView.setOnMouseClicked(event -> {
            GameMedia.playSelectionSound();
            if (isCharacter) {
              selectedCharacter.setStyle("-fx-background-color: Transparent");
              imagecontainer.setStyle("-fx-background-color: darkred");
              selectedCharacter = imagecontainer;
              playerName.setText(next.toUpperCase());
            } else {
              selectedBullet.setStyle("-fx-background-color: Transparent");
              imagecontainer.setStyle("-fx-background-color: darkred");
              selectedBullet = imagecontainer;
              weaponName.setText(next.toUpperCase());
            }
            settingsMap.put(settingsKey, next);
          });

          //initialise a selected value
          if (isCharacter && next.contains(settingsMap.get("character"))) {
            selectedCharacter = imagecontainer;
            imagecontainer.setStyle("-fx-background-color: darkred");
            playerName.setText(next.toUpperCase());
          } else if (!isCharacter && next.contains(settingsMap.get("bullet"))) {
            selectedBullet = imagecontainer;
            imagecontainer.setStyle("-fx-background-color: darkred");
            weaponName.setText(next.toUpperCase());
          }

          imagecontainer.setCenter(imageView);
          pane.add(imagecontainer, col, row);
        } else {
          break;
        }
      }
    }
  }

  private RadioButton getSelectedDifficulty(String difficulty) {
    RadioButton result = null;
    if ("Easy".equals(difficulty)) {
      result = easy;
    } else if ("Normal".equals(difficulty)) {
      result = normal;
    } else if ("Impossibru!".equals(difficulty)) {
      result = hard;
    }
    return result;
  }
}
