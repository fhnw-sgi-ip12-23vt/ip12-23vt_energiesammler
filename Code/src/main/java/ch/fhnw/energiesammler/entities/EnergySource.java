package ch.fhnw.energiesammler.entities;

import ch.fhnw.energiesammler.utils.ImageLoader;

import processing.core.PApplet;
import processing.core.PImage;

public class EnergySource extends AnimatedSprite {
  private final float power;

  public EnergySource(PApplet pApplet, PImage img, float scale, float power, String type) {
    super(pApplet, img, scale);
    standNeutral = new PImage[1];

    standNeutral[0] = ImageLoader.loadImage(pApplet, "energySource/" + type);
    currentImages = standNeutral;
    this.power = power;
  }

  public float getPower() {
    return power;
  }
}
