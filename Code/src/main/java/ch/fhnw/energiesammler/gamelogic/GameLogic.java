package ch.fhnw.energiesammler.gamelogic;

import java.util.ArrayList;

import ch.fhnw.energiesammler.AppConfig;
import ch.fhnw.energiesammler.controls.GameCommand;
import ch.fhnw.energiesammler.entities.AnimatedSprite;
import ch.fhnw.energiesammler.entities.Door;
import ch.fhnw.energiesammler.entities.Enemy;
import ch.fhnw.energiesammler.entities.EnergySource;
import ch.fhnw.energiesammler.entities.Player;
import ch.fhnw.energiesammler.entities.Sprite;
import ch.fhnw.energiesammler.ui.Drawer;

import processing.core.PApplet;

public class GameLogic {
  private ArrayList<Door> allDoors;
  private ArrayList<Door> usedDoors;
  public float collectedEnergy;
  public boolean isGameOver;
  public boolean displayLostWinEnergy;
  public float getEnergy;
  public int timeSecond = 0;
  public int energyGravity = 0;
  public int maxEnergy;
  public int batterySize;
  public int collectedBatteries;
  private int numberOfMaps;
  private int currentMap = 1;
  public float gravity;
  public float bounce;
  private float speed;
  private float jump;
  Platformer platformer;
  PApplet pApplet;
  Level level;
  Drawer drawer;
  Player player;

  public GameLogic(Platformer platformer, PApplet pApplet, Level level, Drawer drawer) {
    this.platformer = platformer;
    this.pApplet = pApplet;
    this.level = level;
    this.drawer = drawer;
    this.player = platformer.getPlayer();

    isGameOver = false;
    usedDoors = new ArrayList<>();
    allDoors = new ArrayList<>();
    numberOfMaps = AppConfig.getValue("level.number", 3);
    collectedEnergy = AppConfig.getValue("battery.startFill", 1);
    batterySize = AppConfig.getValue("battery.size", 2);
    collectedBatteries = AppConfig.getValue("energy.startBatteries", 1);
    maxEnergy = collectedBatteries * batterySize;

    for (int i = 1; i < collectedBatteries; i++) {
      drawer.getDisplayer().addBattery();
    }
  }

  public void initGameValues() {
    gravity = level.blockScale * AppConfig.getValue("gravity.player", 5.5f);
    speed = level.blockScale * AppConfig.getValue("speed.player", 12f);
    jump = level.blockScale * AppConfig.getValue("jump.player", 40f);
    bounce = -level.blockScale * AppConfig.getValue("bounceOnEnemies", 5.5f);
  }

  public void updateAll() {
    checkBoarder();
    player.updateAnimation();
    resolvePlatformCollisions(player, level.getPlatforms());
    for (Sprite enemy : level.enemies) {
      resolvePlatformCollisions(enemy, level.getPlatforms());
      enemy.update();
      ((AnimatedSprite) enemy).updateAnimation();
    }
    collideWithEnemy();
    if (!isInFrontOfDoor()) {
      isInFrontOfDoor();
    }
  }

  public void scroll() {
    if (platformer.view_x + platformer.displayWidth < level.getLevelRight() - level.getSpriteSize() / 2) {
      float rightBoundary = platformer.view_x + platformer.displayWidth - ((float) platformer.displayWidth / 4);
      if (platformer.getPlayer().getRight() > rightBoundary) {
        platformer.view_x += platformer.getPlayer().getRight() - rightBoundary;
      }
    }
    if (platformer.view_x > level.getLevelLeft() + level.getSpriteSize() / 2) {
      float leftBoundary = platformer.view_x + (float) platformer.displayWidth / 4;
      if (platformer.getPlayer().getLeft() < leftBoundary) {
        platformer.view_x -= leftBoundary - platformer.getPlayer().getLeft();
      }
    }
    if (platformer.view_y + platformer.displayHeight < level.getLevelLow() - level.getSpriteSize() / 2) {
      float bottomBoundary = platformer.view_y + platformer.displayHeight - (float) platformer.displayHeight / 10;
      if (platformer.getPlayer().getBottom() > bottomBoundary) {
        platformer.view_y += platformer.getPlayer().getBottom() - bottomBoundary;
      } else {
        // set BottomBoundary limit to not go lower, breaking the level view Y
        platformer.view_y = -(platformer.displayHeight - level.levelLow);
      }
    }
    float topBoundary = platformer.view_y + (float) platformer.displayHeight / 10;
    if (platformer.getPlayer().getTop() < topBoundary) {
      platformer.view_y -= topBoundary - platformer.getPlayer().getTop();
    }

    pApplet.translate(-platformer.view_x, -platformer.view_y);
  }

  /**
   * Level Rand überprüfen.
   */
  public void checkBoarder() {
    boolean overXLeft =
        platformer.getPlayer().getLeft() <= level.getLevelLeft() + level.getSpriteSize() / 2;
    if (overXLeft && !(platformer.inputManager.pressed(GameCommand.RIGHT)
        || platformer.inputManager.hold(GameCommand.RIGHT))) {
      player.change_x = 0;
    }
    boolean overXRight =
        platformer.getPlayer().getRight() >= level.getLevelRight() - level.getSpriteSize() / 2;
    if (overXRight && !(platformer.inputManager.pressed(GameCommand.LEFT)
        || platformer.inputManager.hold(GameCommand.LEFT))) {
      player.change_x = 0;
    }
  }

  //In Schlucht fallen
  public void fallOffCliff() {
    boolean deathByFalling = platformer.getPlayer().getBottom() > (level.getLevelLow());
    if (deathByFalling) {
      isGameOver = true;
      currentMap = 1;
    }
  }

  //Energie einsammeln
  public void collectEnergy() {
    ArrayList<Sprite> energyList = checkCollisionList(player, level.getEnergySources());
    for (Sprite energy : energyList) {
      //Nur Teil von Energie erhalten
      if (collectedEnergy + ((EnergySource) energy).getPower() > maxEnergy
          && collectedEnergy != maxEnergy) {
        level.getEnergySources().remove(energy);
        getEnergy = ((EnergySource) energy).getPower();
        collectedEnergy += maxEnergy - collectedEnergy;
        displayLostWinEnergy = true;
        timeSecond = platformer.millis();
        energyGravity = 0;
        //Energie komplett dazubekommen
      } else if (collectedEnergy != maxEnergy) {
        level.getEnergySources().remove(energy);
        getEnergy = ((EnergySource) energy).getPower();
        collectedEnergy += ((EnergySource) energy).getPower();
        displayLostWinEnergy = true;
        timeSecond = pApplet.millis();
        energyGravity = 0;
      }
    }
  }

  //Batterie einsammeln
  public void collectBattery() {
    for (Sprite battery : checkCollisionList(player, level.batteries)) {
      level.batteries.remove(battery);
      collectedBatteries++;
      maxEnergy = collectedBatteries * batterySize;
      drawer.getDisplayer().addBattery();
    }
  }

  //Gegner berühren
  public void collideWithEnemy() {
    ArrayList<Sprite> enemyList = checkCollisionList(player, level.getEnemies());
    for (Sprite sprite : enemyList) {
      //Überprüfen ob Spieler auf Gegner gesprungen ist.
      if (player.getLastRight() > sprite.getLeft() && player.getLastLeft() < sprite.getRight()
          && player.getLastBottom() < sprite.getTop()) {
        player.change_y = bounce;
        level.enemies.remove(sprite);
        //Defeated Enemy gets added to Enemys turned off and points for highscore
        if (sprite instanceof Enemy enemy) {
          platformer.setEnemiesTurnedOff(enemy);
          drawer.getDisplayer().addToHighscore(enemy.getDamage() * (-1));
        }

      } else {
        displayLostWinEnergy = true;
        timeSecond = pApplet.millis();
        energyGravity = 0;
        getEnergy = ((Enemy) sprite).getDamage();
        if (collectedEnergy + ((Enemy) sprite).getDamage() > 0) {
          level.getEnergySources().remove(sprite);
          level.enemies.remove(sprite);
          collectedEnergy += ((Enemy) sprite).getDamage();
        } else {
          collectedEnergy = 0;
          isGameOver = true;
          currentMap = 1;
        }
      }
    }
    player.setLast(player.getLeft(), player.getRight(), player.getBottom());
  }

  //Vor Türe stehen
  public boolean isInFrontOfDoor() {
    ArrayList<Sprite> doorList = checkCollisionList(player, level.levelDoors);
    return !doorList.isEmpty();
  }

  //Überprüfung ob Spieler auf Platform steht
  public boolean isOnPlatform(Sprite s, ArrayList<Sprite> walls) {
    s.center_y += 5;
    ArrayList<Sprite> colList = checkCollisionList(s, walls);
    s.center_y -= 5;
    return !colList.isEmpty();
  }

  //Kollisionen zwischen Spieler und Platformen auswerten
  public void resolvePlatformCollisions(Sprite s, ArrayList<Sprite> walls) {
    s.change_y += gravity;
    s.center_y += s.change_y;
    ArrayList<Sprite> colList = checkCollisionList(s, walls);
    if (!colList.isEmpty()) {
      Sprite collided = colList.get(0);
      if (s.change_y > 0) {
        s.setBottom(collided.getTop());
      } else if (s.change_y < 0) {
        s.setTop(collided.getBottom());
      }
      s.change_y = 0;
    }

    s.center_x += s.change_x;
    colList = checkCollisionList(s, walls);
    if (!colList.isEmpty()) {
      Sprite collided = colList.get(0);
      if (s.change_x > 0) {
        s.setRight(collided.getLeft());
      } else if (s.change_x < 0) {
        s.setLeft(collided.getRight());
      }
    }
  }

  //Überprüfen ob sich 2 Elemente berühren
  public boolean checkCollision(Sprite s1, Sprite s2) {
    boolean noXOverlap = s1.getRight() <= s2.getLeft() || s1.getLeft() >= s2.getRight();
    boolean noYOverlap = s1.getBottom() <= s2.getTop() || s1.getTop() >= s2.getBottom();
    return !noXOverlap && !noYOverlap;
  }

  //Liste erstellen mit Elementen welche sich berühren
  public ArrayList<Sprite> checkCollisionList(Sprite s, ArrayList<Sprite> list) {
    ArrayList<Sprite> collisionList = new ArrayList<>();
    if (!list.isEmpty()) {
      for (Sprite p : list) {
        if (checkCollision(s, p) && s != p) {
          collisionList.add(p);
        }
      }
    }
    return collisionList;
  }

  //Überprüfen ob 2 Elemente nebeneinander sind
  public boolean checkBoardering(Sprite s1, Sprite s2) {
    boolean noXOverlap = s1.getRight() < s2.getLeft() || s1.getLeft() > s2.getRight();
    boolean noYOverlap = s1.getBottom() < s2.getTop() || s1.getTop() > s2.getBottom();
    return !noXOverlap && !noYOverlap;
  }

  //Liste erstellen von Elementen die nebeneinander sind
  public ArrayList<Sprite> checkBoarderingList(Sprite s, ArrayList<Sprite> list) {
    ArrayList<Sprite> boarderingList = new ArrayList<>();
    for (Sprite p : list) {
      if (s != p && checkBoardering(s, p)) {
        boarderingList.add(p);
      }
    }
    return boarderingList;
  }

  //Durch Türe gehen
  public void enterDoor() {
    Door currentDoor = (Door) checkCollisionList(player, level.getLevelDoors()).get(0);
    usedDoors.add(currentDoor);
    reduceEnergy(currentDoor.getCost());
    if (collectedEnergy <= 0) {
      isGameOver = true;
      currentMap = 1;
    } else {
      currentMap++;
      if (currentMap > numberOfMaps) {
        // Alle Level abgeschlossen
        isGameOver = true;
        drawer.drawEndScreen(platformer.inputManager);
        currentMap = 1;
      } else {
        level.loadLevel();
      }
    }
  }

  //Energie abziehen
  public void reduceEnergy(float lostEnergy) {
    collectedEnergy = collectedEnergy > lostEnergy ? collectedEnergy - lostEnergy : 0f;
  }

  public void addAllDoors(Door door) {
    allDoors.add(door);
  }

  public float getSpeed() {
    return speed;
  }

  public float getJump() {
    return jump;
  }

  public int getCurrentMap() {
    return currentMap;
  }

  public int getNumberOfMaps() {
    return numberOfMaps;
  }

  public ArrayList<Door> getAllDoors() {
    return allDoors;
  }

  public ArrayList<Door> getUsedDoors() {
    return usedDoors;
  }
}
