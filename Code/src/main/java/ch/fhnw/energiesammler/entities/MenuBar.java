package ch.fhnw.energiesammler.entities;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Class for the Menu Bars in the Home Screen.
 */
public class MenuBar extends Sprite {
  PImage choose;
  PImage unchoose;

  /**
   * Constructor.
   *
   * @param pApplet Object with Picture.
   * @param img     Picture.
   * @param scale   Factor for Picture.
   * @param x       Coordinate of the Bar.
   * @param y       Coordinate of the Bar.
   */
  public MenuBar(PApplet pApplet, PImage img, float scale, int x, int y) {
    super(pApplet, img, scale);
    center_x = x;
    center_y = y;
    unchoose = img;
  }

  public void loadChooseImages(PImage img) {
    choose = img;
  }

  public void choose() {
    img = choose;
  }

  public void unchoose() {
    img = unchoose;
  }
}
