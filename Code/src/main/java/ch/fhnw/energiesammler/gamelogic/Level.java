package ch.fhnw.energiesammler.gamelogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import ch.fhnw.energiesammler.AppConfig;
import ch.fhnw.energiesammler.entities.Door;
import ch.fhnw.energiesammler.entities.Enemy;
import ch.fhnw.energiesammler.entities.EnergySource;
import ch.fhnw.energiesammler.entities.Sprite;
import ch.fhnw.energiesammler.utils.ImageLoader;
import ch.fhnw.energiesammler.utils.TaskLocaleUtil;

import processing.core.PApplet;
import processing.core.PImage;

public class Level {
  private ArrayList<Sprite> platforms;
  public ArrayList<Sprite> enemies;
  public ArrayList<Sprite> allHouses;
  public ArrayList<Sprite> batteries;
  public ArrayList<Sprite> levelDoors;
  public Map<String, Float> taskMap;
  PApplet pApplet;
  Pictures pictures;
  Platformer platformer;
  int numberOflevels;
  float levelLeft = 0;
  float levelRight = 0;
  float levelLow = 0;
  float spriteSize = 0;
  float blockScale = 0;
  float energyScale = 0;
  PImage ground, lightning, oven, mixer, bulb, door, house, batteryCollect, wood;
  PImage background;
  private ArrayList<Sprite> energySources;
  private Iterator<Map.Entry<String, Float>> taskIterator;

  public Level(PApplet pApplet, Platformer platformer, Pictures pictures) {
    this.pApplet = pApplet;
    this.pictures = pictures;
    this.platformer = platformer;
    ground = pictures.ground;
    wood = pictures.wood;
    lightning = pictures.lightning;
    oven = pictures.oven;
    mixer = pictures.mixer;
    bulb = pictures.bulb;
    door = pictures.door;
    house = pictures.house;
    batteryCollect = pictures.batteryCollect;
    background = pictures.background;
  }

  /**
   * Create New Task Door Map and resets current level
   */
  public void reset() {
    int numberOfLevels = platformer.getUpdater().getNumberOfMaps();
    taskMap = TaskLocaleUtil.initializeTaskMap(platformer.getGameLanguage(), numberOfLevels * Platformer.DOORCOUNTPERLEVEL);
    taskIterator = taskMap.entrySet().iterator();
    loadLevel();
  }

  public void loadLevel() {
    platforms = new ArrayList<>();
    energySources = new ArrayList<>();
    enemies = new ArrayList<>();
    batteries = new ArrayList<>();
    allHouses = new ArrayList<>();
    levelDoors = new ArrayList<>();

    //Platformen mit Hilfe der Map iterativ erstellen
    int currLevel = platformer.getUpdater().getCurrentMap();
    int numberOfLevels = platformer.getUpdater().getNumberOfMaps();
    if (currLevel <= numberOfLevels) {
      pApplet.background(255);
      createPlatforms(ImageLoader.loadFile("maps/map" + currLevel + ".csv"));
    }

    platformer.getUpdater().initGameValues();

    platformer.getPlayer().center_x = levelLeft + (2 * spriteSize);
    platformer.getPlayer().center_y = (levelLow - (3 * spriteSize));
    platformer.view_x = 0;
    platformer.view_y = -(platformer.displayHeight - levelLow);
  }

  public float getLevelRight() {
    return levelRight;
  }

  public float getLevelLeft() {
    return levelLeft;
  }

  public float getLevelLow() {
    return levelLow;
  }

  public float getSpriteSize() {
    return spriteSize;
  }

  public void spriteSettings(int lines) {

    for (Sprite platform : getPlatforms()) {
      if (platform.center_y == (lines * spriteSize - (spriteSize / 2))) {
        if (platform.getLeft() < levelLeft) {
          levelLeft = platform.getLeft();
        }
        if (platform.getRight() > levelRight) {
          levelRight = platform.getRight();
        }
        if (platform.getBottom() > levelLow) {
          levelLow = platform.getBottom();
        }
      }
    }
  }

  //Platformen mit hilfe der Map erstellen
  public void createPlatforms(String filename) {
    String[] lines = pApplet.loadStrings(filename);
    spriteSize = (float) (platformer.displayHeight / lines.length);
    blockScale = (float) (platformer.displayHeight / lines.length) / 32;
    energyScale = (float) (platformer.displayHeight / lines.length) / 50;
    for (int row = 0; row < lines.length; row++) {
      String[] values = PApplet.split(lines[row], ",");
      for (int col = 0; col < values.length; col++) {
        //Element nach Zeichen in Map erstellen
        switch (values[col]) {
          //Dreckblock erstellen
          case "1" -> {
            Sprite s = new Sprite(pApplet, ground, blockScale);
            s.center_x = spriteSize / 2 + col * spriteSize;
            s.center_y = spriteSize / 2 + row * spriteSize;
            getPlatforms().add(s);
          }
          //Holzblock erstellen
          case "2" -> {
            Sprite w = new Sprite(pApplet, wood, blockScale);
            w.center_x = spriteSize / 2 + col * spriteSize;
            w.center_y = spriteSize / 2 + row * spriteSize;
            getPlatforms().add(w);
          }
          //Gegner von Sorte Ofen erstellen
          case "5" -> {
            String name = "oven";
            Enemy ovens = new Enemy(pApplet, platformer, this, oven, energyScale * 1.5f,
                AppConfig.getValue("dmg." + name, -3.0f), AppConfig.getValue("speed." + name, 1.0f), name);
            ovens.setCenterX(spriteSize / 2 + col * spriteSize);
            ovens.setCenterY(spriteSize / 2 + row * spriteSize);
            enemies.add(ovens);
          }
          //Gegner von Sorte Mixer erstellen
          case "6" -> {
            String name = "mixer";
            Enemy mixers = new Enemy(pApplet, platformer, this, mixer, energyScale,
                AppConfig.getValue("dmg." + name, -0.5f), AppConfig.getValue("speed." + name, 2.0f), name);
            mixers.setCenterX(spriteSize / 2 + col * spriteSize);
            mixers.setCenterY(spriteSize / 2 + row * spriteSize);
            enemies.add(mixers);
          }
          //Gegner von Sorte Glühbirne erstellen
          case "7" -> {
            String name = "bulb";
            Enemy bulbs = new Enemy(pApplet, platformer, this, mixer, energyScale / 1.5f,
                    AppConfig.getValue("dmg." + name, -0.008f), AppConfig.getValue("speed." + name, 3.0f), name);
            bulbs.setCenterX(spriteSize / 2 + col * spriteSize);
            bulbs.setCenterY(spriteSize / 2 + row * spriteSize);
            enemies.add(bulbs);
          }
          //Haus erstellen
          case "8" -> {
            Sprite houses = new Sprite(pApplet, house, blockScale);
            houses.center_x = spriteSize / 2 + col * spriteSize;
            houses.center_y = platformer.displayHeight - (spriteSize + houses.getH() / 2);
            allHouses.add(houses);
          }
          //Türe erstellen
          case "9" -> {
            if (taskIterator.hasNext()) {
              Map.Entry<String, Float> taskEntry = taskIterator.next();
              Door doorLevel =
                  new Door(pApplet, platformer, pictures.door, blockScale, taskEntry.getKey(),
                      taskEntry.getValue());
              doorLevel.center_x = spriteSize / 2 + col * spriteSize;
              doorLevel.center_y = platformer.displayHeight - (spriteSize + doorLevel.getH() / 2);
              levelDoors.add(doorLevel);
              platformer.addAllDoors(doorLevel);
            }
          }
          //Batterie erstellen
          case "b" -> {
            Sprite battery = new Sprite(pApplet, batteryCollect, blockScale / 2);
            battery.center_x = spriteSize / 2 + col * spriteSize;
            battery.center_y = spriteSize / 2 + row * spriteSize;
            batteries.add(battery);
          }
          //Energiequelle erstellen
          case "4" -> {
            EnergySource e =
                new EnergySource(pApplet, ground, energyScale, AppConfig.getValue("energy.lightning", 1.0f), "lightning.png");
            e.center_x = spriteSize / 2 + col * spriteSize;
            e.center_y = spriteSize / 2 + row * spriteSize;
            getEnergySources().add(e);
          }
          //Windmühle erstellen
          case "w" -> {
            EnergySource e =
                new EnergySource(pApplet, ground, blockScale, AppConfig.getValue("energy.windmill", 1.0f), "windmill.png");
            e.center_x = spriteSize / 2 + col * spriteSize;
            e.center_y = spriteSize / 2 + row * spriteSize;
            getEnergySources().add(e);
          }

          //Solarpannel erstellen
          case "s" -> {
            EnergySource e =
                new EnergySource(pApplet, ground, blockScale, AppConfig.getValue("energy.solarpannel", 1.0f), "solarpannel.png");
            e.center_x = spriteSize / 2 + col * spriteSize;
            e.center_y = spriteSize / 2 + row * spriteSize;
            getEnergySources().add(e);
          }
        }
      }
    }
    spriteSettings(lines.length);
  }

  public ArrayList<Sprite> getEnergySources() {
    return energySources;
  }

  public ArrayList<Sprite> getEnemies() {
    return enemies;
  }

  public ArrayList<Sprite> getLevelDoors() {
    return levelDoors;
  }

  public ArrayList<Sprite> getPlatforms() {
    return platforms;
  }
}
