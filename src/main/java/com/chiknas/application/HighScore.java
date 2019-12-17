package com.chiknas.application;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * com.chiknas.application.HighScore, created on 28/06/2019 16:38 <p>
 * @author NikolaosK
 */
@Entity
@Table(name = "highscore")
public class HighScore {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column
  private String player;
  @Column
  private int score;

  public HighScore(){}

  public HighScore(String player, Integer score){
    this.player = player;
    this.score = score;
  }

  public String getPlayer() {
    return player;
  }

  public void setPlayer(String player) {
    this.player = player;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }
}
