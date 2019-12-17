package com.chiknas.application.game.entities;

import java.util.concurrent.ThreadLocalRandom;

/**
 * com.chiknas.application.maingame.Difficulty, created on 01/07/2019 08:35 <p>
 * @author NikolaosK
 */
public enum Difficulty {
  EASY(10, 60, 6, 2),
  NORMAL(30, 110, 8, 3),
  HARD(70, 200, 11, 5);

  private int maxVelocity;
  private int minHealth;
  private int maxHealth;
  private int multiplier;

  Difficulty(int minHealth, int maxHealth, int maxVelocity, int multiplier) {
    this.minHealth = minHealth;
    this.maxHealth = maxHealth;
    this.maxVelocity = maxVelocity;
    this.multiplier = multiplier;
  }

  public static Difficulty getDifficulty(String difficultyLevel) {
    switch (difficultyLevel) {
      case "Easy":
        return Difficulty.EASY;
      case "Normal":
        return Difficulty.NORMAL;
      case "Impossibru!":
        return Difficulty.HARD;
      default:
        throw new IllegalArgumentException("Difficulty mode not supported");
    }
  }

  public int getHealthValue() {
    return ThreadLocalRandom.current().nextInt(minHealth, maxHealth);
  }

  public int getVelocityValue() {
    return ThreadLocalRandom.current().nextInt(2, maxVelocity);
  }

  public int getMultiplier() {return this.multiplier;}
}
