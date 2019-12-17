package com.chiknas.application.database;

import com.chiknas.application.HighScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * com.chiknas.application.database.HighScoreUCI, created on 05/07/2019 16:56 <p>
 * @author NikolaosK
 */
@Service
public class HighScoreUCI {
  @Autowired
  private HighscoreDAO highscoreDAO;

  public List<HighScore> getHighscores() {
    return highscoreDAO.findAll(new Sort(Sort.Direction.DESC, "score"));
  }

  public void removeLastScore(){
    List<HighScore> highscores = getHighscores();
    HighScore lastHighScore = highscores.get(highscores.size() - 1);
    highscoreDAO.delete(lastHighScore);
  }

  public void saveHighScore(HighScore score) {
    if(highscoreDAO.count() >= 9){
      List<HighScore> highscores = getHighscores();
      Optional<HighScore> lowerScore = highscores.stream().filter(highscore -> highscore.getScore() <= score.getScore()).findFirst();
      lowerScore.ifPresent(highScore -> {
        removeLastScore();
        highscoreDAO.save(score);
      });
    }else{
      highscoreDAO.save(score);
    }
  }
}
