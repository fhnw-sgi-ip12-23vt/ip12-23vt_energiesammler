package ch.fhnw.energiesammler.entities;

import ch.fhnw.energiesammler.gamelogic.Level;
import ch.fhnw.energiesammler.gamelogic.Platformer;
import ch.fhnw.energiesammler.utils.ImageLoader;

import processing.core.PImage;

public class Player extends AnimatedSprite {
  Platformer platformer;
  Level level;
  boolean onPlatform;
  boolean inPlace;
  PImage[] standLeft;
  PImage[] standRight;
  PImage[] jumpLeft;
  PImage[] jumpRight;
  float lastLeft;
  float lastRight;
  float lastBottom;
  String gender;

  //Neuen Spieler generieren
  public Player(Platformer platformer, Level level, PImage img, float scale, String gender) {
    super(platformer, img, scale);
    this.gender = gender;
    this.platformer = platformer;
    this.level = level;
    direction = Platformer.RIGHT_FACING;
    onPlatform = false;
    inPlace = true;
    //Bilder für Animationen laden
    loadPlayerAnimations();
  }

  //Neuen Status für Animation wählen
  @Override
  public void updateAnimation() {
    onPlatform = platformer.getUpdater().isOnPlatform(this, level.getPlatforms());
    inPlace = change_x == 0 && change_y == 0;
    super.updateAnimation();
  }

  //Grenzen des Spielers abrufen
  @Override
  public float getLeft() {
    return center_x - getW() / 5f;
  }

  @Override
  public void setLeft(float newLeft) {
    super.setLeft(newLeft - getW() / 5f);
  }

  @Override
  public float getRight() {
    return center_x + getW() / 5f;
  }

  @Override
  public void setRight(float newRight) {
    super.setRight(newRight + getW() / 5f);
  }

  //Blickrichtung wählen
  @Override
  public void selectDirection() {
    if (change_x > 0) {
      direction = Platformer.RIGHT_FACING;
    } else if (change_x < 0) {
      direction = Platformer.LEFT_FACING;
    }
  }

  //Momentanes Bild wählen
  @Override
  public void selectCurrentImages() {
    if (direction == Platformer.RIGHT_FACING) {
      if (inPlace) {
        currentImages = standRight;
      } else if (!onPlatform) {
        currentImages = jumpRight;
      } else {
        currentImages = moveRight;
      }
    } else if (direction == Platformer.LEFT_FACING) {
      if (inPlace) {
        currentImages = standLeft;
      } else if (!onPlatform) {
        currentImages = jumpLeft;
      } else {
        currentImages = moveLeft;
      }
    }
  }

  public void setLast(float lastLeft, float lastRight, float lastBottom) {
    this.lastLeft = lastLeft;
    this.lastRight = lastRight;
    this.lastBottom = lastBottom;
  }

  public float getLastLeft() {
    return lastLeft;
  }

  public float getLastRight() {
    return lastRight;
  }

  public float getLastBottom() {
    return lastBottom;
  }

  private void loadPlayerAnimations() {
    standLeft = new PImage[6];
    standLeft[0] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_standLeft1.png");
    standLeft[1] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_standLeft1.png");
    standLeft[2] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_standLeft1.png");
    standLeft[3] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_standLeft1.png");
    standLeft[4] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_standLeft1.png");
    standLeft[5] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_standLeft1.png");
    standRight = new PImage[6];
    standRight[0] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_standRight1.png");
    standRight[1] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_standRight1.png");
    standRight[2] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_standRight1.png");
    standRight[3] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_standRight1.png");
    standRight[4] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_standRight1.png");
    standRight[5] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_standRight1.png");
    jumpLeft = new PImage[6];
    jumpLeft[0] = ImageLoader.loadImage(platformer, gender + "/" + gender + "_jumpLeft1.png");
    jumpLeft[1] = ImageLoader.loadImage(platformer, gender + "/" + gender + "_jumpLeft1.png");
    jumpLeft[2] = ImageLoader.loadImage(platformer, gender + "/" + gender + "_jumpLeft1.png");
    jumpLeft[3] = ImageLoader.loadImage(platformer, gender + "/" + gender + "_jumpLeft1.png");
    jumpLeft[4] = ImageLoader.loadImage(platformer, gender + "/" + gender + "_jumpLeft1.png");
    jumpLeft[5] = ImageLoader.loadImage(platformer, gender + "/" + gender + "_jumpLeft1.png");
    jumpRight = new PImage[6];
    jumpRight[0] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_jumpRight1.png");
    jumpRight[1] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_jumpRight1.png");
    jumpRight[2] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_jumpRight1.png");
    jumpRight[3] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_jumpRight1.png");
    jumpRight[4] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_jumpRight1.png");
    jumpRight[5] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_jumpRight1.png");
    moveLeft = new PImage[6];
    moveLeft[0] = ImageLoader.loadImage(platformer, gender + "/" + gender + "_runLeft1.png");
    moveLeft[1] = ImageLoader.loadImage(platformer, gender + "/" + gender + "_runLeft2.png");
    moveLeft[2] = ImageLoader.loadImage(platformer, gender + "/" + gender + "_runLeft3.png");
    moveLeft[3] = ImageLoader.loadImage(platformer, gender + "/" + gender + "_runLeft4.png");
    moveLeft[4] = ImageLoader.loadImage(platformer, gender + "/" + gender + "_runLeft5.png");
    moveLeft[5] = ImageLoader.loadImage(platformer, gender + "/" + gender + "_runLeft6.png");
    moveRight = new PImage[6];
    moveRight[0] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_runRight1.png");
    moveRight[1] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_runRight2.png");
    moveRight[2] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_runRight3.png");
    moveRight[3] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_runRight4.png");
    moveRight[4] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_runRight5.png");
    moveRight[5] =
        ImageLoader.loadImage(platformer, gender + "/" + gender + "_runRight6.png");
    currentImages = standRight;
  }

}
