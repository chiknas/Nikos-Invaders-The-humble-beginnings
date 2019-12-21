package com.chiknas.application.leaderboard;

import com.chiknas.application.HighScore;
import com.chiknas.application.Main;
import com.chiknas.application.database.HighScoreUCI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * com.chiknas.application.LeaderBoard, created on 28/06/2019 17:25 <p>
 * @author NikolaosK
 */
public class LeaderBoard implements Initializable {
  @FXML
  private GridPane highscore;
  @FXML
  private Label backButton;

  @Override public void initialize(URL location, ResourceBundle resources) {

    List<HighScore> top9Highscores = Main.run.getBean(HighScoreUCI.class).getHighscores();

    //backButton
    backButton.setGraphic(new ImageView(new Image(
        getClass().getResource("/gameIcons/back-button.png").toExternalForm(), 100, 100, true, true)));
    backButton.onMouseClickedProperty().setValue(event -> Main.navigateMainMenu());
    int rowIndex = 0;
    for (HighScore highScore : top9Highscores) {
      if(rowIndex == 9){
        break;
      }
      Label player = new Label(rowIndex + 1 + ".  " + highScore.getPlayer());
      player.getStyleClass().add("highscores");
      Label score = new Label(String.valueOf(highScore.getScore()));
      score.getStyleClass().add("highscores");
      highscore.add( player, 0, rowIndex);
      highscore.add(score, 1, rowIndex);
      rowIndex++;
    }
  }
}
