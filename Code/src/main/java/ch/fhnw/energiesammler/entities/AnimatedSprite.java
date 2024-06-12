package ch.fhnw.energiesammler.entities;

import ch.fhnw.energiesammler.gamelogic.Platformer;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Class for animation.
 */
public class AnimatedSprite extends Sprite {
  PImage[] currentImages;
  PImage[] standNeutral;
  PImage[] moveLeft;
  PImage[] moveRight;
  int direction;
  int index;
  int frame;

  /**
   * Constructor.
   *
   * @param pApplet Object with Picture.
   * @param img     The Picture.
   * @param scale   Scale - factor of Picture.
   */
  public AnimatedSprite(PApplet pApplet, PImage img, float scale) {
    super(pApplet, img, scale);
    direction = Platformer.NEUTRAL_FACING;
    index = 0;
    frame = 0;
  }

  /**
   * Updating Animation all 4 seconds.
   */
  public void updateAnimation() {
    frame++;
    if (frame % 4 == 0) {
      selectDirection();
      selectCurrentImages();
      advanceToNextImage();
    }
  }

  /**
   * Selecting Direction of Entities.
   */
  public void selectDirection() {
    if (change_x > 0) {
      direction = Platformer.RIGHT_FACING;
    } else if (change_x < 0) {
      direction = Platformer.LEFT_FACING;
    } else {
      direction = Platformer.NEUTRAL_FACING;
    }
  }

  /**
   * Select the right Image for the Direction.
   */
  public void selectCurrentImages() {
    if (direction == Platformer.RIGHT_FACING) {
      currentImages = moveRight;
    } else if (direction == Platformer.LEFT_FACING) {
      currentImages = moveLeft;
    } else {
      currentImages = standNeutral;
    }
  }

  /**
   * Going threw Array.
   */
  public void advanceToNextImage() {
    index++;
    if (index == currentImages.length) {
      index = 0;
    }
    img = currentImages[index];
  }
}
