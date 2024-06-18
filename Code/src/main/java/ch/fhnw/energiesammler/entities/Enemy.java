package ch.fhnw.energiesammler.entities;

import java.util.ArrayList;
import java.util.Objects;

import ch.fhnw.energiesammler.gamelogic.Level;
import ch.fhnw.energiesammler.gamelogic.Platformer;
import ch.fhnw.energiesammler.utils.ImageLoader;

import processing.core.PApplet;
import processing.core.PImage;

public class Enemy extends AnimatedSprite {
  private final float damage;
  private final String name;
  Platformer platformer;
  Level level;
  float boundaryLeft;
  float boundaryRight;

  //Neuen Gegner erstellen
  public Enemy(PApplet pApplet, Platformer platformer, Level level, PImage img, float scale,
               float damage, float speed, String name) {
    super(pApplet, img, scale);
    this.platformer = platformer;
    this.level = level;
    //Bilder für Animation laden
    moveLeft = new PImage[2];
    moveLeft[0] = ImageLoader.loadImage(pApplet, "enemy/" + name + "_run1.png");
    moveLeft[1] = ImageLoader.loadImage(pApplet, "enemy/" + name + "_run2.png");
    moveRight = new PImage[2];
    moveRight[0] = ImageLoader.loadImage(pApplet, "enemy/" + name + "_run1.png");
    moveRight[1] = ImageLoader.loadImage(pApplet, "enemy/" + name + "_run2.png");
    standNeutral = new PImage[2];
    standNeutral[0] = ImageLoader.loadImage(pApplet, "enemy/" + name + "_neutral1.png");
    standNeutral[1] = ImageLoader.loadImage(pApplet, "enemy/" + name + "_neutral1.png");
    this.name = name;
    currentImages = moveRight;
    direction = Platformer.RIGHT_FACING;
    this.damage = damage;
    change_x = speed;
    boundaryRight = this.getRight();
    boundaryLeft = this.getLeft();
  }

  /**
   * Konstruktor.
   *
   * @param pApplet    PApplet Context
   * @param platformer Platformer information
   * @param level      Level information
   * @param img        Image used for the Enemy
   * @param scale      scale of Enemy
   * @param damage     Damage inflicted to the player
   * @param speed      Speed of movement
   * @param name       Name/Type of Enemy
   * @param x          x-Center of the created Enemy
   * @param y          y-Center of the created Enemy
   *                   This Constructor is currently only used for the Mapping to count the defeated Enemies.
   */
  public Enemy(PApplet pApplet, Platformer platformer, Level level, PImage img, float scale,
               float damage, float speed, String name, float x, float y) {
    super(pApplet, img, scale);
    this.platformer = platformer;
    this.level = level;
    //Bilder für Animation laden
    moveLeft = new PImage[2];
    moveLeft[0] = ImageLoader.loadImage(pApplet, "enemy/" +  name + "_run1.png");
    moveLeft[1] = ImageLoader.loadImage(pApplet, "enemy/" + name + "_run2.png");
    moveRight = new PImage[2];
    moveRight[0] = ImageLoader.loadImage(pApplet, "enemy/" +  name + "_run1.png");
    moveRight[1] = ImageLoader.loadImage(pApplet, "enemy/" + name + "_run2.png");
    standNeutral = new PImage[2];
    standNeutral[0] = ImageLoader.loadImage(pApplet, "enemy/" +  name + "_neutral1.png");
    standNeutral[1] = ImageLoader.loadImage(pApplet, "enemy/" + name + "_neutral1.png");
    this.name = name;
    currentImages = moveRight;
    direction = Platformer.RIGHT_FACING;
    this.damage = damage;
    change_x = speed;
    this.center_x = x;
    this.center_y = y;
  }

  public float getDamage() {
    return damage;
  }

  public void setCenterX(float x) {
    this.center_x = x;
    boundaryRight = this.getRight();
    boundaryLeft = this.getLeft();
  }

  public void setCenterY(float y) {
    this.center_y = y;
  }

  //Überprüfen wo Rand der Platform ist
  private void checkEnd() {
    ArrayList<Sprite> c =
        new ArrayList<>(platformer.getUpdater().checkBoarderingList(this, level.getPlatforms()));
    for (int i = 0; i < c.size(); i++) {
      for (Sprite p : platformer.getUpdater().checkBoarderingList(c.get(i), level.getPlatforms())) {
        if (direction == 1 && p.getTop() == this.getBottom()) {
          if (i < c.size() - 1) {
            if (c.get(i).getRight() > c.get(i + 1).getRight()
                && p.getRight() > c.get(i).getRight()) {
              boundaryRight = p.getRight();
            }
          } else {
            if (p.getRight() > c.get(i).getRight()) {
              boundaryRight = p.getRight();
            }
          }
        } else if (direction == 2 && p.getTop() == this.getBottom()) {
          if (i > 0) {
            if (c.get(i).getLeft() < c.get(i - 1).getLeft() && p.getLeft() < c.get(i).getLeft()) {
              boundaryLeft = p.getLeft();
            }
          } else {
            if (p.getLeft() < c.get(i).getLeft()) {
              boundaryLeft = p.getLeft();
            }
          }
        }
      }
    }
  }

  //Überprüfen wo Wand ist
  private void checkWall() {
    ArrayList<Sprite> c =
        new ArrayList<>(platformer.getUpdater().checkBoarderingList(this, level.getPlatforms()));
    for (int i = 0; i < c.size(); i++) {
      if (direction == 1 && c.get(i).getBottom() == this.getBottom()) {
        boundaryRight = c.get(i).getLeft();
      } else if (direction == 2 && c.get(i).getBottom() == this.getBottom()) {
        boundaryLeft = c.get(i).getRight();
      }
    }
  }

  //Standort aktualisieren
  @Override
  public void update() {
    super.update();

    //Platformende und Wand überprüfen
    checkEnd();
    checkWall();

    //Richtung ändern wenn an Platformende oder Wand
    if (getLeft() <= boundaryLeft) {
      setLeft(boundaryLeft);
      change_x *= -1;
    } else if (getRight() >= boundaryRight) {
      setRight(boundaryRight);
      change_x *= -1;
    }
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Enemy e) {
      return this.getName() == e.getName() && this.getDamage() == e.getDamage()
          && this.center_x == e.center_x && this.center_y == e.center_y;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getDamage());
  }
}
