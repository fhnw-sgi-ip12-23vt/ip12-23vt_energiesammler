package ch.fhnw.energiesammler.gamelogic;

import ch.fhnw.energiesammler.utils.ImageLoader;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Laden der verwendeten Bilder.
 */
public class Pictures {
  public PImage ground;
  public PImage wood;
  public PImage lightning;
  public PImage oven;
  public PImage mixer;
  public PImage bulb;
  public PImage door;
  public PImage house;
  public PImage background;
  public PImage menuBackground;
  public PImage batteryCollect;
  public PImage menuBarUnchoosen;
  public PImage menuBarChoosen;
  public PImage characterMale;
  public PImage characterFemale;
  public PImage characterMaleSelected;
  public PImage characterFemaleSelected;
  public PImage backgroundCharacterauswahl;
  public PImage tutorial1;
  public PImage tutorial2;
  public PImage tutorial3;
  public PImage tutorial4;
  PApplet pApplet;
  String currentLanguage;
  private boolean initImageLoad;

  public Pictures(PApplet pApplet, String gameLanguage) {
    this.pApplet = pApplet;
    this.currentLanguage = gameLanguage;
    this.initImageLoad = true;
    loadImages();
  }

  /**
   * Alle Bilder laden.
   */
  private void loadImages() {
    ground = ImageLoader.loadImage(pApplet, "groundBlock2.png");
    wood = ImageLoader.loadImage(pApplet, "wood.png");
    lightning = ImageLoader.loadImage(pApplet, "energySource/lightning.png");
    oven = ImageLoader.loadImage(pApplet, "enemy/oven_neutral1.png");
    mixer = ImageLoader.loadImage(pApplet, "enemy/mixer_neutral1.png");
    bulb = ImageLoader.loadImage(pApplet, "enemy/bulb_neutral1.png");
    door = ImageLoader.loadImage(pApplet, "door_closed.png");
    house = ImageLoader.loadImage(pApplet, "house.png");
    background = ImageLoader.loadImage(pApplet, "background2.png");
    menuBackground = ImageLoader.loadImage(pApplet, "menuBackground.png");
    batteryCollect = ImageLoader.loadImage(pApplet, "energySource/batteryCollect.png");
    characterMale = ImageLoader.loadImage(pApplet, "male/male_stand.png");
    characterFemale = ImageLoader.loadImage(pApplet, "female/female_stand_menu.png");
    menuBarUnchoosen = ImageLoader.loadImage(pApplet, "menuBarUnchoosen.png");
    menuBarChoosen = ImageLoader.loadImage(pApplet, "menuBarChoosen.png");
    characterMaleSelected = ImageLoader.loadImage(pApplet, "male/male_selected.png");
    characterFemaleSelected = ImageLoader.loadImage(pApplet, "female/female_selected.png");
    backgroundCharacterauswahl = ImageLoader.loadImage(pApplet, "background-charakter-auswahl.png");

    loadTutorialImages(currentLanguage);
  }

  public void loadTutorialImages(String gameLanguage) {
    if (initImageLoad || !currentLanguage.equals(gameLanguage)) {
      // Load only firsttime the tutorial
      if (initImageLoad) {
        initImageLoad = false;
      }

      currentLanguage = gameLanguage;
      String relativePath = "tutorials/" + gameLanguage;

      tutorial1 = ImageLoader.loadImage(pApplet, relativePath + "/1.png");
      tutorial2 = ImageLoader.loadImage(pApplet, relativePath + "/2.png");
      tutorial3 = ImageLoader.loadImage(pApplet, relativePath + "/3.png");
      tutorial4 = ImageLoader.loadImage(pApplet, relativePath + "/4.png");
    }
  }
}
