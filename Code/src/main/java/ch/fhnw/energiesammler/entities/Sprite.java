package ch.fhnw.energiesammler.entities;

import ch.fhnw.energiesammler.utils.ImageLoader;

import processing.core.PApplet;
import processing.core.PImage;

public class Sprite {
  private static final float DEFAULT_WIDTH = 10f;
  private static final float DEFAULT_HEIGHT = 10f;
  //Standort des Sprite
  public float center_x, center_y;
  //Standortveränderung des Sprite
  public float change_x, change_y;
  PApplet pApplet;
  PImage img;
  float w, h;

  //Neues Sprite erstellen mit Dateiname
  public Sprite(PApplet pApplet, String filename, float scale, float x, float y) {
    this.pApplet = pApplet;
    img = ImageLoader.loadImage(pApplet, filename);
    w = (img != null && img.width > 0) ? img.width * scale : DEFAULT_WIDTH;
    h = (img != null && img.height > 0) ? img.height * scale : DEFAULT_HEIGHT;
    center_x = x;
    center_y = y;
    change_x = 0;
    change_y = 0;
  }

  //Neues Sprite erstellen mit Bild
  public Sprite(PApplet pApplet, PImage img, float scale) {
    this.pApplet = pApplet;
    this.img = img;
    w = (img != null && img.width > 0) ? img.width * scale : DEFAULT_WIDTH;
    h = (img != null && img.height > 0) ? img.height * scale : DEFAULT_HEIGHT;
    center_x = 0;
    center_y = 0;
    change_x = 0;
    change_y = 0;
  }

  //Höhe zurückgeben
  public float getH() {
    return h;
  }

  //Breite zurückgeben
  public float getW() {
    return w;
  }

  //Bild des Sprites anzeigen
  public void display() {
    pApplet.image(img, center_x - w / 2, center_y - h / 2, w, h);
  }

  //Position aktualisieren
  public void update() {
    center_x += change_x;
    center_y += change_y;
  }

  //Grenzen der Sprites abrufen
  public float getLeft() {
    return center_x - w / 2;
  }

  //Grenzen der Sprites setzen
  public void setLeft(float newLeft) {
    center_x = newLeft + w / 2;
  }

  public float getRight() {
    return center_x + w / 2;
  }

  public void setRight(float newRight) {
    center_x = newRight - w / 2;
  }

  public float getBottom() {
    return center_y + h / 2;
  }

  public void setBottom(float newBottom) {
    center_y = newBottom - h / 2;
  }

  public float getTop() {
    return center_y - h / 2;
  }

  public void setTop(float newTop) {
    center_y = newTop + h / 2;
  }
}
