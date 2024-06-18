package ch.fhnw.energiesammler.entities;

import ch.fhnw.energiesammler.gamelogic.Platformer;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Class for Door.
 */
public class Door extends AnimatedSprite {
  private final String text;
  private final float cost;
  Platformer platformer;

  /**
   * Constructor.
   *
   * @param pApplet    Object with a Picture.
   * @param platformer Game Object.
   * @param img        Picture.
   * @param scale      Scale - Factor for the Picture.
   * @param text       Exercises.
   * @param cost       Watt.
   */
  public Door(PApplet pApplet, Platformer platformer, PImage img, float scale, String text,
              float cost) {
    super(pApplet, img, scale);
    this.platformer = platformer;
    this.text = text;
    this.cost = cost;
  }

  public String getText() {
    return text;
  }

  public float getCost() {
    return cost;
  }


}
